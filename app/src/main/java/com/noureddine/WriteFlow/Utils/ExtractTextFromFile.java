package com.noureddine.WriteFlow.Utils;

import static com.noureddine.WriteFlow.Utils.CopySaveResult.processBidirectionalText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class ExtractTextFromFile {

    private Context context;

    public ExtractTextFromFile(Context context) {
        this.context = context;
    }

    public void extractTextFromFile(File file, String languageCode, OnTextExtractedListener listener) {
        String extension = getFileExtension(file);

        switch (extension.toLowerCase()) {
            case "pdf":
                //extractFromPdf(file, listener);
                break;
            case "doc":
            case "docx":
                extractFromWord(file, listener);
                break;
            case "txt":
                extractFromTxt(file, listener);
                break;
            case "jpg":
            case "jpeg":
            case "png":
                //extractFromImage(Uri.fromFile(file), languageCode, listener);
                break;
            default:
                listener.onError("Unsupported file type: " + extension);
        }
    }

//    private void extractFromPdf(File file, String languageCode, NinjasListener listener) {
//        // 1. تحويل أول صفحة إلى صورة
//        // 2. تمرير الصورة إلى OCR
//        try {
//            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
//            PdfRenderer.Page page = renderer.openPage(0);
//            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//            page.close();
//            renderer.close();
//
//            // OCR
//            extractFromBitmap(bitmap, languageCode, listener);
//
//        } catch (Exception e) {
//            listener.onError("Error reading PDF: " + e.getMessage());
//        }
//    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<Bitmap> extractFromPdf(Context context, File pdfFile) {
        List<Bitmap> pages = new ArrayList<>();
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(fd);

            for (int i = 0; i < renderer.getPageCount(); i++) {
                PdfRenderer.Page page = renderer.openPage(i);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                pages.add(bitmap);
                page.close();
            }

            renderer.close();
            fd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pages;
    }



    private void extractFromWord(File file, OnTextExtractedListener listener) {
        try {
            FileInputStream fis = new FileInputStream(file);
            StringBuilder text = new StringBuilder();

            if (file.getName().endsWith(".docx")) {
                XWPFDocument docx = new XWPFDocument(fis);
                for (XWPFParagraph p : docx.getParagraphs()) {
                    text.append(p.getText()).append("\n");
                }
                docx.close();
            } else {
                HWPFDocument doc = new HWPFDocument(fis);
                text.append(doc.getDocumentText());
                doc.close();
            }

            listener.onTextExtracted(text.toString());

        } catch (Exception e) {
            listener.onError("Error reading Word file: " + e.getMessage());
        }
    }

    private void extractFromTxt(File file, OnTextExtractedListener listener) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder text = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            reader.close();
            listener.onTextExtracted(text.toString());

        } catch (Exception e) {
            listener.onError("Error reading TXT file: " + e.getMessage());
        }
    }

    // أضف هذه الدالة أولاً لاختيار النموذج المناسب بناءً على اللغة
//    private TextRecognizer getRecognizerForLanguage(String languageCode) {
//        switch (languageCode.toLowerCase()) {
//            case "ar":
//                //return TextRecognition.getClient(new ArabicTextRecognizerOptions.Builder().build());
//            case "zh":
//                return TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
//            case "hi":
//                return TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
//            case "ja":
//                return TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());
//            case "ko":
//                return TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
//            case "ru":
//            case "uk":
//                //return TextRecognition.getClient(new CyrillicTextRecognizerOptions.Builder().build());
//            case "he":
//                //return TextRecognition.getClient(new HebrewTextRecognizerOptions.Builder().build());
//            default:
//                return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS); // Latin
//        }
//    }
//
//    private void extractFromImage(Uri uri, String languageCode, NinjasListener listener) {
//        try {
//            InputImage image = InputImage.fromFilePath(context, uri);
//            TextRecognizer recognizer = getRecognizerForLanguage(languageCode);
//
//            recognizer.process(image)
//                    .addOnSuccessListener(result -> listener.onTextExtracted(result.getText()))
//                    .addOnFailureListener(e -> listener.onError("OCR error: " + e.getMessage()));
//        } catch (IOException e) {
//            listener.onError("Image loading error: " + e.getMessage());
//        }
//    }
//
//    private void extractFromBitmap(Bitmap bitmap, String languageCode, NinjasListener listener) {
//        InputImage image = InputImage.fromBitmap(bitmap, 0);
//        TextRecognizer recognizer = getRecognizerForLanguage(languageCode);
//
//        recognizer.process(image)
//                .addOnSuccessListener(result -> listener.onTextExtracted(result.getText()))
//                .addOnFailureListener(e -> listener.onError("OCR error: " + e.getMessage()));
//    }


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot != -1) {
            return name.substring(lastDot + 1);
        } else {
            return "";
        }
    }

    public interface OnTextExtractedListener {
        void onTextExtracted(String text);
        void onError(String error);
    }
}

