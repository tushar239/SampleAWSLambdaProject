package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/*
What is Lambda?
	Lambda can listen to event (e.g. insert ins3).
	Lambda can call another lambda. 
	It can scale by itself. E.g. every request to lambda function creates its new instance. It auto scales itself.

	If there are 2 HTTP requests sent to API Gateway and if API Gateway is connected to Lambda function as a trigger, then 2 instances of lambda functions will be created.	
	You get 1 million free lambda invocations/month.	
	Important: 
	Load Balancer + EC2 instances combinations keep using the same EC2 instances for requests, whereas Lambda function auto scales itself. For million concurrent requests, a million lambda function instances will be created and executed. All have same code. 

	If your lambda function is to run for more than 5 mins, then it’s not allowed. You need to break it in multiple lambda functions.
	
	Amazon publishes its new services to US East (N. Virginia) region first. To see what all services can trigger Lambda, you can go to this region and see.


Information about Building Lambda Function
http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html
http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-interfaces.html

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
It writes a record in DynamoDB and SNS Topic.

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

Testing Gateway API
	https://<<id of api>>.execute-api.us-west-2.amazonaws.com/prod/
	Method: POST
	Request Body: {"firstName":"Tushar","lastName":"Chokshi"}

You can test with Gateway API also. 
You can go to aws console of that lambda function and test the lambda function using above Request Body.

If you want to go one more step ahead, you can create a static website in S3 and call API Gateway's API from html page of the website instead of using rest tool.

You have 3 options to create Lambda Handler
- Using RequestHandler
- Using RequestStreamHandler (you get InputStream to retrieve an input and OutputStream to write an output)
- Your own Java class with some method (Hello.java). Here you have total control over the handler method name and exceptions to be thrown. Above two options do no provide facility to throw exceptions that you want.
  Ideally, Lambda is a function and it should not throw an exception, but if you want to throw it and in API gatway, if you want to translate these exceptions into specific http status code, then you can do so.
  See Hello.hava. 
*/
public class LambdaFunctionHandler implements RequestHandler<RequestClass, ResponseClass> {

	private static SnsTopicOperations sns = new SnsTopicOperations();
	private static String topicArn;
	static {
		topicArn = SnsTopicOperations.createTopic("TopicForInputFromMyFirstLambdaFunction");
		SnsTopicOperations.subscribeEmailToTopic(topicArn, "tushar239@gmail.com");
		// you need to accept subscription by going into your email
	}

	@Override
	public ResponseClass handleRequest(RequestClass input, Context context) {

		// You can see System.out, System.err, logger logs in CloudWatch.
		context.getLogger().log("Input: " + input.toString());

		System.out.println("Function name: " + context.getFunctionName());
		System.out.println("Max mem allocated: " + context.getMemoryLimitInMB());
		System.out.println("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
		System.out.println("CloudWatch log stream name: " + context.getLogStreamName());
		System.out.println("CloudWatch log group name: " + context.getLogGroupName());

		// put record in DynamoDB
		String id = DynamoDBOperations.putRecordInDynamoDB(input);

		ResponseClass res = new ResponseClass();
		res.setId(id);
		res.setRequestClass(input);

		// publish message to topic
		try {
			sns.publishMessageToTopic(input, topicArn);
		} catch (Exception e) {
			System.out.println(e);
		}

		return res;
	}

}
