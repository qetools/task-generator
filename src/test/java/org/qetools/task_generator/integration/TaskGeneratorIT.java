package org.qetools.task_generator.integration;

import static org.hamcrest.core.IsEqual.equalTo;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.qetools.task_generator.AbstractTaskGeneratorTest;
import org.qetools.task_generator.TaskGenerator;
import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.impl.JiraClientRcarz;

public class TaskGeneratorIT extends AbstractTaskGeneratorTest {

	public static final String PROJECT = "MYPROJECT";

	private static FakeJira jira = FakeJira.getInstance();

	public TaskGeneratorIT() {
		super(PROJECT);
	}

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

	@Override
	protected void assertNumberOfIssues(int expectedNumberOfIssues) {
		collector.checkThat(jira.getAllIssues().size(), equalTo(expectedNumberOfIssues));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary,
			String expectedAssignee, String expectedVersion) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("issuetype"), equalTo(expectedIssueType));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
	}

	@Override
	protected TaskGenerator getTaskGenerator() throws IOException {
		return getTaskGenerator("jira3.properties");
	}

	@Override
	protected TaskGenerator getTaskGenerator(String fileName) throws IOException {
		return new TaskGenerator(jiraClient, getFile(fileName));
	}

}
