package com.techno.attendance.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.adapters.HolidayAdapter;
import com.techno.attendance.models.Holiday;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HolidayManagementActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private EditText etHolidayDate, etReason;
    private ListView lvHolidays;
    private HolidayAdapter adapter;
    private List<Holiday> holidays;
    private long selectedDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_management);

        storageManager = new StorageManager(this);
        holidays = new ArrayList<>();

        initializeViews();
        loadHolidays();
    }

    private void initializeViews() {
        etHolidayDate = findViewById(R.id.etHolidayDate);
        etReason = findViewById(R.id.etReason);
        lvHolidays = findViewById(R.id.lvHolidays);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnAddHoliday = findViewById(R.id.btnAddHoliday);

        adapter = new HolidayAdapter(this, holidays);
        lvHolidays.setAdapter(adapter);

        lvHolidays.setOnItemLongClickListener((parent, view, position, id) -> {
            Holiday holiday = holidays.get(position);
            storageManager.removeHoliday(holiday.getDate());
            loadHolidays();
            Toast.makeText(this, "Holiday removed", Toast.LENGTH_SHORT).show();
            return true;
        });

        etHolidayDate.setOnClickListener(v -> showDatePicker());
        btnPickDate.setOnClickListener(v -> showDatePicker());

        btnAddHoliday.setOnClickListener(v -> addHoliday());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = DateUtils.createDate(year, month, dayOfMonth);
                    etHolidayDate.setText(DateUtils.formatDate(selectedDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void addHoliday() {
        if (selectedDate == 0) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        String reason = etReason.getText().toString().trim();
        if (reason.isEmpty()) {
            reason = "Holiday";
        }

        Holiday holiday = new Holiday(selectedDate, reason);
        storageManager.saveHoliday(holiday);
        Toast.makeText(this, "Holiday added", Toast.LENGTH_SHORT).show();

        etHolidayDate.setText("");
        etReason.setText("");
        selectedDate = 0;
        loadHolidays();
    }

    private void loadHolidays() {
        holidays.clear();
        holidays.addAll(storageManager.getAllHolidays());
        adapter.notifyDataSetChanged();
    }
}
