package org.qetools.task_generator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.api.JiraIssue;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.FluentCreate;
import net.rcarz.jiraclient.Issue.SearchResult;
import net.rcarz.jiraclient.JiraException;

public class JiraClientRcarz implements JiraClient {

	private String url;
	private String username;
	private String password;
	private net.rcarz.jiraclient.JiraClient jira;

	public JiraClientRcarz() {

	}

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
		try {
			jira = new net.rcarz.jiraclient.JiraClient(url, new BasicCredentials(username, password));
		} catch (JiraException e) {
			throw new RuntimeException("Cannot initialize jira client.", e);
		}
	}

	@Override
	public JiraIssue create(Map<String, String> fields) {
		System.out.println("Creating new task with fields " + fields.toString());
		FluentCreate builder;
		Issue issue;
		try {
			builder = jira.createIssue(fields.get("PROJECT"), fields.get("issueType"));
			fields.forEach((key, value) -> builder.field(key, value));
			issue = builder.execute();
		} catch (JiraException e) {
			throw new RuntimeException("Error during creating an issue.", e);
		}
		return new JiraIssueRcarz(issue);
	}

	@Override
	public JiraIssue create(JiraIssue issue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<JiraIssue> search(String jql) {
		List<JiraIssue> jiraIssues = new ArrayList<>();
		SearchResult searchResult = null;
		try {
			searchResult = jira.searchIssues(jql);
		} catch (JiraException e) {
			throw new RuntimeException("Error during searching issues.", e);
		}
		if (searchResult == null) {
			throw new RuntimeException("Search result is null.");
		}
		for (Issue issue : searchResult.issues) {
			jiraIssues.add(new JiraIssueRcarz(issue));
		}
		return jiraIssues;
	}

	@Override
	public boolean exists(String summary) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists(Matcher<JiraIssue> matcher) {
		// TODO Auto-generated method stub
		return false;
	}
}
