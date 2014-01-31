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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serialization {
	private static final Map<Type, Serializer> CACHED_SERIALIZERS = new HashMap<Type, Serializer>();
	private static final List<Serializer> SERIALIZERS = new ArrayList<Serializer>(
			Arrays.asList(new SameSerializer(),
					new StringSerializer(),
					new BooleanSerializer(),
					new NumberSerializer(),
					new EnumSerializer(),
					new DateSerializer(),
					new ByteArraySerializer(),
					new ConfigurationBaseSerializer(),
					new SetSerializer(),
					new ListSerializer(),
					new MapSerializer()
			));

	public static Object deserialize(Type target, Object value) {
		return deserialize(new GenericType(target), value);
	}

	public static Object deserialize(GenericType type, Object value) {
		Object ret;
		for (Serializer serializer : SERIALIZERS) {
			if ((ret = serializer.deserialize(type, value)) != null) {
				CACHED_SERIALIZERS.put(type.getRawType(), serializer);
				return ret;
			}
		}
		return null;
	}

	public static Object serialize(Type type, Object obj) {
		return serialize(new GenericType(type), obj);
	}

	public static Object serialize(GenericType type, Object obj) {
		Serializer serializer = CACHED_SERIALIZERS.get(type.getRawType());
		if (serializer != null && serializer.isApplicableSerialize(type, obj)) {
			return serializer.serialize(type, obj);
		} else {
			Object ret;
			for (Serializer ser : SERIALIZERS) {
				if ((ret = ser.serialize(type, obj)) != null) {
					CACHED_SERIALIZERS.put(type.getRawType(), ser);
					return ret;
				}
			}
		}
		return obj;
	}

	public static void registerSerializer(Serializer serializer) {
		SERIALIZERS.add(serializer);
	}
}
