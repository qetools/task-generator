package org.qetools.task_generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.qetools.task_generator.api.JiraIssue;

public class JiraIssueImpl implements JiraIssue {

	private Map<String, String> fields;

	public JiraIssueImpl() {
		this(new HashMap<>());
	}

	public JiraIssueImpl(Map<String, String> fields) {
		this.fields = fields;
	}

	@Override
	public void setField(String key, String value) {
		fields.put(key, value);
	}

	@Override
	public String getField(String key) {
		return fields.get(key);
	}

	@Override
	public Map<String, String> getFieldsMap() {
		return Collections.unmodifiableMap(fields);
	}

}
