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

import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.ConfigurationNode;
import com.flowpowered.cerealization.config.commented.CommentedConfigurationNode;
import com.flowpowered.cerealization.config.ini.StringLoadingIniConfiguration;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static com.flowpowered.cerealization.config.commented.CommentedConfigurationNode.LINE_SEPARATOR;

public class IniConfigurationTest {
	@Test
	public void testBasicLoading() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration("[section]" + LINE_SEPARATOR + "node = value" + LINE_SEPARATOR);
		subject.load();
		ConfigurationNode sectionNode = subject.getNode("section");
		assertNotNull(sectionNode);
		assertTrue(sectionNode.isAttached());
		assertTrue(sectionNode.hasChildren());
		assertEquals("value", subject.getNode("section.node").getString());
	}

	@Test
	public void testBasicSaving() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration(null);
		subject.getNode("section.node").setValue("value");
		subject.save();
		assertEquals("[section]" + LINE_SEPARATOR + "node=value" + LINE_SEPARATOR, subject.getValue());
	}

	@Test
	public void testCommentLoading() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration("# This is the first section!" + LINE_SEPARATOR + "[section]" + LINE_SEPARATOR + "# This is a node!" + LINE_SEPARATOR + "# With a multiline comment!" + LINE_SEPARATOR + "node=value" + LINE_SEPARATOR);
		subject.load();
		ConfigurationNode node = subject.getNode("section");
		assertArrayEquals(new String[] {"This is the first section!"}, ((CommentedConfigurationNode) node).getComment());
		node = subject.getNode("section.node");
		assertArrayEquals(new String[] {"This is a node!", "With a multiline comment!"}, ((CommentedConfigurationNode) node).getComment());
	}

	@Test
	public void testCommentSaving() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration(null);
		subject.getNode("section").setComment("Hello", "World");
		subject.getNode("section", "node").setValue("value");
		subject.getNode("section", "node").setComment("Node Comment");
		subject.save();
		assertEquals("# Hello" + LINE_SEPARATOR + "# World" + LINE_SEPARATOR + "[section]" + LINE_SEPARATOR + "# Node Comment" + LINE_SEPARATOR + "node=value" + LINE_SEPARATOR, subject.getValue());
	}
}
