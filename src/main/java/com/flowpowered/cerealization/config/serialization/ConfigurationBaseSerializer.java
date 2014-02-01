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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.MapConfiguration;
import com.flowpowered.cerealization.config.annotated.AnnotatedSubclassConfiguration;

public class ConfigurationBaseSerializer extends Serializer {
	private static final Map<Class<? extends AnnotatedSubclassConfiguration>,
			Constructor<? extends AnnotatedSubclassConfiguration>> CACHED_CONSTRUCTORS =
			new HashMap<Class<? extends AnnotatedSubclassConfiguration>, Constructor<? extends AnnotatedSubclassConfiguration>>();

	public ConfigurationBaseSerializer() {
		setAllowsNullValue(true);
	}

	@Override
	public boolean isApplicable(GenericType type) {
		return AnnotatedSubclassConfiguration.class.isAssignableFrom(type.getMainType());
	}

	@Override
	public boolean isApplicableDeserialize(GenericType type, Object value) {
		return super.isApplicableDeserialize(type, value) && (value == null || value instanceof Map);
	}

	@Override
	protected int getParametersRequired() {
		return -1;
	}

	@Override
	protected Object handleDeserialize(GenericType type, Object value) {
		if (value == null) {
			value = new HashMap<Object, Object>();
		}

		Class<? extends AnnotatedSubclassConfiguration> configClass = type.getMainType().asSubclass(AnnotatedSubclassConfiguration.class);
		Constructor<? extends AnnotatedSubclassConfiguration> constructor = CACHED_CONSTRUCTORS.get(configClass);
		if (constructor == null) {
			try {
				constructor = configClass.getDeclaredConstructor(Configuration.class);
				constructor.setAccessible(true);
			} catch (NoSuchMethodException e) {
				return null;
			}
			CACHED_CONSTRUCTORS.put(configClass, constructor);
		}
		AnnotatedSubclassConfiguration config = null;
		MapConfiguration rawConfig = new MapConfiguration((Map<?, ?>) value);

		try {
			config = constructor.newInstance(rawConfig);
		} catch (InstantiationException ignore) {
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.getCause().printStackTrace();
		}

		if (config != null) {
			try {
				config.load();
			} catch (ConfigurationException e) {
				e.printStackTrace();
				return null;
			}
		}

		return config;
	}

	@Override
	protected Object handleSerialize(GenericType type, Object val) {
		MapConfiguration config = new MapConfiguration();
		try {
			((AnnotatedSubclassConfiguration) val).save(config);
			config.save();
		} catch (ConfigurationException e) {
			return null;
		}
		return config.getMap();
	}
}
