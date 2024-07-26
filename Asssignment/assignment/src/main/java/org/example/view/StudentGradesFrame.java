package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import org.example.util.DatabaseUtil;

/**
 * StudentGradesFrame 클래스는 학생의 성적을 표시하는 GUI 프레임입니다.
 * 주어진 학생 ID에 대한 성적 정보를 데이터베이스에서 가져와 표 형식으로 표시합니다.
 */
public class StudentGradesFrame extends JFrame {
    private String studentId;

    /**
     * StudentGradesFrame 생성자. 주어진 학생 ID를 사용하여 성적 프레임을 초기화합니다.
     *
     * @param studentId 학생 ID
     */
    public StudentGradesFrame(String studentId) {
        this.studentId = studentId;
        setTitle("수강목록");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    /**
     * GUI 컴포넌트를 배치하는 메서드.
     *
     * @param panel 컴포넌트를 배치할 JPanel
     */
    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("과목명");
        columnNames.add("성적");
        columnNames.add("이수여부");

        Vector<Vector<Object>> data = new Vector<>();

        // 데이터베이스에서 성적 정보를 가져와서 테이블에 추가
        String query = "SELECT c.courseName, e.grade, e.status FROM courses c " +
                "JOIN enrollments e ON c.courseId = e.courseId " +
                "WHERE e.studentId = ?";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("courseName"));
                row.add(rs.getDouble("grade"));
                row.add(rs.getString("status"));
                data.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching grades: " + e.getMessage());
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton, BorderLayout.SOUTH);
    }
}