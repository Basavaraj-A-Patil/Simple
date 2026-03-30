package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.EmailUtil;
import utils.ExtentManager;
import utils.PdfReportUtil;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    public static long startTime;
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        startTime = System.currentTimeMillis();
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail("Test Failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {

        extent.flush();

        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();

        String summary =
                "Total Tests: " + context.getAllTestMethods().length + "\n" +
                "Passed: " + passed + "\n" +
                "Failed: " + failed + "\n" +
                "Skipped: " + skipped;

        PdfReportUtil.generateReport("reports/TestReport.pdf");
        
        EmailUtil.sendEmailWithAttachment(); 
    }
}