package com.techno.attendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.techno.attendance.R;
import com.techno.attendance.utils.ReportUtils;
import java.util.List;

public class MonthlyReportAdapter extends ArrayAdapter<ReportUtils.MemberStats> {
    private Context context;
    private List<ReportUtils.MemberStats> stats;

    public MonthlyReportAdapter(Context context, List<ReportUtils.MemberStats> stats) {
        super(context, 0, stats);
        this.context = context;
        this.stats = stats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_monthly_report, parent, false);
        }

        ReportUtils.MemberStats item = stats.get(position);
        TextView tvMemberId = convertView.findViewById(R.id.tvMemberId);
        TextView tvMemberName = convertView.findViewById(R.id.tvMemberName);
        TextView tvPresent = convertView.findViewById(R.id.tvPresent);
        TextView tvAbsent = convertView.findViewById(R.id.tvAbsent);
        TextView tvLeave = convertView.findViewById(R.id.tvLeave);
        TextView tvAttendancePercentage = convertView.findViewById(R.id.tvAttendancePercentage);

        tvMemberId.setText(item.memberId);
        tvMemberName.setText(item.memberName);
        tvPresent.setText("Present: " + item.presentCount);
        tvAbsent.setText("Absent: " + item.absentCount);
        tvLeave.setText("Leave: " + item.leaveCount);

        int total = item.presentCount + item.absentCount + item.leaveCount;
        float percentage = total > 0 ? (float) item.presentCount * 100 / total : 0;
        tvAttendancePercentage.setText(String.format("%.1f%%", percentage));

        return convertView;
    }
}
