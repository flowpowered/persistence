/*
 * This file is part of Flow Persistence, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Flow Powered <https://flowpowered.com/>
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

import java.math.BigDecimal;
import java.math.BigInteger;

import com.flowpowered.cerealization.CastUtils;

public class NumberSerializer extends Serializer {
    @Override
    public boolean isApplicable(GenericType type) {
        Class<?> target = type.getMainType();
        return target != null && (target.isPrimitive() || Number.class.isAssignableFrom(target)) && !boolean.class.isAssignableFrom(target);
    }

    @Override
    public int getParametersRequired() {
        return 0;
    }

    @Override
    protected Object handleDeserialize(GenericType type, Object rawVal) {
        Class<?> target = type.getMainType();
        // Wrapper classes are evil!
        if (target.equals(Number.class) && rawVal instanceof Number) {
            return rawVal;
        } else if (target.equals(int.class) || target.equals(Integer.class)) {
            if (rawVal instanceof Number) {
                return ((Number) rawVal).intValue();
            } else {
                return Integer.parseInt(String.valueOf(rawVal));
            }
        } else if (target.equals(byte.class) || target.equals(Byte.class)) {
            if (rawVal instanceof Number) {
                return ((Number) rawVal).byteValue();
            } else {
                return Byte.parseByte(String.valueOf(rawVal));
            }
        } else if (target.equals(long.class) || target.equals(Long.class)) {
            if (rawVal instanceof Number) {
                return ((Number) rawVal).longValue();
            } else {
                return Long.parseLong(String.valueOf(rawVal));
            }
        } else if (target.equals(double.class) || target.equals(Double.class)) {
            if (rawVal instanceof Number) {
                return ((Number) rawVal).doubleValue();
            } else {
                return Double.parseDouble(String.valueOf(rawVal));
            }
        } else if (target.equals(float.class) || target.equals(Float.class)) {
            if (rawVal instanceof Number) {
                return ((Number) rawVal).floatValue();
            } else {
                return Float.parseFloat(String.valueOf(rawVal));
            }
        } else if (target.equals(short.class) || target.equals(Short.class)) {
            if (rawVal instanceof Number) {
                return ((Number) rawVal).shortValue();
            } else {
                return Short.parseShort(String.valueOf(rawVal));
            }
        } else if (target.equals(BigInteger.class)) {
            final BigInteger val = CastUtils.castBigInt(rawVal);
            if (val != null) {
                return val;
            } else {
                return new BigInteger(String.valueOf(rawVal));
            }
        } else if (target.equals(BigDecimal.class)) {
            final BigDecimal val = CastUtils.castBigDecimal(rawVal);
            if (val != null) {
                return val;
            } else {
                return new BigDecimal(String.valueOf(rawVal));
            }
        }
        return null;
    }
}
