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
	public void testParsingAndQueries() {
		Map<String, String> map = Utils.parseQuery("summary = \"Task 1\" AND key = \"TEST-1\"");
		collector.checkThat(map.get("summary"), IsEqual.equalTo("Task 1"));
		collector.checkThat(map.get("key"), equalTo("TEST-1"));
	}
}
