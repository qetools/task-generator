package org.qetools.task_generator.core;

import java.util.ArrayList;
import java.util.List;

public class Task {

	private TaskType type;
	private String summary;
	private String assignee;
	private String fixVersion;
	private List<Task> subtasks;

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
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

	public void setAssignee(String owner) {
		this.assignee = owner;
	}

	public String getFixVersion() {
		return fixVersion;
	}

	public void setFixVersion(String fixVersion) {
		this.fixVersion = fixVersion;
	}

	public List<Task> getSubtasks() {
		return subtasks != null ? subtasks : new ArrayList<Task>();
	}

	public void setSubtasks(List<Task> subtasks) {
		this.subtasks = subtasks;
	}

}
