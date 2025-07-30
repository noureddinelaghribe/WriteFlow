package com.noureddine.WriteFlow.Utils;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilesManager {

    private static final String TAG = "FilesManager";
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB in bytes

    private final Context context;
    private final AppCompatActivity activity;
    private FileSelectionListener listener;

    // Supported file types
    private static final String[] SUPPORTED_MIME_TYPES = {
            "application/pdf",           // PDF
            "application/msword",        // DOC
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // DOCX
            "text/plain",                // TXT
            "image/jpeg",                // JPG
            "image/jpg",                 // JPG
            "image/png"                  // PNG
    };

    private static final String[] SUPPORTED_EXTENSIONS = {
            "pdf", "doc", "docx", "txt", "jpg", "jpeg", "png"
    };

    // Activity result launchers
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    // Constructor
    public FilesManager(AppCompatActivity activity) {
        this.context = activity;
        this.activity = activity;
        //initializeLaunchers();
    }

    // Interface for file selection callbacks
    public interface FileSelectionListener {
        void onFileSelected(FileInfo fileInfo);
        void onFileSelectionError(String error);
        void onFileSelectionCancelled();
        void onPermissionDenied();
    }

    // File information holder class
    public static class FileInfo {
        private String fileName;
        private String mimeType;
        private long size;
        private Uri uri;
        private String extension;
        private FileType fileType;

        // Getters
        public String getFileName() { return fileName; }
        public String getMimeType() { return mimeType; }
        public long getSize() { return size; }
        public Uri getUri() { return uri; }
        public String getExtension() { return extension; }
        public FileType getFileType() { return fileType; }

        // Setters
        public void setFileName(String fileName) { this.fileName = fileName; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }
        public void setSize(long size) { this.size = size; }
        public void setUri(Uri uri) { this.uri = uri; }
        public void setExtension(String extension) { this.extension = extension; }
        public void setFileType(FileType fileType) { this.fileType = fileType; }

        public String getFormattedSize() {
            return FilesManager.formatFileSize(size);
        }

        @Override
        public String toString() {
            return String.format(
                    "FileInfo{name='%s', type='%s', size=%s, extension='%s'}",
                    fileName, mimeType, getFormattedSize(), extension
            );
        }
    }

    // File types enum
    public enum FileType {
        PDF, DOC, DOCX, TXT, JPG, PNG, UNKNOWN
    }

//     Initialize activity result launchers
    private void initializeLaunchers() {
        // File picker launcher
        filePickerLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedFileUri = result.getData().getData();
                        if (selectedFileUri != null) {
                            handleSelectedFile(selectedFileUri);
                        } else {
                            notifyError("No file was selected");
                        }
                    } else {
                        if (listener != null) {
                            listener.onFileSelectionCancelled();
                        }
                    }
                }
        );

        // Permission launcher
        permissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openFilePicker();
                    } else {
                        if (listener != null) {
                            listener.onPermissionDenied();
                        }
                        showToast("Storage permission is required to select files");
                    }
                }
        );
    }

    public void setLaunchers(ActivityResultLauncher<Intent> fileLauncher, ActivityResultLauncher<String> permissionLauncher){
        filePickerLauncher = fileLauncher;
        this.permissionLauncher = permissionLauncher;
    }

    // Set file selection listener
    public void setFileSelectionListener(FileSelectionListener listener) {
        this.listener = listener;
    }

    // Main method to select a file
    public void selectFile() {
        checkPermissionAndOpenFilePicker();
    }

    // Select multiple files
    public void selectMultipleFiles() {
        checkPermissionAndOpenMultipleFilePicker();
    }

    // Check permission and open file picker
    private void checkPermissionAndOpenFilePicker() {
        if (hasStoragePermission()) {
            openFilePicker();
        } else {
            requestStoragePermission();
        }
    }

    // Check permission and open multiple file picker
    private void checkPermissionAndOpenMultipleFilePicker() {
        if (hasStoragePermission()) {
            openMultipleFilePicker();
        } else {
            requestStoragePermission();
        }
    }

    // Check if storage permission is granted
    private boolean hasStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+, we don't need READ_EXTERNAL_STORAGE permission for SAF
            return true;
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Request storage permission
    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            openFilePicker();
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    // Open single file picker
    public void openFilePicker() {
        try {
            Intent intent = createFilePickerIntent(false);
            filePickerLauncher.launch(Intent.createChooser(intent, "Select File"));
        } catch (Exception e) {
            notifyError("Error opening file picker: " + e.getMessage());
            Log.e(TAG, "Error opening file picker", e);
        }
    }

    // Open multiple file picker
    private void openMultipleFilePicker() {
        try {
            Intent intent = createFilePickerIntent(true);
            filePickerLauncher.launch(Intent.createChooser(intent, "Select Files"));
        } catch (Exception e) {
            notifyError("Error opening file picker: " + e.getMessage());
            Log.e(TAG, "Error opening file picker", e);
        }
    }

    // Create file picker intent
    private Intent createFilePickerIntent(boolean multiple) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, SUPPORTED_MIME_TYPES);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (multiple) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        return intent;
    }

    // Handle selected file
    public void handleSelectedFile(Uri fileUri) {
        try {
            FileInfo fileInfo = extractFileInfo(fileUri);

            if (!isValidFile(fileInfo)) {
                return; // Error already notified in isValidFile()
            }

            if (listener != null) {
                listener.onFileSelected(fileInfo);
            }

            Log.d(TAG, "File selected successfully: " + fileInfo.toString());

        } catch (Exception e) {
            notifyError("Error processing selected file: " + e.getMessage());
            Log.e(TAG, "Error handling selected file", e);
        }
    }

    // Extract file information from URI
    public FileInfo extractFileInfo(Uri uri) {
        FileInfo info = new FileInfo();
        info.setUri(uri);

        // Get file details from content resolver
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // Get file name
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    info.setFileName(cursor.getString(nameIndex));
                }

                // Get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (sizeIndex >= 0) {
                    info.setSize(cursor.getLong(sizeIndex));
                }
            }
        }

        // Get MIME type
        info.setMimeType(context.getContentResolver().getType(uri));

        // Extract extension and determine file type
        if (info.getFileName() != null) {
            info.setExtension(extractFileExtension(info.getFileName()));
            info.setFileType(determineFileType(info.getMimeType(), info.getExtension()));
        }

        // If size is 0 or -1, try to get it from input stream
        if (info.getSize() <= 0) {
            try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
                if (inputStream != null) {
                    info.setSize(inputStream.available());
                }
            } catch (IOException e) {
                Log.w(TAG, "Could not determine file size", e);
            }
        }

        return info;
    }

    // Validate selected file
    private boolean isValidFile(FileInfo fileInfo) {
        // Check file type
        if (!isSupportedFileType(fileInfo.getMimeType(), fileInfo.getExtension())) {
            notifyError("Unsupported file format. Please select PDF, DOC, DOCX, TXT, JPG, or PNG files only.");
            return false;
        }

        // Check file size
        if (!isValidFileSize(fileInfo.getSize())) {
            notifyError("File size exceeds 50MB limit. Current size: " + fileInfo.getFormattedSize());
            return false;
        }

        return true;
    }

    // Check if file type is supported
    public boolean isSupportedFileType(String mimeType, String extension) {
        // Check MIME type
        if (mimeType != null) {
            for (String supportedType : SUPPORTED_MIME_TYPES) {
                if (supportedType.equals(mimeType)) {
                    return true;
                }
            }
        }

        // Fallback: Check file extension
        if (extension != null) {
            String lowerExtension = extension.toLowerCase();
            for (String supportedExt : SUPPORTED_EXTENSIONS) {
                if (supportedExt.equals(lowerExtension)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Check if file size is valid
    public boolean isValidFileSize(long size) {
        return size > 0 && size <= MAX_FILE_SIZE;
    }

    // Extract file extension
    private String extractFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    // Determine file type
    private FileType determineFileType(String mimeType, String extension) {
        // First try MIME type
        if (mimeType != null) {
            switch (mimeType) {
                case "application/pdf":
                    return FileType.PDF;
                case "application/msword":
                    return FileType.DOC;
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    return FileType.DOCX;
                case "text/plain":
                    return FileType.TXT;
                case "image/jpeg":
                case "image/jpg":
                    return FileType.JPG;
                case "image/png":
                    return FileType.PNG;
            }
        }

        // Fallback to extension
        if (extension != null) {
            switch (extension.toLowerCase()) {
                case "pdf": return FileType.PDF;
                case "doc": return FileType.DOC;
                case "docx": return FileType.DOCX;
                case "txt": return FileType.TXT;
                case "jpg":
                case "jpeg": return FileType.JPG;
                case "png": return FileType.PNG;
            }
        }

        return FileType.UNKNOWN;
    }

    // Read file content as bytes
    public byte[] readFileContent(Uri uri) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            if (inputStream == null) {
                throw new IOException("Cannot open file input stream");
            }

            List<Byte> bytes = new ArrayList<>();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    bytes.add(buffer[i]);
                }
            }

            // Convert to byte array
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                result[i] = bytes.get(i);
            }

            return result;
        }
    }

    // Read text file content
    public String readTextFileContent(Uri uri) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            if (inputStream == null) {
                throw new IOException("Cannot open file input stream");
            }

            StringBuilder content = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                content.append(new String(buffer, 0, bytesRead));
            }

            return content.toString();
        }
    }

    // Get input stream for file
    public InputStream getFileInputStream(Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Cannot open file input stream");
        }
        return inputStream;
    }

    // Format file size for display
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    // Get supported file types
    public String[] getSupportedMimeTypes() {
        return Arrays.copyOf(SUPPORTED_MIME_TYPES, SUPPORTED_MIME_TYPES.length);
    }

    // Get supported extensions
    public String[] getSupportedExtensions() {
        return Arrays.copyOf(SUPPORTED_EXTENSIONS, SUPPORTED_EXTENSIONS.length);
    }

    // Get max file size
    public long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    // Utility methods
    public void notifyError(String message) {
        if (listener != null) {
            listener.onFileSelectionError(message);
        }
        showToast(message);
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
