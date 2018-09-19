package org.qetools.task_generator.jql;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.qetools.task_generator.api.JiraIssue;

public class WithSummary extends BaseMatcher<JiraIssue> {

	private String summary;

	public WithSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj == null || !(obj instanceof JiraIssue)) {
			return false;
		}
		JiraIssue issue = (JiraIssue) obj;
		return summary.equals(issue.getField("summary"));
	}

	@Override
	public void describeTo(Description description) {
	}

}
