package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// You can write python and node.js code for lambda function directly in aws console, but you cannot do the same with java code.
// For Java code based lambda, you need to implement Java code in editor and then upload a jar file in aws console.

// AWS Lambda supports input/output of JSON-serializable types and InputStream/OutputStream types. 
// Logging: 
// Context uses LambdaLogger. You can access it as shown below from context object. It writes the log events to CloudWatch.
// LambdaLogger logger = context.getLogger();
// logger.log("received : " + myCount);

// Context object
// http://docs.aws.amazon.com/lambda/latest/dg/java-context-object.html

// Sample lambda java function that takes json as input and outputs a string
// right click on the project and run this function. This function will be deployed to your AWS account and then it will be run.
// while deploying, it may ask you to provide certain information like Role (what permissions this lambda function should have).
// If you do not have any role already created, you can let it create a default one. 
// You can also define max time for which this lambda function should run (default is 15 seconds). You can also mention memory (default 512 MB) requires by this lambda function.
// You use context.getRemainingTimeInImills() to know how much time remained from those 15 seconds after this lambda function was run.  

// http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html
// input: {"firstName":"Tushar","lastName":"Chokshi"}
public class LambdaFunctionHandler implements RequestHandler<RequestClass, String> {

    @Override
    public String handleRequest(RequestClass input, Context context) {
    	
    		// You can see System.out, System.err, logger logs in CloudWatch.
        context.getLogger().log("Input: " + input.toString());
        
        System.out.println("Function name: " + context.getFunctionName());
        System.out.println("Max mem allocated: " + context.getMemoryLimitInMB());
        System.out.println("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
        System.out.println("CloudWatch log stream name: " + context.getLogStreamName());
        System.out.println("CloudWatch log group name: " + context.getLogGroupName());
        
        // TODO: implement your handler
        return "Hello from Lambda to "+ input.toString();
    }

}
