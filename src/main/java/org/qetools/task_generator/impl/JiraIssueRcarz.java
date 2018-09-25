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
