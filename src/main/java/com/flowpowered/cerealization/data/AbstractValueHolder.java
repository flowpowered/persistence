/*
 * This file is part of Flow Cerealization, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
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
import java.util.Date;
import java.util.List;

public abstract class AbstractValueHolder implements ValueHolder {

	public AbstractValueHolder() {
		super();
	}

	@Override
	public boolean getBoolean() {
		return getBoolean(false);
	}

	@Override
	public byte getByte() {
		return getByte((byte) 0);
	}

	@Override
	public float getFloat() {
		return getFloat(0f);
	}

	@Override
	public short getShort() {
		return getShort((short) 0);
	}

	@Override
	public int getInt() {
		return getInt(0);
	}

	@Override
	public long getLong() {
		return getLong(0);
	}

	@Override
	public double getDouble() {
		return getDouble(0);
	}

	@Override
	public BigInteger getBigInt() {
		return getBigInt(null);
	}

	@Override
	public BigDecimal getDecimal() {
		return getDecimal(null);
	}

	@Override
	public Date getDate() {
		return getDate(null);
	}

	@Override
	public byte[] getBytes() {
		return getBytes(null);
	}

	@Override
	public String getString() {
		return getString(null);
	}

	@Override
	public <T> T getTypedValue(Class<T> type) {
		return getTypedValue(type, null);
	}

	@Override
	public Object getTypedValue(Type type) {
		return getTypedValue(type, null);
	}

	@Override
	public List<?> getList() {
		return getList(null);
	}

	@Override
	public List<String> getStringList() {
		return getStringList(null);
	}

	@Override
	public List<Integer> getIntegerList() {
		return getIntegerList(null);
	}

	@Override
	public List<Double> getDoubleList() {
		return getDoubleList(null);
	}

	@Override
	public List<Boolean> getBooleanList() {
		return getBooleanList(null);
	}

}