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
package org.spout.cereal.config.yaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;

import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.FileConfiguration;
import org.spout.cereal.config.MapBasedConfiguration;
import org.spout.cereal.data.IOFactory;

/**
 * A configuration that loads from a YAML file
 */
public class YamlConfiguration extends MapBasedConfiguration implements FileConfiguration {
	public static final String LINE_BREAK = DumperOptions.LineBreak.getPlatformLineBreak().getString();
	public static final char COMMENT_CHAR = '#';
	public static final Pattern COMMENT_REGEX = Pattern.compile(COMMENT_CHAR + " ?(.*)");
	private final IOFactory factory;
	private final Yaml yaml;
	private String[] header = null;

	public YamlConfiguration(java.io.File file) {
		this(new IOFactory.File(file));
	}

	public YamlConfiguration(InputStream stream) {
		this(new IOFactory.Stream(stream, null));
	}

	public YamlConfiguration(Reader reader) {
		this(new IOFactory.Direct(reader, null));
	}

	public YamlConfiguration(String string) {
		this(new IOFactory.String(string));
	}

	public YamlConfiguration() {
		this((IOFactory) null);
	}

	public YamlConfiguration(IOFactory factory) {
		this.factory = factory;
		DumperOptions options = new DumperOptions();

		options.setIndent(4);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

		yaml = new Yaml(new SafeConstructor(), new EmptyNullRepresenter(), options);
	}

	@Override
	protected Map<?, ?> loadToMap() throws ConfigurationException {
		// Allow the usage of temporary empty YamlConfiguration objects.
		if (factory == null) {
			return Collections.emptyMap();
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(getReader());
			List<String> header = new ArrayList<String>();
			boolean inHeader = true;
			String str;
			StringBuilder buffer = new StringBuilder(10000);
			while ((str = in.readLine()) != null) {
				if (inHeader) {
					if (str.trim().startsWith("#")) {
						header.add(str);
					} else {
						inHeader = false;
					}
				}
				buffer.append(str.replaceAll("\t", "    "));
				buffer.append(LINE_BREAK);
			}

			if (header.size() > 0) {
				setHeader(header.toArray(new String[header.size()]));
			}

			Object val = yaml.load(new StringReader(buffer.toString()));
			if (val instanceof Map<?, ?>) {
				return (Map<?, ?>) val;
			}
		} catch (YAMLException e) {
			throw new ConfigurationException(e);
		} catch (FileNotFoundException ignore) {
		} catch (IOException e) {
			throw new ConfigurationException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ignore) {
			}
		}
		return Collections.emptyMap();
	}

	@Override
	protected void saveFromMap(Map<?, ?> map) throws ConfigurationException {
		// Allow the usage of YamlConfiguration objects not created from a File.
		if (factory == null) {
			return;
		}
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(getWriter());
			if (getHeader() != null) {
				for (String line : getHeader()) {
					writer.append(COMMENT_CHAR).append(" ").append(line).append(LINE_BREAK);
				}

				writer.append(LINE_BREAK);
			}

			yaml.dump(map, writer);
		} catch (YAMLException e) {
			throw new ConfigurationException(e);
		} catch (IOException e) {
			throw new ConfigurationException(e);
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	public String getYamlString() throws ConfigurationException {
		StringWriter writer = null;

		try {
			writer = new StringWriter();
			if (getHeader() != null) {
				for (String line : getHeader()) {
					writer.append(COMMENT_CHAR).append(" ").append(line).append(LINE_BREAK);
				}

				writer.append(LINE_BREAK);
			}

			yaml.dump(getChildren(), writer);
			return writer.toString();
		} catch (YAMLException e) {
			throw new ConfigurationException(e);
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	public void setHeader(String... headerLines) {
		if (headerLines.length == 1) {
			headerLines = headerLines[0].split(LINE_BREAK);
		}
		String[] header = new String[headerLines.length];

		for (int i = 0; i < headerLines.length; ++i) {
			String line = headerLines[i];
			line = line.trim();
			Matcher matcher = COMMENT_REGEX.matcher(line);
			if (matcher.find()) {
				line = matcher.group(1).trim();
			}
			header[i] = line;
		}

		this.header = header;
	}

	public String[] getHeader() {
		return header;
	}

	@Override
	public java.io.File getFile() {
		return factory instanceof IOFactory.File ? ((IOFactory.File) factory).getFile() : null;
	}

	public IOFactory getIOFactory() {
		return factory;
	}

	protected Reader getReader() throws IOException {
		return factory == null ? null : factory.createReader();
	}

	protected Writer getWriter() throws IOException {
		return factory == null ? null : factory.createWriter();
	}
}
