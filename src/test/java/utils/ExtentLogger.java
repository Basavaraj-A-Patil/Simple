package utils;

import io.restassured.response.Response;
import listeners.TestListener;

public class ExtentLogger {

    public static void logInfo(String message) {
        TestListener.getTest().info(message);
    }

    public static void logRequest(String request) {
        TestListener.getTest().info("REQUEST:\n" + request);
    }

    public static void logResponse(Response response) {

        String prettyResponse = response.getBody().asPrettyString();

        TestListener.getTest().info(
                "RESPONSE:\n" + prettyResponse
        );
    }
}