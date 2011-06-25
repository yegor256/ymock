package com.ymock.util.formatter;

import com.ymock.util.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class is a log helper used to format logging arguments
 */
public final class FormatterManager {

    /**
     * Singleton instance of the FormatterManager class
     */
    private static FormatterManager instance;

    /**
     * Storage of all the registered formatters
     */
    private Map<String, Formatter> formatters;

    /**
     * Private constructor.
     * Initialize the object, registers all available formatters -
     * implementations of {@link Formatter} interface.
     *
     * @see #registerFormatters()
     */
    private FormatterManager() {
        formatters = new HashMap<String, Formatter>();
        registerFormatters();
    }


    /**
     * Registers all the available formatters, looks up in classpath for all
     * the implementations of the {@link Formatter} interface and registers them
     * to the manager
     */
    protected final void registerFormatters() {
        Reflections reflections = new Reflections("", new SubTypesScanner());
        Set<Class<? extends Formatter>> subTypes = reflections.getSubTypesOf(Formatter.class);
        for (Class<? extends Formatter> subType : subTypes) {
            try {
                Formatter formatter = subType.newInstance();
                formatters.put(formatter.getFormatterKey(), formatter);
            } catch (InstantiationException e) {
                Logger.warn(this, "Cannot instantiate Formatter: %s", subType);
            } catch (IllegalAccessException e) {
                Logger.warn(this, "Cannot instantiate Formatter: %s", subType);
            }
        }
    }

    /**
     * Returns the singleton instance of {@link FormatterManager}
     *
     * @return the singleton instance of {@link FormatterManager}
     */
    public static FormatterManager getInstance() {
        if (instance == null) {
            instance = new FormatterManager();
        }
        return instance;
    }

    /**
     * Formats the passed args according to the formatter defined by key argument.
     * Is used by {@link Logger#format(String, Object...)}
     *
     * @param key  key for the formatter to be used to format the arguments
     * @param args arguments to be formatted
     * @return formatted arguments string
     */
    public String format(String key, Object... args) {
        Formatter formatter = formatters.get(key);
        if (formatter == null) {
            Logger.warn(this, "Formatter is not registered for key: %s", key);
            if (args.length == 1) {
                return args[0].toString();
            } else {
                return Arrays.toString(args);
            }
        }

        return formatter.format(args);
    }
}
