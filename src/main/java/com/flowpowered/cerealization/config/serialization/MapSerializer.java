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
package com.flowpowered.cerealization.config.serialization;

import java.util.HashMap;
import java.util.Map;

public class MapSerializer extends Serializer {
    @Override
    public boolean isApplicable(GenericType type) {
        return Map.class.equals(type.getMainType());
    }

    @Override
    protected int getParametersRequired() {
        return 2;
    }

    @Override
    public boolean isApplicableDeserialize(GenericType type, Object value) {
        return super.isApplicableDeserialize(type, value) && value instanceof Map<?, ?>;
    }

    @Override
    protected Object handleDeserialize(GenericType type, Object value) {
        Map<?, ?> raw = (Map<?, ?>) value;
        Map<Object, Object> values = new HashMap<Object, Object>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            values.put(Serialization.deserialize(type.getGenerics()[0], entry.getKey()),
                    Serialization.deserialize(type.getGenerics()[1], entry.getValue()));
        }
        return values;
    }

    @Override
    protected Object handleSerialize(GenericType type, Object value) {
        Map<?, ?> raw = (Map<?, ?>) value;
        Map<Object, Object> values = new HashMap<Object, Object>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            values.put(Serialization.serialize(type.getGenerics()[0], entry.getKey()),
                    Serialization.serialize(type.getGenerics()[1], entry.getValue()));
        }
        return values;
    }
}
