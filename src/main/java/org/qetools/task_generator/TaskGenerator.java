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

import static org.qetools.task_generator.jql.WithSummary.withSummary;

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
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;
import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.api.JiraIssue;
import org.qetools.task_generator.core.Epic;
import org.qetools.task_generator.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Generates epics, tasks and subtasks from a YAML template.
 * 
 * @author Andrej Podhradsky
 *
 */
public class TaskGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(TaskGenerator.class);

	private JiraClient jira;
	private File propertyFile;
	private StringSubstitutor variableResolver;

	/**
	 * Constructs the generator with a given Jira client.
	 * 
	 * @param jiraClient
	 *                       Jira client
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public TaskGenerator(JiraClient jiraClient) throws FileNotFoundException, IOException {
		this(jiraClient, null);
	}

	/**
	 * Constructs the generator with a given Jira client. You can also specify a property file which will be used for
	 * variable substitution in a template.
	 * 
	 * @param jiraClient
	 *                         Jira client
	 * @param propertyFile
	 *                         Property file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public TaskGenerator(JiraClient jiraClient, File propertyFile) throws FileNotFoundException, IOException {
		this.jira = jiraClient;
		this.propertyFile = propertyFile;
	}

	/**
	 * Generates epics, tasks and subtasks from a YAML template.
	 * 
	 * @param yamlFile
	 *                     YAML template
	 */
	public void generate(File yamlFile) {
		Template template = loadYamlFile(yamlFile);
		initializeJiraClient(template, yamlFile);

		template.getEpics().forEach(epic -> createEpic(epic));
		template.getTasks().forEach(task -> createTask(task, null));
	}

	/**
	 * Initializes the Jira client with respect of variables defined an a given template. A given YAML file is used just
	 * for resolving relative paths in the template.
	 * 
	 * @param template
	 *                     Template
	 * @param yamlFile
	 *                     YAML file of the template
	 */
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

	/**
	 * Creates epic.
	 * 
	 * @param epic
	 *                 Epic
	 */
	protected void createEpic(Epic epic) {
		resolve(epic);
		LOG.info("Create epic '{}'", epic.getSummary());
		epic.setIssueType("Epic");
		Optional<JiraIssue> issue = Optional.ofNullable(jira.get(withSummary(epic.getSummary())));
		if (issue.isPresent()) {
			LOG.info("\tEpic already exists, key = {}", issue.get().getField("key"));
			epic.setKey(issue.get().getField("key"));
		} else {
			JiraIssue createdIssue = jira.create(fields(epic));
			LOG.info("\tEpic created, key = {}", createdIssue.getField("key"));
			epic.setKey(createdIssue.getField("key"));
		}
		epic.getTasks().forEach(task -> createTask(task, epic));
		epic.getSubtasks().forEach(subtask -> createSubtask(subtask, epic));
	}

	/**
	 * Creates a task which will be linked the an epic.
	 * 
	 * @param task
	 *                 Task
	 * @param epic
	 *                 Epic
	 */
	protected void createTask(Task task, Epic epic) {
		resolve(task);
		LOG.info("Create task '{}'", task.getSummary());
		task.setIssueType("Task");
		task.setEpic(epic);
		setMissingFields(task, epic);
		Optional<JiraIssue> issue = Optional.ofNullable(jira.get(withSummary(task.getSummary())));
		if (issue.isPresent()) {
			LOG.info("\tTask already exists, key = {}", issue.get().getField("key"));
			task.setKey(issue.get().getField("key"));
		} else {
			JiraIssue createdIssue = jira.create(fields(task));
			LOG.info("\tTask created, key = {}", createdIssue.getField("key"));
			task.setKey(createdIssue.getField("key"));
		}
		task.getSubtasks().forEach(subtask -> createSubtask(subtask, task));
	}

	/**
	 * Creates a subtask of a given task / epic.
	 * 
	 * @param subtask
	 *                    Subtask
	 * @param task
	 *                    Parent task / epic
	 */
	protected void createSubtask(Task subtask, Task task) {
		resolve(subtask);
		LOG.info("Create sub-task '{}'", subtask.getSummary());
		subtask.setIssueType("Sub-task");
		subtask.setParent(task);
		setMissingFields(subtask, task);
		Optional<JiraIssue> issue = Optional.ofNullable(jira.get(withSummary(subtask.getSummary())));
		if (issue.isPresent()) {
			LOG.info("\tSub-task already exists, key = {}", issue.get().getField("key"));
		} else {
			JiraIssue createdIssue = jira.create(fields(subtask));
			LOG.info("\tSub-task created, key = {}", createdIssue.getField("key"));
			subtask.setKey(createdIssue.getField("key"));
		}
	}

	protected Map<String, String> fields(Task task) {
		Map<String, String> fields = new HashMap<>();
		fields.put("issuetype", variableResolver.replace(task.getIssueType()));
		fields.put("project", variableResolver.getStringLookup().lookup("JIRA_PROJECT"));
		fields.put("summary", variableResolver.replace(task.getSummary()));
		fields.put("description", variableResolver.replace(task.getDescription()));
		fields.put("assignee", variableResolver.replace(task.getAssignee()));
		fields.put("fixVersion", variableResolver.replace(task.getFixVersion()));
		if (!task.getComponents().isEmpty()) {
			fields.put("components", variableResolver.replace(task.getComponents()));
		} else {
			fields.put("components", variableResolver.replace(task.getComponent()));
		}
		if (task.getSecurity() != null) {
			fields.put("security", variableResolver.replace(task.getSecurity()));
		}
		if (task.getEpic() != null) {
			fields.put("epic", variableResolver.replace(task.getEpic().getKey()));
		}
		if (task.getParent() != null) {
			fields.put("parent", variableResolver.replace(task.getParent().getKey()));
		}
		if (task instanceof Epic) {
			Epic epic = (Epic) task;
			if (epic.getName() != null) {
				fields.put("name", epic.getName());
			} else {
				fields.put("name", epic.getSummary());
			}
		}
		return fields;
	}

	protected Task resolve(Task task) {
		task.setSummary(variableResolver.replace(task.getSummary()));
		task.setAssignee(variableResolver.replace(task.getAssignee()));
		task.setFixVersion(variableResolver.replace(task.getFixVersion()));
		task.setComponent(variableResolver.replace(task.getComponent()));
		task.setComponents(task.getComponents());
		return task;
	}

	/**
	 * Sets missing task fields from a parent.
	 * 
	 * @param child
	 *                   Child
	 * @param parent
	 *                   Parent
	 * @return All task fields
	 */
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
		if (child.getComponent() == null) {
			child.setComponent(parent.getComponent());
		}
		if (child.getComponents().isEmpty()){
			child.setComponents(parent.getComponents());
		}
		if (child.getSecurity() == null) {
			child.setSecurity(parent.getSecurity());
		}
		return child;
	}

	/**
	 * Loads a template from a given YAML file.
	 * 
	 * @param yamlFile
	 *                     YAML file
	 * @return Template
	 */
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
