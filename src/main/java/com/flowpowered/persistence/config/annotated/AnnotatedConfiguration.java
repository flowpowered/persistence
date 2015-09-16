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
package com.flowpowered.persistence.config.annotated;

import com.flowpowered.persistence.config.Configuration;
import com.flowpowered.persistence.config.ConfigurationException;
import com.flowpowered.persistence.config.ConfigurationNodeSource;
import com.flowpowered.persistence.config.ConfigurationWrapper;

public abstract class AnnotatedConfiguration extends ConfigurationWrapper {
    public AnnotatedConfiguration() {
    }

    public AnnotatedConfiguration(Configuration config) {
        super(config);
    }

    public abstract void load(ConfigurationNodeSource source) throws ConfigurationException;

    public abstract void save(ConfigurationNodeSource source) throws ConfigurationException;

    @Override
    public void load() throws ConfigurationException {
        super.load();
        load(this);
    }

    @Override
    public void save() throws ConfigurationException {
        save(this);
        super.save();
    }
}
