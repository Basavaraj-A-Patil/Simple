package utils;

import java.util.List;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class PdfReportUtil {

	public static void generateReport(String filePath) {

		try {
			PdfWriter writer = new PdfWriter(filePath);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			Table headerTable = new Table(new float[] { 2, 10, 2 });
			headerTable.setWidth(UnitValue.createPercentValue(100));

			// 🔹 Left Logo
			Image leftLogo = new Image(ImageDataFactory.create("src/main/resources/Logo/logo_left.png"));
			leftLogo.setWidth(80);

			Cell leftCell = new Cell().add(leftLogo).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);

			// 🔹 Empty Center Cell
			Cell centerCell = new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER);

			// 🔹 Right Logo
			Image rightLogo = new Image(ImageDataFactory.create("src/main/resources/Logo/logo_right.png"));
			rightLogo.setWidth(30);
			rightLogo.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

			Cell rightCell = new Cell()
			        .add(rightLogo)
			        .setBorder(Border.NO_BORDER)
			        .setTextAlignment(TextAlignment.RIGHT);

			// Add cells to table
			headerTable.addCell(leftCell);
			headerTable.addCell(centerCell);
			headerTable.addCell(rightCell);

			// Add to document
			document.add(headerTable);

			// 🔹 Title
			Paragraph title = new Paragraph("Mobile API Automation Report")
			        .setBold()
			        .setFontSize(18)
			        .setTextAlignment(TextAlignment.CENTER);

			document.add(title);
			// 🔹 Get Results
			List<TestResultPdf> results = TestResultPdf.results;

			int total = results.size();
			int passed = 0;
			int failed = 0;
			int skipped = 0;

			for (TestResultPdf result : results) {
			    if ("PASS".equalsIgnoreCase(result.status)) {
			        passed++;
			    } else if ("FAIL".equalsIgnoreCase(result.status)) {
			        failed++;
			    } else if ("SKIP".equalsIgnoreCase(result.status) || "SKIPPED".equalsIgnoreCase(result.status)) {
			        skipped++;
			    }
			}

			// 🔹 Summary
			// 🔹 Create main container table (2 columns)
			Table summaryTable = new Table(new float[] { 1, 1 });
			summaryTable.setWidth(UnitValue.createPercentValue(100));

			// ================= LEFT SIDE (Summary) =================
			Cell summaryCell = new Cell().setBorder(Border.NO_BORDER);

			summaryCell.add(new Paragraph("Summary").setBold().setFontSize(14));
			summaryCell.add(new Paragraph("Total Tests: " + total));
			summaryCell.add(new Paragraph("Passed: " + passed));
			summaryCell.add(new Paragraph("Failed: " + failed));
			summaryCell.add(new Paragraph("Skipped: " + skipped));

			// ================= RIGHT SIDE (Pie Chart) =================
			Cell chartCell = new Cell().setBorder(Border.NO_BORDER);

			String chartPath = PieChartUtil.generatePieChart(passed, failed, skipped);

			if (chartPath != null) {
			    Image chartImage = new Image(ImageDataFactory.create(chartPath));
			    chartImage.setWidth(250);
			    chartImage.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

			    chartCell.add(chartImage);
			}

			// Add both cells
			summaryTable.addCell(summaryCell);
			summaryTable.addCell(chartCell);

			// Add to document
			document.add(new Paragraph("\n"));
			document.add(summaryTable);

			// 🔹 Table Section
			document.add(new Paragraph("\nDetailed Results").setBold().setFontSize(16));
			
			Table table = new Table(5);
			table.setTextAlignment(TextAlignment.CENTER);
			table.setWidth(UnitValue.createPercentValue(100));
			table.addHeaderCell(new Cell().add(new Paragraph("Sr. No.").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
			table.addHeaderCell(new Cell().add(new Paragraph("Test Name").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
			table.addHeaderCell(new Cell().add(new Paragraph("Status Code").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
			table.addHeaderCell(new Cell().add(new Paragraph("Response Time (ms)").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
			table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));

			int index = 1;
			for (TestResultPdf result : results) {
				table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))));
				table.addCell(new Cell().add(new Paragraph(result.testName)));
				table.addCell(new Cell().add(new Paragraph(String.valueOf(result.statusCode))));
				table.addCell(new Cell().add(new Paragraph(String.valueOf(result.responseTime))));
				Paragraph statusPara = new Paragraph(result.status);

				if ("PASS".equalsIgnoreCase(result.status)) {
				    statusPara.setFontColor(new DeviceRgb(0, 128, 0));
				} else if ("FAIL".equalsIgnoreCase(result.status)) {
				    statusPara.setFontColor(ColorConstants.RED);
				} else {
				    statusPara.setFontColor(ColorConstants.ORANGE);
				}

				table.addCell(new Cell().add(statusPara));
			}

			document.add(table);

			document.close();

			System.out.println("PDF Report Generated");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}