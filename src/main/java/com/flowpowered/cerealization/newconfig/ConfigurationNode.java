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
package com.flowpowered.cerealization.newconfig;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.data.AbstractValueHolder;
import com.flowpowered.cerealization.data.ValueHolderBase;

/**
 * A class for configuration nodes.
 */
public class ConfigurationNode extends AbstractValueHolder {
	private List<ConfigurationNode> listCache = null;
	private Map<String, ConfigurationNode> mapCache = null;
	private boolean resolved = false;
	private ValueHolderBase holder;
	private String name;
	private Object value;
	private final Configuration config;
	private ConfigurationNode parent = null;

	/**
	 * Creates a new ConfigurationNode with given Configuration, node name, and value.
	 * 
	 * @param config               the Configuration
	 * @param name                  the name
	 * @param value                 the value
	 * @throws IllegalArgumentException if the config is null
	 */
	public ConfigurationNode(Configuration config, String name, Object value) {
		if (config == null) {
			throw new IllegalArgumentException("config can't be null");
		}
		this.config = config;
		this.value = value;
		this.name = name;
		this.holder = new ValueHolderBase(this);
	}

	/**
	 * Creates a new ConfigurationNode with given parent node, node name, and value.
	 * 
	 * @param parent  the parent node
	 * @param name    the name
	 * @param value   the value
	 */
	protected ConfigurationNode(ConfigurationNode parent, String name, Object value) {
		this(parent.getConfiguration(), name, value);
		this.parent = parent;
	}

	/**
	 * Checks if the node has a parent node.
	 * 
	 * @return {@code true} if has, {@code false} otherwise
	 */
	public boolean hasParent() {
		return this.parent != null;
	}

	/**
	 * Retuns the parent node of this node.
	 * 
	 * @return the parent node
	 */
	public ConfigurationNode getParent() {
		return this.parent;
	}

	/**
	 * Sets the parent node of this node.
	 * 
	 * @param parent  the node to set as parent
	 */
	protected void setParent(ConfigurationNode parent) {
		this.parent = parent;
	}

