package org.qetools.task_generator.integration;

import static java.nio.file.Files.readAllBytes;

import java.net.URI;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;
import spark.Spark;

public class FakeJira {

	public static final String PROJECT = "TEST";

	private static FakeJira instance;

	public static FakeJira getInstance() {
		if (instance == null) {
			instance = new FakeJira();
		}
		return instance;
	}

	private int index;
	private Set<FakeJiraIssue> issues;

	public FakeJira() {
		index = 1;
		issues = new HashSet<>();
	}

	public void start() {
		Spark.get("/rest/api/latest/issue/createmeta", (req, res) -> {
			String issueType = req.queryParams("issuetypeNames").toLowerCase();
			URI resourceURI = getClass().getClassLoader().getResource("createmeta_" + issueType + ".json").toURI();
			byte[] meta = readAllBytes(Paths.get(resourceURI));
			return new String(meta);
		});
		Spark.get("/rest/api/latest/issue/:key", (req, res) -> {
			String key = req.params(":key");
			return JSONObject.fromObject(getByKey(key));
		});
		Spark.get("/rest/api/latest/search", (req, res) -> {
			System.out.println(req.toString());
			System.out.println(req.queryParams("jql"));
			FakeJiraIssue issue = new FakeJiraIssue();
			issue.setKey("FOOTEST-2");
			return JSONObject.fromObject(new FakeJiraResponse(issue));
		});
		Spark.post("/rest/api/latest/issue/", (req, res) -> {
			System.out.println(req.body());
			JSONObject jsonObj = JSONObject.fromObject(req.body());
			String summary = getField(jsonObj, "summary");
			String project = getField(jsonObj, "project.key");
			FakeJiraIssue issue = add(project, summary);
			System.out.println(JSONObject.fromObject(issue));
			return JSONObject.fromObject(issue);
		});
	}

	public void stop() {
		Spark.stop();
	}

	public void clear() {
		issues.clear();
		index = 1;
	}

	public FakeJiraIssue add(String project, String summary) {
		FakeJiraIssue issue = new FakeJiraIssue();
		int id = index++;
		issue.setId(String.valueOf(id));
		issue.setKey(project + "-" + id);
		issue.setField("summary", summary);
		issues.add(issue);
		return issue;
	}

	public FakeJiraIssue getById(String id) {
		return issues.stream().filter(issue -> issue.getId() == id).findFirst().get();
	}

	public FakeJiraIssue getByKey(String key) {
		return issues.stream().filter(issue -> issue.getKey().equals(key)).findFirst().get();
	}

	private static String getField(JSONObject jsonObj, String path) {
		JSONObject fields = jsonObj.getJSONObject("fields");
		jsonObj = fields;
		for (String pathPart : path.split("\\.")) {
			Object obj = jsonObj.get(pathPart);
			if (obj == null) {
				return null;
			}
			if (obj instanceof String) {
				return (String) obj;
			}
			if (obj instanceof JSONObject) {
				jsonObj = (JSONObject) obj;
			} else {
				throw new RuntimeException("Unknown object " + obj);
			}
		}
		return null;
	}
}
