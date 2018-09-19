package org.qetools.task_generator.core;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

	private String name;
	private List<Task> tasks;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTasks() {
		return tasks != null ? tasks : new ArrayList<Task>();
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
