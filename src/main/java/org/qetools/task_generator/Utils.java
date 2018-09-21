package org.qetools.task_generator;

import static org.apache.commons.text.lookup.StringLookupFactory.INSTANCE;

import java.io.File;
import java.nio.file.Paths;

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
}
