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
package com.flowpowered.persistence.config;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.flowpowered.persistence.data.ValueHolderBase;

/**
 * This object holds a reference to a ConfigurationNode and provides all the methods to get its value, but using the default provided in the constructor
 */
public class ConfigurationHolder extends ValueHolderBase implements ConfigurationNodeSource {
    private Configuration configuration;
    private final String[] path;
    private Object def;

    public ConfigurationHolder(Configuration config, Object def, String... path) {
        this.path = path;
        this.configuration = config;
        this.def = def;
    }

    public ConfigurationHolder(Object value, String... path) {
        this(null, value, path);
    }

    private ConfigurationNode getNode() {
        if (getConfiguration() == null) {
            throw new IllegalStateException("The ConfigurationHolder at path " + ArrayUtils.toString(path) + " is not attached to a Configuration!");
        }
        return getConfiguration().getNode(getPathElements());
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration config) {
        this.configuration = config;
    }

    @Override
    public String[] getPathElements() {
        return path;
    }

    /**
     * Gets the default value of this Configuration Holder
     *
     * @return the default value
     */
    public Object getDefaultValue() {
        return this.def;
    }

    /**
     * Sets the default value for this Configuration Holder
     *
     * @param def value to set to
     */
    public void setDefaultValue(Object def) {
        this.def = def;
    }

    @Override
    public Object getValue() {
        return getNode().getValue(this.def);
    }

    @Override
    public Object getValue(Object def) {
        return getNode().getValue(this.def);
    }

    public Object setValue(Object value) {
        return getNode().setValue(value);
    }

    @Override
    public ConfigurationNode getChild(String name) {
        return getNode().getChild(name);
    }

    @Override
    public ConfigurationNode getChild(String name, boolean add) {
        return getNode().getChild(name, add);
    }

    @Override
    public ConfigurationNode addChild(ConfigurationNode node) {
        return getNode().addChild(node);
    }

    @Override
    public ConfigurationNode addNode(String name) {
        return getNode().addNode(name);
    }

    @Override
    public void addChildren(ConfigurationNode... nodes) {
        getNode().addChildren(nodes);
    }

    @Override
    public ConfigurationNode removeChild(String key) {
        return getNode().removeChild(key);
    }

    @Override
    public ConfigurationNode removeChild(ConfigurationNode node) {
        return getNode().removeChild(node);
    }

    @Override
    public Map<String, ConfigurationNode> getChildren() {
        return getNode().getChildren();
    }

    @Override
    public Map<String, Object> getValues() {
        return getNode().getValues();
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return getNode().getKeys(deep);
    }

    @Override
    public ConfigurationNode getNode(String path) {
        return getNode().getNode(path);
    }

    @Override
    public ConfigurationNode getNode(String... path) {
        return getNode().getNode(path);
    }

    @Override
    public boolean hasChildren() {
        return getNode().hasChildren();
    }

    public boolean hasChild(String key) {
        return getNode().hasChild(key);
    }

    public boolean hasNode(String... path) {
        return getNode().hasNode(path);
    }
}
