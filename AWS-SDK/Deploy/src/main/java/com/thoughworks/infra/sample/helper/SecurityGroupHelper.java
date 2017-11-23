package com.thoughworks.infra.sample.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.IpPermission;

public class SecurityGroupHelper {

	private AmazonEC2 ec2;

	public SecurityGroupHelper(AmazonEC2 ec2) {
		this.ec2 = ec2;
	}

	/**
	 * Creates a Security group for the current account user
	 * if the group already exists, throws an exception
	 * @param groupName - name of the Secuity group to be created
	 * @param groupDescription - description for the Security group
	 * @return status
	 * @throws AmazonServiceException
	 */
	public boolean createSecurityGroup(String groupName, String groupDescription) throws AmazonServiceException {
		CreateSecurityGroupRequest csgr = new CreateSecurityGroupRequest();
		csgr.withGroupName(groupName).withDescription(groupDescription);

		CreateSecurityGroupResult result = ec2.createSecurityGroup(csgr);
		System.out.println(String.format("Security group created: [%s]", result.getGroupId()));
		return true;
	}

	/**
	 * Set the IP and Port ranges to allow access to the specific security group 
	 * @return status
	 * @throws AmazonServiceException
	 */
	public boolean setIpRange() throws AmazonServiceException {
		String ipAddr = "0.0.0.0/0";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ipAddr = addr.getHostAddress() + "/24";
		} catch (UnknownHostException e) {
			System.err.println(e.getMessage());
		}
		
		List<String> ipRanges = Collections.singletonList(ipAddr);

		IpPermission ipPermission = new IpPermission().withIpProtocol("tcp").withFromPort(new Integer(22))
				.withToPort(new Integer(22)).withIpRanges(ipRanges);

		List<IpPermission> ipPermissions = new ArrayList<IpPermission>();
		ipPermissions.add(ipPermission);

		IpPermission ipPermissionHttp = new IpPermission().withIpProtocol("tcp").withFromPort(new Integer(80))
				.withToPort(new Integer(80)).withIpRanges(ipRanges);

		ipPermissions.add(ipPermissionHttp);

		AuthorizeSecurityGroupIngressRequest ingressRequest = new AuthorizeSecurityGroupIngressRequest(
				"GettingStartedGroup", ipPermissions);
		ec2.authorizeSecurityGroupIngress(ingressRequest);
		System.out.println(String.format("Ingress port authroized: [%s]", ipPermissions.toString()));
		return true;
	}

}
