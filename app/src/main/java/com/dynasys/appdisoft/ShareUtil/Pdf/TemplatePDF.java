package com.dynasys.appdisoft.ShareUtil.Pdf;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {


    private Context context;
    public File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;

    private Paragraph paragraph;
    private Font ftitle=new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD);
    private Font fSubtitle=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);  //Subtitulo
    private Font fContenidoGrilla=new Font(Font.FontFamily.TIMES_ROMAN ,11,Font.NORMAL);  //Subtitulo
    private Font fText=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);     // Texto contenido
    private Font fHighText=new Font(Font.FontFamily.TIMES_ROMAN,15,Font.BOLD, BaseColor.GREEN);  //Resaltado


    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openDocument(String title){

        createFile(title);
        try{
        document=new Document(PageSize.SMALL_PAPERBACK);
        pdfWriter=PdfWriter.getInstance(document,new FileOutputStream(pdfFile));
        document.open();

        }catch (Exception e){

        }
    }

    public void closeDocument(){
        document.close();
    }
    
    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle, String Date){

        try{
            paragraph=new Paragraph();
            addChildP( new Paragraph(title,ftitle));
            addChildP( new Paragraph(title,ftitle));
            addChildP( new Paragraph(subTitle,fSubtitle));
            addChildP( new Paragraph("Generado MArco: "+Date,fHighText ));

            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        }catch (Exception e){

        }



    }
public void addParagraph(String text){
try{
    paragraph=new Paragraph(text,fText );
    paragraph.setSpacingAfter(5);
    paragraph.setSpacingBefore(5);
    document.add(paragraph);
}catch (Exception e){

}

}
    public void addParagraph02(String text){
        try{
            paragraph=new Paragraph(text,fText );

            document.add(paragraph);
        }catch (Exception e){

        }

    }
    public void addParagraphTotales(String text){
        try{
            paragraph=new Paragraph(text,fText );
            paragraph.setSpacingAfter(5);
          //  paragraph.setSpacingBefore(5);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);
        }catch (Exception e){

        }

    }
    public void addParagraphTotalesSinEspacio(String text){
        try{
            paragraph=new Paragraph(text,fText );

            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);
        }catch (Exception e){

        }

    }
    public void addParagraphTitle(String text){
        try{
            paragraph=new Paragraph(text,fSubtitle );
            //paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        }catch (Exception e){

        }

    }
public void createTable(String[]header, ArrayList<String[]> clients){

        try{
            paragraph=new Paragraph();

            paragraph.setFont(fText);
            PdfPTable pdfPTable=new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(20);
            pdfPTable.setWidths(new float[] { 3, 1,2,2 });
            PdfPCell pdfPCell;
            int indexC=0;
            while (indexC<header.length){
                pdfPCell=new PdfPCell(new Phrase(header[indexC++],fSubtitle));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                //pdfPCell.setBackgroundColor(BaseColor.ORANGE);
                pdfPTable.addCell(pdfPCell);

            }

            for (int indexR = 0; indexR <clients.size() ; indexR++) {
                String[]row=clients.get(indexR);
                for ( indexC = 0; indexC < header.length; indexC++) {
                    pdfPCell=new PdfPCell(new Phrase(row[indexC],fContenidoGrilla));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    //pdfPCell.setFixedHeight(20);
                    pdfPTable.addCell(pdfPCell);
                }

            }
            paragraph.add(pdfPTable);
            document.add(paragraph);

        }catch (Exception e){

        }


}
    private void addChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);

    }
    private void createFile(String Title){
        File folder=new File(Environment.getExternalStorageDirectory().toString(),"PDFDisoft");
        if (!folder.exists()){
            folder.mkdir();
            pdfFile=new File(folder,Title+".pdf");
        }else{

            pdfFile=new File(folder,Title+".pdf");
        }
    }

    public void viewPDF(){
       /* if (pdfFile.exists()){
            Intent intent=new Intent(context,viewPdf.class);
            intent.putExtra("path",pdfFile.getAbsolutePath());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }*/

    }

    public void appviewPDF(Activity activity){

        if (pdfFile.exists()){
            Uri uri;
            //Uri uri = FileProvider.getUriForFile(context, "com.mydomain.fileprovider", new File(pdfFile.getAbsolutePath()));

          if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            //  uri = FileProvider.getUriForFile(context, "com.example.exportpdf.provider", pdfFile);
              //uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", pdfFile);
              Uri pdf = FileProvider.getUriForFile(activity, context.getPackageName() + ".provider", pdfFile);
              //context.grantUriPermission(context.getPackageName(), pdf, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

              Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
              pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              pdfOpenintent.setDataAndType(pdf, "application/pdf");
              try { activity.startActivity(pdfOpenintent);
              } catch (ActivityNotFoundException e) { // handle no application here....
                   }
          }else{
              uri= Uri.fromFile(pdfFile);
          }
          /*  Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri,"application/pdf") ;

            try{
               activity.startActivity(intent);
            }catch (ActivityNotFoundException e){
                Log.d("Error Open Appp", e.getMessage());

                activity.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.adobe.reader")));
                Toast.makeText(activity.getApplicationContext(),"No Cuentas con una App para PDF",Toast.LENGTH_LONG).show();

            }*/
        }else{
            Toast.makeText(activity.getApplicationContext(),"No se Encuentra el Archivo", Toast.LENGTH_LONG).show();

        }
    }
}
