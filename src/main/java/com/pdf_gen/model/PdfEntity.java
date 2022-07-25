package com.pdf_gen.model;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.IOException;
import java.net.MalformedURLException;

public class PdfEntity {

    private String path;

    private String imagePath;

    private String title;

    private String text;

    private PdfDocument pdfDocument;

    private Document document;

    public PdfEntity(String path, String imagePath, String title, String text) {
        this.path = path;
        this.imagePath = imagePath;
        this.title = title;
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public Document createPdfDocument() {
        try {
            PdfWriter pdfWriter = new PdfWriter(getPath());
            this.pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();
            this.document = new Document(pdfDocument,PageSize.A4,false);
            addImage(document);
            addTitle(document);
            addText(document);
            addFooter(document);
            return document;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getCause();
            e.printStackTrace();
        }
        return null;
    }

    private void addTitle(Document document) {
        Text textTitle = new Text(title)
                .setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE)
                .setStrokeWidth(.5f)
                .setStrokeColor(DeviceGray.BLACK);
        Paragraph paragraph = new Paragraph(textTitle);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);
        document.add(new Paragraph());
    }

    private void addText(Document document) {
        Paragraph paragraph = new Paragraph(this.text);
        document.add(paragraph);
    }
    private void addImage(Document document) {
       try {
           ImageData data = ImageDataFactory.create(imagePath);
           Image image = new Image(data);
           image.scale(.5f,.5f);
           document.add(image);
       } catch (MalformedURLException e) {
           e.printStackTrace();
       }
    }

    public void addFooter(Document document) {
        int numberOfPages = document.getPdfDocument().getNumberOfPages();
        for(int i = 1; i <= numberOfPages; i++) {
            document.showTextAligned(new Paragraph(String.format("%s", i)),
                    20, 20, i, TextAlignment.LEFT, VerticalAlignment.TOP, 0);
        }
        document.close();
    }

}
