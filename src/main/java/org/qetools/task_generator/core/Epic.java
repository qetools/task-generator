package org.qetools.task_generator.core;

import java.util.ArrayList;
import java.util.List;

public class Epic {

	private String name;
	private String summary;
	private String assignee;
	private String fixVersion;
	private List<Task> tasks;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getFixVersion() {
		return fixVersion;
	}

	public void setFixVersion(String fixVersion) {
		this.fixVersion = fixVersion;
	}

	public List<Task> getTasks() {
		return tasks != null ? tasks : new ArrayList<Task>();
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
