package com.techno.attendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.techno.attendance.R;
import com.techno.attendance.activities.AttendanceActivity;
import java.util.List;

public class AttendanceAdapter extends ArrayAdapter<AttendanceActivity.AttendanceItem> {
    private Context context;
    private List<AttendanceActivity.AttendanceItem> items;

    public AttendanceAdapter(Context context, List<AttendanceActivity.AttendanceItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        }

        AttendanceActivity.AttendanceItem item = items.get(position);
        TextView tvMemberId = convertView.findViewById(R.id.tvMemberId);
        TextView tvMemberName = convertView.findViewById(R.id.tvMemberName);
        Spinner spinnerStatus = convertView.findViewById(R.id.spinnerStatus);

        tvMemberId.setText(item.member.getMemberId());
        tvMemberName.setText(item.member.getFullName());

        String[] statuses = {"PRESENT", "ABSENT", "LEAVE"};
        android.widget.ArrayAdapter<String> spinnerAdapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_spinner_item, statuses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);

        int selectedPosition = 0;
        if ("ABSENT".equals(item.status)) selectedPosition = 1;
        else if ("LEAVE".equals(item.status)) selectedPosition = 2;
        spinnerStatus.setSelection(selectedPosition);

        spinnerStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                item.status = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        return convertView;
    }
}
