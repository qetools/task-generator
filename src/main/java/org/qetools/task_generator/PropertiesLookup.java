package org.qetools.task_generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.text.lookup.StringLookup;

public class PropertiesLookup implements StringLookup {

	private Properties props;

	public PropertiesLookup(List<File> propertyFiles) {
		this(null, propertyFiles);
	}

	public PropertiesLookup(Map<String, String> map, List<File> propertyFiles) {
		props = new Properties();
		propertyFiles.forEach(file -> {
			try {
				props.load(new FileReader(file));
			} catch (IOException e) {
				throw new RuntimeException("Cannot load properties from '" + file.getAbsolutePath() + "'", e);
			}
		});
	}

	public PropertiesLookup(Properties props) {
		this.props = props;
	}

	@Override
	public String lookup(String key) {
		return System.getProperty(key, (String) props.get(key));
	}

}
