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

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A parent class for implementations of Configuration that wrap other Configurations.
 */
public abstract class ConfigurationWrapper implements Configuration {
    private Configuration config;

    public ConfigurationWrapper() {
        this(null);
    }

    public ConfigurationWrapper(Configuration config) {
        this.config = config;
    }

    @Override
    public Configuration getConfiguration() {
        if (config == null) {
            throw new IllegalArgumentException("The Configuration for a " + getClass().getSimpleName() + " is not set!");
        }
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    @Override
    public void load() throws ConfigurationException {
        getConfiguration().load();
    }

    @Override
    public void save() throws ConfigurationException {
        getConfiguration().save();
    }

    @Override
    public void setNode(ConfigurationNode node) {
        getConfiguration().setNode(node);
    }

    @Override
    public String getPathSeparator() {
        return getConfiguration().getPathSeparator();
    }

    @Override
    public void setPathSeparator(String pathSeparator) {
        getConfiguration().setPathSeparator(pathSeparator);
    }

    @Override
    public Pattern getPathSeparatorPattern() {
        return getConfiguration().getPathSeparatorPattern();
    }

    @Override
    public boolean doesWriteDefaults() {
        return getConfiguration().doesWriteDefaults();
    }

    @Override
    public void setWritesDefaults(boolean writesDefaults) {
        getConfiguration().setWritesDefaults(writesDefaults);
    }

    @Override
    public String[] splitNodePath(String path) {
        return getConfiguration().splitNodePath(path);
    }

    @Override
    public String[] ensureCorrectPath(String[] rawPath) {
        return getConfiguration().ensureCorrectPath(rawPath);
    }

    @Override
    public ConfigurationNode getChild(String name) {
        return getConfiguration().getChild(name);
    }

    @Override
    public ConfigurationNode getChild(String name, boolean add) {
        return getConfiguration().getChild(name, add);
    }

    @Override
    public ConfigurationNode addChild(ConfigurationNode node) {
        return getConfiguration().addChild(node);
    }

    @Override
    public ConfigurationNode addNode(String name) {
        return getConfiguration().addNode(name);
    }

    @Override
    public void addChildren(ConfigurationNode... nodes) {
        getConfiguration().addChildren(nodes);
    }

    @Override
    public ConfigurationNode removeChild(String key) {
        return getConfiguration().removeChild(key);
    }

    @Override
    public ConfigurationNode removeChild(ConfigurationNode node) {
        return getConfiguration().removeChild(node);
    }

    @Override
    public Map<String, ConfigurationNode> getChildren() {
        return getConfiguration().getChildren();
    }

    @Override
    public Map<String, Object> getValues() {
        return getConfiguration().getValues();
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return getConfiguration().getKeys(deep);
    }

    @Override
    public ConfigurationNode getNode(String path) {
        return getConfiguration().getNode(path);
    }

    @Override
    public ConfigurationNode getNode(String... path) {
        return getConfiguration().getNode(path);
    }

    @Override
    public boolean hasChildren() {
        return getConfiguration().hasChildren();
    }

    @Override
    public boolean hasChild(String key) {
        return getConfiguration().hasChild(key);
    }

    @Override
    public boolean hasNode(String... path) {
        return getConfiguration().hasNode(path);
    }

    @Override
    public String[] getPathElements() {
        return getConfiguration().getPathElements();
    }
}
