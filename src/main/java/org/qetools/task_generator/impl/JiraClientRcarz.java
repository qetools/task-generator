package org.qetools.task_generator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.api.JiraIssue;
import org.qetools.task_generator.api.JiraQuery;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
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
		FluentCreate builder;
		Issue issue;
		try {
			builder = jira.createIssue(fields.get("project").toString(), fields.get("issuetype").toString());
			if (fields.get("summary") != null) {
				builder.field(Field.SUMMARY, fields.get("summary"));
			}
			if (fields.get("assignee") != null) {
				builder.field(Field.ASSIGNEE, fields.get("assignee"));
			}
			if (fields.get("fixVersion") != null) {
				builder.field(Field.FIX_VERSIONS, list(fields.get("fixVersion")));
			}
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
		return exists("MYPROJECT", "Task", summary);
	}

	public boolean exists(String project, String issueType, String summary) {
		return search("project = \"" + project + "\" AND issuetype = \"" + issueType + "\" AND summary ~ \"\\\""
				+ summary + "\\\"\"").isEmpty();
	}

	@Override
	public boolean exists(JiraQuery jiraQuery) {
		return !search(jiraQuery.getJiraQueryString()).isEmpty();
	}

	private static List<String> list(String... strings) {
		List<String> list = new ArrayList<>();
		for (String string : strings) {
			list.add(string);
		}
		return list;
	}
}
