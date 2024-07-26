package org.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ScheduleUtils 클래스는 수업 시간표의 충돌을 확인하고, 시간표를 분석하는 유틸리티 메서드를 제공합니다.
 */
public class ScheduleUtils {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     * 두 시간표 간에 시간이 겹치는지 확인합니다.
     *
     * @param schedule1 첫 번째 시간표 문자열 (예: "Mon 09:00-10:00, Wed 13:00-14:00")
     * @param schedule2 두 번째 시간표 문자열
     * @return 시간이 겹치면 true, 그렇지 않으면 false
     */
    public static boolean isTimeConflict(String schedule1, String schedule2) {
        List<TimeSlot> slots1 = parseSchedule(schedule1);
        List<TimeSlot> slots2 = parseSchedule(schedule2);

        for (TimeSlot slot1 : slots1) {
            for (TimeSlot slot2 : slots2) {
                if (slot1.conflictsWith(slot2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 시간표 문자열을 TimeSlot 객체 목록으로 변환합니다.
     *
     * @param schedule 시간표 문자열 (예: "Mon 09:00-10:00, Wed 13:00-14:00")
     * @return TimeSlot 객체 목록
     */
    private static List<TimeSlot> parseSchedule(String schedule) {
        List<TimeSlot> slots = new ArrayList<>();
        String[] daysAndTimes = schedule.split(", ");
        for (String dayAndTime : daysAndTimes) {
            String[] parts = dayAndTime.split(" ");
            String day = parts[0];
            String[] times = parts[1].split("-");
            try {
                Date startTime = TIME_FORMAT.parse(times[0]);
                Date endTime = TIME_FORMAT.parse(times[1]);
                slots.add(new TimeSlot(day, startTime, endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return slots;
    }

    /**
     * TimeSlot 클래스는 하루 중 특정 시간 범위를 나타냅니다.
     */
    static class TimeSlot {
        String day;
        Date startTime;
        Date endTime;

        TimeSlot(String day, Date startTime, Date endTime) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        /**
         * 두 TimeSlot 객체 간에 시간이 겹치는지 확인합니다.
         *
         * @param other 다른 TimeSlot 객체
         * @return 시간이 겹치면 true, 그렇지 않으면 false
         */
        boolean conflictsWith(TimeSlot other) {
            return this.day.equals(other.day) &&
                    this.startTime.before(other.endTime) &&
                    this.endTime.after(other.startTime);
        }
    }
}