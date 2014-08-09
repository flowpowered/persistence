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
import java.util.HashSet;
import java.util.Set;

import com.flowpowered.cerealization.ReflectionUtils;
import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationNode;
import com.flowpowered.cerealization.config.ConfigurationNodeSource;

/**
 * The base class for annotated configurations Annotated configurations are created by subclassing AnnotatedConfiguration and having fields annotated with "@Setting"
 */
public abstract class AnnotatedSubclassConfiguration extends AnnotatedConfiguration {
	private final Set<Field> fields = new HashSet<Field>();
	private boolean fieldsCached = false;
	private boolean isConfigured;

	public AnnotatedSubclassConfiguration(Configuration baseConfig) {
		super(baseConfig);
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	@SuppressWarnings ("unchecked")
	private Set<Field> getFields() {
		if (!fieldsCached) {
			fields.addAll(ReflectionUtils.getDeclaredFieldsRecur(getClass(), Setting.class));
			fieldsCached = true;
		}
		return fields;
	}

	@Override
	public void load(ConfigurationNodeSource source) throws ConfigurationException {
		for (Field field : getFields()) {
			field.setAccessible(true);
			String[] key = field.getAnnotation(Setting.class).value();
			if (key.length == 0) {
				key = new String[] {field.getName()};
			}
			ConfigurationNode node = source.getNode(key);
			final Object value = node.getTypedValue(field.getGenericType());
			try {
				if (value != null) {
					field.set(this, value);
				} else {
					node.setValue(field.getGenericType(), field.get(this));
				}
			} catch (IllegalAccessException e) {
				throw new ConfigurationException(e);
			}
		}
		isConfigured = true;
	}

	@Override
	public void save(ConfigurationNodeSource source) throws ConfigurationException {
		for (Field field : getFields()) {
			field.setAccessible(true);
			String[] key = field.getAnnotation(Setting.class).value();
			if (key.length == 0) {
				key = new String[] {field.getName()};
			}
			try {
				source.getNode(key).setValue(field.getGenericType(), field.get(this));
			} catch (IllegalAccessException e) {
				throw new ConfigurationException(e);
			}
		}
	}
}
