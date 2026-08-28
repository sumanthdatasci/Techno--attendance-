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

public class MembersAdapter extends ArrayAdapter<Member> {
    private Context context;
    private List<Member> members;

    public MembersAdapter(Context context, List<Member> members) {
        super(context, 0, members);
        this.context = context;
        this.members = members;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        }

        Member member = members.get(position);
        TextView tvMemberId = convertView.findViewById(R.id.tvMemberId);
        TextView tvFullName = convertView.findViewById(R.id.tvFullName);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvMemberId.setText(member.getMemberId());
        tvFullName.setText(member.getFullName());
        tvStatus.setText(member.isActive() ? "Active" : "Inactive");
        tvStatus.setTextColor(member.isActive() ? context.getResources().getColor(R.color.present_color) : context.getResources().getColor(R.color.absent_color));

        return convertView;
    }
}
