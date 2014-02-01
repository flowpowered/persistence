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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListSerializer extends Serializer {
	@Override
	public boolean isApplicable(GenericType type) {
		return List.class.equals(type.getMainType());
	}

	@Override
	public boolean isApplicableDeserialize(GenericType type, Object value) {
		return super.isApplicableDeserialize(type, value) && value instanceof Collection<?>;
	}

	@Override
	public int getParametersRequired() {
		return 1;
	}

	@Override
	protected Object handleDeserialize(GenericType type, Object value) {
		List<Object> values = new ArrayList<Object>();
		Collection<?> raw = (Collection<?>) value;
		for (Object obj : raw) {
			values.add(Serialization.deserialize(type.getGenerics()[0], obj));
		}
		return values;
	}

	@Override
	protected Object handleSerialize(GenericType type, Object value) {
		List<Object> values = new ArrayList<Object>();
		Collection<?> raw = (Collection<?>) value;
		for (Object obj : raw) {
			values.add(Serialization.serialize(type.getGenerics()[0], obj));
		}
		return values;
	}
}
