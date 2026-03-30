package clients;

import static io.restassured.RestAssured.given;

import java.util.Map;

import config.ConfigManager;
import io.restassured.response.Response;
import utils.AuthUtil;
import utils.ExtentLogger;
import utils.TestResultPdf;

public class GraphQLClient {

    // 🔹 Pre-auth
    public static Response sendPreAuthRequest(String baseUrl,
                                              String endpoint,
                                              String query,
                                              Map<String, Object> variables,
                                              String operationName) {
    	
    	ExtentLogger.logInfo("Calling API: " + operationName);
    	String requestBody = "{ query: " + query + ", variables: " + variables + " }";
    	ExtentLogger.logRequest(requestBody);
        Response response =  given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigManager.get("apiKey"))
                .header("x-operation-name", operationName)
                .body(Map.of(
                        "query", query,
                        "variables", variables
                ))
                .when()
                .post(endpoint);
        
        ExtentLogger.logResponse(response);
        TestResultPdf.addResult(
        	    new TestResultPdf(
        	        operationName,
        	        response.getStatusCode() == 200 ? "PASS" : "FAIL",
        	        response.getStatusCode(),
        	        response.getTime()
        	    )
        	);
        return response;
    }

    // 🔹 Auth APIs
    public static Response sendAuthRequest(String baseUrl,
                                           String endpoint,
                                           String query,
                                           Map<String, Object> variables,
                                           String operationName) {
    	ExtentLogger.logInfo("Calling API: " + operationName);
    	String requestBody = "{ query: " + query + ", variables: " + variables + " }";
    	ExtentLogger.logRequest(requestBody);
        Response response =  given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + AuthUtil.getAccessToken())
                .header("x-operation-name", operationName)
                .body(Map.of(
                        "query", query,
                        "variables", variables
                ))
                .when()
                .post(endpoint);
        
        ExtentLogger.logResponse(response);
        TestResultPdf.addResult(
        	    new TestResultPdf(
        	        operationName,
        	        response.getStatusCode() == 200 ? "PASS" : "FAIL",
        	        response.getStatusCode(),
        	        response.getTime()
        	    )
        	);
        return response;
    }
}