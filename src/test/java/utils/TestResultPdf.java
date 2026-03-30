package utils;

import java.util.ArrayList;
import java.util.List;

public class TestResultPdf {

    public String testName;
    public String status;
    public int statusCode;
    public long responseTime;

    // Static list to store all results
    public static List<TestResultPdf> results = new ArrayList<>();

    public TestResultPdf(String testName, String status, int statusCode, long responseTime) {
        this.testName = testName;
        this.status = status;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }

    public static void addResult(TestResultPdf result) {
        results.add(result);
    }
}