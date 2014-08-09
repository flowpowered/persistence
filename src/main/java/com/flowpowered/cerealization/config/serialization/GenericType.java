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
package com.flowpowered.cerealization.config.serialization;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * A wrapper around {@link Type} to allow easier access to parameterized types.
 */
public class GenericType {
    private final Class<?> mainType;
    private final GenericType[] neededGenerics;
    private final GenericType arrayType;
    private final Type rawType;

    public GenericType(Type rawType) {
        if (rawType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) rawType;
            mainType = getMainType(type.getRawType());
            neededGenerics = toGenericType(type.getActualTypeArguments());
            arrayType = null;
        } else if (rawType instanceof GenericArrayType) {
            GenericArrayType type = (GenericArrayType) rawType;
            mainType = null;
            neededGenerics = new GenericType[0];
            arrayType = new GenericType(type.getGenericComponentType());
        } else {
            mainType = getMainType(rawType);
            neededGenerics = new GenericType[0];
            arrayType = null;
        }
        this.rawType = rawType;
    }

    public Class<?> getMainType() {
        return mainType;
    }

    public GenericType[] getGenerics() {
        return neededGenerics;
    }

    public Type getRawType() {
        return rawType;
    }

    public boolean isArray() {
        return arrayType != null;
    }

    public GenericType getArrayType() {
        return arrayType;
    }

    private static Class<?> getMainType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof WildcardType) {
            return (Class<?>) ((WildcardType) type).getUpperBounds()[0];
        } else {
            throw new IllegalArgumentException("Unknown main class type: " + type.getClass());
        }
    }

    public static GenericType[] toGenericType(Type[] types) {
        GenericType[] genericTypes = new GenericType[types.length];
        for (int i = 0; i < types.length; ++i) {
            genericTypes[i] = new GenericType(types[i]);
        }
        return genericTypes;
    }
}
