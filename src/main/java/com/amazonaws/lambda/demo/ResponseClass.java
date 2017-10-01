package com.amazonaws.lambda.demo;

public class ResponseClass {

	private String id;

	private RequestClass requestClass;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RequestClass getRequestClass() {
		return requestClass;
	}

	public void setRequestClass(RequestClass requestClass) {
		this.requestClass = requestClass;
	}

}
