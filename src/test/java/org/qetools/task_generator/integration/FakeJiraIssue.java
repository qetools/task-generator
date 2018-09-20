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
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	public void setField(String key, Object value) {
		getFieldsMap().put(key, value);
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
