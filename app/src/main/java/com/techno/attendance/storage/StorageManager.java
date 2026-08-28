package com.techno.attendance.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techno.attendance.models.Member;
import com.techno.attendance.models.AttendanceRecord;
import com.techno.attendance.models.Holiday;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String PREFS_NAME = "attendance_prefs";
    private static final String KEY_MEMBERS = "members";
    private static final String KEY_ATTENDANCE = "attendance";
    private static final String KEY_HOLIDAYS = "holidays";
    private static final String KEY_NEXT_MEMBER_ID = "next_member_id";

    private SharedPreferences prefs;
    private Gson gson;

    public StorageManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // Member Management
    public void saveMember(Member member) {
        List<Member> members = getAllMembers();
        members.removeIf(m -> m.getMemberId().equals(member.getMemberId()));
        members.add(member);
        String json = gson.toJson(members);
        prefs.edit().putString(KEY_MEMBERS, json).apply();
    }

    public List<Member> getAllMembers() {
        String json = prefs.getString(KEY_MEMBERS, "[]");
        Type type = new TypeToken<List<Member>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public Member getMemberById(String memberId) {
        List<Member> members = getAllMembers();
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) {
                return m;
            }
        }
        return null;
    }

    public void deactivateMember(String memberId) {
        Member member = getMemberById(memberId);
        if (member != null) {
            member.setActive(false);
            member.setLeavingDate(System.currentTimeMillis());
            saveMember(member);
        }
    }

    public void reactivateMember(String memberId) {
        Member member = getMemberById(memberId);
        if (member != null) {
            member.setActive(true);
            member.setLeavingDate(0);
            saveMember(member);
        }
    }

    // Attendance Management
    public void saveAttendance(AttendanceRecord record) {
        List<AttendanceRecord> records = getAllAttendance();
        records.removeIf(r -> r.getMemberId().equals(record.getMemberId()) && r.getDate() == record.getDate());
        records.add(record);
        String json = gson.toJson(records);
        prefs.edit().putString(KEY_ATTENDANCE, json).apply();
    }

    public List<AttendanceRecord> getAllAttendance() {
        String json = prefs.getString(KEY_ATTENDANCE, "[]");
        Type type = new TypeToken<List<AttendanceRecord>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public AttendanceRecord getAttendance(String memberId, long date) {
        List<AttendanceRecord> records = getAllAttendance();
        for (AttendanceRecord r : records) {
            if (r.getMemberId().equals(memberId) && r.getDate() == date) {
                return r;
            }
        }
        return null;
    }

    public List<AttendanceRecord> getAttendanceForDate(long date) {
        List<AttendanceRecord> result = new ArrayList<>();
        List<AttendanceRecord> records = getAllAttendance();
        for (AttendanceRecord r : records) {
            if (r.getDate() == date) {
                result.add(r);
            }
        }
        return result;
    }

    // Holiday Management
    public void saveHoliday(Holiday holiday) {
        List<Holiday> holidays = getAllHolidays();
        holidays.removeIf(h -> h.getDate() == holiday.getDate());
        holidays.add(holiday);
        String json = gson.toJson(holidays);
        prefs.edit().putString(KEY_HOLIDAYS, json).apply();
    }

    public List<Holiday> getAllHolidays() {
        String json = prefs.getString(KEY_HOLIDAYS, "[]");
        Type type = new TypeToken<List<Holiday>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public boolean isHoliday(long date) {
        List<Holiday> holidays = getAllHolidays();
        for (Holiday h : holidays) {
            if (h.getDate() == date) {
                return true;
            }
        }
        return false;
    }

    public void removeHoliday(long date) {
        List<Holiday> holidays = getAllHolidays();
        holidays.removeIf(h -> h.getDate() == date);
        String json = gson.toJson(holidays);
        prefs.edit().putString(KEY_HOLIDAYS, json).apply();
    }

    // Backup and Restore
    public String createBackup() {
        BackupData data = new BackupData();
        data.members = getAllMembers();
        data.attendance = getAllAttendance();
        data.holidays = getAllHolidays();
        return gson.toJson(data);
    }

    public void restoreBackup(String backupJson) {
        BackupData data = gson.fromJson(backupJson, BackupData.class);
        String membersJson = gson.toJson(data.members);
        String attendanceJson = gson.toJson(data.attendance);
        String holidaysJson = gson.toJson(data.holidays);
        
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MEMBERS, membersJson);
        editor.putString(KEY_ATTENDANCE, attendanceJson);
        editor.putString(KEY_HOLIDAYS, holidaysJson);
        editor.apply();
    }

    public static class BackupData {
        public List<Member> members;
        public List<AttendanceRecord> attendance;
        public List<Holiday> holidays;
    }
}
