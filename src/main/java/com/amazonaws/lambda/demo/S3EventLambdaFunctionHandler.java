package com.amazonaws.lambda.demo;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

// This lambda function is for listening to an S3 event.
// When you test this lambda function from AWS console by some default S3 put event, this lambda function will be invoked.
// To simulate real time scenario, you can add a S3 Trigger to this lambda function in AWS console. 
// While attaching S3 trigger, you can specify bucket name and operation and key prefix on which this lambda function should be triggered. 

// From S3Event, you can gather a lot of information about an Object that is put in S3, bucket name etc.  

public class S3EventLambdaFunctionHandler implements RequestHandler<S3Event, Object> {

	/*
	 * private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
	 * 
	 * public S3EventLambdaFunctionHandler() {}
	 * 
	 * // Test purpose only. S3EventLambdaFunctionHandler(AmazonS3 s3) { this.s3 =
	 * s3; }
	 * 
	 * @Override public String handleRequest(S3Event event, Context context) {
	 * context.getLogger().log("Received event: " + event);
	 * 
	 * // Get the object from the event and show its content type String bucket =
	 * event.getRecords().get(0).getS3().getBucket().getName(); String key =
	 * event.getRecords().get(0).getS3().getObject().getKey(); try { S3Object
	 * response = s3.getObject(new GetObjectRequest(bucket, key)); String
	 * contentType = response.getObjectMetadata().getContentType();
	 * context.getLogger().log("CONTENT TYPE: " + contentType); return contentType;
	 * } catch (Exception e) { e.printStackTrace();
	 * context.getLogger().log(String.format(
	 * "Error getting object %s from bucket %s. Make sure they exist and" +
	 * " your bucket is in the same region as this function.", key, bucket)); throw
	 * e; } }
	 */

	@Override
	public Object handleRequest(S3Event event, Context context) {
		context.getLogger().log("Received event: " + event);

		List<S3EventNotificationRecord> records = event.getRecords();

		for (S3EventNotificationRecord record : records) {
			System.out.println("AWS Region: " + record.getAwsRegion());
			System.out.println("Event Name: " + record.getEventName());
			System.out.println("Event Source: " + record.getEventSource());
			S3Entity s3 = record.getS3();
			System.out.println("S3 Bucket Name: " + s3.getBucket().getName());
			System.out.println("S3 Object Key: " + s3.getObject().getKey());
			System.out.println("S3 Object Size: " + s3.getObject().getSizeAsLong());
		}

		return null;
	}
}