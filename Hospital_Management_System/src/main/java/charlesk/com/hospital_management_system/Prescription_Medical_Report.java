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
import javax.swing.JOptionPane;

public class Prescription_Medical_Report {

    public static final String RESULT = "Prescription Medical Report.pdf";

    public static void generateReport(int patientId) {

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

            Paragraph subTitle = new Paragraph("PRESCRIPTION REPORT", headingFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);

            document.add(new Paragraph(" "));

            // =========================
            // PRESCRIPTION INFORMATION
            // =========================

            String informationSql = """
                    SELECT
                        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
                        CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
                        dep.department_name,
                        a.appointment_date,
                        pres.prescribed_date,
                        pres.notes
                    FROM patients p
                    INNER JOIN appointments a ON p.patient_id = a.patient_id
                    INNER JOIN doctors d ON a.doctor_id = d.doctor_id
                    INNER JOIN departments dep ON d.department_id = dep.department_id
                    INNER JOIN prescriptions pres ON pres.appointment_id = a.appointment_id
                    WHERE p.patient_id = ?
                    ORDER BY pres.prescribed_date DESC
                    LIMIT 1
                    """;

            String prescriptionNotes = null;

            try(PreparedStatement infoPst = reportCon.prepareStatement(informationSql)){

                infoPst.setInt(1, patientId);

                try(ResultSet infoRs = infoPst.executeQuery()){

                    if(infoRs.next()){

                        Paragraph information = new Paragraph(
                                "Patient: " + infoRs.getString("patient_name") + "\n"
                                + "Doctor: " + infoRs.getString("doctor_name") + "\n"
                                + "Department: " + infoRs.getString("department_name") + "\n"
                                + "Appointment Date: " + infoRs.getString("appointment_date") + "\n"
                                + "Prescription Date: " + infoRs.getString("prescribed_date"), normalFont);

                        document.add(information);
                        prescriptionNotes = infoRs.getString("notes");

                    }else{
                        JOptionPane.showMessageDialog(null, "No prescription was found for Patient ID " + patientId);
                        document.close();
                        return;
                    }
                }
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // =========================
            // PRESCRIBED MEDICINES
            // =========================

            Paragraph medicineHeading = new Paragraph("PRESCRIBED MEDICINES", headingFont);
            medicineHeading.setAlignment(Element.ALIGN_LEFT);
            document.add(medicineHeading);
            document.add(new Paragraph(" "));

            PdfPTable medicineTable = new PdfPTable(6);
            medicineTable.setWidthPercentage(100);
            medicineTable.setWidths(new float[]{3f, 2f, 2f, 2.5f, 1.5f, 1.5f});

            addHeaderCell(medicineTable, "Medicine", tableHeadingFont);
            addHeaderCell(medicineTable, "Form", tableHeadingFont);
            addHeaderCell(medicineTable, "Dosage", tableHeadingFont);
            addHeaderCell(medicineTable, "Frequency", tableHeadingFont);
            addHeaderCell(medicineTable, "Days", tableHeadingFont);
            addHeaderCell(medicineTable, "Quantity", tableHeadingFont);

            // =========================
            // MEDICINE QUERY
            // =========================

            String medicineSql = """
                    SELECT
                        med.medicine_name,
                        med.form,
                        pi.dosage,
                        pi.frequency,
                        pi.duration_days,
                        pi.quantity
                    FROM patients p
                    INNER JOIN appointments a ON p.patient_id = a.patient_id
                    INNER JOIN prescriptions pres ON pres.appointment_id = a.appointment_id
                    INNER JOIN prescription_items pi ON pi.prescription_id = pres.prescription_id
                    INNER JOIN medicines med ON med.medicine_id = pi.medicine_id
                    WHERE p.patient_id = ?
                    AND pres.prescription_id = (
                        SELECT pres2.prescription_id
                        FROM prescriptions pres2
                        INNER JOIN appointments a2 ON pres2.appointment_id = a2.appointment_id
                        WHERE a2.patient_id = ?
                        ORDER BY pres2.prescribed_date DESC
                        LIMIT 1
                    )
                    """;

            try(PreparedStatement medicinePst = reportCon.prepareStatement(medicineSql)){

                medicinePst.setInt(1, patientId);
                medicinePst.setInt(2, patientId);

                try(ResultSet medicineRs = medicinePst.executeQuery()){

                    boolean hasMedicine = false;

                    while(medicineRs.next()){

                        hasMedicine = true;

                        addValueCell(medicineTable, medicineRs.getString("medicine_name"), normalFont);
                        addValueCell(medicineTable, medicineRs.getString("form"), normalFont);
                        addValueCell(medicineTable, medicineRs.getString("dosage"), normalFont);
                        addValueCell(medicineTable, medicineRs.getString("frequency"), normalFont);
                        addValueCell(medicineTable, medicineRs.getString("duration_days"), normalFont);
                        addValueCell(medicineTable, medicineRs.getString("quantity"), normalFont);
                    }

                    if(!hasMedicine){
                        PdfPCell emptyCell = new PdfPCell(new Phrase("No prescribed medicines found.", normalFont));
                        emptyCell.setColspan(6);
                        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        emptyCell.setPadding(8);
                        medicineTable.addCell(emptyCell);
                    }
                }
            }

            document.add(medicineTable);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // =========================
            // PRESCRIPTION NOTES
            // =========================

            document.add(new Paragraph("PRESCRIPTION NOTES", headingFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("The following medicines were prescribed during the patient's hospital visit.", normalFont));
            document.add(new Paragraph(" "));

            if(prescriptionNotes == null || prescriptionNotes.trim().isEmpty()) prescriptionNotes = "-";

            document.add(new Paragraph("Notes: " + prescriptionNotes, normalFont));

            // =========================
            // END OF REPORT
            // =========================

            document.add(new Paragraph(" "));
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
        PdfPCell cell = new PdfPCell(new Phrase(valueOrDash(text), font));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private static String valueOrDash(String value){
        if(value == null || value.trim().isEmpty()) return "-";
        return value;
    }

    // =========================
    // RUN REPORT
    // =========================

    public static void Main(){

        try{
            String choice = JOptionPane.showInputDialog(null, "Enter Patient ID:");
            if(choice != null) {
            } else {
                return;
            }

            int patientId = Integer.parseInt(choice);
            generateReport(patientId);

        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Please enter a valid Patient ID.");
        }
    }
}