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

import org.junit.Test;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationHolder;
import com.flowpowered.cerealization.config.ConfigurationHolderConfiguration;
import com.flowpowered.cerealization.config.MapConfiguration;

import static org.junit.Assert.assertEquals;

public class ConfigurationHolderConfigurationTest {
    private static class TestConfig extends ConfigurationHolderConfiguration {
        public static final int TEST_ONE_VALUE = 42;
        public static final ConfigurationHolder TEST_ONE = new ConfigurationHolder(TEST_ONE_VALUE, "test", "one");
        public static final String TEST_TWO_VALUE = "richard";
        public static final ConfigurationHolder TEST_TWO = new ConfigurationHolder(TEST_TWO_VALUE, "test", "two");

        public TestConfig(Configuration base) {
            super(base);
        }
    }

    @Test
    public void testConfigSet() throws ConfigurationException {
        TestConfig testConfig = new TestConfig(new MapConfiguration());
        testConfig.load();
        assertEquals(testConfig.getConfiguration(), TestConfig.TEST_ONE.getConfiguration());
        assertEquals(testConfig.getConfiguration(), TestConfig.TEST_TWO.getConfiguration());
    }

    @Test
    public void testSetDefaultValues() throws ConfigurationException {
        TestConfig testConfig = new TestConfig(new MapConfiguration());
        testConfig.load();
        assertEquals(TestConfig.TEST_ONE_VALUE, testConfig.getConfiguration().getNode(TestConfig.TEST_ONE.getPathElements()).getValue());
        assertEquals(TestConfig.TEST_TWO_VALUE, testConfig.getConfiguration().getNode(TestConfig.TEST_TWO.getPathElements()).getValue());
    }
}
