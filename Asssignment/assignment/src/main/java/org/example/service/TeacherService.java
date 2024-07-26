package org.example.service;

import org.example.model.Teacher;
import org.example.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 교원 서비스 클래스입니다.
 * 교원 정보를 조회하는 메서드를 제공합니다.
 */
public class TeacherService {

    /**
     * 주어진 교원 ID로 교원 정보를 조회합니다.
     *
     * @param teacherId 교원 ID
     * @return 조회된 교원 객체, 해당 ID의 교원이 없으면 null 반환
     */
    public static Teacher getTeacherById(String teacherId) {
        String query = "SELECT * FROM teachers WHERE teacherId = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, teacherId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String department = rs.getString("department");

                return new Teacher(teacherId, name, gender, phone, email, department);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teacher: " + e.getMessage());
        }

        return null;
    }
}