package com.thoughworks.infra.sample.helper;

import com.amazonaws.auth.AWSCredentials;

public class CloudCredentials implements AWSCredentials{

	public String getAWSAccessKeyId() {
		return LocalConfiguration.ACCESSKEY;
	}

	public String getAWSSecretKey() {
		return LocalConfiguration.SECRETKEY;
	}

}
