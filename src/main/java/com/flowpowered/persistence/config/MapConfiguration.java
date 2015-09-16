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

import java.util.HashMap;
import java.util.Map;

/**
 * This represents a configuration that loads its values from an in-memory {@link Map}
 */
public class MapConfiguration extends MapBasedConfiguration {
    private Map<?, ?> map;

    /**
     * Create a new configuration backed by an empty map. Loading a configuration instantiated with this constructor will do nothing.
     */
    public MapConfiguration() {
        this(null);
    }

    public MapConfiguration(Map<?, ?> map) {
        super();
        this.map = map == null ? new HashMap<Object, Object>() : map;
    }

    @Override
    protected Map<?, ?> loadToMap() {
        return map;
    }

    @Override
    @SuppressWarnings ({"unchecked", "rawtypes"})
    protected void saveFromMap(Map<?, ?> map) {
        this.map.clear();
        ((Map) this.map).putAll(map);
    }

    public Map<?, ?> getMap() {
        return map;
    }
}
