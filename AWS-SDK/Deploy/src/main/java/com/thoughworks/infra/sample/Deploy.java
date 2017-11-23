package com.thoughworks.infra.sample;

import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.thoughworks.infra.sample.helper.ConfigurationHelper;
import com.thoughworks.infra.sample.helper.InstanceHelper;
import com.thoughworks.infra.sample.helper.LocalConfiguration;
import com.thoughworks.infra.sample.helper.SecurityGroupHelper;

/**
 * Task specific workflow to deploy a cloud instance and configure it as required
 */
public class Deploy {

	public static void main(String[] args) throws Exception {
		//Connect to AWS using the credentials and set the region
		AmazonEC2 ec2 = new AmazonEC2Client(LocalConfiguration.CREDENTIALS);
		ec2.setRegion(RegionUtils.getRegion(LocalConfiguration.REGION));

		//Create the security group to add to the Cloud instance
		//This is required so that the VM is accessible through this client system
		SecurityGroupHelper security = new SecurityGroupHelper(ec2);
		security.createSecurityGroup(LocalConfiguration.SECURITY_GROUP_NAME,
				LocalConfiguration.SECURITY_GROUP_DECRIPTION);
		security.setIpRange();
		
		//Deploy the instance based on the specified Machine Image and machine type
		//Recommended to use a Image with Linux 64 bit, with Java 8 and Tomcat pre installed
		InstanceHelper instance = new InstanceHelper(ec2);
		String instanceId = instance.deployInstance(LocalConfiguration.MACHINE_IMAGE, LocalConfiguration.MACHINE_TYPE,
				LocalConfiguration.SECURITY_GROUP_NAME);
		String publicIp = "";
		
		//Once the VM is deployed and allocated on the AWS Cloud, it needs to be started and a public IP needs to be allocated
		if(instance.startInstance(instanceId)){
			//If the starting of the instance succeeds, allocate a public IP
			publicIp = instance.allocateIp(instanceId);
			System.out.println(String.format("Successfully created and started EC2 Instance [%s]",LocalConfiguration.MACHINE_IMAGE));
		} else {
			System.out.println("Error creating EC2 Instance\nExiting..");
			System.exit(1);
		}

		//Configuration helper, configures the VM once it is running and has a valid IP
		ConfigurationHelper config = new ConfigurationHelper(publicIp, LocalConfiguration.VM_USER, LocalConfiguration.VM_PASSWORD);
		if(config.runSshCommands(LocalConfiguration.COMMAND_FILE)){
			System.out.println(String.format("EC2 Instance with IP [%s] successfully configured", publicIp));
		} else {
			System.err.println("Error configuring the EC2 Instance\nExiting..");
			System.exit(2);
		}
	}
}
