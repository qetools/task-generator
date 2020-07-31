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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.qetools.task_generator.jql.WithField.withField;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.IOException;

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
		generate("template-substitution.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 1");
		assertIssue(1, PROJECT + "-2", "Task 11");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingWithEnvSubstitution() throws Exception {
		generate("template-substitution-env.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic", "Epic 1", null, System.getProperty("user.name"), "1.0");
	}

	@Test
	public void testGeneratingWithMissingSubstitution() throws Exception {
		System.setProperty("epicNumber1", "1");
		System.setProperty("subtaskNumber1", "111");
		System.setProperty("subtaskNumber2", "112");
		generate("template-substitution.yaml");
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
		generate("template-substitution-propertyFile.yaml");
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
		generate("template-substitution-propertyFile-var.yaml");
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
		generate("template-substitution-propertyFile.yaml");
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
		generate("template-substitution-propertyFiles.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic 001");
		assertIssue(1, PROJECT + "-2", "Task 011");
		assertIssue(2, PROJECT + "-3", "Subtask 111");
		assertIssue(3, PROJECT + "-4", "Subtask 112");
	}

	@Test
	public void testGeneratingEpicWithPasswordBase64() throws Exception {
		generate("template-epic.yaml", "jira2.properties");
		collector.checkThat(jira.getAllIssues().size(), equalTo(1));
		assertIssue(0, PROJECT + "-1", "Epic 1");
	}

	@Test
	public void testGeneratingEpicAndTaskWithInheritance() throws Exception {
		generate("template-epic-task-inheritance.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Epic", "Epic 1", null,  "user1", "1.0", null, null,"Fuse Online");
		assertIssue(1, PROJECT + "-2", "Task", "Task 11", null, "user11", "2.0");
		assertIssue(2, PROJECT + "-3", "Task", "Task 12", null,  "user12", "1.0");
		assertIssue(3, PROJECT + "-4", "Task", "Task 13", null, "user1", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskWithInheritance() throws Exception {
		generate("template-epic-task-subtask-inheritance.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(13));
		assertIssue(0, PROJECT + "-1", "Epic", "Epic 1", null, "user1", "1.0", null, null, "Fuse Online");
		assertIssue(1, PROJECT + "-2", "Task", "Task 11", null, "user11", "1.1", "TEST-1", null,"Fuse Online");
		assertIssue(2, PROJECT + "-3", "Sub-task", "Subtask 111", null, "user111", "1.1.1", null, "TEST-2", "Fuse Online");
		assertIssue(3, PROJECT + "-4", "Sub-task", "Subtask 112", null, "user112", "1.1");
		assertIssue(4, PROJECT + "-5", "Sub-task", "Subtask 113", null, "user11", "1.1");
		assertIssue(5, PROJECT + "-6", "Task", "Task 12", null, "user12", "1.0");
		assertIssue(6, PROJECT + "-7", "Sub-task", "Subtask 121", null,"user121", "1.1.2");
		assertIssue(7, PROJECT + "-8", "Sub-task", "Subtask 122", null, "user122", "1.0");
		assertIssue(8, PROJECT + "-9", "Sub-task", "Subtask 123", null, "user12", "1.0");
		assertIssue(9, PROJECT + "-10", "Task", "Task 13", null, "user1", "1.0");
		assertIssue(10, PROJECT + "-11", "Sub-task", "Subtask 131", null, "user131", "1.1.3");
		assertIssue(11, PROJECT + "-12", "Sub-task", "Subtask 132", null, "user132", "1.0");
		assertIssue(12, PROJECT + "-13", "Sub-task", "Subtask 133", null, "user1", "1.0");
	}

	@Test
	public void testGeneratingTaskAndSubtaskWithInheritance() throws Exception {
		generate("template-task-subtask-inheritance.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(4));
		assertIssue(0, PROJECT + "-1", "Task", "Task 1", null, "user1", "1.0");
		assertIssue(1, PROJECT + "-2", "Sub-task", "Subtask 11", null, "user11", "2.0");
		assertIssue(2, PROJECT + "-3", "Sub-task", "Subtask 12", null, "user12", "1.0");
		assertIssue(3, PROJECT + "-4", "Sub-task", "Subtask 13", null, "user1", "1.0");
	}

	@Test
	public void testGeneratingWithYamlAnchor() throws Exception {
		generate("template-anchor.yaml");
		collector.checkThat(jira.getAllIssues().size(), equalTo(3));
		assertIssue(0, PROJECT + "-1", "Task", "Task 1", "Cool description\n", "user1", null);
		assertIssue(1, PROJECT + "-2", "Task", "Task 2", "Cool description\n", "user2", null);
		assertIssue(2, PROJECT + "-3", "Task", "Task 3", null, "user3", null);
	}

	@Override
	protected void assertNumberOfIssues(int expectedNumberOfIssues) {
		collector.checkThat(jira.getAllIssues().size(), equalTo(expectedNumberOfIssues));
	}

	private void assertIssue(int index, String expectedKey, String expectedSummary) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'", jira.get(withField("key", expectedKey)),
				notNullValue());
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary, String expectedDescription,
			String expectedAssignee, String expectedVersion) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
				jira.get(withField("key", expectedKey)), notNullValue());
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary, String expectedDescription,
			String expectedAssignee, String expectedVersion, String expectedEpic, String expectedParent) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
				jira.get(withField("key", expectedKey)), notNullValue());
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
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
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
			jira.get(withField("key", expectedKey)), notNullValue());
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
		collector.checkThat(jira.getAllIssues().get(index).getField("epic"), equalTo(expectedEpic));
		collector.checkThat(jira.getAllIssues().get(index).getField("parent"), equalTo(expectedParent));
		collector.checkThat(jira.getAllIssues().get(index).getField("components"), equalTo(expectedComponent));
	}

	@Override
	protected void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary, String expectedVersion, String expectedDescription,
		String expectedAssignee, String expectedComponents){
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'",
			jira.get(withField("key", expectedKey)), notNullValue());
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("summary"), equalTo(expectedSummary));
		collector.checkThat(jira.getAllIssues().get(index).getField("fixVersion"), equalTo(expectedVersion));
		collector.checkThat(jira.getAllIssues().get(index).getField("description"), equalTo(expectedDescription));
		collector.checkThat(jira.getAllIssues().get(index).getField("assignee"), equalTo(expectedAssignee));
		collector.checkThat(jira.getAllIssues().get(index).getField("components"), equalTo(expectedComponents));
	};

	@Override
	protected void assertIssueSecurity(int index, String expectedKey, String expectedSecurityId) {
		collector.checkThat("Cannot find issue with key '" + expectedKey + "'", jira.get(withField("key", expectedKey)),
				notNullValue());
		collector.checkThat(jira.getAllIssues().get(index).getField("key"), equalTo(expectedKey));
		collector.checkThat(jira.getAllIssues().get(index).getField("security"), equalTo(expectedSecurityId));
	}

	@Override
	protected void generate(String yamlFile) throws IOException {
		generate(yamlFile, "jira1.properties");
	}

	@Override
	protected void generate(String yamlFile, String configFile) throws IOException {
		new TaskGenerator(jira, getFile(configFile)).generate(getFile(yamlFile));
	}

}
