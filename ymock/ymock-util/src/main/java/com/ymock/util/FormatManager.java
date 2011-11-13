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
package com.ymock.util;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.reflections.Reflections;

/**
 * Class is a log helper used to fmt logging arguments.
 *
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class FormatManager {

    /**
     * Singleton instance of the FormatManager class.
     */
    public static final FormatManager INSTANCE = new FormatManager();

    /**
     * Storage of all the registered formatters.
     */
    private final transient ConcurrentMap<String, FormattingBean> formatters =
        this.discover();

    /**
     * Private constructor.
     */
    private FormatManager() {
        // intentionally empty
    }

    /**
     * Formats the passed args according to the formatter defined by
     * key argument.
     *
     * @param key Key for the formatter to be used to fmt the arguments
     * @param args Arguments to be formatted
     * @return Formatted arguments string
     * @see Logger#fmt(String,Object[])
     */
    public String fmt(final String key, final Object... args) {
        String result;
        if (this.formatters.containsKey(key)) {
            result = this.formatters.get(key).format(args);
        } else {
            Logger.warn(
                this,
                "Formatter is not registered for key: %s",
                key
            );
            result = "?";
        }
        return result;
    }

    /**
     * Discover all available formatters in classpath,
     * annotated with {@link Formatter} annotations.
     * @return Discovered map of them
     */
    private ConcurrentMap<String, FormattingBean> discover() {
        final Set<Class<?>> fmts = new Reflections("")
            .getTypesAnnotatedWith(Formatter.class);
        final ConcurrentMap<String, FormattingBean> beans =
            new ConcurrentHashMap<String, FormattingBean>();
        for (Class<?> fmt : fmts) {
            for (Method method : fmt.getMethods()) {
                if (!method.isAnnotationPresent(Formatter.class)) {
                    continue;
                }
                final Formatter annotation =
                    method.getAnnotation(Formatter.class);
                final FormattingBean bean = this.bean(method);
                if (bean != null) {
                    beans.put(annotation.value(), bean);
                    Logger.debug(
                        this,
                        "Discovered '%s' formatter at %s",
                        annotation.value(),
                        method.getDeclaringClass().getName()
                    );
                }
            }
        }
        return beans;
    }

    /**
     * Convert annotated method into bean.
     * @param method The method
     * @return The bean
     */
    private FormattingBean bean(final Method method) {
        FormattingBean bean = null;
        try {
            bean = new FormattingBean(
                method.getDeclaringClass().newInstance(),
                method
            );
        } catch (InstantiationException ex) {
            Logger.warn(
                this,
                "Cannot instantiate formatter of class '%s': %s",
                method.getDeclaringClass().getName(),
                ex.getMessage()
            );
        } catch (IllegalAccessException ex) {
            Logger.warn(
                this,
                "Failed to access class '%s': %s",
                method.getDeclaringClass().getName(),
                ex.getMessage()
            );
        }
        return bean;
    }

}
