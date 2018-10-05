package org.qetools.task_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.qetools.task_generator.api.JiraClient;
import org.qetools.task_generator.impl.JiraClientRcarz;

/**
 * 
 * @author apodhrad
 *
 */
public class TaskGeneratorApp {

	public static final String SYSTEM_PROPERTY_CONFIG = "config";

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			throw new IllegalArgumentException("No yaml file specified");
		}
		if (args.length > 1) {
			throw new IllegalArgumentException("You can specify only on yaml file");
		}
		File yamlFile = new File(args[0]);
		if (!yamlFile.exists()) {
			throw new FileNotFoundException("Cannot find yaml file at " + yamlFile.getAbsolutePath());
		}
		File configFile = null;
		String configFilePath = System.getProperty(SYSTEM_PROPERTY_CONFIG);
		if (configFilePath != null) {
			configFile = new File(configFilePath);
			if (!configFile.exists()) {
				throw new FileNotFoundException("Cannot find configuration file at " + configFile.getAbsolutePath());
			}
		}

		JiraClient jiraClient = new JiraClientRcarz();
		TaskGenerator taskGenerator = new TaskGenerator(jiraClient, configFile);
		taskGenerator.generate(yamlFile);
	}
}
