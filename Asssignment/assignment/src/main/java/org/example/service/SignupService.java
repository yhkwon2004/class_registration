package org.example.service;

import org.example.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 회원가입 서비스 클래스입니다.
 * 학생 또는 교직원의 회원가입을 처리합니다.
 */
public class SignupService {
    /**
     * 주어진 사용자 정보를 사용하여 회원가입을 시도합니다.
     *
     * @param userId 사용자 ID (학생 ID 또는 교직원 ID)
     * @param password 사용자 비밀번호
     * @param name 사용자 이름
     * @param gender 사용자 성별
     * @param phone 사용자 전화번호
     * @param email 사용자 이메일
     * @param department 사용자 소속 학과
     * @param semester 사용자 학기
     * @param userType 사용자 유형 ("student" 또는 "teacher")
     * @return 회원가입 성공 시 true, 실패 시 false
     */
    public static boolean signup(String userId, String password, String name, String gender, String phone, String email, String department, String semester, String userType) {
        String query;
        if (userType.equals("student")) {
            query = "INSERT INTO students (studentId, password, name, gender, phone, email, department, semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            query = "INSERT INTO teachers (teacherId, password, name, gender, phone, email, department, semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, gender);
            pstmt.setString(5, phone);
            pstmt.setString(6, email);
            pstmt.setString(7, department);
            pstmt.setString(8, semester);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Signup failed: " + e.getMessage());
        }
        return false;
    }
}