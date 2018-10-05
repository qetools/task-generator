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
package org.qetools.task_generator.api;

import java.util.Map;

/**
 * Jira Client API.
 * 
 * @author Andrej Podhradsky
 *
 */
public interface JiraClient {

	/**
	 * Sets Jira url.
	 * 
	 * @param url Jira url
	 */
	void setUrl(String url);

	/**
	 * Sets credentials.
	 * 
	 * @param username Username
	 * @param password Password
	 */
	void setCredentials(String username, String password);

	/**
	 * Initializes the Jira client.
	 */
	void initialize();

	/**
	 * Creates a Jira issue.
	 * 
	 * @param fields Map of keys and values
	 * @return Jira issue
	 */
	JiraIssue create(Map<String, String> fields);

	/**
	 * Checks if an issue with a given summary exists or not.
	 * 
	 * @param summary Issue summary
	 * @return true if an issue with the summary exists, false otherwise
	 */
	boolean exists(String summary);

	/**
	 * Checks if an issue matching a given query exists or not.
	 * 
	 * @param jiraQuery Jira query
	 * @return true if an issue matching the query exists, false otherwise
	 */
	boolean exists(JiraQuery jiraQuery);

}
