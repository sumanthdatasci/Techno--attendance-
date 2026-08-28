package com.techno.attendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.techno.attendance.R;
import com.techno.attendance.models.Member;
import java.util.List;

public class AbsenteesAdapter extends ArrayAdapter<Member> {
    private Context context;
    private List<Member> absentees;

    public AbsenteesAdapter(Context context, List<Member> absentees) {
        super(context, 0, absentees);
        this.context = context;
        this.absentees = absentees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_absentee, parent, false);
        }

        Member member = absentees.get(position);
        TextView tvMemberId = convertView.findViewById(R.id.tvMemberId);
        TextView tvFullName = convertView.findViewById(R.id.tvFullName);
        TextView tvMobileNumber = convertView.findViewById(R.id.tvMobileNumber);

        tvMemberId.setText(member.getMemberId());
        tvFullName.setText(member.getFullName());
        if (member.getMobileNumber() != null && !member.getMobileNumber().isEmpty()) {
            tvMobileNumber.setText(member.getMobileNumber());
        } else {
            tvMobileNumber.setText("N/A");
        }

        return convertView;
    }
}
