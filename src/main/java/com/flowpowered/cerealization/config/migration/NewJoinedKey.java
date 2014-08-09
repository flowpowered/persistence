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
package com.flowpowered.cerealization.config.migration;

import org.apache.commons.lang3.StringUtils;

import com.flowpowered.cerealization.config.Configuration;

/**
 * This implementation of MigrationAction converts configuration keys based on predefined stringpath patterns where % is replaced by the old key. The stringpath pattern is split by the configuration's
 * path separator
 *
 * @see com.flowpowered.cerealization.config.Configuration#getPathSeparator()
 */
public final class NewJoinedKey implements MigrationAction {
    private final String newKeyPattern;
    private final Configuration config;

    public NewJoinedKey(String newKeyPattern, Configuration config) {
        this.newKeyPattern = newKeyPattern;
        this.config = config;
    }

    @Override
    public String[] convertKey(String[] key) {
        String oldKey = StringUtils.join(key, config.getPathSeparator());
        return config.getPathSeparatorPattern().split(newKeyPattern.replace("%", oldKey));
    }

    @Override
    public Object convertValue(Object value) {
        return value;
    }
}
