package com.amazonaws.lambda.demo;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DynamoDBOperations {

	protected static DynamoDB getDynamoDbHandler() {
		// You don't need to set this credentials as this lambda function is assigned a role that can access DynamoDB.
		// AWSCredentials awsCredentials = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);
		AmazonDynamoDB client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));

		DynamoDB dynamoDB = new DynamoDB(client);

		return dynamoDB;
	}

	public static String putRecordInDynamoDB(RequestClass input) {
		DynamoDB dynamoDB = getDynamoDbHandler();

		Table table = dynamoDB.getTable("person");

		String id = "1";
		try {
			System.out.println("Adding a new item...");
			PutItemOutcome outcome = table.putItem(new Item().withPrimaryKey("id", id)
					.with("firstName", input.getFirstName()).with("lastName", input.getLastName()));

			System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return id;
	}
}
