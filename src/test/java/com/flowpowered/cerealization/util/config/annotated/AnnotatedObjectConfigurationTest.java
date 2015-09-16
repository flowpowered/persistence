/*
 * This file is part of Flow Persistence, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Flow Powered <https://flowpowered.com/>
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
package com.flowpowered.cerealization.util.config.annotated;

import org.junit.Assert;
import org.junit.Test;

import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationNode;
import com.flowpowered.cerealization.config.MapConfiguration;
import com.flowpowered.cerealization.config.annotated.AnnotatedObjectConfiguration;
import com.flowpowered.cerealization.config.annotated.Load;
import com.flowpowered.cerealization.config.annotated.Save;
import com.flowpowered.cerealization.config.annotated.Setting;

public class AnnotatedObjectConfigurationTest {
    // Save settings
    private static final String SAVE_TEST_OBJECT_STRING = "save test";
    private static final int SAVE_TEST_OBJECT_INT = 1234;
    private static final String SAVE_TEST_CONFIG_OBJECT_STRING = "save object test";
    private static final int SAVE_TEST_CONFIG_OBJECT_INT = 5678;
    private static final double SAVE_TEST_SPECIAL_NODE = 4356.2;
    // Load settings
    private static final String LOAD_TEST_OBJECT_STRING = "load test";
    private static final int LOAD_TEST_OBJECT_INT = 91234;
    private static final String LOAD_TEST_CONFIG_OBJECT_STRING = "load object test";
    private static final int LOAD_TEST_CONFIG_OBJECT_INT = 95678;
    private static final double LOAD_TEST_SPECIAL_NODE = 94356.2;

    @Test
    public void testSave() throws ConfigurationException {
        final AnnotatedObjectConfiguration annotated = new AnnotatedObjectConfiguration(new MapConfiguration());
        annotated.add(new TestObject(), "object");
        annotated.save();
        Assert.assertEquals(SAVE_TEST_OBJECT_STRING, annotated.getNode("object", "string").getString());
        Assert.assertEquals(SAVE_TEST_OBJECT_INT, annotated.getNode("object", "integer").getInt());
        Assert.assertEquals(SAVE_TEST_CONFIG_OBJECT_STRING, annotated.getNode("object", "config-object", "obj-string").getString());
        Assert.assertEquals(SAVE_TEST_CONFIG_OBJECT_INT, annotated.getNode("object", "config-object", "obj-integer").getInt());
        Assert.assertEquals(SAVE_TEST_SPECIAL_NODE, annotated.getNode("object", "special", "node").getDouble(), 0);
    }

    @Test
    public void testLoad() throws ConfigurationException {
        final MapConfiguration map = new MapConfiguration();
        map.getNode("object", "string").setValue(LOAD_TEST_OBJECT_STRING);
        map.getNode("object", "integer").setValue(LOAD_TEST_OBJECT_INT);
        map.getNode("object", "config-object", "obj-string").setValue(LOAD_TEST_CONFIG_OBJECT_STRING);
        map.getNode("object", "config-object", "obj-integer").setValue(LOAD_TEST_CONFIG_OBJECT_INT);
        map.getNode("object", "special", "node").setValue(LOAD_TEST_SPECIAL_NODE);
        final TestObject object = new TestObject();
        final AnnotatedObjectConfiguration annotated = new AnnotatedObjectConfiguration(map);
        annotated.add(object, "object");
        annotated.load(map);
        Assert.assertEquals(LOAD_TEST_OBJECT_STRING, object.string);
        Assert.assertEquals(LOAD_TEST_OBJECT_INT, object.integer);
        Assert.assertEquals(LOAD_TEST_CONFIG_OBJECT_STRING, object.configObject.string);
        Assert.assertEquals(LOAD_TEST_CONFIG_OBJECT_INT, object.configObject.integer);
        Assert.assertEquals(LOAD_TEST_SPECIAL_NODE, object.special, 0);
    }

    @Test
    public void testClass() throws ConfigurationException {
        {
            final AnnotatedObjectConfiguration annotated = new AnnotatedObjectConfiguration(new MapConfiguration());
            annotated.add(TestClass.class, "object");
            annotated.save();

            Assert.assertEquals(SAVE_TEST_OBJECT_STRING, annotated.getNode("object", "string").getString());
            Assert.assertEquals(SAVE_TEST_OBJECT_INT, annotated.getNode("object", "integer").getInt());
            Assert.assertEquals(SAVE_TEST_CONFIG_OBJECT_STRING, annotated.getNode("object", "config-object", "obj-string").getString());
            Assert.assertEquals(SAVE_TEST_CONFIG_OBJECT_INT, annotated.getNode("object", "config-object", "obj-integer").getInt());
            Assert.assertEquals(SAVE_TEST_SPECIAL_NODE, annotated.getNode("object", "special", "node").getDouble(), 0);
        }

        {
            final MapConfiguration map = new MapConfiguration();
            map.getNode("object", "string").setValue(LOAD_TEST_OBJECT_STRING);
            map.getNode("object", "integer").setValue(LOAD_TEST_OBJECT_INT);
            map.getNode("object", "config-object", "obj-string").setValue(LOAD_TEST_CONFIG_OBJECT_STRING);
            map.getNode("object", "config-object", "obj-integer").setValue(LOAD_TEST_CONFIG_OBJECT_INT);
            map.getNode("object", "special", "node").setValue(LOAD_TEST_SPECIAL_NODE);

            final AnnotatedObjectConfiguration annotated = new AnnotatedObjectConfiguration(map);
            annotated.add(TestClass.class, "object");
            annotated.load(map);

            Assert.assertEquals(LOAD_TEST_OBJECT_STRING, TestClass.string);
            Assert.assertEquals(LOAD_TEST_OBJECT_INT, TestClass.integer);
            Assert.assertEquals(LOAD_TEST_CONFIG_OBJECT_STRING, TestClass.configObject.string);
            Assert.assertEquals(LOAD_TEST_CONFIG_OBJECT_INT, TestClass.configObject.integer);
            Assert.assertEquals(LOAD_TEST_SPECIAL_NODE, TestClass.special, 0);
        }
    }

    private final static class TestClass {
        @Setting
        private static String string = SAVE_TEST_OBJECT_STRING;
        @Setting
        private static int integer = SAVE_TEST_OBJECT_INT;
        private static final TestConfigObject configObject = new TestConfigObject();
        @Setting ({"special", "node"})
        private static double special = SAVE_TEST_SPECIAL_NODE;

        @Load
        private static void load(ConfigurationNode node) {
            configObject.string = node.getNode("config-object", "obj-string").getString();
            configObject.integer = node.getNode("config-object", "obj-integer").getInt();
        }

        @Save
        private static void save(ConfigurationNode node) {
            final ConfigurationNode objectNode = node.getNode("config-object");
            objectNode.getNode("obj-string").setValue(configObject.string);
            objectNode.getNode("obj-integer").setValue(configObject.integer);
        }
    }

    private static class TestObject {
        @Setting
        private String string = SAVE_TEST_OBJECT_STRING;
        @Setting
        private int integer = SAVE_TEST_OBJECT_INT;
        private final TestConfigObject configObject = new TestConfigObject();
        @Setting ({"special", "node"})
        private double special = SAVE_TEST_SPECIAL_NODE;

        @Load
        private void load(ConfigurationNode node) {
            configObject.string = node.getNode("config-object", "obj-string").getString();
            configObject.integer = node.getNode("config-object", "obj-integer").getInt();
        }

        @Save
        private void save(ConfigurationNode node) {
            final ConfigurationNode objectNode = node.getNode("config-object");
            objectNode.getNode("obj-string").setValue(configObject.string);
            objectNode.getNode("obj-integer").setValue(configObject.integer);
        }
    }

    private static class TestConfigObject {
        private String string = SAVE_TEST_CONFIG_OBJECT_STRING;
        private int integer = SAVE_TEST_CONFIG_OBJECT_INT;
    }
}
