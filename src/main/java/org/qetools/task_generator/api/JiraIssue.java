package org.qetools.task_generator.api;

import java.util.Map;

public interface JiraIssue {

	void setField(String key, String value);

	String getField(String key);

	Map<String, String> getFieldsMap();
}
