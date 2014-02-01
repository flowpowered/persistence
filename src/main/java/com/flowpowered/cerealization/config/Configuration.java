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
package com.flowpowered.cerealization.config;

import java.util.regex.Pattern;

public interface Configuration extends ConfigurationNodeSource {
	/**
	 * Load the configuration's values
	 *
	 * @throws ConfigurationException if an error occurs while loading the configuration
	 */
	void load() throws ConfigurationException;

	/**
	 * Save the configuration's values
	 *
	 * @throws com.flowpowered.cerealization.config.ConfigurationException when an error occurs
	 */
	void save() throws ConfigurationException;

	/**
	 * Adds the given node to the configuration structure This will attempt to use the node's existing parents in the configuration structure where possible
	 *
	 * @param node The node to add
	 */
	void setNode(ConfigurationNode node);

	/**
	 * The path separator to use with {@link #getNode(String)} The path separator splits paths as a literal string, not a regular expression.
	 *
	 * @return The configuration's path separator
	 */
	String getPathSeparator();

	/**
	 * Sets this configuration's path separator. More information on how the path separator functions in {@link #getPathSeparator()}
	 *
	 * @param pathSeparator The path separator
	 * @see #getPathSeparator()
	 */
	void setPathSeparator(String pathSeparator);

	Pattern getPathSeparatorPattern();

	/**
	 * Whether this configuration writes default values (from {@link ConfigurationNode#getValue(Object)} to the configuration structure
	 *
	 * @return Whether this configuration writes defaults
	 */
	boolean doesWriteDefaults();

	/**
	 * Sets whether this configuration writes defaults
	 *
	 * @param writesDefaults Whether this configuration writes defaults
	 * @see #doesWriteDefaults() for info on what this means
	 */
	void setWritesDefaults(boolean writesDefaults);

	/**
	 * Split the provided path into a string array suitable for accessing the correct configuration children. Normally this just splits the path with the {@link #getPathSeparator()}, but can limit how
	 * deep a child path can go or whether this configuration can even have children.
	 *
	 * @param path The path to split
	 * @return The connectly split path.
	 */
	String[] splitNodePath(String path);

	/**
	 * Make sure the provided path meets the requirements. A correct implementation of Configuration will impose the same restrictions on this and {@link #splitNodePath(String)}, so invoking this method
	 * on an array from {@link #splitNodePath(String)} would return the original array.
	 *
	 * @param rawPath The raw path of the configuration
	 * @return The corrected input path
	 */
	String[] ensureCorrectPath(String[] rawPath);
}
