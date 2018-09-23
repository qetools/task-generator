package org.qetools.task_generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.api.JiraIssue;
import org.qetools.task_generator.api.JiraQuery;

public class JiraClientImpl implements JiraClient {

	private int index;
	private String url;
	private String username;
	private String password;
	private List<JiraIssue> issues;

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void initialize() {
		index = 1;
		if (!"bot".equals(username) || !"admin123".equals(password)) {
			throw new RuntimeException("Incorrect credentials");
		}
		issues = new ArrayList<>();
	}

	@Override
	public JiraIssue create(Map<String, String> fields) {
		JiraIssue issue = new JiraIssueImpl(fields);
		issue.setField("key", fields.get("project") + "-" + index++);
		issues.add(issue);
		return issue;
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
	public boolean exists(JiraQuery jiraQuery) {
		if (jiraQuery == null) {
			throw new IllegalArgumentException("Query cannot be null");
		}
		return issues.stream().anyMatch(issue -> jiraQuery.matches(issue));
	}

}
