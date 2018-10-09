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
package org.qetools.task_generator;

import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class UtilsTest {

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Test
	public void testParsingQuery() {
		Map<String, String> map = Utils.parseQuery("summary = \"Task 1\"");
		collector.checkThat(map.get("summary"), equalTo("Task 1"));
	}

	@Test
	public void testParsingQueryWithTilda() {
		Map<String, String> map = Utils.parseQuery("summary ~ \"Task 1\"");
		collector.checkThat(map.get("summary"), equalTo("Task 1"));
	}

	@Test
	public void testParsingAndQueries() {
		Map<String, String> map = Utils.parseQuery("summary = \"Task 1\" AND key = \"TEST-1\"");
		collector.checkThat(map.get("summary"), IsEqual.equalTo("Task 1"));
		collector.checkThat(map.get("key"), equalTo("TEST-1"));
	}

	@Test
	public void testParsingAndQueriesWithTilda() {
		Map<String, String> map = Utils.parseQuery("summary ~ \"Task 1\" AND key = \"TEST-1\"");
		collector.checkThat(map.get("summary"), IsEqual.equalTo("Task 1"));
		collector.checkThat(map.get("key"), equalTo("TEST-1"));
	}
}
