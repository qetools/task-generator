package org.qetools.task_generator.integration;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;

public class FakeJiraTest {

	@BeforeClass
	public static void startFakeJira() {
		FakeJira.getInstance().start();
	}

	@AfterClass
	public static void stopFakeJira() {
		FakeJira.getInstance().stop();
	}

	@Before
	public void clearFakeJira() {
		FakeJira.getInstance().clear();
	}

	@Test
	public void testGettingIssue() throws Exception {
		FakeJira.getInstance().add("TEST", "Issue A");
		FakeJira.getInstance().add("TEST", "Issue B");

		JiraClient jira = new JiraClient("http://localhost:4567/", null);

		Issue issue1 = jira.getIssue("TEST-1");
		Assert.assertEquals("TEST-1", issue1.getKey());
		Assert.assertEquals("1", issue1.getId());

		Issue issue2 = jira.getIssue("TEST-2");
		Assert.assertEquals("TEST-2", issue2.getKey());
		Assert.assertEquals("2", issue2.getId());
	}

	@Test
	public void testCreatingSubtaskIssue() throws Exception {
		JiraClient jira = new JiraClient("http://localhost:4567/", null);

		Issue task = jira.createIssue("TEST", "Sub-task").field(Field.SUMMARY, "Subtask A")
				.field(Field.ASSIGNEE, "apodhrad").field(Field.PARENT, "Task A").execute();
		Assert.assertEquals(task.getId(), "1");
		Assert.assertEquals(task.getKey(), "TEST-1");
		Assert.assertEquals(task.getSummary(), "Subtask A");
	}

	@Test
	public void testCreatingTaskIssue() throws Exception {
		JiraClient jira = new JiraClient("http://localhost:4567/", null);

		Issue task = jira.createIssue("TEST", "Task").field(Field.SUMMARY, "Task A").field(Field.ASSIGNEE, "apodhrad")
				.execute();
		Assert.assertEquals(task.getId(), "1");
		Assert.assertEquals(task.getKey(), "TEST-1");
		Assert.assertEquals(task.getSummary(), "Task A");
	}

	@Test
	public void testCreatingEpicIssue() throws Exception {
		JiraClient jira = new JiraClient("http://localhost:4567/", null);

		Issue task = jira.createIssue("TEST", "Epic").field(Field.SUMMARY, "Epic A").field(Field.ASSIGNEE, "apodhrad")
				.execute();
		Assert.assertEquals(task.getId(), "1");
		Assert.assertEquals(task.getKey(), "TEST-1");
		Assert.assertEquals(task.getSummary(), "Epic A");
	}

}
