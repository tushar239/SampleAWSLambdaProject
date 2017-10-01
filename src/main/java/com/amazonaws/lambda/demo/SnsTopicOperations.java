package com.amazonaws.lambda.demo;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

/*
http://docs.aws.amazon.com/sns/latest/dg/using-awssdkjava.html
*/
public class SnsTopicOperations {

	private static AmazonSNSClient snsClient;
	static {
		// AmazonSNSClient snsClient = new
		// AmazonSNSClient(newClasspathPropertiesFileCredentialsProvider());
		snsClient = new AmazonSNSClient();
		snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

	public static String createTopic(String topicName) {
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		// print TopicArn
		System.out.println(createTopicResult);
		// get request id for CreateTopicRequest from SNS metadata
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));

		String topicArn = createTopicResult.getTopicArn();

		return topicArn;
	}

	public static void subscribeEmailToTopic(String topicArn, String email) {
		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email", email);
		snsClient.subscribe(subRequest);
		// get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
		System.out.println("Check your email and confirm subscription.");
	}

	public void publishMessageToTopic(RequestClass input, String topicArn) throws JsonProcessingException {
		PublishRequest publishRequest = new PublishRequest(topicArn, input.toString());
		ObjectMapper objectMapper = new ObjectMapper();		
		PublishResult publishResult = snsClient.publish(topicArn, objectMapper.writeValueAsString(publishRequest));
		// print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
	}

	public void deleteTopic(String topicArn) {
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		snsClient.deleteTopic(deleteTopicRequest);
		// get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
	}
}
