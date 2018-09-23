package org.qetools.task_generator;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public abstract class AbstractTaskGeneratorTest {

	private String project;

	public AbstractTaskGeneratorTest(String project) {
		this.project = project;
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Test
	public void testGeneratingEpic() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
	}

	@Test
	public void testGeneratingEpicRetry() throws Exception {
		File yamlFile = getFile("template-epic.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
	}

	@Test
	public void testGeneratingEpicUpdate() throws Exception {
		File yamlFile = getFile("template-epic-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Epic", "Epic 2", "user2", "2.0");
	}

	@Test
	public void testGeneratingEpicAndTask() throws Exception {
		File yamlFile = getFile("template-epic-task.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-task.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-task-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0");
		assertIssue(2, project + "-3", "Task", "Task 12", "user12", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtask() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0");
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0");
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-task-subtask-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(4);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0");
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0");
		assertIssue(3, project + "-4", "Sub-task", "Subtask 112", "user112", "1.0");
	}

	@Test
	public void testGeneratingEpicAndSubtask() throws Exception {
		File yamlFile = getFile("template-epic-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", "1.0");
	}

	@Test
	public void testGeneratingEpicAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-epic-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", "1.0");
	}

	@Test
	public void testGeneratingEpicAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-epic-subtask-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0");
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", "1.0");
		assertIssue(2, project + "-3", "Sub-task", "Subtask 12", "user12", "1.0");
	}

	@Test
	public void testGeneratingTask() throws Exception {
		File yamlFile = getFile("template-task.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
	}

	@Test
	public void testGeneratingTaskRetry() throws Exception {
		File yamlFile = getFile("template-task.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
	}

	@Test
	public void testGeneratingTaskUpdate() throws Exception {
		File yamlFile = getFile("template-task-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
		assertIssue(1, project + "-2", "Task", "Task 2", "user2", null);
	}

	@Test
	public void testGeneratingTaskAndSubtask() throws Exception {
		File yamlFile = getFile("template-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null);
	}

	@Test
	public void testGeneratingTaskAndSubtaskRetry() throws Exception {
		File yamlFile = getFile("template-task-subtask.yaml");
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null);
	}

	@Test
	public void testGeneratingTaskAndSubtaskUpdate() throws Exception {
		File yamlFile = getFile("template-task-subtask-update.yaml");
		getTaskGenerator().generate(yamlFile);
		getTaskGenerator().generate(yamlFile);
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null);
		assertIssue(2, project + "-3", "Sub-task", "Subtask 12", "user12", null);
	}

	protected abstract void assertNumberOfIssues(int expectedNumberOfIssues);

	protected abstract void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary,
			String expectedAssignee, String expectedVersion);

	protected abstract TaskGenerator getTaskGenerator() throws IOException;

	protected abstract TaskGenerator getTaskGenerator(String fileName) throws IOException;

	protected File getFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}

}
