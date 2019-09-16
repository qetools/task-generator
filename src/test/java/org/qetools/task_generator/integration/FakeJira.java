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
package org.qetools.task_generator.integration;

import static java.nio.file.Files.readAllBytes;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.qetools.task_generator.Utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spark.Request;
import spark.Spark;

public class FakeJira {

	public static final String PROJECT = "TEST";

	private static FakeJira instance;

	public static FakeJira getInstance() {
		if (instance == null) {
			instance = new FakeJira("bot", "admin123");
		}
		return instance;
	}

	private int index;
	private List<FakeJiraIssue> issues;
	private String credentials;

	public FakeJira() {
		index = 1;
		issues = new ArrayList<>();
	}

	public FakeJira(String username, String password) {
		index = 1;
		issues = new ArrayList<>();
		if (username == null ^ password == null) {
			throw new IllegalArgumentException("Please specify correct credentials");
		}
		if (username != null && password != null) {
			credentials = username + ":" + password;
		}
	}

	public void start() {
		Spark.get("/rest/api/latest/issue/createmeta", (req, res) -> {
			if (!checkCredentials(req)) {
				return "Bad authentication";
			}
			String issueType = req.queryParams("issuetypeNames").toLowerCase();
			URI resourceURI = getClass().getClassLoader().getResource("createmeta_" + issueType + ".json").toURI();
			byte[] meta = readAllBytes(Paths.get(resourceURI));
			return new String(meta);
		});
		Spark.get("/rest/api/latest/issue/:key", (req, res) -> {
			if (!checkCredentials(req)) {
				return "Bad authentication";
			}
			String key = req.params(":key");
			return JSONObject.fromObject(getByKey(key));
		});
		Spark.get("/rest/api/latest/search", (req, res) -> {
			if (!checkCredentials(req)) {
				return "Bad authentication";
			}
			Map<String, String> map = Utils.parseQuery(req.queryParams("jql"));
			for (FakeJiraIssue issue : issues) {
				boolean result = true;
				for (String key : map.keySet()) {
					if (!map.get(key).equals(issue.getField(key))) {
						result = false;
						break;
					}
				}
				if (result) {
					return JSONObject.fromObject(new FakeJiraResponse(issue));
				}
			}
			return JSONObject.fromObject(new FakeJiraResponse());
		});
		Spark.post("/rest/api/latest/issue/", (req, res) -> {
			if (!checkCredentials(req)) {
				return "Bad authentication";
			}
			JSONObject jsonObj = JSONObject.fromObject(req.body());
			String summary = getField(jsonObj, "summary");
			String description = getField(jsonObj, "description");
			String project = getField(jsonObj, "project.key");
			String issueType = getField(jsonObj, "issuetype.name");
			String assignee = getField(jsonObj, "assignee.name");
			String fixVersion = getField(jsonObj, "fixVersions.name");
			String epic = getField(jsonObj, "customfield_12311140");
			String parent = getField(jsonObj, "parent.key");
			FakeJiraIssue issue = add(project, summary);
			issue.setField("issuetype", issueType);
			issue.setField("assignee", assignee);
			issue.setField("description", description);
			issue.setField("fixVersion", fixVersion);
			issue.setField("epic", epic);
			issue.setField("parent", parent);
			return JSONObject.fromObject(issue);
		});
		Spark.init();
		Spark.awaitInitialization();
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

	public List<FakeJiraIssue> getAllIssues() {
		return issues;
	}

	public FakeJiraIssue getById(String id) {
		return issues.stream().filter(issue -> issue.getId() == id).findFirst().get();
	}

	public FakeJiraIssue getByKey(String key) {
		return issues.stream().filter(issue -> issue.getKey().equals(key)).findFirst().get();
	}

	protected boolean checkCredentials(Request req) {
		if (credentials == null) {
			return true;
		}
		String authHeader = req.headers("Authorization");
		if (authHeader != null) {
			if (!authHeader.contains("Basic")) {
				return false;
			}
			String decodedCredentials = new String(
					Base64.getDecoder().decode(authHeader.substring("Basic".length()).trim()),
					Charset.forName("UTF-8"));
			return credentials.equals(decodedCredentials);
		}
		return false;
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
			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				if (jsonArray.isEmpty()) {
					return null;
				}
				jsonObj = (JSONObject) ((JSONArray) obj).get(0);
			} else {
				throw new RuntimeException("Unknown object " + obj);
			}
		}
		return null;
	}
}
