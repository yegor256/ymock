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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;


/**
 * Class is a log helper used to fmt logging arguments.
 *
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @version $Id$
 */
public final class FormatterManager {

    /**
     * Singleton instance of the FormatterManager class.
     */
    public static final FormatterManager INSTANCE = new FormatterManager();

    /**
     * Storage of all the registered formatters.
     */
    private final Map<String, FormatterBean> formatters = this.discover();

    /**
     * Private constructor.
     */
    private FormatterManager() {
        // intentionally empty
    }

    /**
     * Discover all available formatters in classpath,
     * annotated with {@link FormatGroup} and {@link Format} annotations.
     */
    private Map<String, FormatterBean> discover() {
        final Set<URL> urls = ClasspathHelper.getUrlsForPackagePrefix("");
        final Reflections reflections = new Reflections(
            new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(new TypeAnnotationsScanner()));
        final Set<Class<?>> formatGroups = reflections.
            getTypesAnnotatedWith(FormatGroup.class);
        for (Class<?> formatGroup : formatGroups) {
            final FormatGroup annotatedGroup =
                formatGroup.getAnnotation(FormatGroup.class);
            for (Method m : formatGroup.getMethods()) {
                if (m.isAnnotationPresent(Format.class)) {
                    final Format annotationFormat =
                        m.getAnnotation(Format.class);
                    try {
                        final FormatterBean formatterBean =
                            new FormatterBean(formatGroup.newInstance(), m);
                        this.formatters.put(annotatedGroup.value()
                            + "." + annotationFormat.value(), formatterBean);
                    } catch (InstantiationException e) {
                        Logger.warn(
                            this,
                            "Cannot create formatter for class: %s method: %s",
                            annotatedGroup,
                            m
                        );
                    } catch (IllegalAccessException e) {
                        Logger.warn(
                            this,
                            "Error invoking formatter method for class: %s method: %s",
                            annotatedGroup,
                            m
                        );
                    }

                }
            }
        }
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
        final FormatterBean formatterBean = this.formatters.get(key);
        if (formatterBean == null) {
            Logger.warn(this, "Formatter is not registered for key: %s", key);
        } else {
            try {
                return formatterBean.getMethod().
                    invoke(formatterBean.getGroup(), args).toString();
            } catch (IllegalAccessException e) {
                Logger.warn(this, FormatterManager.ERROR_MSG2,
                    formatterBean.getGroup().getClass()
                    , formatterBean.getMethod());
            } catch (InvocationTargetException e) {
                Logger.warn(this, FormatterManager.ERROR_MSG2,
                    formatterBean.getGroup().getClass()
                    , formatterBean.getMethod());
            }
        }
        String unformattedReturn = null;
        if (args.length == 1) {
            unformattedReturn = args[0].toString();
        } else {
            unformattedReturn = Arrays.toString(args);
        }
        return unformattedReturn;
    }

    /**
     * Internal class holding info to cal formatter method.
     */
    private static class FormatterBean {
        /**
         * formatter group.
         */
        private Object group;
        /**
         * formatter method.
         */
        private Method method;
        /**
         * FormatterBean constructor.
         * @param groupParam  formatter group
         * @param methodParam formatter method
         */
        FormatterBean(final Object groupParam, final Method methodParam) {
            this.group = groupParam;
            this.method = methodParam;
        }
        /**
         * Return formatter group.
         * @return formatter group
         */
        public Object getGroup() {
            return this.group;
        }
        /**
         * Returns formatter method.
         * @return formatter method
         */
        public Method getMethod() {
            return this.method;
        }
    }

}
