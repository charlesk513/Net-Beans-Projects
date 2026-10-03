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

public class PatientHistory {

    public static final String RESULT = "Patient Medical History Report.pdf";

    public static void generateReport(int patientId) {

        Document document = new Document(PageSize.A4.rotate());

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

            Paragraph subTitle = new Paragraph("PATIENT MEDICAL HISTORY REPORT", headingFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);

            Paragraph generated = new Paragraph("Generated: " + LocalDate.now(), normalFont);
            generated.setAlignment(Element.ALIGN_CENTER);
            document.add(generated);

            document.add(new Paragraph(" "));

            // =========================
            // PATIENT INFORMATION
            // =========================

            String patientSql = "SELECT patient_id, first_name, last_name, gender, date_of_birth, phone, address FROM patients WHERE patient_id = ?";

            try(PreparedStatement patientPst = reportCon.prepareStatement(patientSql)){

                patientPst.setInt(1, patientId);

                try(ResultSet patientRs = patientPst.executeQuery()){

                    if(patientRs.next()){

                        document.add(new Paragraph("PATIENT INFORMATION", headingFont));
                        document.add(new Paragraph(" "));

                        PdfPTable patientTable = new PdfPTable(4);
                        patientTable.setWidthPercentage(100);
                        patientTable.setWidths(new float[]{1.5f, 3f, 1.5f, 3f});

                        addLabelCell(patientTable, "Patient ID", headingFont);
                        addValueCell(patientTable, patientRs.getString("patient_id"), normalFont);
                        addLabelCell(patientTable, "Patient Name", headingFont);
                        addValueCell(patientTable, patientRs.getString("first_name") + " " + patientRs.getString("last_name"), normalFont);
                        addLabelCell(patientTable, "Gender", headingFont);
                        addValueCell(patientTable, patientRs.getString("gender"), normalFont);
                        addLabelCell(patientTable, "Date of Birth", headingFont);
                        addValueCell(patientTable, patientRs.getString("date_of_birth"), normalFont);
                        addLabelCell(patientTable, "Phone", headingFont);
                        addValueCell(patientTable, patientRs.getString("phone"), normalFont);
                        addLabelCell(patientTable, "Address", headingFont);
                        addValueCell(patientTable, patientRs.getString("address"), normalFont);

                        document.add(patientTable);

                    }else{
                        JOptionPane.showMessageDialog(null,"Patient ID " + patientId + " was not found.");
                        document.close();
                        return;
                    }
                }
            }

            document.add(new Paragraph(" "));

            // =========================
            // INTRODUCTORY PARAGRAPH
            // =========================

            Paragraph introduction = new Paragraph("This report presents the patient's recorded hospital " + "history, including appointments, attending doctors, " + "departments and diagnoses recorded during hospital visits.", normalFont);
            introduction.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(introduction);

            document.add(new Paragraph(" "));

            // =========================
            // APPOINTMENT + DIAGNOSIS
            // =========================

            document.add(new Paragraph("APPOINTMENT AND DIAGNOSIS HISTORY", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable appointmentTable = new PdfPTable(6);
            appointmentTable.setWidthPercentage(100);
            appointmentTable.setWidths(new float[]{2f, 2.5f, 2.5f, 2.5f, 3f, 2f});

            addHeaderCell(appointmentTable, "Date", tableHeadingFont);
            addHeaderCell(appointmentTable, "Doctor", tableHeadingFont);
            addHeaderCell(appointmentTable, "Department", tableHeadingFont);
            addHeaderCell(appointmentTable, "Diagnosis", tableHeadingFont);
            addHeaderCell(appointmentTable, "Treatment Plan", tableHeadingFont);
            addHeaderCell(appointmentTable, "Status", tableHeadingFont);

            String appointmentSql =
                    "SELECT a.appointment_date, a.status, "
                    + "CONCAT(d.first_name, ' ', d.last_name) AS doctor_name, "
                    + "dep.department_name, "
                    + "dg.diagnosis_name, dg.treatment_plan "
                    + "FROM appointments a "
                    + "INNER JOIN doctors d ON a.doctor_id = d.doctor_id "
                    + "INNER JOIN departments dep ON d.department_id = dep.department_id "
                    + "LEFT JOIN diagnoses dg ON a.appointment_id = dg.appointment_id "
                    + "WHERE a.patient_id = ? "
                    + "ORDER BY a.appointment_date DESC";

            try(PreparedStatement appointmentPst = reportCon.prepareStatement(appointmentSql)){

                appointmentPst.setInt(1, patientId);

                try(ResultSet appointmentRs = appointmentPst.executeQuery()){

                    boolean hasAppointments = false;

                    while(appointmentRs.next()){

                        hasAppointments = true;

                        addValueCell(appointmentTable, appointmentRs.getString("appointment_date"), normalFont);
                        addValueCell(appointmentTable, appointmentRs.getString("doctor_name"), normalFont);
                        addValueCell(appointmentTable, appointmentRs.getString("department_name"), normalFont);
                        addValueCell(appointmentTable, valueOrDash(appointmentRs.getString("diagnosis_name")), normalFont);
                        addValueCell(appointmentTable, valueOrDash(appointmentRs.getString("treatment_plan")), normalFont);
                        addValueCell(appointmentTable, appointmentRs.getString("status"), normalFont);
                    }

                    if(!hasAppointments){
                        PdfPCell emptyCell = new PdfPCell(new Phrase("No appointment or diagnosis records found.", normalFont));
                        emptyCell.setColspan(6);
                        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        emptyCell.setPadding(8);
                        appointmentTable.addCell(emptyCell);
                    }
                }
            }

            document.add(appointmentTable);
            document.add(new Paragraph(" "));

            // =========================
            // LABORATORY TESTS
            // =========================

            document.add(new Paragraph("LABORATORY TEST HISTORY", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable labTable = new PdfPTable(5);
            labTable.setWidthPercentage(100);
            labTable.setWidths(new float[]{2.5f, 2f, 2.5f, 4f, 2f});

            addHeaderCell(labTable, "Test Name", tableHeadingFont);
            addHeaderCell(labTable, "Test Date", tableHeadingFont);
            addHeaderCell(labTable, "Doctor", tableHeadingFont);
            addHeaderCell(labTable, "Result", tableHeadingFont);
            addHeaderCell(labTable, "Status", tableHeadingFont);

            String labSql =
                    "SELECT lt.test_name, lt.test_date, lt.result, lt.status, "
                    + "CONCAT(d.first_name, ' ', d.last_name) AS doctor_name "
                    + "FROM lab_tests lt "
                    + "INNER JOIN appointments a ON lt.appointment_id = a.appointment_id "
                    + "INNER JOIN doctors d ON a.doctor_id = d.doctor_id "
                    + "WHERE a.patient_id = ? "
                    + "ORDER BY lt.test_date DESC";

            try(PreparedStatement labPst = reportCon.prepareStatement(labSql)){

                labPst.setInt(1, patientId);

                try(ResultSet labRs = labPst.executeQuery()){

                    boolean hasLabTests = false;

                    while(labRs.next()){

                        hasLabTests = true;

                        addValueCell(labTable,labRs.getString("test_name"), normalFont);
                        addValueCell(labTable, labRs.getString("test_date"),normalFont);
                        addValueCell(labTable, labRs.getString("doctor_name"), normalFont);
                        addValueCell(labTable, valueOrDash(labRs.getString("result")), normalFont);
                        addValueCell(labTable, labRs.getString("status"), normalFont);
                    }

                    if(!hasLabTests){
                        PdfPCell emptyCell = new PdfPCell(new Phrase("No laboratory test records found.",normalFont));
                        emptyCell.setColspan(5);
                        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        emptyCell.setPadding(8);
                        labTable.addCell(emptyCell);
                    }
                }
            }

            document.add(labTable);

            // =========================
            // END OF REPORT
            // =========================

            document.add(new Paragraph(" "));

            Paragraph end = new Paragraph("End of Report", normalFont);
            end.setAlignment(Element.ALIGN_CENTER);
            document.add(end);

            document.close();

            //JOptionPane.showMessageDialog(null,"Patient Medical History Report generated successfully.");

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

            JOptionPane.showMessageDialog(null,"Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================
    // TABLE HEADER CELL
    // =========================

    private static void addHeaderCell(PdfPTable table, String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }

    // =========================
    // LABEL CELL
    // =========================

    private static void addLabelCell(PdfPTable table, String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        table.addCell(cell);
    }

    // =========================
    // NORMAL VALUE CELL
    // =========================

    private static void addValueCell(PdfPTable table, String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(valueOrDash(text), font));
        cell.setPadding(6);
        table.addCell(cell);
    }

    // =========================
    // NULL HANDLING
    // =========================

    private static String valueOrDash(String value){
        if(value == null || value.trim().isEmpty()) return "-";
        return value;
    }

    // =========================
    // TEST
    // =========================

    public static void Main()throws IOException{
        try{
            String choice = JOptionPane.showInputDialog(null,"Enter Patient ID: ");
            int x = Integer.parseInt(choice);
            generateReport(x);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Error: "+ e.getMessage());
        }
    }
}