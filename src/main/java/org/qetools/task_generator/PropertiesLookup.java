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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.text.lookup.StringLookup;

/**
 * Looks up for properties defined in properties files or as a system property.
 * 
 * @author Andrej Podhradsky
 *
 */
public class PropertiesLookup implements StringLookup {

	private Properties props;

	/**
	 * Constructs the lookup from a list of properties files. Note that it depends
	 * on the order. The last property definition overrides the previous one.
	 * 
	 * @param propertyFiles List of properties files
	 */
	public PropertiesLookup(List<File> propertyFiles) {
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
