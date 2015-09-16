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
package com.flowpowered.cerealization.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.flowpowered.cerealization.ReflectionUtils;

/**
 * This is a configuration holder class that takes another Configuration and wraps some reflection to get all the fields in the subclass that have values of the type {@link ConfigurationHolder}. These
 * fields will be automatically associated with the attached configuration and have their default values loaded into the configuration as needed on load
 */
public abstract class ConfigurationHolderConfiguration extends ConfigurationWrapper {
    private final List<Field> holders = new ArrayList<Field>();

    public ConfigurationHolderConfiguration(Configuration base) {
        super(base);
        for (Field field : ReflectionUtils.getDeclaredFieldsRecur(getClass())) {
            field.setAccessible(true);

            if (ConfigurationHolder.class.isAssignableFrom(field.getType())) {
                holders.add(field);
            }
        }
    }

    @Override
    public void load() throws ConfigurationException {
        super.load();
        for (Field field : this.holders) {
            try {
                ConfigurationHolder holder = (ConfigurationHolder) field.get(this);
                if (holder != null) {
                    holder.setConfiguration(getConfiguration());
                    holder.getValue(); // Initialize the ConfigurationHolder's value
                }
            } catch (IllegalAccessException e) {
            }
        }
    }
}
