/*
 * This file is part of Flow Cerealization, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <https://spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.cerealization.config.annotated;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.flowpowered.cerealization.ReflectionUtils;
import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationNode;
import com.flowpowered.cerealization.config.ConfigurationNodeSource;

/**
 * A configuration wrapper used to save annotated objects. <p> Fields from objects annotated with Setting will be saved to the configuration file. Methods from objects annotated with Load or Save and
 * with a ConfigurationNode as a parameter will be invoked during the corresponding event. <p> Example:
 *
 * <pre>
 * public class AnnotatedObjectExample {
 *     {@code @Setting}
 *     private int num = 42;
 *     {@code @Setting}({"foo", "bar"})
 *     private String str = "hello world";
 *     private long abc = 1234567890;
 *
 *     {@code @Load}
 *     private void load(ConfigurationNode node) {
 *         abc = node.getNode("abc").getLong();
 *     }
 *
 *     {@code @Save}
 *     private void save(ConfigurationNode node) {
 *         node.getNode("abc").setValue(long);
 *     }
 * }
 * </pre>
 *
 * A new instance is made from the class above, and the object is saved with path "example". <p> This will result in a configuration file which will look like this:
 *
 * <pre>
 * example:
 *     num: 42
 *     foo:
 *         bar: hello world
 *     abc: 1234567890
 * </pre>
 */
public class AnnotatedObjectConfiguration extends AnnotatedConfiguration {
    private final Map<Object, Set<Member>> objectMembers = new HashMap<Object, Set<Member>>();
    private final Map<Object, String[]> objectPaths = new HashMap<Object, String[]>();

    /**
     * Creates a new empty AnnotatedObjectConfiguration wrapper.
     */
    public AnnotatedObjectConfiguration() {
    }

    /**
     * Creates a new AnnotatedObjectConfiguration wrapper wrapping the provided configuration.
     */
    public AnnotatedObjectConfiguration(Configuration config) {
        super(config);
    }

    /**
     * Adds an object or a class to be saved or loaded by the configuration. <p> The node path of the object in the configuration is specified as an array (varargs) of strings. Only annotated fields and
     * methods will be used. <p> The individual paths of the fields can be specified with the Setting annotation. If none are specified, the field name is used. <p> A field named "exa" annotated with
     * {@code @Setting({"cd.ef"})} in an object with path "ab" will have its value saved at "ab.cd.ef". If no path is specified in Setting, the path will be "ab.exa". <p> If the target is a class, only
     * static fields and methods will be registered.
     *
     * @param object The object or class to load or save
     * @param path The path at which the object or class should be or is located in the configuration
     */
    @SuppressWarnings ("unchecked")
    public void add(Object object, String... path) {
        if (!objectMembers.containsKey(object)) {
            final Set<Member> members = new HashSet<Member>();
            if (object instanceof Class<?>) {
                members.addAll(ReflectionUtils.getDeclaredFieldsRecur((Class<?>) object, Modifier.STATIC, Setting.class));
                members.addAll(ReflectionUtils.getDeclaredMethodsRecur((Class<?>) object, Modifier.STATIC, Load.class, Save.class));
            } else {
                members.addAll(ReflectionUtils.getDeclaredFieldsRecur(object.getClass(), Setting.class));
                members.addAll(ReflectionUtils.getDeclaredMethodsRecur(object.getClass(), Load.class, Save.class));
            }
            objectMembers.put(object, members);
        }
        if (!objectPaths.containsKey(object)) {
            objectPaths.put(object, path);
        }
    }

    /**
     * Removes an object or a class from the configuration. It will not be used during the next save or load invocations.
     *
     * @param object the object or the class to remove
     */
    public void remove(Object object) {
        objectMembers.remove(object);
        objectPaths.remove(object);
    }

    @Override
    public void load(ConfigurationNodeSource source) throws ConfigurationException {
        for (Entry<Object, Set<Member>> entry : objectMembers.entrySet()) {
            final Object object = entry.getKey();
            load(source, object instanceof Class<?> ? null : object, objectPaths.get(object), entry.getValue());
        }
    }

    private void load(ConfigurationNodeSource source, Object object, String[] path, Set<Member> members) throws ConfigurationException {
        final Set<Method> methods = new HashSet<Method>();
        for (Member member : members) {
            if (member instanceof Method) {
                final Method method = (Method) member;
                if (method.isAnnotationPresent(Load.class)) {
                    methods.add(method);
                }
                continue;
            }
            final Field field = (Field) member;
            field.setAccessible(true);
            String[] fieldPath = field.getAnnotation(Setting.class).value();
            if (fieldPath.length == 0) {
                fieldPath = new String[] {field.getName()};
            }
            final ConfigurationNode fieldNode = source.getNode(ArrayUtils.addAll(path, fieldPath));
            final Object value = fieldNode.getTypedValue(field.getGenericType());
            try {
                if (value != null) {
                    field.set(object, value);
                } else {
                    fieldNode.setValue(field.getGenericType(), field.get(object));
                }
            } catch (IllegalAccessException ex) {
                throw new ConfigurationException(ex);
            }
        }
        invokeMethods(methods, object, source.getNode(path));
    }

    @Override
    public void save(ConfigurationNodeSource source) throws ConfigurationException {
        for (Entry<Object, Set<Member>> entry : objectMembers.entrySet()) {
            final Object object = entry.getKey();
            save(source, object instanceof Class<?> ? null : object, objectPaths.get(object), entry.getValue());
        }
    }

    private void save(ConfigurationNodeSource source, Object object, String[] path, Set<Member> members) throws ConfigurationException {
        final Set<Method> methods = new HashSet<Method>();
        for (Member member : members) {
            if (member instanceof Method) {
                final Method method = (Method) member;
                if (method.isAnnotationPresent(Save.class)) {
                    methods.add(method);
                }
                continue;
            }
            final Field field = (Field) member;
            field.setAccessible(true);
            String[] fieldPath = field.getAnnotation(Setting.class).value();
            if (fieldPath.length == 0) {
                fieldPath = new String[] {field.getName()};
            }
            final ConfigurationNode fieldNode = source.getNode(ArrayUtils.addAll(path, fieldPath));
            try {
                fieldNode.setValue(field.getGenericType(), field.get(object));
            } catch (IllegalAccessException ex) {
                throw new ConfigurationException(ex);
            }
        }
        invokeMethods(methods, object, source.getNode(path));
    }

    private void invokeMethods(Set<Method> methods, Object target, ConfigurationNode nodeParam)
            throws ConfigurationException {
        for (Method method : methods) {
            method.setAccessible(true);
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 0
                    || !ConfigurationNode.class.isAssignableFrom(parameters[0])) {
                continue;
            }
            try {
                method.invoke(target, nodeParam);
            } catch (IllegalAccessException ex) {
                throw new ConfigurationException(ex);
            } catch (InvocationTargetException ex) {
                throw new ConfigurationException(ex);
            }
        }
    }
}
