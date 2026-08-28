package com.techno.attendance.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.adapters.MonthlyReportAdapter;
import com.techno.attendance.models.Member;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import com.techno.attendance.utils.ReportUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthlyReportActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private Spinner spinnerMonth, spinnerYear;
    private ListView lvReport;
    private int selectedMonth, selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        storageManager = new StorageManager(this);
        long today = DateUtils.getStartOfToday();
        selectedMonth = DateUtils.getMonth(today);
        selectedYear = DateUtils.getYear(today);

        initializeViews();
        generateReport();
    }

    private void initializeViews() {
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        lvReport = findViewById(R.id.lvReport);
        Button btnGenerateReport = findViewById(R.id.btnGenerateReport);

        // Setup month spinner
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        android.widget.ArrayAdapter<String> monthAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setSelection(selectedMonth);

        // Setup year spinner
        List<String> years = new ArrayList<>();
        int currentYear = selectedYear;
        for (int i = 0; i < 5; i++) {
            years.add(String.valueOf(currentYear - i));
        }
        android.widget.ArrayAdapter<String> yearAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        btnGenerateReport.setOnClickListener(v -> {
            selectedMonth = spinnerMonth.getSelectedItemPosition();
            selectedYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());
            generateReport();
        });
    }

    private void generateReport() {
        List<ReportUtils.MemberStats> reportData = new ArrayList<>();
        List<Member> members = storageManager.getAllMembers();

        for (Member member : members) {
            if (member.isActive()) {
                ReportUtils.MemberStats stats = ReportUtils.getMemberStats(storageManager, member.getMemberId(), selectedMonth, selectedYear);
                reportData.add(stats);
            }
        }

        MonthlyReportAdapter adapter = new MonthlyReportAdapter(this, reportData);
        lvReport.setAdapter(adapter);
    }
}
