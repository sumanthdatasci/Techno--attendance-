package com.techno.attendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.techno.attendance.R;
import com.techno.attendance.models.Holiday;
import com.techno.attendance.utils.DateUtils;
import java.util.List;

public class HolidayAdapter extends ArrayAdapter<Holiday> {
    private Context context;
    private List<Holiday> holidays;

    public HolidayAdapter(Context context, List<Holiday> holidays) {
        super(context, 0, holidays);
        this.context = context;
        this.holidays = holidays;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_holiday, parent, false);
        }

        Holiday holiday = holidays.get(position);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvReason = convertView.findViewById(R.id.tvReason);

        tvDate.setText(DateUtils.formatDate(holiday.getDate()));
        tvReason.setText(holiday.getReason());

        return convertView;
    }
}
