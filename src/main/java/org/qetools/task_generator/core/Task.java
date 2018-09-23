package org.qetools.task_generator.core;

import java.util.ArrayList;
import java.util.List;

public class Task {

	private String key;
	private String issueType;
	private String summary;
	private String assignee;
	private String fixVersion;
	private List<Task> subtasks;
	private Epic epic;
	private Task parent;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
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

	public Epic getEpic() {
		return epic;
	}

	public void setEpic(Epic epic) {
		this.epic = epic;
	}

	public Task getParent() {
		return parent;
	}

	public void setParent(Task parent) {
		this.parent = parent;
	}

}
