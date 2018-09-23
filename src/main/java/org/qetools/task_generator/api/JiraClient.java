package org.qetools.task_generator.api;

import java.util.Map;

public interface JiraClient {

	void setUrl(String url);

	void setCredentials(String username, String password);

	void initialize();

	JiraIssue create(Map<String, String> fields);

	boolean exists(String summary);

	boolean exists(JiraQuery jiraQuery);

}
