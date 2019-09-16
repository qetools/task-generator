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
			if (fields.get("description") != null) {
				builder.field(Field.DESCRIPTION, fields.get("description"));
			}
			if (fields.get("fixVersion") != null) {
				builder.field(Field.FIX_VERSIONS, list(fields.get("fixVersion")));
			}
			if (fields.get("epic") != null) {
				builder.field("customfield_12311140", fields.get("epic"));
			}
			if (fields.get("parent") != null) {
				builder.field(Field.PARENT, fields.get("parent"));
			}
			if (fields.get("name") != null) {
				builder.field("customfield_12311141", fields.get("name"));
			}
			issue = builder.execute();
		} catch (JiraException e) {
			throw new RuntimeException("Error during creating an issue.", e);
		}
		return new JiraIssueRcarz(issue);
	}

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

	private static List<String> list(String... strings) {
		List<String> list = new ArrayList<>();
		for (String string : strings) {
			list.add(string);
		}
		return list;
	}

	@Override
	public JiraIssue get(JiraQuery jiraQuery) {
		List<JiraIssue> issues = search(jiraQuery.getJiraQueryString());
		if (issues.isEmpty()) {
			return null;
		}
		return issues.get(0);
	}
}
