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
package com.flowpowered.cerealization.util.config;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationNode;
import com.flowpowered.cerealization.config.MapConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {
	private MapConfiguration config;

	@Before
	public void setUp() throws ConfigurationException {
		config = createConfiguration();
		config.load();
	}

	public Map<Object, Object> getConfigMap() {
		Map<Object, Object> newData = new HashMap<Object, Object>();
		newData.put("string-type", "someString");
		newData.put("int-type", 45);
		Map<Object, Object> testNested = new HashMap<Object, Object>();
		testNested.put("bar", "baz");
		newData.put("foo", testNested);
		return newData;
	}

	public MapConfiguration createConfiguration() {
		return new MapConfiguration(getConfigMap());
	}

	@Test
	public void testLoadSave() throws ConfigurationException {
		config.load();
		config.save();
		assertEquals(getConfigMap(), config.getMap());
	}

	@Test
	public void testGetNode() {
		ConfigurationNode node = config.getNode("string-type");
		assertEquals("someString", node.getValue());
		node = config.getNode("foo.bar");
		assertEquals("baz", node.getValue());
	}

	@Test
	public void testGetNewNode() {
		ConfigurationNode node = config.getNode("unknown.node");
		assertTrue(node != null);
		assertEquals(null, node.getValue());
		assertFalse(node.isAttached());
		assertEquals(null, node.getParent());
	}

	private static final String TEST_PATH = "another.unknown.node";
	private static final String TEST_VALUE = "Never gonna give you up!";

	@Test
	public void testSetNewNode() {
		ConfigurationNode node = config.getNode(TEST_PATH);
		assertEquals(null, node.getValue());
		node.setValue(TEST_VALUE);
		assertEquals(TEST_VALUE, node.getString());
		assertEquals(node, config.getNode(TEST_PATH));
		assertEquals(TEST_VALUE, config.getNode(TEST_PATH).getString());
	}

	@Test
	public void testPathSeparator() {
		String actualValue = config.getNode("foo", "bar").getString();
		String value = config.getNode("foo.bar").getString();
		assertEquals(actualValue, value);
		config.setPathSeparator("/");
		value = config.getNode("foo/bar").getString();
		assertEquals(actualValue, value);
		config.setPathSeparator(".");
	}
}
