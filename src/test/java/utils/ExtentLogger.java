package utils;

import com.aventstack.extentreports.ExtentTest;

import io.restassured.response.Response;
import listeners.TestListener;

public class ExtentLogger {

    public static void logInfo(String message) {
        ExtentTest test = TestListener.getTest();

        if (test != null) {
            test.info(message);
        } else {
            System.out.println("[INFO] " + message);
        }
    }

    public static void logRequest(String message) {
        ExtentTest test = TestListener.getTest();

        if (test != null) {
            test.info("<b>Request:</b>" + message);
        } else {
            System.out.println("[REQUEST] " + message);
        }
    }

    public static void logResponse(String response) {
        ExtentTest test = TestListener.getTest();

        if (test != null) {
            test.info("<b>Response:</b>" + response);
        } else {
            System.out.println("[RESPONSE] " + response);
        }
    }
}