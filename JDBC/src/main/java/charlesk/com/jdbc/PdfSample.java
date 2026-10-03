package charlesk.com.jdbc;

import com.itextpdf.text.Document;
/*This is the most important class in iText library and represent PDF document instance. 
If you need to generate a PDF document from scratch, you will use the Document class. 
First you must create a Document instance. Then you must open it. 
After that you add content to the document. Finally you close the Document instance.
*/
import com.itextpdf.text.Paragraph;
/*This class represents a indented “paragraph” of text. In a paragraph you can set the paragraph alignment, 
indentation and spacing before and after the paragraph.
*/
import com.itextpdf.text.Chapter;
// This class represents a chapter in the PDF document. It is created using a Paragraph as title and an int as chapter number.
    import com.itextpdf.text.Font;
/* This class contains all specifications of a font, such as family of font, size, style, and color. 
Various fonts are declared as static constants in this class.
*/
import com.itextpdf.text.List;
// This class represents a list, which, in turn, contains a number of ListItems.
 // This is a table that can be put at an absolute position but can also be added to the document as the class Table.
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
//: An Anchor can be a reference or a destination of a reference. A link like we have in HTML pages.
 import com.itextpdf.text.pdf.PdfWriter;
/* When this PdfWriter is added to a certain PdfDocument, the PDF representation of every Element added to this Document will 
be written to the outputstream attached to writer (file or network).
*/
 import com.itextpdf.text.pdf.PdfReader;

import com.itextpdf.text.Phrase; //
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
    //Used to read a PDF document. Simple and clear.
   import com.itextpdf.text.BaseColor;

import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;


import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import java.io.IOException;

        /**
         *
         * @author AugustineBeilel
         */
