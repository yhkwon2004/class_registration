package org.example.service;

import org.example.model.Student;
import org.example.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 학생 서비스 클래스입니다.
 * 학생 정보를 조회하고 평균 성적을 계산하는 메서드를 제공합니다.
 */
public class StudentService {

    /**
     * 주어진 학생 ID로 학생 정보를 조회합니다.
     *
     * @param studentId 학생 ID
     * @return 조회된 학생 객체, 해당 ID의 학생이 없으면 null 반환
     */
    public static Student getStudentById(String studentId) {
        String query = "SELECT * FROM students WHERE studentId = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String department = rs.getString("department");
                int semester = rs.getInt("semester");

                double averageGrade = getAverageGrade(studentId);

                return new Student(studentId, name, gender, phone, email, department, semester, averageGrade);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }

        return null;
    }

    /**
     * 주어진 학생 ID로 학생의 평균 성적을 계산합니다.
     *
     * @param studentId 학생 ID
     * @return 학생의 평균 성적
     */
    public static double getAverageGrade(String studentId) {
        String query = "SELECT AVG(grade) AS avg_grade FROM enrollments WHERE studentId = ?";
        double averageGrade = 0.0;

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                averageGrade = rs.getDouble("avg_grade");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating average grade: " + e.getMessage());
        }

        return averageGrade;
    }
}