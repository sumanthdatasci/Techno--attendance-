package com.techno.attendance.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.adapters.AttendanceAdapter;
import com.techno.attendance.models.Member;
import com.techno.attendance.models.AttendanceRecord;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private long currentDate;
    private List<AttendanceItem> attendanceItems;
    private AttendanceAdapter adapter;
    private TextView tvAttendanceDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        storageManager = new StorageManager(this);
        currentDate = DateUtils.getStartOfToday();
        attendanceItems = new ArrayList<>();

        initializeViews();
        loadAttendance();
    }

    private void initializeViews() {
        tvAttendanceDate = findViewById(R.id.tvAttendanceDate);
        Button btnPreviousDate = findViewById(R.id.btnPreviousDate);
        Button btnNextDate = findViewById(R.id.btnNextDate);
        Button btnToday = findViewById(R.id.btnToday);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnMarkAllPresent = findViewById(R.id.btnMarkAllPresent);
        Button btnSaveAttendance = findViewById(R.id.btnSaveAttendance);
        ListView lvAttendance = findViewById(R.id.lvAttendance);

        adapter = new AttendanceAdapter(this, attendanceItems);
        lvAttendance.setAdapter(adapter);

        btnPreviousDate.setOnClickListener(v -> {
            currentDate = DateUtils.getPreviousDay(currentDate);
            loadAttendance();
        });

        btnNextDate.setOnClickListener(v -> {
            currentDate = DateUtils.getNextDay(currentDate);
            loadAttendance();
        });

        btnToday.setOnClickListener(v -> {
            currentDate = DateUtils.getStartOfToday();
            loadAttendance();
        });

        btnPickDate.setOnClickListener(v -> showDatePicker());

        btnMarkAllPresent.setOnClickListener(v -> {
            for (AttendanceItem item : attendanceItems) {
                item.status = "PRESENT";
            }
            adapter.notifyDataSetChanged();
        });

        btnSaveAttendance.setOnClickListener(v -> saveAttendance());
    }

    private void loadAttendance() {
        tvAttendanceDate.setText(DateUtils.formatDate(currentDate));
        attendanceItems.clear();

        List<Member> members = storageManager.getAllMembers();
        List<AttendanceRecord> records = storageManager.getAttendanceForDate(currentDate);

        for (Member member : members) {
            if (isMemberActiveOnDate(member, currentDate)) {
                AttendanceItem item = new AttendanceItem();
                item.member = member;

                AttendanceRecord record = storageManager.getAttendance(member.getMemberId(), currentDate);
                item.status = record != null ? record.getStatus() : "PRESENT";

                attendanceItems.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void saveAttendance() {
        for (AttendanceItem item : attendanceItems) {
            AttendanceRecord record = new AttendanceRecord(
                    item.member.getMemberId(),
                    currentDate,
                    item.status
            );
            storageManager.saveAttendance(record);
        }
        Toast.makeText(this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    currentDate = DateUtils.createDate(year, month, dayOfMonth);
                    loadAttendance();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private boolean isMemberActiveOnDate(Member member, long date) {
        if (!member.isActive()) return false;
        if (member.getJoiningDate() > date) return false;
        if (member.getLeavingDate() > 0 && member.getLeavingDate() < date) return false;
        return true;
    }

    public static class AttendanceItem {
        public Member member;
        public String status; // PRESENT, ABSENT, LEAVE
    }
}
