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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.qetools.task_generator.core.Epic;
import org.qetools.task_generator.core.Task;

/**
 * A template which is consumed by the task generator.
 * 
 * @author Andrej Podhradsky
 *
 */
public class Template {

	private Properties properties;
	private List<String> propertyFiles;
	private List<Epic> epics;
	private List<Task> tasks;

	public List<String> getPropertyFiles() {
		return propertyFiles == null ? new ArrayList<>() : propertyFiles;
	}

	public void setPropertyFiles(List<String> propertyFiles) {
		this.propertyFiles = propertyFiles;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public List<Epic> getEpics() {
		return epics == null ? new ArrayList<>() : epics;
	}

	public void setEpics(List<Epic> epics) {
		this.epics = epics;
	}

	public List<Task> getTasks() {
		return tasks == null ? new ArrayList<>() : tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
