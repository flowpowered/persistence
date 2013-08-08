/*
 * This file is part of Cerealization.
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * Cerealization is licensed under the Spout License Version 1.
 *
 * Cerealization is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Cerealization is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.cereal.util.config.annotated;

import org.junit.Assert;
import org.junit.Test;

import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.ConfigurationNode;
import org.spout.cereal.config.MapConfiguration;
import org.spout.cereal.config.annotated.AnnotatedObjectConfiguration;
import org.spout.cereal.config.annotated.Load;
import org.spout.cereal.config.annotated.Save;
import org.spout.cereal.config.annotated.Setting;

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
