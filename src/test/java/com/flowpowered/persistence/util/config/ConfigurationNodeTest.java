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
package com.flowpowered.persistence.util.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.flowpowered.persistence.config.Configuration;
import com.flowpowered.persistence.config.ConfigurationNode;
import com.flowpowered.persistence.config.MapConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ConfigurationNodeTest {
    private Configuration base;

    @Before
    public void setUp() {
        base = new MapConfiguration();
    }

    @Test
    public void testGetPath() {
        ConfigurationNode config = new ConfigurationNode(base, new String[] {"a", "b", "c"}, null);
        assertEquals("a.b.c", config.getPath());
    }

    @Test
    public void testGetKeys() {
        ConfigurationNode parent = base.getChild("a");
        parent.getChild("a1", true).setValue("b1");
        parent.getChild("a2", true).setValue("b2");
        parent.getChild("a3", true).setValue("b3");
        parent.getChild("a3").getChild("c3", true);
        assertEquals(new HashSet<String>(Arrays.asList("a1", "a2", "a3")), parent.getKeys(false));
        assertEquals(new HashSet<String>(Arrays.asList("a")), base.getKeys(false));
        assertEquals(new HashSet<String>(Arrays.asList("a", "a.a1", "a.a2", "a.a3", "a.a3.c3")), base.getKeys(true));
    }

    @Test
    public void testGetValues() {
        ConfigurationNode parent = base.getChild("a");
        Map<String, Object> vals = new HashMap<String, Object>();
        for (int i = 1; i <= 3; ++i) {
            parent.getChild("a" + i).setValue("b" + i);
            vals.put("a" + i, "b" + i);
        }
        assertEquals(vals, parent.getValues());
    }

    @Test
    public void testRemove() {
        ConfigurationNode toRemove = base.getNode("to-remove");
        toRemove.setValue("test");
        assertEquals(toRemove, base.getNode("to-remove"));
        toRemove.remove();
        assertFalse(base.getChildren().containsKey("to-remove"));
        assertEquals(null, base.getNode("to-remove").getValue());
    }
}
