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
package org.qetools.task_generator.jql;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.qetools.task_generator.api.JiraIssue;
import org.qetools.task_generator.api.JiraQuery;

public class WithField extends BaseMatcher<JiraIssue> implements JiraQuery {

	private String key;
	private String value;

	public static WithField withField(String key, String value) {
		return new WithField(key, value);
	}

	public WithField(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj == null || !(obj instanceof JiraIssue)) {
			return false;
		}
		JiraIssue issue = (JiraIssue) obj;
		return (value == null && issue.getField(key) == null) || (value != null && value.equals(issue.getField(key)));
	}

	@Override
	public void describeTo(Description description) {
	}

	@Override
	public String getJiraQueryString() {
		return key + " = \"" + value + "\"";
	}

}
