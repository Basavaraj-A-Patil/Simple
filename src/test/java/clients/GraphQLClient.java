package clients;

import static io.restassured.RestAssured.given;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.ConfigManager;
import io.restassured.response.Response;
import utils.AuthUtil;
import utils.ExtentLogger;
import utils.TestResultPdf;
import utils.TestStatusManager;

public class GraphQLClient {

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
       
        ExtentLogger.logResponse(response.asPrettyString());
        String status = TestStatusManager.getStatus();
        TestResultPdf.addResult(
        	    new TestResultPdf(
        	        operationName,
        	        status,
        	        response.getStatusCode(),
        	        response.getTime()
        	    )
        	);
        return response;
    }

    private static final String BASE_URL = ConfigManager.get("baseUrl");

    public static Response sendRequest(String endpoint,
                                       String query,
                                       Map<String, Object> variables,
                                       String operationName) {

        long startTime = System.currentTimeMillis();
        ExtentLogger.logInfo("Calling API: " + operationName);

        try {

            // Request Body Map
            Map<String, Object> requestMap = Map.of(
                    "query", query,
                    "variables", variables
            );

            // Pretty JSON
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writerWithDefaultPrettyPrinter()
                                       .writeValueAsString(requestMap);

            // Token
            String token = AuthUtil.getAccessToken();

            // Headers
            Map<String, String> headers = Map.of(
                    "Content-Type", "application/json",
                    "Authorization", "Bearer " + token,
                    "x-operation-name", operationName
            );

            // Logs
            ExtentLogger.logInfo("<b>Request URL:</b> " + BASE_URL + endpoint);
            ExtentLogger.logInfo("<b>Request Headers:</b><pre>" + formatHeaders(headers) + "</pre>");
            ExtentLogger.logRequest("<pre>" + requestBody + "</pre>");

            // API Call
            Response response = given()
                    .baseUri(BASE_URL)
                    .headers(headers)
                    .body(requestMap)
                    .when()
                    .post(endpoint);

            long responseTime = System.currentTimeMillis() - startTime;

            // Response Logs
            ExtentLogger.logInfo("<b>Status Code:</b> " + response.getStatusCode());
            ExtentLogger.logInfo("<b>Response Time (ms):</b> " + responseTime);
     //       ExtentLogger.logInfo("<b>Response Headers:</b><pre>"  response.getHeaders().toString() + "</pre>");
            ExtentLogger.logResponse(response.asPrettyString());

            // PDF Logging
            TestResultPdf.addResult(
                    new TestResultPdf(
                            operationName,
                            response.getStatusCode() == 200 ? "PASS" : "FAIL",
                            response.getStatusCode(),
                            responseTime
                    )
            );

            return response;

        } catch (Exception e) {
            ExtentLogger.logInfo("Exception occurred: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Header Formatter with Masking
    private static String formatHeaders(Map<String, String> headers) {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : headers.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equalsIgnoreCase("Authorization") && value != null) {
                value = maskToken(value);
            }

            sb.append(key)
              .append(": ")
              .append(value)
              .append("\n");
        }

        return sb.toString();
    }

    //Mask Token
    private static String maskToken(String token) {
        if (token == null || token.length() < 15) return token;

        return token.substring(0, 10) + "********" +
               token.substring(token.length() - 5);
    }
}