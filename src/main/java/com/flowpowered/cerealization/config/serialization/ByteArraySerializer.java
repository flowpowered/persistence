package com.flowpowered.cerealization.config.serialization;

import com.flowpowered.cerealization.CastUtils;

public class ByteArraySerializer extends Serializer {
	@Override
	public boolean isApplicable(GenericType type) {
		if (!type.isArray()) {
			return false;
		}
		Class<?> arrayType = type.getArrayType().getMainType();
		return arrayType == byte.class;
	}

	@Override
	protected int getParametersRequired() {
		return -1;
	}

	@Override
	protected Object handleDeserialize(GenericType type, Object value) {
		return CastUtils.castBytes(value);
	}

}
