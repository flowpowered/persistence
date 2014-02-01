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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.MapConfiguration;
import com.flowpowered.cerealization.config.ini.IniConfiguration;
import com.flowpowered.cerealization.config.migration.ConfigurationMigrator;
import com.flowpowered.cerealization.config.migration.MigrationAction;
import com.flowpowered.cerealization.config.migration.MigrationException;
import com.flowpowered.cerealization.config.migration.NewJoinedKey;
import com.flowpowered.cerealization.config.migration.NewKey;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationMigratorTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private class TestConfigurationMigrator extends ConfigurationMigrator {
		private final Map<String[], MigrationAction> migrationActions;

		protected TestConfigurationMigrator(Configuration configuration, Map<String[], MigrationAction> migrationActions) {
			super(configuration);
			this.migrationActions = migrationActions;
		}

		@Override
		protected Map<String[], MigrationAction> getMigrationActions() {
			return migrationActions;
		}

		@Override
		protected boolean shouldMigrate() {
			return true; // Always migrate so we can test easily
		}
	}

	@Test
	public void testNewJoinedKeyMigrationAction() {
		ConfigurationMigrator migrator = new TestConfigurationMigrator(new MapConfiguration(), Collections.<String[], MigrationAction>emptyMap());
		NewJoinedKey action = new NewJoinedKey("now.before.%", migrator.getConfiguration());
		assertArrayEquals(new String[] {"now", "before", "input", "key"}, action.convertKey(new String[] {"input", "key"}));
	}

	@Test
	public void testConfigurationFileMoved() throws IOException, ConfigurationException, MigrationException {
		File testFile = folder.newFile("test.ini");
		IniConfiguration testConfig = new IniConfiguration(testFile);
		testConfig.getNode("test", "node").setValue("node-value");
		testConfig.getNode("test", "node2").setValue("node2-value");
		testConfig.getNode("test2", "node").setValue("node-value");
		testConfig.getNode("test2", "node2").setValue("node2-value");
		testConfig.save();
		ConfigurationMigrator migrator = new TestConfigurationMigrator(testConfig, Collections.<String[], MigrationAction>emptyMap());
		migrator.migrate();
		assertTrue(new File(testFile.getAbsolutePath() + ".old").isFile());
	}

	//@Test(expected = MigrationException.class)
	@Test
	public void testErrorOnOldFileExisting() throws IOException, ConfigurationException, MigrationException {
		File testNewFile = folder.newFile("testOldFileExisting.ini");
		IniConfiguration config = new IniConfiguration(testNewFile);
		config.getNode("test", "node").setValue("test value");
		config.save();

		ConfigurationMigrator migrator = new TestConfigurationMigrator(config, Collections.<String[], MigrationAction>emptyMap());
		migrator.migrate();
	}

	@Test
	public void testConfigurationKeyMove() throws MigrationException {
		Configuration testConfig = new MapConfiguration();
		ConfigurationMigrator testMigrator = new TestConfigurationMigrator(testConfig, Collections.<String[], MigrationAction>singletonMap(new String[] {"test", "key"}, new NewKey("test2", "key2")));
		final Object obj = new Object();
		testConfig.getNode("test", "key").setValue(obj);
		testMigrator.migrate();

		assertEquals(obj, testConfig.getNode("test2", "key2").getValue());
	}
}
