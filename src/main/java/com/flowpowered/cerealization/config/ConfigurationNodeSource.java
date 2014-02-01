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

import java.util.Map;
import java.util.Set;

public interface ConfigurationNodeSource {
	/**
	 * Gets a child of the current node. Remember that this returns a DIRECT child, without splitting at the {@link com.flowpowered.cerealization.config.Configuration#getPathSeparator()}
	 *
	 * @param name The name of the child
	 * @return The ConfigurationNode with the child's path.
	 * @see #getChild(String, boolean)
	 */
	public ConfigurationNode getChild(String name);

	/**
	 * Gets a child of the current node. Remember that this returns a DIRECT child, without splitting at the {@link com.flowpowered.cerealization.config.Configuration#getPathSeparator()}
	 *
	 * @param name The name of the child
	 * @param add Whether to store the configuration to the node structure.
	 * @return The ConfigurationNode with the child's path.
	 */
	public ConfigurationNode getChild(String name, boolean add);

	/**
	 * Adds the provided node as a child to this one As with the other methods handling children, this method only works with DIRECT children
	 *
	 * @param node The node to add as a child
	 * @return The previous node at the specified path (can be null)
	 */
	public ConfigurationNode addChild(ConfigurationNode node);

	/**
	 * Adds a new node as a child to this one Will overwrite existing
	 *
	 * @param name The path to the node to add
	 * @return The previous node at the specified path (can be null)
	 */
	public ConfigurationNode addNode(String name);

	/**
	 * Add the given children. The process for adding children is the same as that for {@link #addChild(ConfigurationNode)}
	 *
	 * @param nodes The nodes to add
	 * @see #addChild(ConfigurationNode)
	 */
	public void addChildren(ConfigurationNode... nodes);

	/**
	 * Remove the child at the specified path, if any, from the configuration structure
	 *
	 * @param key The name of the child
	 * @return The child at the key given, if any
	 */
	public ConfigurationNode removeChild(String key);

	/**
	 * Remove the specified child from the configuration structure
	 *
	 * @param node The node to remove
	 * @return null if unsuccessful, otherwise the node passed in
	 */
	public ConfigurationNode removeChild(ConfigurationNode node);

	/**
	 * Return the children of this node. The returned map is unmodifiable
	 *
	 * @return This node's children
	 */
	public Map<String, ConfigurationNode> getChildren();

	/**
	 * Return the raw Object values of this node's children ConfigurationNodes are converted into Map<String, Object>s for the result of this method
	 *
	 * @return The node's children values
	 */
	public Map<String, Object> getValues();

	/**
	 * Return the keys in this configuration. If {@code deep} is true, this will also include paths from child nodes, joined by {@link com.flowpowered.cerealization.config.Configuration#getPathSeparator()} Keys
	 * returned are relative to the current node.
	 *
	 * @param deep Whether to also fetch keys in children nodes of this configuration
	 * @return the keys in this configuration
	 */
	public Set<String> getKeys(boolean deep);

	/**
	 * Returns the node at the specified child path, splitting by the configuration's path separator. This can return both direct children and indirect children.
	 *
	 * @param path The path to get a node at
	 * @return The node at the specified path
	 * @see #getNode(String...) for more information on how this method behaves. (This is also the method called when a path given contains the path separator)
	 */
	public ConfigurationNode getNode(String path);

	/**
	 * Get a child node of this node source, going across multiple levels. If there is no node at the specified path a detached configuration node will be returned, which will be attached to {@link
	 * #getConfiguration()} if its value is set or a child is added.
	 *
	 * @param path The path elements to get to the requested node.
	 * @return The child node. Never null.
	 */
	public ConfigurationNode getNode(String... path);

	/**
	 * Returns whether this node source has children. This is the same as running {@code getChildren().size() > 0}
	 *
	 * @return whether this node source has children
	 */
	public boolean hasChildren();

	/**
	 * Returns whether the direct child (not checking children of children, etc) at {@code key} exists
	 *
	 * @param key The key to check
	 * @return Whether key is a direct child of this node
	 */
	public boolean hasChild(String key);

	/**
	 * Returns whether the node at {@code key} exists
	 *
	 * @param path The key to check
	 * @return Whether key is a direct child of this node
	 */
	public boolean hasNode(String... path);

	/**
	 * Returns the configuration this node source is attached to. This may return the same object if this {@link ConfigurationNodeSource} is a Configuration.
	 *
	 * @return the attached configuration.
	 */
	public Configuration getConfiguration();

	/**
	 * Returns the elements of the configuration path going to this node source. Can return an empty array if we are at the root of the tree.
	 *
	 * @return the elements to here
	 */
	public String[] getPathElements();
}
