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
package org.qetools.task_generator.integration;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.qetools.task_generator.TaskGeneratorApp.SYSTEM_PROPERTY_CONFIG;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;

import org.qetools.task_generator.AbstractTaskGeneratorTest;
import org.qetools.task_generator.TaskGeneratorApp;

import java.io.IOException;

public class TaskGeneratorIT extends AbstractTaskGeneratorTest {

	public static final String PROJECT = "MYPROJECT";

	private static FakeJira jira = FakeJira.getInstance();

	public TaskGeneratorIT() {
		super(PROJECT);
	}

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

	@After
	public void cleanSystemProperties() {
		System.getProperties().remove(SYSTEM_PROPERTY_CONFIG);
	}

	@Override
	protected void assertNumberOfIssues(int expectedNumberOfIssues) {
		assertThat("Enexpected number of issues", jira.getAllIssues().size(), equalTo(expectedNumberOfIssues));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary, String expectedDescription,
			String expectedAssignee, String expectedVersion) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("issuetype"), equalTo(expectedIssueType));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary, String expectedVersion, String expectedDescription,
		String expectedAssignee, String expectedComponent) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("issuetype"), equalTo(expectedIssueType));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("component"), equalTo(expectedComponent));

	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary,  String expectedDescription,
			String expectedAssignee, String expectedVersion, String expectedEpic, String expectedParent) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("issuetype"), equalTo(expectedIssueType));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
		collector.checkThat(jira.getAllIssues().get(index).getField("epic"), equalTo(expectedEpic));
		collector.checkThat(jira.getAllIssues().get(index).getField("parent"), equalTo(expectedParent));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary, String expectedDescription,
		String expectedAssignee, String expectedVersion, String expectedEpic, String expectedParent, String expectedComponent) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
		collector.checkThat(jira.getAllIssues().get(index).getField("epic"), equalTo(expectedEpic));
		collector.checkThat(jira.getAllIssues().get(index).getField("parent"), equalTo(expectedParent));
		collector.checkThat(jira.getAllIssues().get(index).getField("component"), equalTo(expectedComponent));
	}

	@Override
	protected void assertIssueSecurity(int index, String expectedKey, String expectedSecurityId) {
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("security"), equalTo(expectedSecurityId));
	}

	@Override
	protected void generate(String yamlFile) throws IOException {
		generate(yamlFile, "jira3.properties");
	}

	@Override
	protected void generate(String yamlFile, String configFile) throws IOException {
		if (configFile != null) {
			System.setProperty(SYSTEM_PROPERTY_CONFIG, getFile(configFile).getAbsolutePath());
		}
		TaskGeneratorApp.main(new String[] { getFile(yamlFile).getAbsolutePath() });
	}

}
