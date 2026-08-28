package com.techno.attendance.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.models.Member;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import java.util.Calendar;

public class AddEditMemberActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private EditText etMemberId, etFullName, etMobileNumber, etJoiningDate, etLeavingDate;
    private Button btnSave, btnCancel;
    private Member member;
    private long joiningDate, leavingDate;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_member);

        storageManager = new StorageManager(this);
        isEdit = getIntent().hasExtra("member_id");

        if (isEdit) {
            String memberId = getIntent().getStringExtra("member_id");
            member = storageManager.getMemberById(memberId);
        }

        initializeViews();
        if (isEdit && member != null) {
            populateFields();
        }
    }

    private void initializeViews() {
        etMemberId = findViewById(R.id.etMemberId);
        etFullName = findViewById(R.id.etFullName);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etJoiningDate = findViewById(R.id.etJoiningDate);
        etLeavingDate = findViewById(R.id.etLeavingDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        if (isEdit) {
            etMemberId.setEnabled(false);
        }

        etJoiningDate.setOnClickListener(v -> showDatePicker(true));
        etLeavingDate.setOnClickListener(v -> showDatePicker(false));

        btnSave.setOnClickListener(v -> saveMember());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void populateFields() {
        etMemberId.setText(member.getMemberId());
        etFullName.setText(member.getFullName());
        etMobileNumber.setText(member.getMobileNumber());
        etJoiningDate.setText(DateUtils.formatDate(member.getJoiningDate()));
        joiningDate = member.getJoiningDate();
        if (member.getLeavingDate() > 0) {
            etLeavingDate.setText(DateUtils.formatDate(member.getLeavingDate()));
            leavingDate = member.getLeavingDate();
        }
    }

    private void saveMember() {
        String memberId = etMemberId.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();

        if (memberId.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Member ID and Full Name are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (joiningDate == 0) {
            Toast.makeText(this, "Please select joining date", Toast.LENGTH_SHORT).show();
            return;
        }

        Member newMember = new Member(memberId, fullName, mobileNumber, joiningDate);
        if (leavingDate > 0) {
            newMember.setLeavingDate(leavingDate);
        }
        if (isEdit && member != null) {
            newMember.setActive(member.isActive());
        }

        storageManager.saveMember(newMember);
        Toast.makeText(this, "Member saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDatePicker(boolean isJoiningDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    long selectedDate = DateUtils.createDate(year, month, dayOfMonth);
                    if (isJoiningDate) {
                        joiningDate = selectedDate;
                        etJoiningDate.setText(DateUtils.formatDate(selectedDate));
                    } else {
                        leavingDate = selectedDate;
                        etLeavingDate.setText(DateUtils.formatDate(selectedDate));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }
}
