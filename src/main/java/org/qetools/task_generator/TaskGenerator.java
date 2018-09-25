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

import static org.qetools.task_generator.jql.WithField.withField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.api.JiraIssue;
import org.qetools.task_generator.core.Epic;
import org.qetools.task_generator.core.Task;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TaskGenerator {

	private JiraClient jira;
	private File propertyFile;
	private StringSubstitutor variableResolver;

	public TaskGenerator(JiraClient jira) throws FileNotFoundException, IOException {
		this(jira, null);
	}

	public TaskGenerator(JiraClient jira, File propertyFile) throws FileNotFoundException, IOException {
		this.jira = jira;
		this.propertyFile = propertyFile;
	}

	public void generate(File yamlFile) {
		Template template = loadYamlFile(yamlFile);
		initializeJiraClient(template, yamlFile);

		template.getEpics().forEach(epic -> createEpic(epic));
		template.getTasks().forEach(task -> createTask(task, null));
	}

	protected void initializeJiraClient(Template template, File yamlFile) {
		List<File> propertyFiles = new ArrayList<>();
		if (propertyFile != null) {
			propertyFiles.add(propertyFile);
		}
		template.getPropertyFiles().forEach(path -> propertyFiles.add(Utils.getRelativeFile(yamlFile, path)));
		variableResolver = new StringSubstitutor(new PropertiesLookup(propertyFiles));

		String jiraUrl = variableResolver.getStringLookup().lookup("JIRA_URL");
		String jiraUsername = variableResolver.getStringLookup().lookup("JIRA_USERNAME");
		String jiraPassword = variableResolver.getStringLookup().lookup("JIRA_PASSWORD");
		String jiraPasswordBase64 = variableResolver.getStringLookup().lookup("JIRA_PASSWORD_BASE64");

		jira.setUrl(jiraUrl);
		if (jiraPassword == null) {
			if (jiraPasswordBase64 == null) {
				throw new RuntimeException("Please specify a password");
			}
			jiraPassword = new String(Base64.getDecoder().decode(jiraPasswordBase64), Charset.forName("UTF-8"));
		}
		jira.setCredentials(jiraUsername, jiraPassword);
		jira.initialize();
	}

	protected void createEpic(Epic epic) {
		epic.setIssueType("Epic");
		if (!jira.exists(withField("summary", epic.getSummary()))) {
			JiraIssue createdIssue = jira.create(fields(epic));
			epic.setKey(createdIssue.getField("key"));
		}
		epic.getTasks().forEach(task -> createTask(task, epic));
		epic.getSubtasks().forEach(subtask -> createSubtask(subtask, epic));
	}

	protected void createTask(Task task, Epic epic) {
		task.setIssueType("Task");
		task.setEpic(epic);
		if (!jira.exists(withField("summary", task.getSummary()))) {
			JiraIssue createdIssue = jira.create(fields(setMissingFields(task, epic)));
			task.setKey(createdIssue.getField("key"));
		}
		task.getSubtasks().forEach(subtask -> createSubtask(subtask, task));
	}

	protected void createSubtask(Task subtask, Task task) {
		subtask.setIssueType("Sub-task");
		subtask.setParent(task);
		if (!jira.exists(withField("summary", subtask.getSummary()))) {
			jira.create(fields(setMissingFields(subtask, task)));
		}
	}

	protected Map<String, String> fields(Task task) {
		Map<String, String> fields = new HashMap<>();
		fields.put("issuetype", variableResolver.replace(task.getIssueType()));
		fields.put("project", variableResolver.getStringLookup().lookup("JIRA_PROJECT"));
		fields.put("summary", variableResolver.replace(task.getSummary()));
		fields.put("assignee", variableResolver.replace(task.getAssignee()));
		fields.put("fixVersion", variableResolver.replace(task.getFixVersion()));
		if (task.getEpic() != null) {
			fields.put("epic", variableResolver.replace(task.getEpic().getKey()));
		}
		if (task.getParent() != null) {
			fields.put("parent", variableResolver.replace(task.getParent().getKey()));
		}
		return fields;
	}

	protected Task setMissingFields(Task child, Task parent) {
		if (parent == null) {
			return child;
		}
		if (child.getAssignee() == null) {
			child.setAssignee(parent.getAssignee());
		}
		if (child.getFixVersion() == null) {
			child.setFixVersion(parent.getFixVersion());
		}
		return child;
	}

	private static Template loadYamlFile(File yamlFile) {
		Constructor constructor = new Constructor(Template.class);
		Yaml yaml = new Yaml(constructor);

		InputStream input = null;
		try {
			input = new FileInputStream(yamlFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		Template template = yaml.loadAs(input, Template.class);
		return template;
	}
}
