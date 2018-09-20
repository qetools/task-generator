package org.qetools.task_generator.integration;

import java.util.ArrayList;
import java.util.List;

public class FakeJiraResponse {

	private List<FakeJiraIssue> issues;

	public FakeJiraResponse(FakeJiraIssue... issues) {
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
