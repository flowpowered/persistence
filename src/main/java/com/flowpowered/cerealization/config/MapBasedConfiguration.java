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
package com.flowpowered.cerealization.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A base class for configurations that load their values from a {@link Map}
 */
public abstract class MapBasedConfiguration extends AbstractConfiguration {
	/**
	 * Implementations can use this method to provide the necessary data for calls of load.
	 *
	 * @return A map with raw configuration data
	 * @throws ConfigurationException when an error occurs while loading.
	 */
	protected abstract Map<?, ?> loadToMap() throws ConfigurationException;

	/**
	 * Save the  data from this configuration. This method is called from {@link #save()}
	 *
	 * @param map Configuration as a set of nested Maps
	 * @throws ConfigurationException When an error occurs while saving the given data.
	 */
	protected abstract void saveFromMap(Map<?, ?> map) throws ConfigurationException;

	@Override
	protected Map<String, ConfigurationNode> loadToNodes() throws ConfigurationException {
		Map<?, ?> items = loadToMap();
		Map<String, ConfigurationNode> children = new LinkedHashMap<String, ConfigurationNode>();
		for (Map.Entry<?, ?> entry : items.entrySet()) {
			children.put(entry.getKey().toString(), createConfigurationNode(new String[] {entry.getKey().toString()}, entry.getValue()));
		}
		return children;
	}

	@Override
	protected void saveFromNodes(Map<String, ConfigurationNode> nodes) throws ConfigurationException {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, ConfigurationNode> entry : getChildren().entrySet()) {
			ret.put(entry.getKey(), entry.getValue().getValue());
		}
		saveFromMap(ret);
	}
}
