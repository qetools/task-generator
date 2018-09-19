package org.qetools.task_generator;

import java.io.File;
import java.nio.file.Paths;

public class Utils {

	public static File getRelativeFile(File file, String path) {
		if (Paths.get(path).isAbsolute()) {
			return new File(path);
		}
		File parent = file.isFile() ? file.getParentFile() : file;
		return new File(parent, path);
	}
}
