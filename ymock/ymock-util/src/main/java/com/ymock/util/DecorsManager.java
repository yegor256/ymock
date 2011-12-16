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

import java.net.URL;
import java.util.Arrays;
import java.util.Formattable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;

/**
 * Manager of all {@link Decor} annotated classes found in classpath.
 *
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class DecorsManager {

    /**
     * Storage of all found decors.
     * @checkstyle LineLength (2 lines)
     */
    private final transient ConcurrentMap<String, Class<? extends Formattable>> decors =
        new ConcurrentHashMap<String, Class<? extends Formattable>>();

    /**
     * Protected ctor, for this package only.
     */
    protected DecorsManager() {
        final long start = System.currentTimeMillis();
        for (Class<?> type : DecorsManager.discover()) {
            String name = ((Decor) type.getAnnotation(Decor.class)).value();
            if (!name.matches("[a-zA-Z0-9\\-\\.]*")) {
                throw RuntimeProblem.make(
                    "Decor name '%s' has invalid format",
                    name
                );
            }
            if (name.isEmpty()) {
                name = type.getName();
            }
            this.decors.put(name, (Class<Formattable>) type);
        }
        LoggerFactory.getLogger(DecorsManager.class).info(
            String.format(
                "%d decor(s) discovered in classpath in %dms: %s",
                this.decors.size(),
                System.currentTimeMillis() - start,
                StringUtils.join(this.decors.keySet(), ", ")
            )
        );
    }

    /**
     * Get decor by key.
     * @param key Key for the formatter to be used to fmt the arguments
     * @param arg The arbument to supply
     * @return The decor
     */
    public Formattable decor(final String key, final Object arg) {
        final Class<? extends Formattable> type = this.find(key, arg);
        Formattable decor;
        try {
            decor = type.getConstructor(Object.class).newInstance(arg);
        } catch (NoSuchMethodException ex) {
            throw RuntimeProblem.make(
                ex,
                "One-argument constructor is absent in %s",
                type.getName()
            );
        } catch (InstantiationException ex) {
            throw RuntimeProblem.make(
                ex,
                "Can't instantiate %s",
                type.getName()
            );
        } catch (IllegalAccessException ex) {
            throw RuntimeProblem.make(
                ex,
                "Can't access one-arg constructor in %s",
                type.getName()
            );
        } catch (java.lang.reflect.InvocationTargetException ex) {
            throw RuntimeProblem.make(
                ex,
                "Can't invoke one-arg constructor in %s",
                type.getName()
            );
        }
        return decor;
    }

    /**
     * Find decor.
     * @param key Key for the formatter to be used to fmt the arguments
     * @param arg The arbument to supply
     * @return The type of decor found
     */
    private Class<? extends Formattable> find(final String key,
        final Object arg) {
        Class<? extends Formattable> type = null;
        if (key.isEmpty()) {
            boolean found = false;
            for (Class<? extends Formattable> cls : this.decors.values()) {
                found = Arrays
                    .asList(((Decor) cls.getAnnotation(Decor.class)).types())
                    .contains(arg.getClass());
                if (found) {
                    type = cls;
                    break;
                }
            }
            if (!found) {
                throw RuntimeProblem.make(
                    "No decors for type '%s' found",
                    arg.getClass().getName()
                );
            }
        } else {
            if (!this.decors.containsKey(key)) {
                throw RuntimeProblem.make("Decor '%s' not found", key);
            }
            type = this.decors.get(key);
        }
        return type;
    }

    /**
     * Discover all annotated classes.
     * @return Set of them
     * @todo #33 It's a defect in Reflections, which leads to NPE.
     */
    @SuppressWarnings(
        { "PMD.AvoidCatchingGenericException", "PMD.AvoidCatchingNPE" }
    )
    private static Set<Class<?>> discover() {
        final Set<URL> found = new HashSet<URL>();
        found.addAll(
            ClasspathHelper.forClassLoader(ClasspathHelper.classLoaders())
        );
        found.addAll(ClasspathHelper.forJavaClassPath());
        found.addAll(ClasspathHelper.forManifest());
        final Set<URL> urls = new HashSet<URL>();
        for (URL url : found) {
            urls.add(url);
        }
        Set<Class<?>> types;
        try {
            types = new Reflections(
                new ConfigurationBuilder()
                    .setUrls(urls)
                    .setScanners(new TypeAnnotationsScanner())
            ).getTypesAnnotatedWith(Decor.class);
        } catch (NullPointerException ex) {
            types = new HashSet<Class<?>>();
            LoggerFactory.getLogger(DecorsManager.class).error(
                String.format(
                    "NPE during classpath discovery: %s",
                    ex.getMessage()
                )
            );
        }
        return types;
    }

}
