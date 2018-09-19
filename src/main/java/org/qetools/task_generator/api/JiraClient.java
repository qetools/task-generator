package org.qetools.task_generator.api;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;

public interface JiraClient {

	void setUrl(String url);

	void setCredentials(String username, String password);

	void initialize();

	JiraIssue create(Map<String, String> fields);

	JiraIssue create(JiraIssue issue);

	List<JiraIssue> search(String jql);

	boolean exists(String summary);

	boolean exists(Matcher<JiraIssue> matcher);

}
