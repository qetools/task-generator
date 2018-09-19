package org.qetools.task_generator.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.api.JiraIssue;

public class JiraClientImpl implements JiraClient {

	public static final String PROJECT = "TEST";

	private int index;
	private List<JiraIssue> issues;

	@Override
	public void setUrl(String url) {
		// ignore
	}

	@Override
	public void setCredentials(String username, String password) {
		// ignore
	}

	@Override
	public void initialize() {
		index = 1;
		issues = new ArrayList<>();
	}

	@Override
	public JiraIssue create(Map<String, String> fields) {
		JiraIssue issue = new JiraIssueImpl(fields);
		issue.setField("key", PROJECT + "-" + index++);
		issues.add(issue);
		return issue;
	}

	@Override
	public JiraIssue create(JiraIssue issue) {
		issue.setField("key", PROJECT + "-" + index++);
		issues.add(issue);
		return issue;
	}

	@Override
	public List<JiraIssue> search(String jql) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<JiraIssue> getAllIssues() {
		return Collections.unmodifiableList(issues);
	}

	@Override
	public boolean exists(String summary) {
		if (summary == null) {
			throw new IllegalArgumentException("Summary cannot be null");
		}
		return issues.stream().anyMatch(issue -> summary.equals(issue.getField("summary")));
	}
	
	@Override
	public boolean exists(Matcher<JiraIssue> matcher) {
		if (matcher == null) {
			throw new IllegalArgumentException("Summary cannot be null");
		}
		return issues.stream().anyMatch(issue -> matcher.matches(issue));
	}

}
