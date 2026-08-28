package com.techno.attendance.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import com.techno.attendance.R;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.ShareUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BackupRestoreActivity extends AppCompatActivity {
    private static final int PICK_BACKUP_FILE = 1;
    private StorageManager storageManager;
    private EditText etBackupPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);

        storageManager = new StorageManager(this);
        initializeViews();
    }

    private void initializeViews() {
        etBackupPath = findViewById(R.id.etBackupPath);
        Button btnCreateBackup = findViewById(R.id.btnCreateBackup);
        Button btnShareBackup = findViewById(R.id.btnShareBackup);
        Button btnRestoreBackup = findViewById(R.id.btnRestoreBackup);
        Button btnPickBackupFile = findViewById(R.id.btnPickBackupFile);

        btnCreateBackup.setOnClickListener(v -> createBackup());
        btnShareBackup.setOnClickListener(v -> shareBackup());
        btnPickBackupFile.setOnClickListener(v -> pickBackupFile());
        btnRestoreBackup.setOnClickListener(v -> restoreBackup());
    }

    private void createBackup() {
        try {
            String backupJson = storageManager.createBackup();
            File backupDir = new File(getExternalFilesDir(null), "backups");
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            String backupFileName = "attendance_backup_" + System.currentTimeMillis() + ".json";
            File backupFile = new File(backupDir, backupFileName);

            FileWriter writer = new FileWriter(backupFile);
            writer.write(backupJson);
            writer.close();

            etBackupPath.setText(backupFile.getAbsolutePath());
            Toast.makeText(this, "Backup created: " + backupFileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error creating backup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareBackup() {
        String backupPath = etBackupPath.getText().toString().trim();
        if (backupPath.isEmpty()) {
            Toast.makeText(this, "No backup file selected", Toast.LENGTH_SHORT).show();
            return;
        }

        File backupFile = new File(backupPath);
        if (!backupFile.exists()) {
            Toast.makeText(this, "Backup file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        ShareUtils.shareBackupFile(this, backupFile);
    }

    private void pickBackupFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        startActivityForResult(intent, PICK_BACKUP_FILE);
    }

    private void restoreBackup() {
        String backupPath = etBackupPath.getText().toString().trim();
        if (backupPath.isEmpty()) {
            Toast.makeText(this, "No backup file selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String backupJson = new String(java.nio.file.Files.readAllBytes(new File(backupPath).toPath()));
            storageManager.restoreBackup(backupJson);
            Toast.makeText(this, "Backup restored successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error restoring backup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BACKUP_FILE && resultCode == RESULT_OK) {
            android.net.Uri uri = data.getData();
            etBackupPath.setText(uri.getPath());
        }
    }
}
