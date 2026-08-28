package com.techno.attendance.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.adapters.AbsenteesAdapter;
import com.techno.attendance.models.Member;
import com.techno.attendance.storage.StorageManager;
import com.techno.attendance.utils.DateUtils;
import com.techno.attendance.utils.ReportUtils;
import com.techno.attendance.utils.ShareUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AbsenteesActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private long currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absentees);

        storageManager = new StorageManager(this);
        currentDate = DateUtils.getStartOfToday();

        initializeViews();
    }

    private void initializeViews() {
        TextView tvAbsenteesDate = findViewById(R.id.tvAbsenteesDate);
        TextView tvAbsenteeCount = findViewById(R.id.tvAbsenteeCount);
        ListView lvAbsentees = findViewById(R.id.lvAbsentees);
        Button btnShareWhatsApp = findViewById(R.id.btnShareWhatsApp);

        tvAbsenteesDate.setText(DateUtils.formatDate(currentDate));

        List<Member> absentees = ReportUtils.getAbsenteesForDate(storageManager, currentDate);
        tvAbsenteeCount.setText("Total Absentees: " + absentees.size());

        if (absentees.isEmpty()) {
            tvAbsenteeCount.setText("No absentees today");
        }

        AbsenteesAdapter adapter = new AbsenteesAdapter(this, absentees);
        lvAbsentees.setAdapter(adapter);

        btnShareWhatsApp.setOnClickListener(v -> shareToWhatsApp(absentees));
    }

    private void shareToWhatsApp(List<Member> absentees) {
        if (absentees.isEmpty()) {
            Toast.makeText(this, "No absentees to share", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("📋 Today's Absentees\n");
        message.append("📅 ").append(DateUtils.formatDate(currentDate)).append("\n\n");

        int count = 1;
        for (Member member : absentees) {
            message.append(count).append(". ").append(member.getFullName());
            if (member.getMobileNumber() != null && !member.getMobileNumber().isEmpty()) {
                message.append(" – ").append(member.getMemberId());
            }
            message.append("\n");
            count++;
        }

        message.append("\n❌ Total Absentees: ").append(absentees.size());

        ShareUtils.shareToWhatsApp(this, message.toString());
    }
}
