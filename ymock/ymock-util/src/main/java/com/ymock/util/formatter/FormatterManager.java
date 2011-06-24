package com.ymock.util.formatter;

import com.ymock.util.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Initialize the object, registers system available formatters.
     *
     * @see #registerSystemFormatters()
     */
    private FormatterManager() {
        formatters = new HashMap<String, Formatter>();
        registerSystemFormatters();
    }


    /**
     * TODO #19 register all system formatters - formatters that should be
     * available and registered by default.
     * Their access keys should be either this
     * class public constants, or the public constants in the
     * formatter implementation corresponding classes.
     * <p/>
     * Registers all the system available formatters
     */
    protected final void registerSystemFormatters() {

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
     * Registers formatter by the specified key
     *
     * @param key       key onder which the passed formatter is registred
     * @param formatter formatter to register
     */
    public void registerFormatter(String key, Formatter formatter) {
        formatters.put(key, formatter);
    }

    /**
     * Unregisters formatter by the specified keyters formatter
     *
     * @param key key for which formatter should be unregistered
     */
    public void unregisterFormatter(String key) {
        formatters.remove(key);
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
