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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Various utility methods that deal with reflection
 */
public class ReflectionUtils {
	/**
	 * Get all the public fields in a class, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz) {
		return getFieldsRecur(clazz, false);
	}

	/**
	 * Get all the public fields in a class with the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, int modifiers) {
		return getFieldsRecur(clazz, modifiers, false);
	}

	/**
	 * Get all the public fields in a class, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param includeObject Whether to include fields in {@link Object}
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, boolean includeObject) {
		return getFieldsRecur(clazz, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the public fields in a class with the specified modifiers, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include fields in {@link Object}
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, int modifiers, boolean includeObject) {
		return getFieldsRecur(clazz, modifiers, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the public fields in a class with the desired annotation, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, Class<? extends Annotation>... annotations) {
		return getFieldsRecur(clazz, 0, false, annotations);
	}

	/**
	 * Get all the public fields in a class with the desired annotation and the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, int modifiers, Class<? extends Annotation>... annotations) {
		return getFieldsRecur(clazz, modifiers, false, annotations);
	}

	/**
	 * Get all the public fields in a class with the desired annotation, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param includeObject Whether to include fields in {@link Object}
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, boolean includeObject, Class<? extends Annotation>... annotations) {
		return getFieldsRecur(clazz, 0, includeObject, annotations);
	}

	/**
	 * Get all the public fields in a class with the specified modifiers (optionally, with the desired annotation), as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include fields in {@link Object}
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 * @see Class#getFields()
	 */
	public static List<Field> getFieldsRecur(Class<?> clazz, int modifiers, boolean includeObject, Class<? extends Annotation>... annotations) {
		final List<Field> fields = new ArrayList<Field>();
		while (clazz != null && (includeObject || !Object.class.equals(clazz))) {
			for (Field field : clazz.getFields()) {
				if (hasModifiers(field, modifiers) && (annotations == null || hasAnyAnnotation(field, annotations))) {
					fields.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * Get all the fields in a class, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz) {
		return getDeclaredFieldsRecur(clazz, false);
	}

	/**
	 * Get all the fields in a class with the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, int modifiers) {
		return getDeclaredFieldsRecur(clazz, modifiers, false);
	}

	/**
	 * Get all the fields in a class, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param includeObject Whether to include fields in {@link Object}
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, boolean includeObject) {
		return getDeclaredFieldsRecur(clazz, 0, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the fields with the specified modifiers in a class, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include fields in {@link Object}
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, int modifiers, boolean includeObject) {
		return getDeclaredFieldsRecur(clazz, modifiers, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the fields in a class with the desired annotation, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, Class<? extends Annotation>... annotations) {
		return getDeclaredFieldsRecur(clazz, 0, false, annotations);
	}

	/**
	 * Get all the fields in a class with the desired annotation and the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, int modifiers, Class<? extends Annotation>... annotations) {
		return getDeclaredFieldsRecur(clazz, modifiers, false, annotations);
	}

	/**
	 * Get all the fields in a class with the desired annotation, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param includeObject Whether to include fields in {@link Object}
	 * @return The public fields in the class
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, boolean includeObject, Class<? extends Annotation>... annotations) {
		return getDeclaredFieldsRecur(clazz, 0, includeObject, annotations);
	}

	/**
	 * Get all the fields in a class  with the specified modifiers (optionally, with the desired annotation), as well as those in its superclasses.
	 *
	 * @param clazz The class to get all fields in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include fields in {@link Object}
	 * @param annotations if not null, only include fields with any of these annotations
	 * @return The public fields in the class
	 * @see Class#getDeclaredFields()
	 */
	public static List<Field> getDeclaredFieldsRecur(Class<?> clazz, int modifiers, boolean includeObject, Class<? extends Annotation>... annotations) {
		final List<Field> fields = new ArrayList<Field>();
		while (clazz != null && (includeObject || !Object.class.equals(clazz))) {
			for (Field field : clazz.getDeclaredFields()) {
				if (hasModifiers(field, modifiers) && (annotations == null || hasAnyAnnotation(field, annotations))) {
					fields.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * Attempts to list all the classes in the specified package as determined by the context class loader
	 *
	 * @param packageName the package name to search
	 * @param recursive if the search should include all subdirectories
	 * @return a list of classes that exist within that package
	 * @throws ClassNotFoundException if the package had invalid classes, or does not exist
	 */
	public static List<Class<?>> getClassesForPackage(String packageName, boolean recursive) throws ClassNotFoundException {
		ArrayList<File> directories = new ArrayList<File>();
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = cld.getResources(path);
			while (resources.hasMoreElements()) {
				File file = new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8"));
				directories.add(file);
				if (recursive) {
					findDirs(directories, file);
				}
			}
		} catch (NullPointerException ex) {
			throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Null pointer exception)", ex);
		} catch (UnsupportedEncodingException encex) {
			throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Unsupported encoding)", encex);
		} catch (IOException ioex) {
			throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packageName, ioex);
		}

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : directories) {
			if (directory.exists()) {
				String[] files = directory.list();
				for (String file : files) {
					if (file.endsWith(".class")) {
						try {
							String path = directory.getCanonicalPath().replaceAll("/", ".").replaceAll("\\\\", ".");
							int start = path.indexOf(packageName);
							path = path.substring(start, path.length());
							classes.add(Class.forName(path + '.' + file.substring(0, file.length() - 6)));
						} catch (IOException ex) {
							throw new ClassNotFoundException("IOException was thrown when trying to get path for " + file);
						}
					}
				}
			} else {
				throw new ClassNotFoundException(packageName + " (" + directory.getPath() + ") does not appear to be a valid package");
			}
		}
		return classes;
	}

	/**
	 * Recursively builds a list of all subdirectories
	 *
	 * @param dirs list to add to
	 * @param dir to search
	 */
	private static void findDirs(List<File> dirs, File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				dirs.add(f);
				findDirs(dirs, f);
			}
		}
	}

