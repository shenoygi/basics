package com.thoughworks.infra.sample.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;

public class LocalConfiguration {
	public static final String PROPERTIES_FILE = "config.properties";
	public static final AWSCredentials CREDENTIALS;
	private static Properties properties;
	public static final String VM_PASSWORD;
	public static final String REGION;
	public static final String MACHINE_TYPE;
	public static final String MACHINE_IMAGE;
	public static final String SECURITY_GROUP_NAME;
	public static final String SECURITY_GROUP_DECRIPTION;
	public static final String ACCESSKEY;
	public static final String SECRETKEY;
	public static final String VM_USER;
	public static final String COMMAND_FILE;

	static {
		loadProperties();
		ACCESSKEY = getProperty("access.key");
		SECRETKEY = getProperty("secret.key");
		MACHINE_IMAGE = getProperty("aws.image.name");
		MACHINE_TYPE = getProperty("aws.image.type");
		REGION = getProperty("aws.region");
		VM_USER = getProperty("aws.vm.user");
		VM_PASSWORD = getProperty("aws.vm.password");
		COMMAND_FILE = getProperty("commands.file");
		SECURITY_GROUP_NAME = getProperty("security.group.name");
		SECURITY_GROUP_DECRIPTION = getProperty("security.group.description");
		CREDENTIALS = new CloudCredentials();
	}

	public static void loadProperties() {
		if (properties == null) {
			properties = new Properties();
			File config = new File(PROPERTIES_FILE);
			InputStream inStream;
			try {
				inStream = new FileInputStream(config);
				properties.load(inStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
