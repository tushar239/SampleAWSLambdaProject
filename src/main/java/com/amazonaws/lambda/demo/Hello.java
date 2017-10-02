package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import exceptions.BadRequestException;

/*
Out of three options to create Lambda Handlers as mentioned in LambdaFunctionHandler.java, here is the example of 3rd option.
In this option, you can provide a method name that you like and throw exceptions as you want. You can translate these exceptions into specific http status codes in API Gateway, if you trigger thei Lambda function using API Gateway.

http://docs.aws.amazon.com/lambda/latest/dg/get-started-step4-optional.html

Eclipse doesn't have a feature to upload this kind of function, but you can do it using AWS console. There you need to specify Handler as 'com.amazonaws.lambda.demo.Hello::myHandler' 
*/
public class Hello {
	public String myHandler(int myCount, Context context) throws Exception{
		if(myCount <= 0) throw new BadRequestException("BAD_REQ: Check your count again. It can't be <=0");
		
		LambdaLogger logger = context.getLogger();
		logger.log("received : " + myCount);
		return String.valueOf(myCount);
	}
}
