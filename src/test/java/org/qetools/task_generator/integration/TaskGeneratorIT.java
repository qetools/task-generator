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
