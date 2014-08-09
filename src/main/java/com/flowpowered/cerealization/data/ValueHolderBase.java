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
package com.flowpowered.cerealization.data;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.flowpowered.cerealization.CastUtils;
import com.flowpowered.cerealization.config.serialization.Serialization;

public class ValueHolderBase implements ValueHolder {
    private final ValueHolder actualValue;

    public ValueHolderBase() {
        this(null);
    }

    public ValueHolderBase(ValueHolder actualValue) {
        this.actualValue = actualValue;
    }

    @Override
    public boolean getBoolean() {
        return getBoolean(false);
    }

    @Override
    public boolean getBoolean(boolean def) {
        final Boolean val = CastUtils.castBoolean(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public byte getByte() {
        return getByte((byte) 0);
    }

    @Override
    public byte getByte(byte def) {
        final Byte val = CastUtils.castByte(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public float getFloat() {
        return getFloat(0f);
    }

    @Override
    public float getFloat(float def) {
        final Float val = CastUtils.castFloat(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public short getShort() {
        return getShort((short) 0);
    }

    @Override
    public short getShort(short def) {
        final Short val = CastUtils.castShort(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public int getInt() {
        return getInt(0);
    }

    @Override
    public int getInt(int def) {
        final Integer val = CastUtils.castInt(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public long getLong() {
        return getLong(0);
    }

    @Override
    public long getLong(long def) {
        final Long val = CastUtils.castLong(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public double getDouble() {
        return getDouble(0);
    }

    @Override
    public double getDouble(double def) {
        final Double val = CastUtils.castDouble(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public BigInteger getBigInt() {
        return getBigInt(null);
    }

    @Override
    public BigInteger getBigInt(BigInteger def) {
        final BigInteger val = CastUtils.castBigInt(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public BigDecimal getDecimal() {
        return getDecimal(null);
    }

    @Override
    public BigDecimal getDecimal(BigDecimal def) {
        final BigDecimal val = CastUtils.castBigDecimal(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public Date getDate() {
        return getDate(null);
    }

    @Override
    public Date getDate(Date def) {
        final Date val = CastUtils.castDate(getValue(def));
        return val == null ? def : val;
    }

    @Override
    public byte[] getBytes() {
        return getBytes(null);
    }

    @Override
    public byte[] getBytes(byte[] def) {
        final Object val = getValue(def);
        if (val == null) {
            return def;
        }
        final byte[] bytes = CastUtils.castBytes(val);
        return bytes == null ? val.toString().getBytes() : bytes;
    }

    @Override
    public String getString() {
        return getString(null);
    }

    @Override
    public String getString(String def) {
        final Object val = getValue(def);
        return val == null ? def : val.toString();
    }

    @Override
    public Object getValue() {
        if (actualValue == null) {
            throw new UnsupportedOperationException("ValueHolderBase must have a reference to another ValueHolder or override getValue");
        }
        return actualValue.getValue();
    }

    @Override
    public Object getValue(Object def) {
        if (actualValue == null) {
            throw new UnsupportedOperationException("ValueHolderBase must have a reference to another ValueHolder or override getValue");
        }
        return actualValue.getValue(def);
    }

    @Override
    public <T> T getTypedValue(Class<T> type) {
        return getTypedValue(type, null);
    }

    @Override
    public <T> T getTypedValue(Class<T> type, T def) {
        final Object val = Serialization.deserialize(type, getValue());
        return type.isInstance(val) ? type.cast(val) : def;
    }

    @Override
    public Object getTypedValue(Type type) {
        return getTypedValue(type, null);
    }

    @Override
    public Object getTypedValue(Type type, Object def) {
        Object val = Serialization.deserialize(type, getValue());
        if (val == null) {
            val = def;
        }
        return val;
    }

    @Override
    public List<?> getList() {
        return getList(null);
    }

    @Override
    public List<?> getList(List<?> def) {
        Object val = getValue();
        if (val instanceof List<?>) {
            return (List<?>) val;
        } else if (val instanceof Collection<?>) {
            return new ArrayList<Object>((Collection<?>) val);
        } else {
            return def == null ? Collections.emptyList() : def;
        }
    }

    @Override
    public List<String> getStringList() {
        return getStringList(null);
    }

    @Override
    public List<String> getStringList(List<String> def) {
        List<?> val = getList(def);
        List<String> ret = new ArrayList<String>();
        for (Object item : val) {
            ret.add(item == null ? null : item.toString());
        }
        return ret;
    }

    @Override
    public List<Integer> getIntegerList() {
        return getIntegerList(null);
    }

    @Override
    public List<Integer> getIntegerList(List<Integer> def) {
        List<?> val = getList(def);
        List<Integer> ret = new ArrayList<Integer>();
        for (Object item : val) {
            Integer asInt = CastUtils.castInt(item);
            if (asInt == null) {
                return def == null ? Collections.<Integer>emptyList() : def;
            }
            ret.add(asInt);
        }
        return ret;
    }

    @Override
    public List<Double> getDoubleList() {
        return getDoubleList(null);
    }

    @Override
    public List<Double> getDoubleList(List<Double> def) {
        List<?> val = getList(def);
        List<Double> ret = new ArrayList<Double>();
        for (Object item : val) {
            Double asDouble = CastUtils.castDouble(item);
            if (asDouble == null) {
                return def == null ? Collections.<Double>emptyList() : def;
            }
            ret.add(asDouble);
        }
        return ret;
    }

    @Override
    public List<Boolean> getBooleanList() {
        return getBooleanList(null);
    }

    @Override
    public List<Boolean> getBooleanList(List<Boolean> def) {
        List<?> val = getList(def);
        List<Boolean> ret = new ArrayList<Boolean>();
        for (Object item : val) {
            Boolean asBoolean = CastUtils.castBoolean(item);
            if (asBoolean == null) {
                return def == null ? Collections.<Boolean>emptyList() : def;
            }
            ret.add(asBoolean);
        }
        return ret;
    }

    public static class NullHolder extends ValueHolderBase {
        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Object getValue(Object def) {
            return def;
        }
    }
}
