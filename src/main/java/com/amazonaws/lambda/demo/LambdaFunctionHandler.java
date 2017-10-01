package com.amazonaws.lambda.demo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/*
Information about Building Lambda Function
http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html

You can write python and node.js code for lambda function directly in aws console, but you cannot do the same with java code.
For Java code based lambda, you need to implement Java code in editor and then upload a jar file in aws console.

AWS Lambda supports input/output of JSON-serializable types and InputStream/OutputStream types.

Logging: 
Context uses LambdaLogger. You can access it as shown below from context object. It writes the log events to CloudWatch.
LambdaLogger logger = context.getLogger();
logger.log("received : " + myCount);

Context object
http://docs.aws.amazon.com/lambda/latest/dg/java-context-object.html

Here is the Sample lambda java function that takes json as input and outputs a json.
Right click on the project and run this function. This function will be deployed to your AWS account and then it will be run.
While deploying, it may ask you to provide certain information like Role (what permissions this lambda function should have).
If you do not have any role already created, you can let it create a default one. But for this code, it will require access to DynamoDB. So, create a role that has full access to DynamoDB (or at least get/put/list access to DynamoDB).
You can also define max time for which this lambda function should run (default is 15 seconds). You can also mention memory (default 512 MB) requires by this lambda function.
You use context.getRemainingTimeInImills() to know how much time remained from those 15 seconds after this lambda function was run.  

This function is creating a record in DynamoDB. For that, either this function needs to access DynamoDB using access credentials or you need to create and assign a Role to this function that can access DynamoDB.

Pre-requisite:
you need to have a 'person' table created in DynamoDB.

Integrating with AWS API Gateway:
	Create an API with POST. As an integrated resource, use this lambda function.
	If you want, you can create a stage for this API and get the API link that you can hit through browser or you can just use testing tool of aws console.

input: {"firstName":"Tushar","lastName":"Chokshi"}
	
*/
public class LambdaFunctionHandler implements RequestHandler<RequestClass, ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass input, Context context) {
    	
    		// You can see System.out, System.err, logger logs in CloudWatch.
        context.getLogger().log("Input: " + input.toString());
        
        System.out.println("Function name: " + context.getFunctionName());
        System.out.println("Max mem allocated: " + context.getMemoryLimitInMB());
        System.out.println("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
        System.out.println("CloudWatch log stream name: " + context.getLogStreamName());
        System.out.println("CloudWatch log group name: " + context.getLogGroupName());
        
        
        DynamoDB dynamoDB = getDynamoDbHandler();

        Table table = dynamoDB.getTable("person");
        
        String id = "1";
        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(
                            new Item()
                                    .withPrimaryKey("id", id)
                                    .with("firstName", input.getFirstName())
                                    .with("lastName", input.getLastName()));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        ResponseClass res = new ResponseClass();
        res.setId(id);
        res.setRequestClass(input);
        
        return res;
    }

    protected static DynamoDB getDynamoDbHandler() {
	    	// you don't need to set this credentials as this lambda function is assigned a role that can access DynamoDB.
        //AWSCredentials awsCredentials = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);
        AmazonDynamoDB client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));

        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB;

    }
}
