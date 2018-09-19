package org.qetools.task_generator;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.qetools.task_generator.impl.JiraClientImpl.PROJECT;
import static org.qetools.task_generator.jql.WithField.withField;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.qetools.task_generator.TaskGenerator;
import org.qetools.task_generator.impl.JiraClientImpl;

public class TaskGeneratorTest {

	private JiraClientImpl jira;

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Before
	public void initializeJiraClient() {
		jira = new JiraClientImpl();
		jira.initialize();
	}

	@Test
	public void testGeneratingEpic() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic 1");
	}

	@Test
	public void testGeneratingEpicRetry() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic 1");
	}

	@Test
	public void testGeneratingEpicUpdate() throws Exception {
		File yamlFile = getFile("template-epic-update.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Epic 2");
	}

	@Test
	public void testGeneratingEpicAndTask() throws Exception {
		File yamlFile = getFile("template-epic-task.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
	}

	@Test
	public void testGeneratingEpicAndTaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-task.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
	}

	@Test
	public void testGeneratingEpicAndTaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-task-update.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Task 12");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtask() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask-update.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	private void assertIssue(int index, String expectedKey, String expectedSummary) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
				jira.exists(withField("key", expectedKey)), is(true));
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
	}

	private File getFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}
}
