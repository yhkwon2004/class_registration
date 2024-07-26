package org.example.service;

import org.example.util.DatabaseUtil;
import org.example.util.ScheduleUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * EnrollmentService 클래스는 학생의 수강신청, 취소 및 수강 과목 조회 등의 기능을 제공합니다.
 *
 * 필드:
 * - isEnrollmentInProgress: 수강신청 진행 중 여부를 나타내는 boolean 값
 *
 * 메소드:
 * - startEnrollment(String studentId): 수강신청 시작
 * - endEnrollment(): 수강신청 종료
 * - enrollStudent(String studentId, String courseId, int semester): 수강신청
 * - isCourseEnrolled(String studentId, String courseId): 수강신청 여부 확인
 * - isScheduleConflict(String studentId, String courseId, int semester): 시간표 충돌 확인
 * - getCourseSchedule(String courseId): 과목 시간표 가져오기
 * - getCurrentCredits(String studentId, int semester): 현재 학점 가져오기
 * - getCourseCredits(String courseId): 과목 학점 가져오기
 * - addEnrollment(String studentId, String courseId, int semester): 수강신청 추가
 * - cancelEnrollment(String studentId, String courseId): 수강신청 취소
 * - getEnrolledCourses(String studentId, int semester): 수강신청 과목 조회
 */
public class EnrollmentService {
    private static boolean isEnrollmentInProgress = false;

    /**
     * 수강신청을 시작합니다.
     *
     * @param studentId 학생 ID
     * @return 다른 사용자가 이미 수강신청을 진행 중인 경우 false, 아니면 true
     */
    public static synchronized boolean startEnrollment(String studentId) {
        if (isEnrollmentInProgress) {
            return false; // 다른 사용자가 이미 수강신청을 진행 중
        }
        isEnrollmentInProgress = true;
        return true;
    }

    /**
     * 수강신청을 종료합니다.
     */
    public static synchronized void endEnrollment() {
        isEnrollmentInProgress = false;
    }

    /**
     * 수강신청을 처리합니다.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @param semester 학기
     * @return 수강신청 성공 시 true, 실패 시 false
     */
    public static synchronized boolean enrollStudent(String studentId, String courseId, int semester) {
        // Check if the course is already enrolled
        if (isCourseEnrolled(studentId, courseId)) {
            System.out.println("Course already enrolled.");
            return false;
        }

        // Check if the course schedule conflicts with existing enrollments
        if (isScheduleConflict(studentId, courseId, semester)) {
            System.out.println("Schedule conflict.");
            return false;
        }

        // Check if the student exceeds the credit limit
        int currentCredits = getCurrentCredits(studentId, semester);
        int courseCredits = getCourseCredits(courseId);
        double averageGrade = StudentService.getAverageGrade(studentId);

        if ((currentCredits + courseCredits > 18 && averageGrade < 4.0) ||
                (currentCredits + courseCredits > 21 && averageGrade >= 4.0)) {
            System.out.println("Credit limit exceeded.");
            return false;
        }

        // Enroll the student
        return addEnrollment(studentId, courseId, semester);
    }

    /**
     * 학생이 이미 수강신청한 과목인지 확인합니다.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @return 이미 수강신청한 경우 true, 아니면 false
     */
    private static boolean isCourseEnrolled(String studentId, String courseId) {
        String query = "SELECT COUNT(*) FROM enrollments WHERE studentId = ? AND courseId = ? AND status = 'in_progress'";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking enrolled course: " + e.getMessage());
        }
        return false;
    }

    /**
     * 수강신청하려는 과목과 기존 수강 과목의 시간표가 충돌하는지 확인합니다.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @param semester 학기
     * @return 시간표가 충돌하는 경우 true, 아니면 false
     */
    private static boolean isScheduleConflict(String studentId, String courseId, int semester) {
        String query = "SELECT c.schedule FROM courses c " +
                "JOIN enrollments e ON c.courseId = e.courseId " +
                "WHERE e.studentId = ? AND e.semester = ? AND e.status = 'in_progress'";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, semester);
            ResultSet rs = pstmt.executeQuery();
            String newCourseSchedule = getCourseSchedule(courseId);
            while (rs.next()) {
                String existingCourseSchedule = rs.getString("schedule");
                if (ScheduleUtils.isTimeConflict(existingCourseSchedule, newCourseSchedule)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking schedule conflict: " + e.getMessage());
        }
        return false;
    }

    /**
     * 과목의 시간표를 가져옵니다.
     *
     * @param courseId 과목 ID
     * @return 과목의 시간표 문자열
     */
    private static String getCourseSchedule(String courseId) {
        String query = "SELECT schedule FROM courses WHERE courseId = ?";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("schedule");
            }
        } catch (SQLException e) {
            System.out.println("Error getting course schedule: " + e.getMessage());
        }
        return null;
    }

    /**
     * 학생의 현재 학점을 가져옵니다.
     *
     * @param studentId 학생 ID
     * @param semester 학기
     * @return 현재 학점
     */
    private static int getCurrentCredits(String studentId, int semester) {
        String query = "SELECT SUM(c.credit) FROM courses c " +
                "JOIN enrollments e ON c.courseId = e.courseId " +
                "WHERE e.studentId = ? AND e.semester = ? AND e.status = 'in_progress'";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, semester);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting current credits: " + e.getMessage());
        }
        return 0;
    }

    /**
     * 과목의 학점을 가져옵니다.
     *
     * @param courseId 과목 ID
     * @return 과목 학점
     */
    private static int getCourseCredits(String courseId) {
        String query = "SELECT credit FROM courses WHERE courseId = ?";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("credit");
            }
        } catch (SQLException e) {
            System.out.println("Error getting course credits: " + e.getMessage());
        }
        return 0;
    }

    /**
     * 학생의 수강신청을 추가합니다.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @param semester 학기
     * @return 수강신청 성공 시 true, 실패 시 false
     */
    private static boolean addEnrollment(String studentId, String courseId, int semester) {
        String query = "INSERT INTO enrollments (studentId, courseId, semester, status) VALUES (?, ?, ?, 'in_progress')";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            pstmt.setInt(3, semester);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error adding enrollment: " + e.getMessage());
        }
        return false;
    }

    /**
     * 학생의 수강신청을 취소합니다.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @return 수강신청 취소 성공 시 true, 실패 시 false
     */
    public static boolean cancelEnrollment(String studentId, String courseId) {
        String query = "DELETE FROM enrollments WHERE studentId = ? AND courseId = ? AND status = 'in_progress'";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error canceling enrollment: " + e.getMessage());
        }
        return false;
    }

    /**
     * 학생의 수강신청 과목 목록을 가져옵니다.
     *
     * @param studentId 학생 ID
     * @param semester 학기
     * @return 수강신청 과목 목록
     */
    public static List<String> getEnrolledCourses(String studentId, int semester) {
        List<String> enrolledCourses = new ArrayList<>();
        String query = "SELECT c.courseName, c.courseId FROM courses c " +
                "JOIN enrollments e ON c.courseId = e.courseId " +
                "WHERE e.studentId = ? AND e.semester = ? AND e.status = 'in_progress'";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, semester);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrolledCourses.add(rs.getString("courseName") + " (" + rs.getString("courseId") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching enrolled courses: " + e.getMessage());
        }
        return enrolledCourses;
    }
}