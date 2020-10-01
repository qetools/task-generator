/*******************************************************************************
 * Copyright (C) 2018 Andrej Podhradsky
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.qetools.task_generator;

import static org.apache.commons.text.lookup.StringLookupFactory.INSTANCE;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringSubstitutor;

/**
 * 
 * @author Andrej Podhradsky
 *
 */
public class Utils {

	/**
	 * Returns a file which is computed from a parent file and a relative path. The
	 * path can also an absolute path.
	 * 
	 * @param parentfile Parent file
	 * @param path       Path
	 * @return File
	 */
	public static File getRelativeFile(File parentfile, String path) {
		StringSubstitutor defaultResolver = new StringSubstitutor(INSTANCE.systemPropertyStringLookup());
		path = defaultResolver.replace(path);
		if (Paths.get(path).isAbsolute()) {
			return new File(path);
		}
		File parent = parentfile.isFile() ? parentfile.getParentFile() : parentfile;
		return new File(parent, path);
	}

	/**
	 * Maps keys and values from a query. At the moment, only logical conjunctions
	 * are supported. For example:
	 * 
	 * <pre>
	 * key1 = value AND key2 = value2
	 * </pre>
	 * 
	 * @param query
	 * @return Map of keys and values
	 */
	public static Map<String, String> parseQuery(String query) {
		Map<String, String> map = new HashMap<>();
		String keyValueRegex = "(\\w+)\\s*[=~]\\s*\'?\"([^(AND)]+)\"\'?";
		Pattern keyValuePattern = Pattern.compile(keyValueRegex);
		Matcher m = keyValuePattern.matcher(query);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}
		return map;
	}

	/**
	 * Creates a list of strings from given strings. It also parses array strings
	 * such as
	 * 
	 * <pre>
	 * [string1, string2]
	 * </pre>
	 * 
	 * so that it doesn't create a list of a list.
	 * 
	 * @param strings
	 * @return
	 */
	public static List<String> list(String... strings) {
		List<String> list = new ArrayList<>();
		for (String string : strings) {
			if (string.matches("\\[\\w*(, \\w+)*\\]")) {
				String newString = string.substring(1, string.length() - 1);
				String[] newStrings = newString.split(",");
				for (String newStringItem : newStrings) {
					list.add(newStringItem.trim());
				}
			} else {
				list.add(string);
			}
		}
		return list;
	}
}
