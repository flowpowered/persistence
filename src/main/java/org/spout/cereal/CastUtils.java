/*
 * This file is part of Cerealization.
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * Cerealization is licensed under the Spout License Version 1.
 *
 * Cerealization is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Cerealization is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.cereal;

/**
 * Class containing generic casting functions.
 */
public class CastUtils {
	private CastUtils() {
	}

	/**
	 * Casts a value to a float. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as a float
	 */
	public static Float castFloat(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Number) {
			return ((Number) o).floatValue();
		}

		try {
			return Float.valueOf(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a byte. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as a byte
	 */
	public static Byte castByte(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Number) {
			return ((Number) o).byteValue();
		}

		try {
			return Byte.valueOf(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a short. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as a short
	 */
	public static Short castShort(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Number) {
			return ((Number) o).shortValue();
		}

		try {
			return Short.valueOf(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to an integer. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as an int
	 */
	public static Integer castInt(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Number) {
			return ((Number) o).intValue();
		}

		try {
			return Integer.valueOf(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a double. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as a double
	 */
	public static Double castDouble(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}

		try {
			return Double.valueOf(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a long. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as a long
	 */
	public static Long castLong(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Number) {
			return ((Number) o).longValue();
		}

		try {
			return Long.valueOf(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a boolean. May return null.
	 * @param o The object to attempt to cast
	 * @return The object as a boolean
	 */
	public static Boolean castBoolean(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof Boolean) {
			return (Boolean) o;
		} else if (o instanceof String) {
			try {
				return Boolean.parseBoolean((String) o);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		return null;
	}
}
