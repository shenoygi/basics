package com.thoughworks.infra.sample.helper;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.policy.actions.EC2Actions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.ec2.model.AllocateAddressRequest;
import com.amazonaws.services.ec2.model.AllocateAddressResult;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.AssociateAddressResult;
import com.amazonaws.services.ec2.model.DescribeAddressesResult;
import com.amazonaws.services.ec2.model.DomainType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;

public class InstanceHelper {

	private AmazonEC2 ec2;

	public InstanceHelper(AmazonEC2 ec2) {
		this.ec2 = ec2;
	}

	/**
	 * Deploys a Instance on the EC2 cloud
	 * @param instanceImage - Pre cnonfigured Machines image
	 * @param instanceType - Instance type to deploy
	 * @param securityGroup - Security group to add to the instance
	 * @return instanceId
	 * @throws AmazonServiceException
	 */
	public String deployInstance(String instanceImage, String instanceType, String securityGroup)
			throws AmazonServiceException {
		RunInstancesRequest request = new RunInstancesRequest();
		request.withImageId(instanceImage).withInstanceType(instanceType).withMinCount(1).withMaxCount(1)
				.withKeyName(LocalConfiguration.ACCESSKEY).withSecurityGroups(securityGroup);

		RunInstancesResult result = ec2.runInstances(request);
		return result.getReservation().getReservationId();
	}

	/**
	 * Allocate a public IP to the instance
	 * @param instanceId
	 * @return publicIP of the Instance
	 */
	public String allocateIp(String instanceId) {
		AllocateAddressRequest allocate_request = new AllocateAddressRequest().withDomain(DomainType.Vpc);
		AllocateAddressResult allocate_response = ec2.allocateAddress(allocate_request);
		String allocation_id = allocate_response.getAllocationId();
		AssociateAddressRequest associate_request = new AssociateAddressRequest().withInstanceId(instanceId)
				.withAllocationId(allocation_id);
		AssociateAddressResult associate_response = ec2.associateAddress(associate_request);
		DescribeAddressesResult addresses = ec2.describeAddresses();
		for(Address address : addresses.getAddresses()){
			if(address.getAllocationId().equals(allocation_id)){
				return address.getPublicIp();
			}
		}
		return "";
	}

	/**
	 * Start the instance specified
	 * @param instanceId
	 * @return status
	 */
	public boolean startInstance(String... instanceId) {
		StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(instanceId);
		StartInstancesResult result = ec2.startInstances(request);
		return true;
	}

	/**
	 * Stop the instance specified
	 * @param instanceId
	 * @return status
	 */
	public boolean stopInstance(String... instanceId) {
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instanceId);
		StopInstancesResult result = ec2.stopInstances(request);
		return true;
	}
}
