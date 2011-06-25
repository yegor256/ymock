package com.ymock.util.formatter;

/**
 * The base interface for all the log parameters formatters.
 * All the classes implementing the interface will be automatically
 * loaded and registered by {@link FormatterManager} class
 */
public interface Formatter {
    /**
     * Formats the passed args
     *
     * @param args arguments to be formatted
     * @return formatted arguments string
     */
    String format(Object... args);

    /**
     * Returns key by which formatter is registered in {@link FormatterManager}
     *
     * @return key by which formatter is registered in {@link FormatterManager}
     */
    String getFormatterKey();
}