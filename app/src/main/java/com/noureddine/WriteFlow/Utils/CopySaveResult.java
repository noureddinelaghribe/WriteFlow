package com.noureddine.WriteFlow.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.noureddine.WriteFlow.R;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CopySaveResult {

    private Activity activity;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public CopySaveResult(Activity activity, ActivityResultLauncher<String> requestPermissionLauncher) {
        this.activity = activity;
        this.requestPermissionLauncher = requestPermissionLauncher;
    }

    public void copyClipboard(String text){
        // Inside your Fragment class, e.g., in onViewCreated or another method
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingInflatedId")
    public void saveAsFile(String text,String type){

        // Inflate the dialog layout
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_name_save_file, null);

        // Get reference to the EditText in the dialog layout
        final EditText editText = dialogView.findViewById(R.id.dialog_edit_text);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Name File")
                .setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                    intent.setData(uri);
                                    activity.startActivity(intent);
                                    Toast.makeText(activity, "Please grant all files access permission", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    // If the specific intent isn't available, try the general storage settings
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    activity.startActivity(intent);
                                    Toast.makeText(activity, "Please grant all files access permission", Toast.LENGTH_LONG).show();
                                }
                            } else {

                                // txt word pdf html
                                switch (type){
                                    case "txt":
                                        saveAsTxt(text,editText.getText().toString().trim());
                                        break;
                                    case "word":
                                        saveTextAsWord(text,editText.getText().toString().trim());
                                        break;
                                    case "pdf":
                                        saveTextAsPDF(text,editText.getText().toString().trim());
                                        break;
                                    case "html":
                                        saveAsHtml(text,editText.getText().toString().trim());
                                        break;
                                }

                            }
                        } else {
                            // For Android 10 (API 29) and below, request READ_EXTERNAL_STORAGE
                            requestPermissionLauncher.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void saveAsTxt(String text,String fileName){

        String SAVE_FOLDER_NAME = "PhraseFlow";
        String SAVE_FILE_NAME = fileName+".txt";

        File file = activity.getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        if (path != null) {
            // Create a new file in that directory
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(text.getBytes());
                Toast.makeText(activity, "File saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Error saving file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "External storage not available", Toast.LENGTH_SHORT).show();
        }

    }


    private void saveTextAsWord(String text, String fileName) {

        String SAVE_FOLDER_NAME = "PhraseFlow";
        String SAVE_FILE_NAME = fileName+".docx";

        File file = activity.getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        try {
            // Create a new document
            XWPFDocument document = new XWPFDocument();

            // Create a new paragraph
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(text);

            // Define the file path
            //File file = new File(getExternalFilesDir(null), fileName + ".docx");

            // Write the document to file
            FileOutputStream out = new FileOutputStream(path);
            document.write(out);
            out.close();
            document.close();

            Toast.makeText(activity, "Document saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error saving document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTextAsPDF(String text, String fileName) {
        String SAVE_FOLDER_NAME = "PhraseFlow";
        String SAVE_FILE_NAME = fileName+".pdf";

        File file = activity.getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.add(new Paragraph(text));
            document.close();

            Toast.makeText(activity, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAsHtml(String content, String fileName) {
        try {
            // Create HTML content
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    //"    <title>" + fileName + "</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    content +
                    "\n</body>\n" +
                    "</html>";

            String SAVE_FOLDER_NAME = "PhraseFlow";
            String SAVE_FILE_NAME = fileName+".html";

            File file = activity.getDatabasePath(SAVE_FILE_NAME);
            File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

            String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

            if (!sdir.exists()) {
                sdir.mkdirs();
            }

            // Write to the file
            FileWriter writer = new FileWriter(path);
            writer.write(htmlContent);
            writer.close();

            Toast.makeText(activity, "HTML saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error saving HTML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
