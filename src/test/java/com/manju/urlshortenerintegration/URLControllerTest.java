package com.manju.urlshortenerintegration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class URLControllerTest {
	
	@Test
	public void testReturnShortURLWith_ValidInput() {
		String myJson = "{\"originalURL\":\"https://www.instagram.com/\"}";
		given()
			.contentType(ContentType.JSON)
			.body(myJson)
		.when()	
			.post("http://localhost:8080/shortenURL")
		.then()
			.log()
			.body()
			.statusCode(200)
			.body(containsString("http://localhost:8080/"));
	}
	
	@Test
	public void testReturnShortURLWith_InValidInput() {
		String myJson = "{\"originalURL\":\"www.facebook.com/\"}";
		given()
			.contentType(ContentType.JSON)
			.body(myJson)
		.when()	
			.post("http://localhost:8080/shortenURL")
		.then()
			.log()
			.body()
			.statusCode(400)
			.body("message", equalTo("Please provide valid URL"));
	}
	
	@Test
	public void testRedirectingWith_ValidInput() {
		String myJson = "{\"originalURL\":\"https://www.linkedin.com/\"}";
		String response = given()
			.contentType(ContentType.JSON)
			.body(myJson)
			.when()
			.post("http://localhost:8080/shortenURL")
			.getBody().asString();
		
		given()
			.redirects().follow(false)
		.when()
			.get(response)
		.then()
			.log()
			.headers()
			.statusCode(302)
			.header("location", "https://www.linkedin.com/");
	}
	
	@Test
	public void testRedirectingWith_InValidInput() {
		when()
			.get("http://localhost:8080/1212121")
		.then()
			.log()
			.body()
			.statusCode(400)
			.body(containsString("please provide valid URL"));
	}

}
