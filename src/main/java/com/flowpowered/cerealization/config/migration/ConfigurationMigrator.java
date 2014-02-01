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
package com.flowpowered.cerealization.config.migration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationNode;
import com.flowpowered.cerealization.config.FileConfiguration;

/**
 * A simple migrator for configurations that moves values from one key to another. It can also convert values
 */
public abstract class ConfigurationMigrator {
	private final Configuration configuration;

	protected ConfigurationMigrator(Configuration configuration) {
		Validate.notNull(configuration);
		this.configuration = configuration;
	}

	/**
	 * Put together a collection of all keys to be migrated and their associated actions. This can be put in a static HashMap, or generated on each invocation
	 *
	 * @return The map of configuration keys to their associated actions
	 */
	protected abstract Map<String[], MigrationAction> getMigrationActions();

	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * This method checks whether migration is needed on the {@link Configuration} this instance is constructed with
	 *
	 * @return Whether migration is needed
	 */
	protected abstract boolean shouldMigrate();

	/**
	 * Perform migration of the configuration this object was constructed with If migration was not necessary ({@link #shouldMigrate()} returned false), the method invocation will be considered
	 * successful. If {@link #configuration} is a {@link com.flowpowered.cerealization.config.FileConfiguration}, the file the configuration vas previously stored in will be moved to (file name).old as a backup of
	 * the data before migration
	 *
	 * @throws MigrationException if the configuration could not be successfully migrated
	 */
	public void migrate() throws MigrationException {
		if (!shouldMigrate()) {
			return;
		}

		if (configuration instanceof FileConfiguration) {
			File oldFile = ((FileConfiguration) configuration).getFile();
			try {
				FileUtils.moveFile(oldFile, new File(oldFile.getAbsolutePath() + ".old"));
			} catch (IOException e) {
				throw new MigrationException(e);
			}
		}

		for (Map.Entry<String[], MigrationAction> entry : getMigrationActions().entrySet()) {
			final ConfigurationNode existingNode = configuration.getNode(entry.getKey());
			final Object existing = existingNode.getValue();
			existingNode.remove();
			if (existing == null || entry.getValue() == null) {
				continue;
			}
			final String[] newKey = entry.getValue().convertKey(entry.getKey());
			final Object newValue = entry.getValue().convertValue(existing);
			configuration.getNode(newKey).setValue(newValue);
		}
		try {
			configuration.save();
		} catch (ConfigurationException e) {
			throw new MigrationException(e);
		}
	}
}
