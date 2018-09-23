package org.qetools.task_generator;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.qetools.task_generator.jql.WithField.withField;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class TaskGeneratorTest extends AbstractTaskGeneratorTest {

	public static final String PROJECT = "TEST";

	private JiraClientImpl jira;

	public TaskGeneratorTest() {
		super(PROJECT);
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Before
	public void initializeJiraClient() {
		jira = new JiraClientImpl();
	}

	@After
	public void cleanSystemProperties() {
		System.getProperties().remove("epicNumber1");
		System.getProperties().remove("taskNumber1");
		System.getProperties().remove("subtaskNumber1");
		System.getProperties().remove("subtaskNumber2");
		System.getProperties().remove("customFileName");
	}

	@Test
	public void testGeneratingWithSubstitution() throws Exception {
		System.setProperty("epicNumber1", "1");
		System.setProperty("taskNumber1", "11");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithEnvSubstitution() throws Exception {
		File yamlFile = getFile("template-substitution-env.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic", "Epic 1", System.getProperty("user.name"), "1.0");
	}

	@Test
	public void testGeneratingWithMissingSubstitution() throws Exception {
		System.setProperty("epicNumber1", "1");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		File yamlFile = getFile("template-substitution.yaml");
		getTaskGenerator().generate(yamlFile);
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
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 01");
		assertIssue(1, PROJECT + "-2", "Task 01");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithSubstitutionInPropertyFileDefinedByVariable() throws Exception {
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		System.setProperty("customFileName", "custom1");
		File yamlFile = getFile("template-substitution-propertyFile-var.yaml");
		getTaskGenerator().generate(yamlFile);
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
		getTaskGenerator().generate(yamlFile);
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
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 001");
		assertIssue(1, PROJECT + "-2", "Task 011");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingEpicWithPasswordBase64() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		getTaskGenerator("jira2.properties").generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic 1");
	}

	@Test
	public void testGeneratingEpicAndTaskWithInheritance() throws Exception {
		File yamlFile = getFile("template-epic-task-inheritance.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, PROJECT + "-2", "Task", "Task 11", "user11", "2.0");
		assertIssue(2, PROJECT + "-3", "Task", "Task 12", "user12", "1.0");
		assertIssue(3, PROJECT + "-4", "Task", "Task 13", "user1", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskWithInheritance() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask-inheritance.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(13));
		assertIssue(0, PROJECT + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, PROJECT + "-2", "Task", "Task 11", "user11", "1.1");
		assertIssue(2, PROJECT + "-3", "Sub-task", "Subtask 111", "user111", "1.1.1");
		assertIssue(3, PROJECT + "-4", "Sub-task", "Subtask 112", "user112", "1.1");
		assertIssue(4, PROJECT + "-5", "Sub-task", "Subtask 113", "user11", "1.1");
		assertIssue(5, PROJECT + "-6", "Task", "Task 12", "user12", "1.0");
		assertIssue(6, PROJECT + "-7", "Sub-task", "Subtask 121", "user121", "1.1.2");
		assertIssue(7, PROJECT + "-8", "Sub-task", "Subtask 122", "user122", "1.0");
		assertIssue(8, PROJECT + "-9", "Sub-task", "Subtask 123", "user12", "1.0");
		assertIssue(9, PROJECT + "-10", "Task", "Task 13", "user1", "1.0");
		assertIssue(10, PROJECT + "-11", "Sub-task", "Subtask 131", "user131", "1.1.3");
		assertIssue(11, PROJECT + "-12", "Sub-task", "Subtask 132", "user132", "1.0");
		assertIssue(12, PROJECT + "-13", "Sub-task", "Subtask 133", "user1", "1.0");
	}

	@Test
	public void testGeneratingTaskAndSubtaskWithInheritance() throws Exception {
		File yamlFile = getFile("template-task-subtask-inheritance.yaml");
		getTaskGenerator().generate(yamlFile);
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Task", "Task 1", "user1", "1.0");
		assertIssue(1, PROJECT + "-2", "Sub-task", "Subtask 11", "user11", "2.0");
		assertIssue(2, PROJECT + "-3", "Sub-task", "Subtask 12", "user12", "1.0");
		assertIssue(3, PROJECT + "-4", "Sub-task", "Subtask 13", "user1", "1.0");
	}

	@Override
	protected void assertNumberOfIssues(int expectedNumberOfIssues) {
		collector.checkThat(jira.getAllIssues().size(), equalTo(expectedNumberOfIssues));
	}

	private void assertIssue(int index, String expectedKey, String expectedSummary) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
				jira.exists(withField("key", expectedKey)), is(true));
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary,
			String expectedAssignee, String expectedVersion) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
				jira.exists(withField("key", expectedKey)), is(true));
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
	}

	@Override
	protected TaskGenerator getTaskGenerator() throws IOException {
		return getTaskGenerator("jira1.properties");
	}

	@Override
	protected TaskGenerator getTaskGenerator(String fileName) throws IOException {
		return new TaskGenerator(jira, getFile(fileName));
	}

}