	/**
	 * Get all the public methods in a class, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz) {
		return getMethodsRecur(clazz, false);
	}

	/**
	 * Get all the public methods in a class with the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, int modifiers) {
		return getMethodsRecur(clazz, modifiers, false);
	}

	/**
	 * Get all the public methods in a class, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param includeObject Whether to include methods in {@link Object}
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, boolean includeObject) {
		return getMethodsRecur(clazz, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the public methods in a class with the specified modifiers, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include methods in {@link Object}
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, int modifiers, boolean includeObject) {
		return getMethodsRecur(clazz, modifiers, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the public methods in a class with the desired annotation, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, Class<? extends Annotation>... annotations) {
		return getMethodsRecur(clazz, false, annotations);
	}

	/**
	 * Get all the public methods in a class with the desired annotation and the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, int modifiers, Class<? extends Annotation>... annotations) {
		return getMethodsRecur(clazz, modifiers, false, annotations);
	}

	/**
	 * Get all the public methods in a class with the desired annotation, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param includeObject Whether to include methods in {@link Object}
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, boolean includeObject, Class<? extends Annotation>... annotations) {
		return getMethodsRecur(clazz, 0, false, annotations);
	}

	/**
	 * Get all the public methods in a class with the specified modifiers (optionally, with the desired annotation), as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include methods in {@link Object}
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 * @see Class#getMethods()
	 */
	public static List<Method> getMethodsRecur(Class<?> clazz, int modifiers, boolean includeObject, Class<? extends Annotation>... annotations) {
		final List<Method> methods = new ArrayList<Method>();
		while (clazz != null && (includeObject || !Object.class.equals(clazz))) {
			for (Method method : clazz.getMethods()) {
				if (hasModifiers(method, modifiers) && (annotations == null || hasAnyAnnotation(method, annotations))) {
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	/**
	 * Get all the methods in a class, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz) {
		return getDeclaredMethodsRecur(clazz, false);
	}

	/**
	 * Get all the methods in a class with the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, int modifiers) {
		return getDeclaredMethodsRecur(clazz, modifiers, false);
	}

	/**
	 * Get all the methods in a class, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param includeObject Whether to include methods in {@link Object}
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, boolean includeObject) {
		return getDeclaredMethodsRecur(clazz, 0, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the methods in a class with the specified modifiers, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include methods in {@link Object}
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, int modifiers, boolean includeObject) {
		return getDeclaredMethodsRecur(clazz, modifiers, includeObject, (Class<? extends Annotation>[]) null);
	}

	/**
	 * Get all the methods in a class with the desired annotation, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, Class<? extends Annotation>... annotations) {
		return getDeclaredMethodsRecur(clazz, 0, false, annotations);
	}

	/**
	 * Get all the methods in a class with the desired annotation and the specified modifiers, as well as those in its superclasses (excluding {@link Object}).
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, int modifiers, Class<? extends Annotation>... annotations) {
		return getDeclaredMethodsRecur(clazz, modifiers, false, annotations);
	}

	/**
	 * Get all the methods in a class with the desired annotation, as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param includeObject Whether to include methods in {@link Object}
	 * @return The public methods in the class
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, boolean includeObject, Class<? extends Annotation>... annotations) {
		return getDeclaredMethodsRecur(clazz, 0, includeObject, annotations);
	}

	/**
	 * Get all the methods in a class with the specified modifiers (optionally, with the desired annotation), as well as those in its superclasses.
	 *
	 * @param clazz The class to get all methods in
	 * @param modifiers the modifier mask
	 * @param includeObject Whether to include methods in {@link Object}
	 * @param annotations if not null, only include methods with any of these annotations
	 * @return The public methods in the class
	 * @see Class#getDeclaredMethods()
	 */
	public static List<Method> getDeclaredMethodsRecur(Class<?> clazz, int modifiers, boolean includeObject, Class<? extends Annotation>... annotations) {
		final List<Method> methods = new ArrayList<Method>();
		while (clazz != null && (includeObject || !Object.class.equals(clazz))) {
			for (Method method : clazz.getDeclaredMethods()) {
				if ((annotations == null || hasAnyAnnotation(method, annotations)) && hasModifiers(method, modifiers)) {
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	/**
	 * Check if the {@link Member} has all of the specified modifiers.
	 *
	 * @param member the Member to check
	 * @param modifiers the modifier mask
	 * @return Whether or not the member has all the modifiers.
	 */
	public static boolean hasModifiers(Member member, int modifiers) {
		return (modifiers & member.getModifiers()) == modifiers;
	}

	/**
	 * Check if the {@link AccessibleObject} has any of the specified annotations.
	 *
	 * @param object the object to check
	 * @param annotations the annotations to look for
	 * @return Whether or not the object has the annotations
	 */
	public static boolean hasAnyAnnotation(AccessibleObject object, Class<? extends Annotation>... annotations) {
		for (Class<? extends Annotation> annotation : annotations) {
			if (object.isAnnotationPresent(annotation)) {
				return true;
			}
		}
		return false;
	}
}
