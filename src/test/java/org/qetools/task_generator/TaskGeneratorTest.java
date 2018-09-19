package org.qetools.task_generator;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.qetools.task_generator.impl.JiraClientImpl.PROJECT;
import static org.qetools.task_generator.jql.WithField.withField;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
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

	@After
	public void cleanSystemProperties() {
		System.getProperties().remove("epicNumber1");
		System.getProperties().remove("taskNumber1");
		System.getProperties().remove("subtaskNumber1");
		System.getProperties().remove("subtaskNumber2");
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

	@Test
	public void testGeneratingEpicAndSubtask() throws Exception {
		File yamlFile = getFile("template-epic-subtask.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingEpicAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-subtask.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingEpicAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-subtask-update.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
		assertIssue(2, PROJECT + "-3", "Subtask 12");
	}

	@Test
	public void testGeneratingTask() throws Exception {
		File yamlFile = getFile("template-task.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Task 1");
	}

	@Test
	public void testGeneratingTaskRetry() throws Exception {
		File yamlFile = getFile("template-task.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Task 1");
	}

	@Test
	public void testGeneratingTaskUpdate() throws Exception {
		File yamlFile = getFile("template-task-update.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Task 2");
	}

	@Test
	public void testGeneratingTaskAndSubtask() throws Exception {
		File yamlFile = getFile("template-task-subtask.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingTaskAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-task-subtask.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(2));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
	}

	@Test
	public void testGeneratingTaskAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-task-subtask-update.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Task 1");
		assertIssue(1, PROJECT + "-2", "Subtask 11");
		assertIssue(2, PROJECT + "-3", "Subtask 12");
	}

	@Test
	public void testGeneratingWithSubstitution() throws Exception {
		System.setProperty("epicNumber1", "1");
		System.setProperty("taskNumber1", "11");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithMissingSubstitution() throws Exception {
		System.setProperty("epicNumber1", "1");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task ${taskNumber1}");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithSubstitutionInPropertyFile() throws Exception {
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution-propertyFile.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 01");
		assertIssue(1, PROJECT + "-2", "Task 01");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithSubstitutionInPropertyFileAndOverridenBySystemProperty() throws Exception {
		System.setProperty("taskNumber1", "11");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution-propertyFile.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 01");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithSubstitutionInPropertyFiles() throws Exception {
		System.setProperty("taskNumber1", "011");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution-propertyFiles.yaml");
		new TaskGenerator(jira).generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 001");
		assertIssue(1, PROJECT + "-2", "Task 011");
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
