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
import java.util.Date;
import java.util.List;

public interface ValueHolder {
	/**
	 * Return this node's value as a boolean
	 *
	 * @return the boolean value
	 * @see #getBoolean(boolean)
	 * @see #getValue()
	 */
	public boolean getBoolean();

	/**
	 * Return this node's value as a boolean
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a boolean
	 * @return the boolean value
	 * @see #getValue(Object)
	 */
	public boolean getBoolean(boolean def);

	/**
	 * Return this node's value as a byte
	 *
	 * @return the byte value
	 * @see #getByte(byte)
	 * @see #getValue()
	 */
	public byte getByte();

	/**
	 * Return this value as a byte
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a byte
	 * @return the byte value
	 * @see #getValue(Object)
	 */
	public byte getByte(byte def);

	/**
	 * Return this node's value as a float
	 *
	 * @return the float value
	 * @see #getFloat(float)
	 * @see #getValue()
	 */
	public float getFloat();

	/**
	 * Return this value as a float
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a float
	 * @return the float value
	 * @see #getValue(Object)
	 */
	public float getFloat(float def);

	/**
	 * Return this node's value as a short
	 *
	 * @return the short value
	 * @see #getShort(short)
	 * @see #getValue()
	 */
	public short getShort();

	/**
	 * Return this value as a short
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a short
	 * @return the short value
	 * @see #getValue(Object)
	 */
	public short getShort(short def);

	/**
	 * Return this node's value as an integer
	 *
	 * @return the integer value
	 * @see #getInt(int)
	 * @see #getValue()
	 */
	public int getInt();

	/**
	 * Return this value as an integer
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't an integer
	 * @return the integer value
	 * @see #getValue(Object)
	 */
	public int getInt(int def);

	/**
	 * Return this node's value as a long
	 *
	 * @return the long value
	 * @see #getLong(long)
	 * @see #getValue()
	 */
	public long getLong();

	/**
	 * Return this node's value as a long
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a long
	 * @return the long value
	 * @see #getValue(Object)
	 */
	public long getLong(long def);

	/**
	 * Return this node's value as a double
	 *
	 * @return the double value
	 * @see #getDouble(double)
	 * @see #getValue()
	 */
	public double getDouble();

	/**
	 * Return this node's value as a double
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a double
	 * @return the double value
	 * @see #getValue(Object)
	 */
	public double getDouble(double def);

	/**
	 * Return this node's value as a Date
	 *
	 * @return the Date value
	 * @see #getDate(Date)
	 * @see #getValue(Object)
	 */
	public Date getDate();

	/**
	 * Return this node's value as a Date
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a Date
	 * @return the Date value
	 * @see #getValue(Object)
	 */
	public Date getDate(Date def);

	/**
	 * Return this node's value as a String
	 *
	 * @return the String value
	 * @see #getString(String)
	 * @see #getValue()
	 */
	public String getString();

	/**
	 * Return this node's value as a String
	 *
	 * @param def The default value, returned if this node doesn't have a set value
	 * @return the String value
	 * @see #getValue(Object)
	 */
	public String getString(String def);

	/**
	 * Return this node's value
	 *
	 * @return the value
	 * @see #getValue(Object)
	 */
	public Object getValue();

	/**
	 * Return this node's value
	 *
	 * @param def The default value, returned if this node doesn't have a set value
	 * @return the value
	 */
	public abstract Object getValue(Object def);

	/**
	 * Return this node's value as the given type
	 *
	 * @param <T> The type to get as
	 * @param type The type to get as and check for
	 * @return the value as the give type, or null if the value is not present or not of the given type
	 * @see #getTypedValue(Class, Object)
	 * @see #getValue()
	 */
	public <T> T getTypedValue(Class<T> type);

	/**
	 * Return this node's value as the given type
	 *
	 * @param <T> The type to get as
	 * @param type The type to get as and check for
	 * @param def The value to use as default
	 * @return the value as the give type, or {@code def} if the value is not present or not of the given type
	 * @see #getValue(Object)
	 */
	public <T> T getTypedValue(Class<T> type, T def);

	/**
	 * Return this node's value as the given type
	 *
	 * @param type The type to get as and check for
	 * @return the value as the give type, or null if the value is not present or not of the given type
	 * @see #getTypedValue(Class, Object)
	 * @see #getValue()
	 */
	public Object getTypedValue(Type type);

	/**
	 * Return this node's value as the given type
	 *
	 * @param type The type to get as and check for
	 * @param def The value to use as default
	 * @return the value as the give type, or {@code def} if the value is not present or not of the given type
	 * @see #getValue(Object)
	 */
	public Object getTypedValue(Type type, Object def);

	/**
	 * Return this node's value as a list
	 *
	 * @return the list value
	 * @see #getList(java.util.List)
	 * @see #getValue()
	 */
	public List<?> getList();

	/**
	 * Return this node's value as a list
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a boolean. If this is null it will act as an empty list.
	 * @return the List value
	 * @see #getValue(Object)
	 */
	public abstract List<?> getList(List<?> def);

	/**
	 * Return this node's value as a string list. Note that this will not necessarily return the same collection that is in this configuration's value. This means that changes to the return value of this
	 * method might not affect the configuration, so after changes the value of this node should be set to this list.
	 *
	 * @return the string list value
	 * @see #getStringList(java.util.List)
	 * @see #getValue()
	 */
	public List<String> getStringList();

	/**
	 * Return this node's value as a string list. Note that this will not necessarily return the same collection that is in this configuration's value. This means that changes to the return value of this
	 * method might not affect the configuration, so after changes the value of this node should be set to this list.
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a boolean. If this is null it will act as an empty list.
	 * @return the string list value
	 * @see #getValue(Object)
	 */
	public abstract List<String> getStringList(List<String> def);

	/**
	 * Return this node's value as an integer list. Note that this will not necessarily return the same collection that is in this configuration's value. This means that changes to the return value of
	 * this method might not affect the configuration, so after changes the value of this node should be set to this list.
	 *
	 * @return the integer list value
	 * @see #getStringList(java.util.List)
	 * @see #getValue()
	 */
	public List<Integer> getIntegerList();

	/**
	 * Return this node's value as a string list. Note that this will not necessarily return the same collection that is in this value. This means that changes to the return value of this method might
	 * not affect the value, so after changes the value of this node should be set to this list.
	 *
	 * @param def The default value, returned if this node doesn't have a set value or the value isn't a boolean. If this is null it will act as an empty list.
	 * @return the string list value
	 * @see #getValue(Object)
	 */
	public abstract List<Integer> getIntegerList(List<Integer> def);

	public List<Double> getDoubleList();

	public abstract List<Double> getDoubleList(List<Double> def);

	public List<Boolean> getBooleanList();

	public abstract List<Boolean> getBooleanList(List<Boolean> def);
}
