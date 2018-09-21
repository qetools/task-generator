package org.qetools.task_generator.integration;

import java.util.HashMap;
import java.util.Map;

public class FakeJiraIssue {

	private String id;
	private String key;
	private Map<String, Object> fields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return getField("key").toString();
	}

	public void setKey(String key) {
		setField("key", key);
	}

	public void setField(String key, Object value) {
		getFieldsMap().put(key, value);
	}

	public Object getField(String key) {
		return getFieldsMap().get(key);
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	private Map<String, Object> getFieldsMap() {
		if (fields == null) {
			fields = new HashMap<>();
		}
		return fields;
	}

}
