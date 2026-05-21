
package com.myproject.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {

            ExtentSparkReporter spark =
                new ExtentSparkReporter("reports/TestReport.html");

            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Selenium Test Report");
            spark.config().setReportName("Regression Suite Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("URL", "https://the-internet.herokuapp.com");
        }

        return extent;
    }

    public static ExtentTest startTest(String testName, String description) {
        ExtentTest extentTest =
            getInstance().createTest(testName, description);

        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flushReport() {
        if (extent != null)
            extent.flush();
    }
}

