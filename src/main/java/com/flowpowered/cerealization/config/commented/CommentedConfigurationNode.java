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
package com.flowpowered.cerealization.config.commented;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationNode;

/**
 * A ConfigurationNode type that also stores comments. These normally exist in {@link CommentedConfiguration}s.
 */
public class CommentedConfigurationNode extends ConfigurationNode {
	public static final String LINE_SEPARATOR;

	static {
		String sep = System.getProperty("line.separator");
		if (sep == null) {
			sep = "\n";
		}
		LINE_SEPARATOR = sep;
	}

	private String[] comment;

	public CommentedConfigurationNode(Configuration config, String[] path, Object value) {
		super(config, path, value);
	}

	/**
	 * Returns the comment lines attached to this configuration node Will return null if this node doesn't have a comment
	 *
	 * @return The comment for this node
	 */
	public String[] getComment() {
		return comment;
	}

	/**
	 * Sets the comment that is attached to this configuration node. In this method the comment is provided as one line, containing the line separator character
	 *
	 * @param comment The comment to set
	 */
	public void setComment(String comment) {
		checkAdded();
		this.comment = comment.split(LINE_SEPARATOR);
	}

	/**
	 * Sets the comment of the configuration, already split by line
	 *
	 * @param comment The comment lines
	 */
	public void setComment(String... comment) {
		checkAdded();
		this.comment = comment;
	}

	@Override
	public CommentedConfigurationNode createConfigurationNode(String[] path, Object value) {
		return new CommentedConfigurationNode(getConfiguration(), path, value);
	}
}
