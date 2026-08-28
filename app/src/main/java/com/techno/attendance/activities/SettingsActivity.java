package com.techno.attendance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();
    }

    private void initializeViews() {
        Button btnHolidayManagement = findViewById(R.id.btnHolidayManagement);
        Button btnAbout = findViewById(R.id.btnAbout);

        btnHolidayManagement.setOnClickListener(v -> 
            startActivity(new Intent(this, HolidayManagementActivity.class))
        );

        btnAbout.setOnClickListener(v -> 
            startActivity(new Intent(this, AboutActivity.class))
        );
    }
}
