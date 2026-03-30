package utils;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import java.util.List;

public class PdfReportUtil {

    public static void generateReport(String filePath) {

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 🔹 Title
            document.add(new Paragraph("Mobile API Automation Report")
                    .setBold()
                    .setFontSize(16));

            // 🔹 Get Results
            List<TestResultPdf> results = TestResultPdf.results;

            int total = results.size();
            int passed = 0;
            int failed = 0;

            for (TestResultPdf result : results) {
                if ("PASS".equalsIgnoreCase(result.status)) {
                    passed++;
                } else {
                    failed++;
                }
            }

            // 🔹 Summary
            document.add(new Paragraph("\nSummary").setBold().setFontSize(14));
            document.add(new Paragraph("Total Tests: " + total));
            document.add(new Paragraph("Passed: " + passed));
            document.add(new Paragraph("Failed: " + failed));

            // 🔹 Table Section
            document.add(new Paragraph("\nDetailed Results").setBold().setFontSize(14));

            Table table = new Table(5);
            
            table.addHeaderCell("Sr. No.").setBold();
            table.addHeaderCell("Test Name").setBold();
            table.addHeaderCell("Status Code").setBold();
            table.addHeaderCell("Response Time (ms)").setBold();
            table.addHeaderCell("Status").setBold();

            int index = 1;
            for (TestResultPdf result : results) {
            	 table.addCell(String.valueOf(index++));
                table.addCell(result.testName);
                table.addCell(String.valueOf(result.statusCode));
                table.addCell(String.valueOf(result.responseTime));
                Paragraph statusPara = new Paragraph(result.status);
                if ("PASS".equalsIgnoreCase(result.status)) {
                    statusPara.setFontColor(ColorConstants.GREEN);
                } else {
                    statusPara.setFontColor(ColorConstants.RED);
                }
                table.addCell(statusPara);
            }

            document.add(table);

            document.close();

            System.out.println("PDF Report Generated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}