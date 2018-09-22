package org.qetools.task_generator.api;

import org.hamcrest.Matcher;

public interface JiraQuery extends Matcher<JiraIssue> {

	String getJiraQueryString();
}
