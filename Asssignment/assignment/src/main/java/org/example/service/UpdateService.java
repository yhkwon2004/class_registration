package org.example.service;

import org.example.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 업데이트 서비스 클래스입니다.
 * 학생과 교원의 연락처 및 이메일 정보를 업데이트하는 메서드를 제공합니다.
 */
public class UpdateService {

    /**
     * 주어진 학생 ID로 학생의 연락처와 이메일 정보를 업데이트합니다.
     *
     * @param studentId 학생 ID
     * @param phone 업데이트할 전화번호
     * @param email 업데이트할 이메일 주소
     * @return 업데이트 성공 시 true, 실패 시 false
     */
    public static boolean updateStudentInfo(String studentId, String phone, String email) {
        String query = "UPDATE students SET phone = ?, email = ? WHERE studentId = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, email);
            pstmt.setString(3, studentId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 주어진 교원 ID로 교원의 연락처와 이메일 정보를 업데이트합니다.
     *
     * @param teacherId 교원 ID
     * @param phone 업데이트할 전화번호
     * @param email 업데이트할 이메일 주소
     * @return 업데이트 성공 시 true, 실패 시 false
     */
    public static boolean updateTeacherInfo(String teacherId, String phone, String email) {
        String query = "UPDATE teachers SET phone = ?, email = ? WHERE teacherId = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, email);
            pstmt.setString(3, teacherId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}