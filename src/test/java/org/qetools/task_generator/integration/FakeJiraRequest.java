package org.qetools.task_generator.integration;

import java.util.ArrayList;
import java.util.List;

public class FakeJiraRequest {

	private List<FakeJiraIssue> issues;

	public FakeJiraRequest(FakeJiraIssue... issues) {
		this.issues = new ArrayList<>();
		for (FakeJiraIssue issue : issues) {
			this.issues.add(issue);
		}
	}

	public List<FakeJiraIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<FakeJiraIssue> issues) {
		this.issues = issues;
	}

}
