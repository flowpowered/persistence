/*
 * This file is part of Flow Cerealization, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
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
package com.flowpowered.cerealization.data;

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
