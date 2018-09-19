package org.qetools.task_generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.qetools.task_generator.core.Epic;
import org.qetools.task_generator.core.Task;

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
