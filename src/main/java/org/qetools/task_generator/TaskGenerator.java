package org.qetools.task_generator;

import static org.qetools.task_generator.jql.WithField.withField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.core.Epic;
import org.qetools.task_generator.core.Task;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TaskGenerator {

	public static final String JIRA_URL = "https://issues.jboss.org";

	private JiraClient jira;

	public TaskGenerator(JiraClient jira) throws FileNotFoundException, IOException {
//		Properties jiraCredentials = new Properties();
//		jiraCredentials.load(new FileReader(System.getProperty("user.home") + "/.ssh/.jira_credentials"));
//		String username = jiraCredentials.getProperty("username");
//		String password = jiraCredentials.getProperty("password");

		this.jira = jira;
		this.jira.setUrl(JIRA_URL);
//		this.jira.setCredentials(username, password);
	}

	public TaskGenerator(JiraClient jira, File credentialsFile) throws FileNotFoundException, IOException {
		Properties jiraCredentials = new Properties();
		jiraCredentials.load(new FileReader(credentialsFile));
		String username = jiraCredentials.getProperty("username");
		String password = jiraCredentials.getProperty("password");

		this.jira = jira;
		this.jira.setUrl(JIRA_URL);
		this.jira.setCredentials(username, password);
	}

	public void generate(File yamlFile) {
		Template template = loadYamlFile(yamlFile);
		template.getEpics().forEach(epic -> createEpic(epic));
		template.getTasks().forEach(task -> createTask(task, null));
	}

	public void createEpic(Epic epic) {
		if (!jira.exists(withField("summary", epic.getSummary()))) {
			jira.create(fields(epic));
		}
		epic.getTasks().forEach(task -> createTask(task, epic));
	}

	public void createTask(Task task, Epic epic) {
		if (!jira.exists(withField("summary", task.getSummary()))) {
			jira.create(fields(task));
		}
		task.getSubtasks().forEach(subtask -> createSubtask(subtask, task));
	}

	public void createSubtask(Task subtask, Task task) {
		if (!jira.exists(withField("summary", subtask.getSummary()))) {
			jira.create(fields(subtask));
		}
	}

	private static Map<String, String> fields(Epic epic) {
		Map<String, String> fields = new HashMap<>();
		fields.put("summary", epic.getSummary());
		fields.put("assignee", epic.getAssignee());
		fields.put("fixVersion", epic.getFixVersion());
		return fields;
	}

	private static Map<String, String> fields(Task task) {
		Map<String, String> fields = new HashMap<>();
		fields.put("summary", task.getSummary());
		fields.put("assignee", task.getAssignee());
		fields.put("fixVersion", task.getFixVersion());
		return fields;
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
