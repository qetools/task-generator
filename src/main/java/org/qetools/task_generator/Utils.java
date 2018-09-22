package org.qetools.task_generator;

import static org.apache.commons.text.lookup.StringLookupFactory.INSTANCE;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringSubstitutor;

public class Utils {

	public static File getRelativeFile(File file, String path) {
		StringSubstitutor defaultResolver = new StringSubstitutor(INSTANCE.systemPropertyStringLookup());
		path = defaultResolver.replace(path);
		if (Paths.get(path).isAbsolute()) {
			return new File(path);
		}
		File parent = file.isFile() ? file.getParentFile() : file;
		return new File(parent, path);
	}

	public static Map<String, String> parseQuery(String query) {
		Map<String, String> map = new HashMap<>();
		String keyValueRegex = "(\\w+)\\s*=\\s*\"([^(AND)]+)\"";
		Pattern keyValuePattern = Pattern.compile(keyValueRegex);
		Matcher m = keyValuePattern.matcher(query);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}
		return map;
	}
}
