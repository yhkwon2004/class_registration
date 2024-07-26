package org.example.service;

import org.example.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 로그인 서비스 클래스입니다.
 * 사용자 ID와 비밀번호를 통해 학생 또는 교직원의 로그인을 처리합니다.
 */
public class LoginService {
    /**
     * 주어진 사용자 ID와 비밀번호를 사용하여 로그인 시도합니다.
     *
     * @param userId 사용자 ID (학생 ID 또는 교직원 ID)
     * @param password 사용자 비밀번호
     * @param userType 사용자 유형 ("student" 또는 "teacher")
     * @return 로그인 성공 시 true, 실패 시 false
     */
    public static boolean login(String userId, String password, String userType) {
        String tableName = userType.equals("student") ? "students" : "teachers";
        String query = "SELECT password FROM " + tableName + " WHERE " + (userType.equals("student") ? "studentId" : "teacherId") + " = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}