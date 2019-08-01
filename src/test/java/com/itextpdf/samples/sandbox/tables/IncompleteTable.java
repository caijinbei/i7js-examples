/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class IncompleteTable {
    public static final String DEST = "./target/sandbox/tables/incomplete_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new IncompleteTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // The second argument determines 'large table' functionality is used
        // It defines whether parts of the table will be written before all data is added.
        Table table = new Table(UnitValue.createPercentArray(5), true);

        for (int i = 0; i < 5; i++) {
            table.addHeaderCell(new Cell().setKeepTogether(true).add(new Paragraph("Header " + i)));
        }

        // For the "large tables" they shall be added to the document before its child elements are populated
        doc.add(table);

        for (int i = 0; i < 500; i++) {
            if (i % 5 == 0) {

                // Moves to the new row of table
                // Flush() and Complete() methods are required and used only for the 'large tables'
                table.flush();
            }

            table.addCell(new Cell().setKeepTogether(true).add(new Paragraph("Test " + i)
                    .setMargins(0, 0, 0, 0)));
        }

        // Indicates that no more content will be added to the table
        table.complete();

        doc.close();
    }
}
