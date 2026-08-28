package com.techno.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.activities.*;
import com.techno.attendance.models.Member;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import com.techno.attendance.utils.ReportUtils;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private TextView tvDate, tvTotalMembers, tvPresentCount, tvAbsentCount, tvLeaveCount, tvHolidayStatus;
    private Button btnTakeAttendance, btnAbsentees, btnMembers, btnReports, btnBackupRestore, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageManager = new StorageManager(this);
        initializeViews();
        initializeSampleData();
        updateDashboard();
    }

    private void initializeViews() {
        tvDate = findViewById(R.id.tvDate);
        tvTotalMembers = findViewById(R.id.tvTotalMembers);
        tvPresentCount = findViewById(R.id.tvPresentCount);
        tvAbsentCount = findViewById(R.id.tvAbsentCount);
        tvLeaveCount = findViewById(R.id.tvLeaveCount);
        tvHolidayStatus = findViewById(R.id.tvHolidayStatus);

        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);
        btnAbsentees = findViewById(R.id.btnAbsentees);
        btnMembers = findViewById(R.id.btnMembers);
        btnReports = findViewById(R.id.btnReports);
        btnBackupRestore = findViewById(R.id.btnBackupRestore);
        btnSettings = findViewById(R.id.btnSettings);

        btnTakeAttendance.setOnClickListener(v -> startActivity(new Intent(this, AttendanceActivity.class)));
        btnAbsentees.setOnClickListener(v -> startActivity(new Intent(this, AbsenteesActivity.class)));
        btnMembers.setOnClickListener(v -> startActivity(new Intent(this, MembersActivity.class)));
        btnReports.setOnClickListener(v -> startActivity(new Intent(this, MonthlyReportActivity.class)));
        btnBackupRestore.setOnClickListener(v -> startActivity(new Intent(this, BackupRestoreActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void updateDashboard() {
        long today = DateUtils.getStartOfToday();
        tvDate.setText(DateUtils.formatDate(today));

        ReportUtils.AttendanceSummary summary = ReportUtils.getAttendanceSummary(storageManager, today);
        tvTotalMembers.setText("Total: " + summary.totalActiveMembers);
        tvPresentCount.setText("Present: " + summary.presentCount);
        tvAbsentCount.setText("Absent: " + summary.absentCount);
        tvLeaveCount.setText("Leave: " + summary.leaveCount);

        if (storageManager.isHoliday(today)) {
            tvHolidayStatus.setText("Holiday");
            tvHolidayStatus.setTextColor(getResources().getColor(R.color.holiday_color));
        } else {
            tvHolidayStatus.setText("Working Day");
            tvHolidayStatus.setTextColor(getResources().getColor(R.color.text_primary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboard();
    }

    private void initializeSampleData() {
        List<Member> existingMembers = storageManager.getAllMembers();
        if (existingMembers.isEmpty()) {
            String[] names = {"Ravi Kumar", "Suresh Kumar", "Anil Kumar", "Vikram Singh",
                    "Priya Sharma", "Deepak Patel", "Neha Gupta", "Arjun Reddy",
                    "Sanjay Kumar", "Kavya Nair"};
            String[] mobileNumbers = {"9876543210", "9876543211", "9876543212", "9876543213",
                    "9876543214", "9876543215", "9876543216", "9876543217",
                    "9876543218", "9876543219"};

            long today = DateUtils.getStartOfToday();
            for (int i = 0; i < names.length; i++) {
                Member member = new Member(
                        String.format("%03d", i + 1),
                        names[i],
                        mobileNumbers[i],
                        today - (30 * 24 * 60 * 60 * 1000L)
                );
                storageManager.saveMember(member);
            }
        }
    }
}
