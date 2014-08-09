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
package com.flowpowered.cerealization.config;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * A basic implementation of ConfigurationNodeSource.
 */
public abstract class AbstractConfigurationNodeSource implements ConfigurationNodeSource {
    protected final Map<String, ConfigurationNode> children = new LinkedHashMap<String, ConfigurationNode>();
    protected Configuration config;

    public AbstractConfigurationNodeSource(Configuration config) {
        this.config = config;
    }

    @Override
    public Configuration getConfiguration() {
        return config;
    }

    @Override
    public ConfigurationNode getChild(String name) {
        return getChild(name, false);
    }

    @Override
    public ConfigurationNode getChild(String name, boolean add) {
        ConfigurationNode node = children.get(name);
        if (node == null) {
            node = createConfigurationNode(ArrayUtils.add(getPathElements(), name), null);
            node.setParent(this);
            if (add) {
                addChild(node);
            }
        }
        return node;
    }

    @Override
    public ConfigurationNode addChild(ConfigurationNode node) {
        ConfigurationNode ret = children.put(node.getPathElements()[node.getPathElements().length - 1], node);
        node.setAttached(true);
        node.setParent(this);
        return ret;
    }

    @Override
    public ConfigurationNode addNode(String name) {
        if (name.contains(getConfiguration().getPathSeparator())) {
            String[] split = name.split(getConfiguration().getPathSeparator());
            ConfigurationNode newNode = createConfigurationNode(ArrayUtils.add(getPathElements(), split[0]), null);
            addChild(newNode);
            ArrayUtils.remove(split, 0);
            newNode.addNode(StringUtils.join(split, getConfiguration().getPathSeparator()));
            return newNode;
        }
        ConfigurationNode newNode = createConfigurationNode(ArrayUtils.add(getPathElements(), name), null);
        addChild(newNode);
        return newNode;
    }

    @Override
    public void addChildren(ConfigurationNode... nodes) {
        for (ConfigurationNode child : nodes) {
            addChild(child);
        }
    }

    @Override
    public ConfigurationNode removeChild(String key) {
        return removeChild(children.get(key));
    }

    /**
     * Detach a node from this node source and remove its children If the node's parent isn't this, the method will silently return After this method, the node will have a parent of null, no children,
     * and be marked as detached
     *
     * @param node The node to detach
     */
    protected void detachChild(ConfigurationNode node) {
        if (node.getParent() != this) {
            return;
        }
        node.setAttached(false);
        node.setParent(null);
        for (Iterator<ConfigurationNode> i = node.children.values().iterator(); i.hasNext(); ) {
            node.detachChild(i.next());
            i.remove();
        }
    }

    @Override
    public ConfigurationNode removeChild(ConfigurationNode node) {
        if (node != null) {
            if (node.getParent() != this) {
                return null;
            }
            if (children.remove(node.getPathElements()[node.getPathElements().length - 1]) == null) {
                return null;
            }
            detachChild(node);
        }
        return node;
    }

    @Override
    public Map<String, ConfigurationNode> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    @Override
    public Map<String, Object> getValues() {
        Map<String, Object> ret = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, ConfigurationNode> entry : getChildren().entrySet()) {
            ret.put(entry.getKey(), entry.getValue().getValue());
        }
        return ret;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        Set<String> keys = new LinkedHashSet<String>(deep ? children.size() * 2 : children.size());
        for (Map.Entry<String, ConfigurationNode> entry : children.entrySet()) {
            keys.add(entry.getKey());
            if (deep) {
                for (String key : entry.getValue().getKeys(true)) {
                    keys.add(entry.getKey() + getConfiguration().getPathSeparator() + key);
                }
            }
        }
        return keys;
    }

    @Override
    public ConfigurationNode getNode(String path) {
        if (path.contains(getConfiguration().getPathSeparator())) {
            return getNode(getConfiguration().splitNodePath(path));
        } else {
            return getChild(path);
        }
    }

    @Override
    public ConfigurationNode getNode(String... path) {
        if (path.length == 0) {
            throw new IllegalArgumentException("Path must not be empty!");
        }
        path = getConfiguration().ensureCorrectPath(path);
        ConfigurationNode node = getChild(path[0]);
        for (int i = 1; i < path.length && node != null && node.isAttached(); ++i) {
            node = node.getChild(path[i]);
        }

        return node == null || !node.isAttached() ? createConfigurationNode(path, null) : node;
    }

    public ConfigurationNode createConfigurationNode(String[] path, Object value) {
        return new ConfigurationNode(getConfiguration(), path, value);
    }

    @Override
    public boolean hasChildren() {
        return children.size() > 0;
    }

    @Override
    public boolean hasChild(String key) {
        return children.containsKey(key);
    }

    @Override
    public boolean hasNode(String... path) {
        if (path.length == 0) {
            throw new IllegalArgumentException("Path must not be empty!");
        }
        path = getConfiguration().ensureCorrectPath(path);
        AbstractConfigurationNodeSource current = this;
        for (String key : path) {
            ConfigurationNode node = current.children.get(key);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return true;
    }
}
