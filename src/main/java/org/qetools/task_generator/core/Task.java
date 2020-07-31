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
package org.qetools.task_generator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Task or subtask. Tasks can be linked to an epic. Tasks can contain subtasks.
 * 
 * @author Andrej Podhradsky
 *
 */
public class Task {

	private String key;
	private String issueType;
	private String summary;
	private String assignee;
	private String fixVersion;
	private List<Task> subtasks;
	private Epic epic;
	private Task parent;
	private String description;
	private String component;
	private List<String> components;
	private String security;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public List<String> getComponents(){
		return components != null ? components : new ArrayList<String>();
	}
	public void setComponents(List<String> components){
		this.components = components;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}
}
