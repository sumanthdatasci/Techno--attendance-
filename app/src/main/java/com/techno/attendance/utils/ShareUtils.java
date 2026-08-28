package com.techno.attendance.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShareUtils {
    public static void shareToWhatsApp(Context context, String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // WhatsApp not installed, open generic share
            Intent genericShare = new Intent();
            genericShare.setAction(Intent.ACTION_SEND);
            genericShare.putExtra(Intent.EXTRA_TEXT, message);
            genericShare.setType("text/plain");
            context.startActivity(Intent.createChooser(genericShare, "Share Absentees"));
        }
    }

    public static void shareBackupFile(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.setType("application/json");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share Backup"));
    }
}
