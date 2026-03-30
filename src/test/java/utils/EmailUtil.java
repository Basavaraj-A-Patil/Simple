package utils;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import config.ConfigManager;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailUtil {

    public static void sendEmailWithAttachment() {

    	final String fromEmail = ConfigManager.get("email.from");
    	final String password = ConfigManager.get("email.password");
        final String toEmail = ConfigManager.get("email.to");
        final String ccEmail = ConfigManager.get("email.cc");

        try {

            // 🔹 Get Test Results
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

            String executionStatus = (failed > 0) ? "FAILED" : "PASSED";

            // 🔹 Time + Duration
            long endTime = System.currentTimeMillis();
            long durationMillis = endTime - listeners.TestListener.startTime;

            Duration duration = Duration.ofMillis(durationMillis);

            String formattedDuration = String.format("%02d:%02d:%02d",
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart());

            String executedOn = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            // 🔹 SMTP Config
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, password);
                        }
                    });

            // 🔹 Message
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail, "Automation Team"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setRecipients(
            		Message.RecipientType.CC,
            		InternetAddress.parse(ccEmail));

            // 🔹 Subject
            message.setSubject("Simple Connect API : Execution Complete Notification - " + executionStatus);

            // 🔥 HTML BODY
            String body =
                    "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +

                    "<p>Hi Team,</p>" +

                    "<p>We would like to inform you that the execution of the <b>Simple Connect API</b> automation suite has been completed.</p>" +

                    "<h3>Project Details:</h3>" +

                    "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse;'>" +
                    "<tr><td><b>Project Name</b></td><td>Simple Connect</td></tr>" +
                    "<tr><td><b>Execution Status</b></td><td style='color:"
                    + (executionStatus.equals("PASSED") ? "green" : "red") + ";'><b>"
                    + executionStatus + "</b></td></tr>" +
                    "<tr><td><b>Executed On</b></td><td>" + executedOn + "</td></tr>" +
                    "<tr><td><b>Execution Duration</b></td><td>" + formattedDuration + "</td></tr>" +
                    "</table>" +

                    "<h3>Test Summary:</h3>" +

                    "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; text-align:center;'>" +
                    "<tr style='background-color:#f2f2f2;'>" +
                    "<th>Total Tests</th>" +
                    "<th>Passed</th>" +
                    "<th>Failed</th>" +
                    "</tr>" +

                    "<tr>" +
                    "<td>" + total + "</td>" +
                    "<td style='color:green; font-weight:bold;'>" + passed + "</td>" +
                    "<td style='color:red; font-weight:bold;'>" + failed + "</td>" +
                    "</tr>" +

                    "</table>" +

                    "<p>Detailed report is attached for your reference.</p>" +

                    "<br>" +
                    "<p>Thanks & Regards,<br>Automation Team</p>" +

                    "</body></html>";

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // 🔹 Attach PDF
            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile(new File("reports/TestReport.pdf"));
            multipart.addBodyPart(pdfAttachment);

            // 🔹 Attach Extent Report
            MimeBodyPart htmlAttachment = new MimeBodyPart();
            htmlAttachment.attachFile(new File("reports/ExtentReport.html"));
            multipart.addBodyPart(htmlAttachment);

            message.setContent(multipart);

            // 🔹 Send Email
            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}