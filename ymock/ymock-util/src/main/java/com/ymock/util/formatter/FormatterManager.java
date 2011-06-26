/**
 * Copyright (c) 2011, yMock.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the yMock.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ymock.util.formatter;

import com.ymock.util.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 * Class is a log helper used to fmt logging arguments.
 */
public final class FormatterManager {

    /**
     * Singleton instance of the FormatterManager class.
     */
    private static FormatterManager instance;

    /**
     * Storage of all the registered formatters.
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
        this.formatters = new HashMap<String, Formatter>();
        this.registerFormatters();
    }


    /**
     * Registers all the available formatters, looks up in classpath for all
     * the implementations of the {@link Formatter} interface and registers them
     * to the manager.
     */
    protected void registerFormatters() {
        final Reflections reflections = new Reflections("",
            new SubTypesScanner());
        final Set<Class<? extends Formatter>> subTypes = reflections.
            getSubTypesOf(Formatter.class);
        for (Class<? extends Formatter> subType : subTypes) {
            final String errorMsg = "Cannot instantiate Formatter: %s";
            try {
                final Formatter formatter = subType.newInstance();
                this.formatters.put(formatter.getFormatterKey(), formatter);
            } catch (InstantiationException e) {
                Logger.warn(this, errorMsg, subType);
            } catch (IllegalAccessException e) {
                Logger.warn(this, errorMsg, subType);
            }
        }
    }

    /**
     * Returns the singleton instance of {@link FormatterManager}.
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
     * Formats the passed args according to the formatter defined by
     * key argument.
     * Is used by {@link Logger#fmt(String, Object...)}
     *
     * @param key  key for the formatter to be used to fmt the arguments
     * @param args arguments to be formatted
     * @return formatted arguments string
     */
    public String fmt(final String key, final Object... args) {
        final Formatter formatter = this.formatters.get(key);
        if (formatter == null) {
            Logger.warn(this, "Formatter is not registered for key: %s", key);
            String unformattedReturn = null;
            if (args.length == 1) {
                unformattedReturn = args[0].toString();
            } else {
                unformattedReturn = Arrays.toString(args);
            }
            return unformattedReturn;
        }

        return formatter.format(args);
    }
}
