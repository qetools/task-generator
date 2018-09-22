package org.qetools.task_generator.integration;

import static org.hamcrest.core.IsEqual.equalTo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.qetools.task_generator.TaskGenerator;
import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.impl.JiraClientRcarz;

public class TaskGeneratorIT {

	public static final String PROJECT = "MYPROJECT";

	private static FakeJira jira = FakeJira.getInstance();

	private JiraClient jiraClient;

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@BeforeClass
	public static void startFakeJira() {
		jira.start();
	}

	@AfterClass
	public static void stopFakeJira() {
		jira.stop();
	}

	@Before
	public void clearFakeJira() {
		jira.clear();
	}

	@Before
	public void initializeJiraClient() {
		jiraClient = new JiraClientRcarz();
	}

	@Test
	public void testGeneratingEpic() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic 1");
	}

	@Test
	public void testGeneratingEpicRetry() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic 1");
	}

	@Test
	public void testGeneratingEpicUpdate() throws Exception {
		File yamlFile = getFile("template-epic-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Epic 2");
	}

	@Test
	public void testGeneratingEpicAndTask() throws Exception {
		File yamlFile = getFile("template-epic-task.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
	}

	@Test
	public void testGeneratingEpicAndTaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-task.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
	}

	@Test
	public void testGeneratingEpicAndTaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-task-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Task 12");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtask() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingEpicAndSubtask() throws Exception {
		File yamlFile = getFile("template-epic-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingEpicAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingEpicAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-subtask-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
		assertIssue(2, PROJECT + "-3", "Subtask 12");
	}

	@Test
	public void testGeneratingTask() throws Exception {
		File yamlFile = getFile("template-task.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Task 1");
	}

	@Test
	public void testGeneratingTaskRetry() throws Exception {
		File yamlFile = getFile("template-task.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Task 1");
	}

	@Test
	public void testGeneratingTaskUpdate() throws Exception {
		File yamlFile = getFile("template-task-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Task 2");
	}

	@Test
	public void testGeneratingTaskAndSubtask() throws Exception {
		File yamlFile = getFile("template-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingTaskAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingTaskAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-task-subtask-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
		assertIssue(2, PROJECT + "-3", "Subtask 12");
	}

	private void assertIssue(int index, String expectedKey, String expectedSummary) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
	}

	private void assertIssue(int index, String expectedKey, String expectedSummary, String expectedAssignee,
			String expectedVersion) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
	}

	private File getFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}

	private TaskGenerator getTaskGenerator() throws FileNotFoundException, IOException {
		return getTaskGenerator("jira3.properties");
	}

	private TaskGenerator getTaskGenerator(String fileName) throws FileNotFoundException, IOException {
		return new TaskGenerator(jiraClient, getFile(fileName));
	}
}
