package com.noureddine.WriteFlow.Utils;

import static androidx.test.InstrumentationRegistry.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.model.ProcessingWord;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;
import com.noureddine.WriteFlow.viewModels.ChatViewModel;
import com.noureddine.WriteFlow.viewModels.GeminiViewModel;

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
    public static String SAVE_FOLDER_NAME = "WordLoom";
    private GeminiViewModel geminiViewModel;
    private DialogLoading dialogLoading;
    private ChatViewModel viewModel;
    private String []modelAi = {"openai","gemini"};
    private EncryptedPrefsManager prefs;
    private FirebaseRepository firebaseRepository;


    public CopySaveResult(Activity activity, ActivityResultLauncher<String> requestPermissionLauncher) {
        this.activity = activity;
        this.requestPermissionLauncher = requestPermissionLauncher;
        dialogLoading = new DialogLoading(activity);
        dialogLoading.loadingProgressDialog("Saving file ...");
        prefs = EncryptedPrefsManager.getInstance(activity);

        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ChatViewModel.class);
        geminiViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(GeminiViewModel.class);

        firebaseRepository = new FirebaseRepository(activity);


    }

    public CopySaveResult(Activity activity) {
        this.activity = activity;
    }

    public void copyClipboard(String text){
        // Inside your Fragment class, e.g., in onViewCreated or another method
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingInflatedId")
    public void saveAsFile(String text,String type,String userId){

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
                                        dialogLoading.showLoadingProgressDialog();
                                        saveAsTxt(text,editText.getText().toString().trim());
                                        break;
                                    case "word":
                                        dialogLoading.showLoadingProgressDialog();
                                        saveTextAsWord(text,editText.getText().toString().trim());
                                        break;
                                    case "pdf":
                                        dialogLoading.showLoadingProgressDialog();
                                        saveTextAsPDF(text,editText.getText().toString().trim());
                                        break;
                                    case "html":
                                        //saveAsHtml(text,editText.getText().toString().trim());

                                        if (prefs.getToolPreferences().getAiDetectorModel().equals(modelAi[1])){
                                            geminiViewModel.convertText2Html(text);
                                            geminiViewModel.getIsLoading().observe((LifecycleOwner) activity, isLoading -> {
                                                Log.d("TAG", "saveAsFile html: isLoading gemini");
                                                if (isLoading) dialogLoading.showLoadingProgressDialog();
                                            });
                                            geminiViewModel.getResultApi().observe((LifecycleOwner) activity, result -> {
                                                Log.d("TAG", "saveAsFile html: "+result.getResult());

                                                ProcessingWord processingWord = new ProcessingWord(
                                                        userId,
                                                        type,
                                                        result.getPromptTokens(),
                                                        result.getCandidatesTokens(),
                                                        System.currentTimeMillis()
                                                );

                                                firebaseRepository.ProcessingAnalytics(processingWord);

                                                saveAsHtml(result.getResult(),editText.getText().toString().trim());
                                                dialogLoading.dismissLoadingProgressDialog();
                                            });
                                            geminiViewModel.getError().observe((LifecycleOwner) activity, error -> {
                                                if (error != null) {
                                                    Log.d("TAG", "saveAsFile html: "+error);
                                                    Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
                                                    dialogLoading.dismissLoadingProgressDialog();
                                                }
                                            });
                                        }else if(prefs.getToolPreferences().getAiDetectorModel().equals(modelAi[0])){
                                            viewModel.convertText2Html(text);
                                            viewModel.getLoadingLiveData().observe((LifecycleOwner) activity, isLoading -> {
                                                Log.d("TAG", "saveAsFile html: isLoading openai");
                                                if (isLoading) dialogLoading.showLoadingProgressDialog();
                                            });
                                            viewModel.getResponseLiveData().observe((LifecycleOwner) activity, response -> {
                                                Log.d("TAG", "saveAsFile html: "+response);
                                                saveAsHtml(response.getResult(),editText.getText().toString().trim());

                                                ProcessingWord processingWord = new ProcessingWord(
                                                        userId,
                                                        type,
                                                        response.getPromptTokens(),
                                                        response.getCandidatesTokens(),
                                                        System.currentTimeMillis()
                                                );

                                                firebaseRepository.ProcessingAnalytics(processingWord);

                                                dialogLoading.dismissLoadingProgressDialog();
                                            });
                                            viewModel.getErrorLiveData().observe((LifecycleOwner) activity, errorMessage -> {
                                                if (errorMessage != null) {
                                                    Log.d("TAG", "saveAsFile html: "+errorMessage);
                                                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                                                    dialogLoading.dismissLoadingProgressDialog();
                                                }
                                            });
                                        }

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

        String SAVE_FILE_NAME = fileName+".txt";

        File file = activity.getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        text = TextProcessing.insertNewLineAfterSymbol(text,".");
        text = TextProcessing.insertNewLineAfterSymbol(text,",");

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        if (path != null) {
            // Create a new file in that directory
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(text.getBytes());
                dialogLoading.dismissLoadingProgressDialog();
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

            text = TextProcessing.insertNewLineAfterSymbol(text,".");
            text = TextProcessing.insertNewLineAfterSymbol(text,",");

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

            dialogLoading.dismissLoadingProgressDialog();
            Toast.makeText(activity, "Document saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error saving document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void saveAsHtml(String htmlContent, String fileName) {

        try {

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


    private void saveTextAsPDF(String text, String fileName) {

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

            BaseFont baseFont = BaseFont.createFont("assets/fonts/NotoSansArabic-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12, Font.NORMAL);
            Paragraph caisseInfo;

            if (containsArabicICU(text)){
                Toast.makeText(activity, "ar", Toast.LENGTH_SHORT).show();
                caisseInfo = new Paragraph(processBidirectionalText(text), font);
                caisseInfo.setAlignment(Paragraph.ALIGN_RIGHT);
            }else {
                Toast.makeText(activity, "en", Toast.LENGTH_SHORT).show();
                caisseInfo = new Paragraph(text);
                caisseInfo.setAlignment(Paragraph.ALIGN_LEFT);
            }

            caisseInfo.setPaddingTop(5);
            document.add(caisseInfo);

//            document.add(new Paragraph(processBidirectionalText(text), font).setAlignment(Paragraph.ALIGN_RIGHT));
            document.close();

            dialogLoading.dismissLoadingProgressDialog();
            Toast.makeText(activity, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean containsArabicICU(String input) {
        if (input == null) return false;
        int length = input.length();
        for (int offset = 0; offset < length; ) {
            int codePoint = input.codePointAt(offset);
            int script = UScript.getScript(codePoint);
            if (script == UScript.ARABIC) {
                return true;
            }
            offset += Character.charCount(codePoint);
        }
        return false;
    }

    public static String processBidirectionalText(String input) {
        String shapedInput = shapeArabicText(input);
        Bidi bidi = new Bidi(shapedInput, Bidi.DIRECTION_RIGHT_TO_LEFT);
        return bidi.writeReordered(Bidi.DO_MIRRORING);
    }


    private static String shapeArabicText(String input) {
        try {
            ArabicShaping arabicShaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
            return arabicShaping.shape(input);
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }


}
