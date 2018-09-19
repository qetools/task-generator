package org.qetools.task_generator.impl;

import java.util.Collections;
import java.util.Map;

import org.qetools.task_generator.api.JiraIssue;

import net.rcarz.jiraclient.Issue;

public class JiraIssueRcarz implements JiraIssue {

	private Issue issue;
	private Map<String, String> map;

	public JiraIssueRcarz(Issue issue) {
		this.issue = issue;
	}

	@Override
	public void setField(String key, String value) {
		map.put(key, value);
	}

	@Override
	public String getField(String key) {
		return (String) issue.getField(key);
	}

	@Override
	public Map<String, String> getFieldsMap() {
		return Collections.unmodifiableMap(map);
	}

}
