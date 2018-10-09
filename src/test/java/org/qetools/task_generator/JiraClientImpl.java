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

	public JiraClientImpl() {
		index = 1;
		issues = new ArrayList<>();
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
		if (!"bot".equals(username) || !"admin123".equals(password)) {
			throw new RuntimeException("Incorrect credentials");
		}
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

	@Override
	public JiraIssue get(JiraQuery jiraQuery) {
		return issues.stream().filter(issue -> jiraQuery.matches(issue)).findFirst().orElse(null);
	}

}
