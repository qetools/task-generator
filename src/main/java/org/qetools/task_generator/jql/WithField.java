package org.qetools.task_generator.jql;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.qetools.task_generator.api.JiraIssue;

public class WithField extends BaseMatcher<JiraIssue> {

	private String key;
	private String value;

	public static WithField withField(String key, String value) {
		return new WithField(key, value);
	}

	public WithField(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj == null || !(obj instanceof JiraIssue)) {
			return false;
		}
		JiraIssue issue = (JiraIssue) obj;
		return (value == null && issue.getField(key) == null) || (value != null && value.equals(issue.getField(key)));
	}

	@Override
	public void describeTo(Description description) {
	}

}
