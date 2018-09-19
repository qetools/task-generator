package org.qetools.task_generator;

import java.util.ArrayList;
import java.util.List;

import org.qetools.task_generator.core.Epic;
import org.qetools.task_generator.core.Task;

public class Template {

	private List<Epic> epics;
	private List<Task> tasks;

	public List<Epic> getEpics() {
		return epics;
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
