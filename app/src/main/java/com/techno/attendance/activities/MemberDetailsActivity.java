package com.techno.attendance.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.models.Member;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import com.techno.attendance.utils.ReportUtils;

public class MemberDetailsActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private Member member;
    private TextView tvMemberId, tvFullName, tvMobileNumber, tvJoiningDate, tvStatus, tvTotalDays, tvPresentDays, tvAbsentDays;
    private Button btnEdit, btnDeactivate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        storageManager = new StorageManager(this);
        String memberId = getIntent().getStringExtra("member_id");
        member = storageManager.getMemberById(memberId);

        if (member == null) {
            Toast.makeText(this, "Member not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        displayMemberDetails();
    }

    private void initializeViews() {
        tvMemberId = findViewById(R.id.tvMemberId);
        tvFullName = findViewById(R.id.tvFullName);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvJoiningDate = findViewById(R.id.tvJoiningDate);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotalDays = findViewById(R.id.tvTotalDays);
        tvPresentDays = findViewById(R.id.tvPresentDays);
        tvAbsentDays = findViewById(R.id.tvAbsentDays);

        btnEdit = findViewById(R.id.btnEdit);
        btnDeactivate = findViewById(R.id.btnDeactivate);
        btnDelete = findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, AddEditMemberActivity.class);
            intent.putExtra("member_id", member.getMemberId());
            startActivity(intent);
        });

        btnDeactivate.setOnClickListener(v -> {
            if (member.isActive()) {
                storageManager.deactivateMember(member.getMemberId());
                btnDeactivate.setText("Reactivate");
                Toast.makeText(this, "Member deactivated", Toast.LENGTH_SHORT).show();
            } else {
                storageManager.reactivateMember(member.getMemberId());
                btnDeactivate.setText("Deactivate");
                Toast.makeText(this, "Member reactivated", Toast.LENGTH_SHORT).show();
            }
            member = storageManager.getMemberById(member.getMemberId());
            displayMemberDetails();
        });

        btnDelete.setOnClickListener(v -> {
            storageManager.deactivateMember(member.getMemberId());
            Toast.makeText(this, "Member deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void displayMemberDetails() {
        tvMemberId.setText("ID: " + member.getMemberId());
        tvFullName.setText("Name: " + member.getFullName());
        tvMobileNumber.setText("Mobile: " + (member.getMobileNumber() != null ? member.getMobileNumber() : "N/A"));
        tvJoiningDate.setText("Joined: " + DateUtils.formatDate(member.getJoiningDate()));
        tvStatus.setText(member.isActive() ? "Status: Active" : "Status: Inactive");
        tvStatus.setTextColor(member.isActive() ? getResources().getColor(R.color.present_color) : getResources().getColor(R.color.absent_color));

        btnDeactivate.setText(member.isActive() ? "Deactivate" : "Reactivate");

        // Current month stats
        long today = DateUtils.getStartOfToday();
        int month = DateUtils.getMonth(today);
        int year = DateUtils.getYear(today);
        ReportUtils.MemberStats stats = ReportUtils.getMemberStats(storageManager, member.getMemberId(), month, year);

        tvTotalDays.setText("Total Days: " + (stats.presentCount + stats.absentCount + stats.leaveCount));
        tvPresentDays.setText("Present: " + stats.presentCount);
        tvAbsentDays.setText("Absent: " + stats.absentCount);
    }
}