public class PdfSample
{
   public static void main(String[] args) throws BadElementException, IOException, FileNotFoundException, DocumentException
   {
      Document document = new Document();

//                  OutputStream file = new FileOutputStream(new File("PasswordProtected.pdf"));
//                  PdfWriter writer = PdfWriter.getInstance(document, file);
//                        Document document = new Document();

//                /* iText allows to add metadata to the PDF which can be viewed in your Adobe Reader under File -> Properties */
//                   
//                        document.addTitle("BCS_BSE");
//                        document.addSubject("Using iText");
//                        document.addKeywords("Java, PDF, iText");
//                        document.addAuthor("Augustine Beilel");
//                        document.addCreator("beilelaugustine25@gmail.com");                  


         PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\AugustineBeilel\\Desktop\\FinalYearProjectSystemImages\\HelloWorld.pdf"));
         document.open();

        //adding image to the pdf from a local directory
        document.add(new Paragraph(""));
        //Add Image
        Image image1 = Image.getInstance("C:\\Users\\AugustineBeilel\\Desktop\\me.jpg");
        //Fixed Positioning
        //image1.setAbsolutePosition(100f, 550f);
        //Scale to new height and new width of image
        image1.scaleAbsolute(100, 100);
        image1.getImageMask();
        //image1.setAlignment(image1.ALIGN_LEFT);
        //Add to document
        document.add(image1);


//                //adding image to the document from internet
//                String imageUrl = "http://www.eclipse.org/xtend/images/java8_logo.png";
//                Image image2 = Image.getInstance(new URL(imageUrl));
//                document.add(image2);


            //styling the content of PDF e.g the usage of Fonts, chapters, and sections.

        Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL, new CMYKColor(255, 0, 0, 0));
        Font redFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(0, 255, 0, 0));
        Font yellowFont = FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new CMYKColor(0, 0, 255, 0));


        Paragraph para1=new Paragraph("The World Global Focus",blueFont);
        para1.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(para1);

        Paragraph para22 = new Paragraph("'British High Commission Council'",blueFont);
            para22.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(para22);

        Paragraph para2 = new Paragraph("'P.O.BOX 1245'",blueFont);
            para2.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(para2);

        Paragraph para3 = new Paragraph("'London - Street'",blueFont);
            para3.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(para3);


        Paragraph Date=new Paragraph(new Date().toString(),FontFactory.getFont(FontFactory.TIMES_ROMAN,11,Font.BOLD,BaseColor.BLUE));
        Date.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(Date);

         //Paragraph with color and font styles
          Paragraph paragraphOne = new Paragraph("THE WORLD WAR THREE CATALYST", redFont);
          paragraphOne.setAlignment(Paragraph.ALIGN_LEFT);
          document.add(paragraphOne);

        //Create chapter and sections
        Paragraph chapterTitle = new Paragraph("Background", yellowFont);
        Chapter chapter1 = new Chapter(chapterTitle,1 );
        chapter1.setNumberDepth(5);

        Paragraph sectionTitle = new Paragraph("Introduction", redFont);
        Section section1 = chapter1.addSection(sectionTitle);

        Paragraph sectionContent = new Paragraph("Russia invaded Ukraine on 24th Feb, 2022. There was massive airstrikes followed by ground patrols ", blueFont);
        section1.add(sectionContent);

        Paragraph subTitle = new Paragraph("Overview", redFont);
        Section section2 = chapter1.addSection(subTitle);

        document.add(chapter1);

        PdfPTable table = new PdfPTable(3); // 3 columns.
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table

        document.add(new Paragraph(""));
        PdfPCell cell=new PdfPCell(new Paragraph("A comprehensive Report on Russian invasion in Ukraine",blueFont));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        //cell.setBorder(30);
        table.addCell(cell);


            PdfPCell c1 = new PdfPCell(new Phrase("Refugees"));
            //c1.setPaddingLeft(10);


            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("weapons"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("FighterJets"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);

            table.addCell("200,000");
            table.addCell("Missiles");
            table.addCell("64");
            table.addCell("7654");
            table.addCell("Tanks");
            table.addCell("10");

            document.add(table);


    //                     //Another format adding table to the Pdf
    //                        PdfPTable table = new PdfPTable(6); // 3 columns.
    //                table.setWidthPercentage(100); //Width 100%
    //                table.setSpacingBefore(10f); //Space before table
    //                table.setSpacingAfter(10f); //Space after table
    //
    //                //Set Column widths
    //                float[] columnWidths = {1f, 1f, 1f,1f , 1f,1f};
    //                table.setWidths(columnWidths);
    //                
    //                //table.getSpacingBefore();
    //                document.add(new Paragraph(""));
    //                PdfPCell cell=new PdfPCell(new Paragraph("A comprehensive Report on Russian invasion in Ukraine",FontFactory.getFont(FontFactory.TIMES_BOLD,18,Font.BOLD,BaseColor.MAGENTA)));
    //                cell.setColspan(6);
    //                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    //                cell.setBackgroundColor(BaseColor.GREEN);
    //                cell.setBorder(30);
    //                table.addCell(cell);
    //                
    //                PdfPCell cell1 = new PdfPCell(new Paragraph("Number Of Refugees"));
    //                cell1.setBorderColor(BaseColor.BLUE);
    //                cell1.setPaddingLeft(10);
    //                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
    //                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
    //                 table.addCell(cell1);
    //                 table.addCell("200,000");
    //                
    //                PdfPCell cell2 = new PdfPCell(new Paragraph("Russian Tanks Destroyed"));
    //                cell2.setBorderColor(BaseColor.BLACK);
    //                cell2.setPaddingLeft(10);
    //                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
    //                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
    //                cell2.setBackgroundColor(BaseColor.GREEN);
    //                cell2.setBorder(30);
    //                 table.addCell(cell2);
    //                 table.addCell("302");
    //                
    //                PdfPCell cell3 = new PdfPCell(new Paragraph("Sanctions"));
    //                cell3.setBorderColor(BaseColor.RED);
    //                cell3.setPaddingLeft(10);
    //                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
    //                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
    //                table.addCell(cell3);
    //                table.addCell("Travel Ban");
    //                
    //                document.add(table);



                     document.close();
                     writer.close();
                  }
               }

