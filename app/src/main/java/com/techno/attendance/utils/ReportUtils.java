package com.techno.attendance.utils;

import com.techno.attendance.models.Member;
import com.techno.attendance.models.AttendanceRecord;
import com.techno.attendance.storage.StorageManager;
import java.util.List;
import java.util.ArrayList;

public class ReportUtils {
    public static class AttendanceSummary {
        public int totalActiveMembers;
        public int presentCount;
        public int absentCount;
        public int leaveCount;
        public boolean isHoliday;

        public AttendanceSummary() {
            this.totalActiveMembers = 0;
            this.presentCount = 0;
            this.absentCount = 0;
            this.leaveCount = 0;
            this.isHoliday = false;
        }
    }

    public static AttendanceSummary getAttendanceSummary(StorageManager storage, long date) {
        AttendanceSummary summary = new AttendanceSummary();

        List<Member> members = storage.getAllMembers();
        List<AttendanceRecord> records = storage.getAttendanceForDate(date);

        // Count active members for this date
        for (Member member : members) {
            if (member.isActive() && isMemberActiveOnDate(member, date)) {
                summary.totalActiveMembers++;
            }
        }

        // Count attendance statuses
        for (AttendanceRecord record : records) {
            if ("PRESENT".equals(record.getStatus())) {
                summary.presentCount++;
            } else if ("ABSENT".equals(record.getStatus())) {
                summary.absentCount++;
            } else if ("LEAVE".equals(record.getStatus())) {
                summary.leaveCount++;
            }
        }

        // If no records for any active member, assume all present
        if (records.isEmpty() && summary.totalActiveMembers > 0) {
            summary.presentCount = summary.totalActiveMembers;
        }

        return summary;
    }

    public static List<Member> getAbsenteesForDate(StorageManager storage, long date) {
        List<Member> absentees = new ArrayList<>();
        List<AttendanceRecord> records = storage.getAttendanceForDate(date);

        for (AttendanceRecord record : records) {
            if ("ABSENT".equals(record.getStatus())) {
                Member member = storage.getMemberById(record.getMemberId());
                if (member != null) {
                    absentees.add(member);
                }
            }
        }

        return absentees;
    }

    public static class MonthlyStats {
        public int totalActiveMembers;
        public int attendanceDays;
        public int totalPresent;
        public int totalAbsent;
        public int totalLeave;
        public int holidayCount;
        public double attendancePercentage;
    }

    public static MonthlyStats getMonthlyStats(StorageManager storage, int month, int year) {
        MonthlyStats stats = new MonthlyStats();
        
        long startOfMonth = DateUtils.createDate(year, month, 1);
        long endOfMonth = DateUtils.getEndOfMonth(startOfMonth);

        List<Member> members = storage.getAllMembers();
        List<AttendanceRecord> records = storage.getAllAttendance();
        List<Long> attendanceDays = new ArrayList<>();

        // Count active members
        for (Member member : members) {
            if (member.isActive()) {
                stats.totalActiveMembers++;
            }
        }

        // Count attendance stats
        for (AttendanceRecord record : records) {
            if (record.getDate() >= startOfMonth && record.getDate() <= endOfMonth) {
                if (!attendanceDays.contains(record.getDate())) {
                    attendanceDays.add(record.getDate());
                }

                if ("PRESENT".equals(record.getStatus())) {
                    stats.totalPresent++;
                } else if ("ABSENT".equals(record.getStatus())) {
                    stats.totalAbsent++;
                } else if ("LEAVE".equals(record.getStatus())) {
                    stats.totalLeave++;
                }
            }
        }

        stats.attendanceDays = attendanceDays.size();

        // Calculate attendance percentage
        if (stats.totalActiveMembers > 0 && stats.attendanceDays > 0) {
            int expected = stats.totalActiveMembers * stats.attendanceDays;
            stats.attendancePercentage = (double) stats.totalPresent / expected * 100;
        }

        return stats;
    }

    public static class MemberStats {
        public String memberId;
        public String fullName;
        public int presentCount;
        public int absentCount;
        public int leaveCount;
        public double attendancePercentage;
    }

    public static MemberStats getMemberStats(StorageManager storage, String memberId, int month, int year) {
        Member member = storage.getMemberById(memberId);
        MemberStats stats = new MemberStats();
        
        if (member == null) {
            return stats;
        }

        stats.memberId = memberId;
        stats.fullName = member.getFullName();

        long startOfMonth = DateUtils.createDate(year, month, 1);
        long endOfMonth = DateUtils.getEndOfMonth(startOfMonth);

        List<AttendanceRecord> records = storage.getAllAttendance();
        int daysInMonth = 0;

        for (AttendanceRecord record : records) {
            if (record.getMemberId().equals(memberId) && 
                record.getDate() >= startOfMonth && record.getDate() <= endOfMonth) {
                daysInMonth++;
                if ("PRESENT".equals(record.getStatus())) {
                    stats.presentCount++;
                } else if ("ABSENT".equals(record.getStatus())) {
                    stats.absentCount++;
                } else if ("LEAVE".equals(record.getStatus())) {
                    stats.leaveCount++;
                }
            }
        }

        if (daysInMonth > 0) {
            stats.attendancePercentage = (double) stats.presentCount / daysInMonth * 100;
        }

        return stats;
    }

    private static boolean isMemberActiveOnDate(Member member, long date) {
        if (!member.isActive()) {
            return false;
        }
        if (member.getJoiningDate() > date) {
            return false;
        }
        if (member.getLeavingDate() > 0 && member.getLeavingDate() < date) {
            return false;
        }
        return true;
    }
}
