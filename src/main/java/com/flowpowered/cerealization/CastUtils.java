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
package com.flowpowered.cerealization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Class containing generic casting functions.
 */
public class CastUtils {
	private CastUtils() {
	}

	/**
	 * Casts a value to a float. May return null.
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 * Casts a value to a BigInteger. May return null.
	 *
	 * @param o The object to attempt to cast
	 * @return The object as a BigInteger
	 */
	public static BigInteger castBigInt(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof BigInteger) {
			return (BigInteger) o;
		}

		if (o instanceof Number) {
			return BigInteger.valueOf(((Number) o).longValue());
		}
		try {
			return new BigInteger(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a BigDecimal. May return null.
	 *
	 * @param o The object to attempt to cast
	 * @return The object as a BigDecimal
	 */
	public static BigDecimal castBigDecimal(Object o) {
		if (o == null) {
			return null;
		}

		if (o instanceof BigDecimal) {
			return (BigDecimal) o;
		}

		if (o instanceof Number) {
			return BigDecimal.valueOf(((Number) o).doubleValue());
		}
		try {
			return new BigDecimal(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Casts a value to a boolean. May return null.
	 *
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

	/**
	 * Casts a value to a Date. May return null.
	 *
	 * @param o The object to attempt to cast
	 * @return The object as a Date
	 */
	public static Date castDate(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			return (Date) o;
		}
		Long l = castLong(o);
		if (l != null) {
			return new Date(l);
		}
		String str = String.valueOf(o);
		try {
			return DateFormat.getDateTimeInstance().parse(str);
		} catch (ParseException e) {
		}
		try {
			return DateFormat.getDateInstance().parse(str);
		} catch (ParseException e) {
		}
		try {
			return DateFormat.getTimeInstance().parse(str);
		} catch (ParseException e) {
		}
		try {
			return DateFormat.getInstance().parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * Casts a value to a byte array. May return null.
	 *
	 * @param o The object to attempt to cast
	 * @return The object as a byte array
	 */
	public static byte[] castBytes(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof byte[]) {
			return (byte[]) o;
		}
		return null;
	}
}