	protected String normalizePath(String path) {
		if (this.config.isCaseSensitive() || path == null) {
			return path;
		}
		return path.toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Returns a list of names of all nodes in the path to this node.
	 * 
	 * @return  a list of path elements
	 */
	public List<String> getPathElements() {
		List<String> elements;
		if (hasParent()) {
			elements = getParent().getPathElements();
		} else {
			elements = new ArrayList<String>();
		}
		if (this.name != null) {
			elements.add(this.name);
		}
		return elements;
	}

	/**
	 * Returns the path of the node, separated by the node's Configuration's path separator.
	 * 
	 * @return the path
	 */
	public String getPath() {
		return StringUtils.join(getPathElements(), this.config.getPathSeparator());
	}

	/**
	 * Returns the Configuration of the node.
	 * 
	 * @return the Configuration
	 */
	public Configuration getConfiguration() {
		return this.config;
	}

	/**
	 * Checks if this node is a map.
	 * 
	 * @return {@code true} if it's a map, {@code false} otherwise
	 */
	public boolean isMap() {
		if (this.resolved) {
			return this.mapCache != null;
		}
		return this.value instanceof Map<?, ?>;
	}

	/**
	 * Checks if this node is a list.
	 * 
	 * @return {@code true} if it's a list, {@code false} otherwise
	 */
	public boolean isList() {
		if (this.resolved) {
			return this.listCache != null;
		}
		return this.value instanceof Collection<?>;
	}

	/**
	 * Checks if this node is a scalar (a node without child nodes).
	 * 
	 * @return {@code true} if it's a scalar, {@code false} otherwise
	 */
	public boolean isScalar() {
		return !isMap() && !isList();
	}

	/**
	 * Checks if this node's children has been loaded.
	 * <p>
	 * Only map or list nodes can be resolved.
	 * 
	 * @return {@code true} if resolved, {@code false} otherwise
	 */
	public boolean isResolved() {
		return this.resolved;
	}

	/**
	 * Returns a child node of this node with specified name.
	 * <p>
	 * Only map nodes can look up children by name.
	 * 
	 * @param name           name of the child node
	 * @return               the child, or {@code null} if doesn't exist
	 * @throws ConfigurationException if the node is not a map
	 */
	public ConfigurationNode getChild(String name) throws ConfigurationException {
		return getChild(name, false);
	}

	/**
	 * Returns a child node of this node with specified name.
	 * <p>
	 * Only map nodes can look up children by name.
	 * <p>
	 * If the child doesn't exist, and {@code add} is {@code true}, then creates one. If the node is null, and attempting to create the child, the node will be turned into a map.
	 * 
	 * @param name           name of the child node
	 * @param add            weather or not create the child if doesn't exist
	 * @return               the child, or {@code null} if doesn't exist and not being created
	 * @throws ConfigurationException if the node is not a map and not null, or if it's null and add is false
	 */
	public ConfigurationNode getChild(String name, boolean add) throws ConfigurationException {
		if (add && (isMap() || isNull()) && !hasChild(name)) {
			return addChild(name, null);
		}
		return getChildrenMap().get(normalizePath(name));
	}

	/**
	 * Checks if the node has a child node with specified name.
	 * <p>
	 * Only map nodes can look up children by name. For other types of nodes will always return {@code false}.
	 * 
	 * @param name  name of the child node
	 * @return      {@code true} if has, {@code false} otherwise
	 */
	public boolean hasChild(String name) {
		if (!isMap()) {
			return false;
		}
		try {
			return getChildrenMap().containsKey(normalizePath(name));
		} catch (ConfigurationException e) {
			return false;
		}
	}

	/**
	 * Returns map of child nodes of this map node.
	 * 
	 * @return               a map of child nodes
	 * @throws ConfigurationException if the node is not a map
	 */
	public Map<String, ConfigurationNode> getChildrenMap() throws ConfigurationException {
		if (!isMap()) {
			throw new ConfigurationException("Node is not a map!", getPath());
		}
		if (!this.resolved) {
			this.mapCache = new HashMap<String, ConfigurationNode>();
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.holder.getValue()).entrySet()) {
				String childName = normalizePath(entry.getKey().toString());
				this.mapCache.put(childName, new ConfigurationNode(this, childName, entry.getValue()));
			}
			this.value = null;
			this.resolved = true;
		}
		return new HashMap<String, ConfigurationNode>(this.mapCache);
	}

	/**
	 * Returns map of child nodes of this list node or map node.
	 * 
	 * @return               a list of child nodes
	 * @throws ConfigurationException if the node is a scalar
	 */
	public List<ConfigurationNode> getChildrenList() throws ConfigurationException {
		if (isMap()) {
			if (this.resolved) {
				return new ArrayList<ConfigurationNode>(this.mapCache.values());
			}
			return new ArrayList<ConfigurationNode>(getChildrenMap().values());
		}
		if (!isList()) {
			throw new ConfigurationException("Node is not a list!", getPath());
		}
		if (!this.resolved) {
			this.listCache = new ArrayList<ConfigurationNode>();
			for (Object o : (Collection<?>) this.holder.getValue()) {
				this.listCache.add(new ConfigurationNode(this, "", o));
			}
			this.value = null;
			this.resolved = true;
		}
		return new ArrayList<ConfigurationNode>(this.listCache);
	}

	/**
	 * Adds a nameless child node to this node.
	 * <p>
	 * Adding nameless children works only for list nodes.
	 * <p>
	 * If this node is null, it will be turned into a list node.
	 * 
	 * @param value           value of the new node
	 * @return                the added node
	 * @throws ConfigurationException  if this node is not a list
	 */
	public ConfigurationNode addChild(Object value) throws ConfigurationException {
		return addChild("", value);
	}

	/**
	 * Adds a child node to this node.
	 * <p>
	 * Adding child nodes doesn't work for scalar nodes.
	 * <p>
	 * If the node is a map, and a child with specified name already exists, it will be overriden.
	 * <p>
	 * If this node is null, it will be turned into a map node, or list node if the name is empty.
	 * 
	 * @param name            name of the new node
	 * @param value           value of the new node
	 * @return                the added node
	 * @throws ConfigurationException  if this node is a non-null scalar or the {@code name} is empty and the node is not a list
	 */
	public ConfigurationNode addChild(String name, Object value) throws ConfigurationException {
		if (isScalar()) {
			if (isNull()) {
				if (name == null || name.isEmpty()) {
					this.listCache = new ArrayList<ConfigurationNode>();
				} else {
					this.mapCache = new HashMap<String, ConfigurationNode>();
				}
				this.resolved = true;
			} else {
				throw new ConfigurationException("Can't add child to scalar node", getPath());
			}
		}
		ConfigurationNode node;
		name = normalizePath(name);
		if (!this.resolved) {
			getChildrenList(); // This can resolve both Map and List
		}
		if (isList()) {
			node = new ConfigurationNode(this, "", value);
			this.listCache.add(node);
			return node;
		}
		if (name == null || name.isEmpty()) {
			throw new ConfigurationException("Can't add nameless child to a map node", getPath());
		}
		node = new ConfigurationNode(this, name, value);
		this.mapCache.put(name, node);
		return node;
	}

	/**
	 * Adds specified node as a child node of this node.
	 * <p>
	 * Adding child nodes doesn't work for scalar nodes.
	 * <p>
	 * If the node is a map, and a child with the same name already exists, it will be overriden.
	 * Note that a copy of specified node will be added, not the node itself.
	 * <p>
	 * If this node is null, it will be turned into a map node, or list node if the name is empty.
	 * 
	 * @param node           the node to be added
	 * @return               a copy of specified node, being a child of this node
	 * @throws ConfigurationException if this node is a non-null scalar or the name of the specified node is empty and this node is not a list
	 */
	public ConfigurationNode addChild(ConfigurationNode node) throws ConfigurationException {
		return addChild(node.getName(), node.dump());
	}

	/**
	 * Adds specified nodes as child nodes of this node.
	 * <p>
	 * Adding child nodes doesn't work for scalar nodes.
	 * <p>
	 * If the node is a map, and a child with the same name already exists, it will be overriden.
	 * Note that a copy of specified nodes will be added, not the nodes themselves.
	 * <p>
	 * If this node is null, it will be turned into a map node.
	 * 
	 * @param nodes          nodes to be added
	 * @throws ConfigurationException if this node is a non-null scalar, or this node is not a list and one of {@code nodes} has empty name
	 */
	public void addChildren(ConfigurationNode... nodes) throws ConfigurationException {
		if (isScalar()) {
			if (isNull()) {
				this.mapCache = new HashMap<String, ConfigurationNode>();
				this.resolved = true;
			} else {
				throw new ConfigurationException("Can't add child to scalar node", getPath());
			}
		}
		if (!this.resolved) {
			getChildrenList(); // This can resolve both Map and List
		}
		if (isList()) {
			for (ConfigurationNode node : nodes) {
				this.listCache.add(new ConfigurationNode(this, "", node.dump()));
			}
			return;
		}
		for (ConfigurationNode node : nodes) {
			String name = normalizePath(node.getName());
			if (name == null || name.isEmpty()) {
				throw new ConfigurationException("Can't add nameless child to a map node", getPath());
			}
			this.mapCache.put(name, new ConfigurationNode(this, name, node.dump()));
		}
	}

	/**
	 * Adds all entries of specified map as new child nodes of this node.
	 * <p>
	 * Adding child nodes doesn't work for scalar nodes.
	 * <p>
	 * If the node is a map, and a child with the same name already exists, it will be overriden.
	 * <p>
	 * If this node is null, it will be turned into a map node.
	 * 
	 * @param map            the map
	 * @throws ConfigurationException if this node is a non-null scalar, or this node is not a list and the {@code map} has empty text keys
	 */
	public void addChildren(Map<?, ?> map) throws ConfigurationException {
		if (isScalar()) {
			if (isNull()) {
				this.mapCache = new HashMap<String, ConfigurationNode>();
				this.resolved = true;
			} else {
				throw new ConfigurationException("Can't add child to scalar node", getPath());
			}
		}
		if (!this.resolved) {
			getChildrenList(); // This can resolve both Map and List
		}
		if (isList()) {
			for (Object value : map.values()) {
				this.listCache.add(new ConfigurationNode(this, "", value));
			}
			return;
		}
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			String name = entry.getKey().toString();
			if (name == null || name.isEmpty()) {
				throw new ConfigurationException("Can't add nameless child to a map node", getPath());
			}
			this.mapCache.put(name, new ConfigurationNode(this, name, entry.getValue()));
		}
	}

	/**
	 * Adds all elements of specified collection as new child nodes of this list node.
	 * <p>
	 * If this node is null, it will be turned into a list node.
	 * 
	 * @param collection     the collection
	 * @throws ConfigurationException if this node is not a list node and not null
	 */
	public void addChildren(Collection<?> collection) throws ConfigurationException {
		if (!isList()) {
			if (isNull()) {
				this.listCache = new ArrayList<ConfigurationNode>();
				this.resolved = true;
			} else {
				throw new ConfigurationException("Can't add nameless child to non-list node", getPath());
			}
		}
		if (!this.resolved) {
			getChildrenList();
		}
		for (Object value : collection) {
			this.listCache.add(new ConfigurationNode(this, "", value));
		}
	}

	/**
	 * Returns number of children of the node.
	 * 
	 * @return number of children
	 */
	public int getChildrenCount() {
		if (isScalar()) {
			return 0;
		}
		if (!this.resolved) {
			try {
				getChildrenList(); // This can resolve both Map and List
			} catch (ConfigurationException e) {
				e.printStackTrace();
				return 0;
			}
		}
		if (isList()) {
			return this.listCache.size();
		}
		return this.mapCache.size();
	}

	/**
	 * Returns list of final (scalar) nodes among descendants of this node
	 * 
	 * @return list of final nodes
	 */
	public List<ConfigurationNode> getFinalNodeList() {
		List<ConfigurationNode> list = new ArrayList<ConfigurationNode>();
		if (isScalar()) {
			list.add(this);
		} else {
			try {
				for (ConfigurationNode node : getChildrenList()) {
					list.addAll(node.getFinalNodeList());
				}
			} catch (ConfigurationException e) {
				throw new IllegalStateException("The node wasn't a scalar but we got the exception:", e);
			}
		}
		return list;
	}

	/**
	 * Returns number of final (scalar) nodes among descendants of this node
	 * 
	 * @return number of final nodes
	 */
	public int getFinalNodeCount() {
		if (isScalar()) {
			return 1;
		}
		int count = 0;
		try {
			for (ConfigurationNode node : getChildrenList()) {
				count += node.getFinalNodeCount();
			}
		} catch (ConfigurationException e) {
			throw new IllegalStateException("The node wasn't a scalar but we got the exception:", e);
		}
		return count;
	}

	/**
	 * Removes a child node with specified name from this node's children.
	 * 
	 * @param name  name of the child to remove
	 * @return      the removed child, or {@code null} if didn't remove anything
	 */
	public ConfigurationNode removeChild(String name) {
		name = normalizePath(name);
		if (hasChild(name)) {
			try {
				return removeChild(getChild(name));
			} catch (ConfigurationException e) {
				throw new IllegalStateException("The child was present but we got the exception:", e);
			}
		}
		return null;
	}

	/**
	 * Removes specified child node from this node's children.
	 * 
	 * @param node  the node to remove
	 * @return      {@code node} if removed, or {@code null} if not this node's child
	 */
	public ConfigurationNode removeChild(ConfigurationNode node) {
		if (isScalar() || node.getParent() != this) {
			return null;
		}
		if (!this.resolved) {
			throw new IllegalStateException("Unresolved node shouldn't have any children!");
		}
		if (isList()) {
			this.listCache.remove(node);
		} else {
			this.mapCache.remove(node.getName()); // It's our node, no need to normalize name.
		}
		node.setParent(null);
		return node;
	}

	/**
	 * Removes all children of the node.
	 */
	public void removeAllChildren() {
		if (isScalar()) {
			return;
		}
		try {
			for (ConfigurationNode node : getChildrenList()) {
				node.setParent(null);
			}
		} catch (ConfigurationException e) {
			throw new IllegalStateException("The node wasn't a scalar but we got the exception:", e);
		}
		if (isMap()) {
			this.mapCache = new HashMap<String, ConfigurationNode>();
		} else {
			this.listCache = new ArrayList<ConfigurationNode>();
		}
	}

	/**
	 * Sets value of the node to specified {@code value}, and removes children, if any.
	 * 
	 * @param value  the value
	 */
	public void setValue(Object value) {
		this.value = value;
		removeAllChildren();
		this.listCache = null;
		this.mapCache = null;
		this.resolved = false;
	}

	/**
	 * Dumps the node with all children to a standard JRE class (lie Map, List, or Integer), which can be then dumped to file (eg. with SnakeYaml).
	 * 
	 * @return dumped node
	 */
	public Object dump() {
		if (!this.resolved) {
			return this.value;
		}
		if (isList()) {
			List<Object> list = new ArrayList<Object>();
			for (ConfigurationNode node : this.listCache) {
				list.add(node.dump());
			}
			return list;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<String, ConfigurationNode> entry : this.mapCache.entrySet()) {
			map.put(entry.getKey(), entry.getValue().dump());
		}
		return map;
	}

	/**
	 * Returns a descendant node of this node with given path (relative to this node).
	 * 
	 * @param path           list of path elements of the descendant node
	 * @return               the node with specified path, or {@code null} if not found
	 * @throws ConfigurationException if one of the nodes in the path is a scalar or list node
	 */
	public ConfigurationNode getNode(String... path) throws ConfigurationException {
		return getNode(false, path);
	}

	/**
	 * Returns a descendant node of this node with given path (relative to this node).
	 * <p>
	 * If any the nodes in path don't exist, and {@code add} is {@code true}, then creates them. If the node is null, and attempting to create the child, the node will be turned into a map.
	 * 
	 * @param add            weather or not create the node if doesn't exist
	 * @param path           list of path elements of the descendant node
	 * @return               the descendant node, or {@code null} if doesn't exist and not being created
	 * @throws ConfigurationException if any node in the path is not a map and not null
	 */
	public ConfigurationNode getNode(boolean add, String... path) throws ConfigurationException {
		return getNode(StringUtils.join(path, this.config.getPathSeparator()), add);
	}

	/**
	 * Returns a descendant node of this node with given path (relative to this node).
	 * 
	 * @param path           path of the descendant node, separated with this node's Configuration's path separator
	 * @return               the node with specified path, or {@code null} if not found
	 * @throws ConfigurationException if one of the nodes in the path is a scalar or list node
	 */
	public ConfigurationNode getNode(String path) throws ConfigurationException {
		return getNode(path, false);
	}

	/**
	 * Returns a descendant node of this node with given path (relative to this node).
	 * <p>
	 * If any the nodes in path don't exist, and {@code add} is {@code true}, then creates them. If the node is null, and attempting to create the child, the node will be turned into a map.
	 * 
	 * @param path           path of the descendant node, separated with this node's Configuration's path separator
	 * @param add            weather or not create the node if doesn't exist
	 * @return               the descendant node, or {@code null} if doesn't exist and not being created
	 * @throws ConfigurationException if any node in the path is not a map and not null
	 */
	public ConfigurationNode getNode(String path, boolean add) throws ConfigurationException {
		String[] elements = normalizePath(path).split(Pattern.quote(this.config.getPathSeparator()), 2);
		if (elements.length == 0 || path.isEmpty()) {
			return null;
		}
		ConfigurationNode node = getChild(elements[0], add);
		if (node == null || elements.length == 1) {
			return node;
		}
		return node.getNode(elements[1], add);
	}

	/**
	 * Checks if the node has a descendant node with given path (relative to this node).
	 * 
	 * @param path  path elements of the descendant node
	 * @return      {@code true} if has, {@code false} otherwise
	 */
	public boolean hasNode(String... path) {
		return hasNode(StringUtils.join(path, this.config.getPathSeparator()));
	}

	/**
	 * Checks if the node has a descendant node with given path (relative to this node).
	 * 
	 * @param path  path of the descendant node, separated with this node's Configuration's path separator
	 * @return      {@code true} if has, {@code false} otherwise
	 */
	public boolean hasNode(String path) {
		String[] elements = normalizePath(path).split(Pattern.quote(this.config.getPathSeparator()), 2);
		if (elements.length == 0 || path.isEmpty() || !hasChild(elements[0])) {
			return false;
		}
		if (elements.length == 1) {
			return true;
		}
		try {
			return getChild(elements[0]).hasNode(elements[1]);
		} catch (ConfigurationException e) {
			throw new IllegalStateException("The child was present but we got the exception:", e);
		}
	}

	@Override
	public List<?> getList(List<?> def) {
		if (isList()) {
			return (List<?>) dump();
		}

		if (isNull() && def != null && getConfiguration().doesWriteDefaults()) {
			setValue(def);
		}

		return def == null ? Collections.emptyList() : def;
	}

	/**
	 * Dumps value of the node as a map.
	 * 
	 * @return dumped map, or null if the node is not a map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap() {
		// TODO: A version with default
		if (isMap()) {
			return (Map<String, Object>) dump();
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Object getValue(Object def) {
		if (isScalar()) {
			if (this.value != null) {
				return this.value;
			}

			if (def != null && getConfiguration().doesWriteDefaults()) {
				setValue(def);
			}
		}

		return def;
	}

	@Override
	public String getString(String defaultValue) {
		return this.holder.getString(defaultValue);
	}

	@Override
	public int getInt(int defaultValue) {
		return this.holder.getInt(defaultValue);
	}

	@Override
	public long getLong(long defaultValue) {
		return this.holder.getLong(defaultValue);
	}

	@Override
	public BigInteger getBigInt(BigInteger defaultValue) {
		return this.holder.getBigInt(defaultValue);
	}

	@Override
	public double getDouble(double defaultValue) {
		return this.holder.getDouble(defaultValue);
	}

	@Override
	public float getFloat(float defaultValue) {
		return this.holder.getFloat(defaultValue);
	}

	@Override
	public BigDecimal getDecimal(BigDecimal defaultValue) {
		return this.holder.getDecimal(defaultValue);
	}

	@Override
	public byte[] getBytes(byte[] defaultValue) {
		return this.holder.getBytes(defaultValue);
	}

	@Override
	public Date getDate(Date defaultValue) {
		return this.holder.getDate(defaultValue);
	}

	@Override
	public boolean getBoolean(boolean defaultValue) {
		return this.holder.getBoolean(defaultValue);
	}

	@Override
	public byte getByte(byte def) {
		return this.holder.getByte(def);
	}

	@Override
	public short getShort(short def) {
		return this.holder.getShort(def);
	}

	@Override
	public <T> T getTypedValue(Class<T> type, T def) {
		return this.holder.getTypedValue(type, def);
	}

	@Override
	public Object getTypedValue(Type type, Object def) {
		return this.holder.getTypedValue(type, def);
	}

	@Override
	public List<String> getStringList(List<String> def) {
		return this.holder.getStringList(def);
	}

	@Override
	public List<Integer> getIntegerList(List<Integer> def) {
		return this.holder.getIntegerList(def);
	}

	@Override
	public List<Double> getDoubleList(List<Double> def) {
		return this.holder.getDoubleList(def);
	}

	@Override
	public List<Boolean> getBooleanList(List<Boolean> def) {
		return this.holder.getBooleanList(def);
	}

	public boolean isNull() {
		return !this.resolved && this.value == null;
	}
}
