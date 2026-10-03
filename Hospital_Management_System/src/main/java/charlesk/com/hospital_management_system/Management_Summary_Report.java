package charlesk.com.hospital_management_system;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class Management_Summary_Report {

    public static final String RESULT = "Management Summary Report.pdf";

    public static void generateReport(){

        Document document = new Document(PageSize.A4);

        try(Connection reportCon = DatabaseConnection.getConnection()){

            PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();

            // =========================
            // FONTS
            // =========================

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
            Font tableHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);

            // =========================
            // TITLE
            // =========================

            Paragraph title = new Paragraph("HOSPITAL MANAGEMENT SYSTEM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subTitle = new Paragraph("MANAGEMENT SUMMARY REPORT", headingFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);

            Paragraph generated = new Paragraph("Generated: " + LocalDate.now(), normalFont);
            generated.setAlignment(Element.ALIGN_CENTER);
            document.add(generated);

            document.add(new Paragraph(" "));

            // =========================
            // OVERVIEW
            // =========================

            document.add(new Paragraph("OVERVIEW", headingFont));

            Paragraph overview = new Paragraph("This report provides a general summary of hospital operations, including patients, staff, appointments, admissions and financial activity recorded in the system.", normalFont);
            overview.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(overview);

            document.add(new Paragraph(" "));

            // =========================
            // HOSPITAL STATISTICS
            // =========================

            document.add(new Paragraph("HOSPITAL STATISTICS", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable statisticsTable = new PdfPTable(2);
            statisticsTable.setWidthPercentage(100);
            statisticsTable.setWidths(new float[]{4f, 2f});

            addHeaderCell(statisticsTable, "Category", tableHeadingFont);
            addHeaderCell(statisticsTable, "Total", tableHeadingFont);

            String statisticsSql = """
                    SELECT 'Registered Patients' AS category, COUNT(*) AS total FROM patients
                    UNION ALL
                    SELECT 'Doctors', COUNT(*) FROM doctors
                    UNION ALL
                    SELECT 'Nurses', COUNT(*) FROM nurses
                    UNION ALL
                    SELECT 'Departments', COUNT(*) FROM departments
                    UNION ALL
                    SELECT 'Appointments', COUNT(*) FROM appointments
                    UNION ALL
                    SELECT 'Admissions', COUNT(*) FROM admissions
                    UNION ALL
                    SELECT 'Laboratory Tests', COUNT(*) FROM lab_tests
                    UNION ALL
                    SELECT 'Prescriptions', COUNT(*) FROM prescriptions
                    """;

            try(PreparedStatement statisticsPst = reportCon.prepareStatement(statisticsSql);
                ResultSet statisticsRs = statisticsPst.executeQuery()){

                while(statisticsRs.next()){
                    addValueCell(statisticsTable, statisticsRs.getString("category"), normalFont);
                    addValueCell(statisticsTable, statisticsRs.getString("total"), normalFont);
                }
            }

            document.add(statisticsTable);
            document.add(new Paragraph(" "));

            // =========================
            // APPOINTMENT SUMMARY
            // =========================

            document.add(new Paragraph("APPOINTMENT SUMMARY", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable appointmentTable = new PdfPTable(2);
            appointmentTable.setWidthPercentage(100);
            appointmentTable.setWidths(new float[]{4f, 2f});

            addHeaderCell(appointmentTable, "Status", tableHeadingFont);
            addHeaderCell(appointmentTable, "Number", tableHeadingFont);

            String appointmentSql = """
                    SELECT status, COUNT(*) AS number
                    FROM appointments
                    GROUP BY status
                    ORDER BY status
                    """;

            try(PreparedStatement appointmentPst = reportCon.prepareStatement(appointmentSql);
                ResultSet appointmentRs = appointmentPst.executeQuery()){

                while(appointmentRs.next()){
                    addValueCell(appointmentTable, appointmentRs.getString("status"), normalFont);
                    addValueCell(appointmentTable, appointmentRs.getString("number"), normalFont);
                }
            }

            document.add(appointmentTable);
            document.add(new Paragraph(" "));

            // =========================
            // WARD SUMMARY
            // =========================

            document.add(new Paragraph("WARD SUMMARY", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable wardTable = new PdfPTable(3);
            wardTable.setWidthPercentage(100);
            wardTable.setWidths(new float[]{3f, 3f, 2f});

            addHeaderCell(wardTable, "Ward", tableHeadingFont);
            addHeaderCell(wardTable, "Department", tableHeadingFont);
            addHeaderCell(wardTable, "Capacity", tableHeadingFont);

            String wardSql = """
                    SELECT w.ward_name, d.department_name, w.capacity
                    FROM wards w
                    INNER JOIN departments d ON w.department_id = d.department_id
                    ORDER BY w.ward_name
                    """;

            try(PreparedStatement wardPst = reportCon.prepareStatement(wardSql);
                ResultSet wardRs = wardPst.executeQuery()){

                while(wardRs.next()){
                    addValueCell(wardTable, wardRs.getString("ward_name"), normalFont);
                    addValueCell(wardTable, wardRs.getString("department_name"), normalFont);
                    addValueCell(wardTable, wardRs.getString("capacity"), normalFont);
                }
            }

            document.add(wardTable);
            document.add(new Paragraph(" "));

            // =========================
            // FINANCIAL SUMMARY
            // =========================

            document.add(new Paragraph("FINANCIAL SUMMARY", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable financialTable = new PdfPTable(2);
            financialTable.setWidthPercentage(100);
            financialTable.setWidths(new float[]{4f, 2f});

            addHeaderCell(financialTable, "Category", tableHeadingFont);
            addHeaderCell(financialTable, "Amount", tableHeadingFont);

            String financialSql = """
                    SELECT
                        COALESCE((SELECT SUM(total_amount) FROM bills), 0) AS total_billed,
                        COALESCE((SELECT SUM(amount) FROM payments), 0) AS total_paid
                    """;

            try(PreparedStatement financialPst = reportCon.prepareStatement(financialSql);
                ResultSet financialRs = financialPst.executeQuery()){

                if(financialRs.next()){

                    double totalBilled = financialRs.getDouble("total_billed");
                    double totalPaid = financialRs.getDouble("total_paid");
                    double outstandingBalance = totalBilled - totalPaid;

                    addValueCell(financialTable, "Total Amount Billed", normalFont);
                    addValueCell(financialTable, "UGX " + String.format("%,.2f", totalBilled), normalFont);

                    addValueCell(financialTable, "Total Payments Received", normalFont);
                    addValueCell(financialTable, "UGX " + String.format("%,.2f", totalPaid), normalFont);

                    addValueCell(financialTable, "Outstanding Balance", normalFont);
                    addValueCell(financialTable, "UGX " + String.format("%,.2f", outstandingBalance), normalFont);
                }
            }

            document.add(financialTable);
            document.add(new Paragraph(" "));

            // =========================
            // STAFF BY DEPARTMENT
            // =========================

            document.add(new Paragraph("STAFF BY DEPARTMENT", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable staffTable = new PdfPTable(3);
            staffTable.setWidthPercentage(100);
            staffTable.setWidths(new float[]{4f, 2f, 2f});

            addHeaderCell(staffTable, "Department", tableHeadingFont);
            addHeaderCell(staffTable, "Doctors", tableHeadingFont);
            addHeaderCell(staffTable, "Nurses", tableHeadingFont);

            String staffSql = """
                    SELECT
                        dep.department_name,
                        COUNT(DISTINCT d.doctor_id) AS doctors,
                        COUNT(DISTINCT n.nurse_id) AS nurses
                    FROM departments dep
                    LEFT JOIN doctors d ON dep.department_id = d.department_id
                    LEFT JOIN nurses n ON dep.department_id = n.department_id
                    GROUP BY dep.department_id, dep.department_name
                    ORDER BY dep.department_name
                    """;

            try(PreparedStatement staffPst = reportCon.prepareStatement(staffSql);
                ResultSet staffRs = staffPst.executeQuery()){

                while(staffRs.next()){
                    addValueCell(staffTable, staffRs.getString("department_name"), normalFont);
                    addValueCell(staffTable, staffRs.getString("doctors"), normalFont);
                    addValueCell(staffTable, staffRs.getString("nurses"), normalFont);
                }
            }

            document.add(staffTable);

            // =========================
            // END OF REPORT
            // =========================

            document.add(new Paragraph(" "));

            Paragraph end = new Paragraph("End of Report", normalFont);
            end.setAlignment(Element.ALIGN_CENTER);
            document.add(end);

            document.close();

            // =========================
            // OPEN PDF
            // =========================

            File reportFile = new File(RESULT);

            if(Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(reportFile);
            }else{
                new ProcessBuilder("xdg-open", reportFile.getAbsolutePath()).start();
            }

        }catch(SQLException | DocumentException | IOException e){
            if(document.isOpen()) document.close();
            JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================
    // TABLE HEADER
    // =========================

    private static void addHeaderCell(PdfPTable table, String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }

    // =========================
    // TABLE VALUE
    // =========================

    private static void addValueCell(PdfPTable table, String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        table.addCell(cell);
    }

    // =========================
    // RUN REPORT
    // =========================

    public static void Main(){
        generateReport();
    }
}