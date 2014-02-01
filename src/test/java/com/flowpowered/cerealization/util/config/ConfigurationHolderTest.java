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
package com.flowpowered.cerealization.util.config;

import org.junit.Test;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationHolder;
import com.flowpowered.cerealization.config.MapConfiguration;

import static org.junit.Assert.assertEquals;

public class ConfigurationHolderTest {
	@Test
	public void testGetWithDefaultValue() {
		Configuration config = new MapConfiguration();
		ConfigurationHolder subject = new ConfigurationHolder(config, (Object) "hello", "unknown", "path");
		assertEquals("hello", subject.getString());
	}

	@Test
	public void testGetExistingValue() {
		Configuration config = new MapConfiguration();
		config.getNode("path", "with", "value").setValue("valuehere");
		ConfigurationHolder subject = new ConfigurationHolder(config, (Object) null, "path", "with", "value");
		assertEquals("valuehere", subject.getValue());
	}

	@Test
	public void testGettingNewValueWritesToConfig() {
		Configuration config = new MapConfiguration();
		ConfigurationHolder subject = new ConfigurationHolder(config, (Object) "hello", "unknown", "path");
		subject.getValue();
		assertEquals("hello", config.getNode("unknown", "path").getString());
	}
}
