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
package com.flowpowered.cerealization.config.serialization;

import java.util.Comparator;

/**
 * Handles serializing and deserializing objects for use in annotated configurations.
 */
public abstract class Serializer {
	private boolean allowsNullValue;

	public Object deserialize(GenericType type, Object value) {
		if (value == null && !allowsNullValue()) {
			return null;
		}

		if (isApplicableDeserialize(type, value) && (getParametersRequired() == -1
				|| type.getGenerics().length == getParametersRequired())) {
			return handleDeserialize(type, value);
		} else {
			return null;
		}
	}

	public Object serialize(GenericType type, Object value) {
		if (value == null && !allowsNullValue()) {
			return null;
		}

		if (isApplicableSerialize(type, value) && (getParametersRequired() == -1
				|| type.getGenerics().length == getParametersRequired())) {
			return handleSerialize(type, value);
		} else {
			return null;
		}
	}

	protected abstract Object handleDeserialize(GenericType type, Object value);

	protected Object handleSerialize(GenericType type, Object value) {
		return value;
	}

	public boolean isApplicableDeserialize(GenericType type, Object value) {
		return isApplicable(type);
	}

	public abstract boolean isApplicable(GenericType type);

	public boolean isApplicableSerialize(GenericType type, Object value) {
		return isApplicable(type);
	}

	protected abstract int getParametersRequired();

	public boolean allowsNullValue() {
		return allowsNullValue;
	}

	protected void setAllowsNullValue(boolean allowsNullValue) {
		this.allowsNullValue = allowsNullValue;
	}

	public static class NeededGenericsComparator implements Comparator<Serializer> {
		@Override
		public int compare(Serializer a, Serializer b) {
			return Integer.valueOf(a.getParametersRequired()).compareTo(b.getParametersRequired());
		}
	}
}
