/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22094289/itext-precisely-position-an-image-on-top-of-a-pdfptable
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class AddOverlappingImage {
    public static final String DEST = "./target/sandbox/tables/add_overlapping_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddOverlappingImage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method set table to use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                Cell cell = new Cell();
                cell.add(new Paragraph(((char) r) + String.valueOf(c)));
                table.addCell(cell);
            }
        }

        // Adds drawn on a canvas image to the table
        table.setNextRenderer(new OverlappingImageTableRenderer(table,
                ImageDataFactory.create("./src/test/resources/img/hero.jpg")));

        doc.add(table);

        doc.close();
    }


    private class OverlappingImageTableRenderer extends TableRenderer {
        private ImageData image;

        public OverlappingImageTableRenderer(Table modelElement, ImageData img) {
            super(modelElement);
            this.image = img;
        }

        @Override
        public void drawChildren(DrawContext drawContext) {

            // Use the coordinates of the cell in the fourth row and the second column to draw the image
            Rectangle rect = rows.get(3)[1].getOccupiedAreaBBox();
            super.drawChildren(drawContext);

            drawContext.getCanvas().addImage(image, rect.getLeft() + 10, rect.getTop() - image.getHeight(), false);
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overriden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new OverlappingImageTableRenderer((Table) modelElement, image);
        }
    }
}
