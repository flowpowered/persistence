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
package org.spout.cereal.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * A factory for creating IO objects from a source
 */
public interface IOFactory {
	public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

	public Reader createReader() throws IOException;

	public Writer createWriter() throws IOException;

	/**
	 * This factory should only be used when you know where a config is going to be read from or written to.
	 */
	public static class Direct implements IOFactory {
		private final Reader reader;
		private final Writer writer;

		public Direct(Reader reader, Writer writer) {
			this.reader = reader;
			this.writer = writer;
		}

		public Reader createReader() throws IOException {
			return reader;
		}

		public Writer createWriter() throws IOException {
			return writer;
		}
	}

	public static class File implements IOFactory {
		private final java.io.File file;

		public File(java.io.File file) {
			this.file = file;
		}

		public java.io.File getFile() {
			return file;
		}

		private void createFile() throws IOException {
			if (file != null && !file.exists()) {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
		}

		public Reader createReader() throws IOException {
			createFile();
			return new InputStreamReader(new FileInputStream(file), UTF_8_CHARSET);
		}

		public Writer createWriter() throws IOException {
			createFile();
			return new OutputStreamWriter(new FileOutputStream(file), UTF_8_CHARSET);
		}
	}

	public static class Stream implements IOFactory {
		private final InputStream input;
		private final OutputStream output;

		/**
		 * An IO factory that creates Readers from existing streams. Either of the parameters may be null, but not both.
		 *
		 * @param input The input stream
		 * @param output The output stream.
		 */
		public Stream(InputStream input, OutputStream output) {
			this.input = input;
			this.output = output;
		}

		public Reader createReader() throws IOException {
			return new InputStreamReader(input, UTF_8_CHARSET);
		}

		public Writer createWriter() throws IOException {
			return new OutputStreamWriter(output, UTF_8_CHARSET);
		}
	}

	public static class String implements IOFactory {
		private StringBuffer buffer;

		public String(StringBuilder buffer) {
			this.buffer = new StringBuffer(buffer);
		}

		public String(StringBuffer buffer) {
			this.buffer = buffer;
		}

		public String(java.lang.String s) {
			this.buffer = new StringBuffer(s == null ? "" : s);
		}

		public String() {
		}

		public void setData(java.lang.String value) {
			this.buffer = new StringBuffer(value);
		}

		public StringBuffer getBuffer() {
			return buffer;
		}

		public Reader createReader() throws IOException {
			return buffer == null ? new StringReader("") : new StringReader(buffer.toString());
		}

		public Writer createWriter() throws IOException {
			StringWriter w = new StringWriter();
			this.buffer = w.getBuffer();
			return w;
		}
	}
}
