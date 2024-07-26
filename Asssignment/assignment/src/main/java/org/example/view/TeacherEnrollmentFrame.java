package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.model.Teacher;
import org.example.util.DatabaseUtil;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * TeacherEnrollmentFrame 클래스는 교사가 수강신청한 학생 목록을 조회하고 파일로 저장할 수 있는 GUI 프레임입니다.
 */
public class TeacherEnrollmentFrame extends JFrame {
    private Teacher teacher;
    private JComboBox<String> courseComboBox;
    private DefaultListModel<String> inProgressListModel;
    private DefaultListModel<String> completedListModel;
    private JList<String> inProgressList;
    private JList<String> completedList;
    private JButton viewButton;
    private JButton saveButton;

    /**
     * TeacherEnrollmentFrame 생성자. 주어진 교사 객체를 사용하여 프레임을 초기화합니다.
     *
     * @param teacher 교사 객체
     */
    public TeacherEnrollmentFrame(Teacher teacher) {
        this.teacher = teacher;
        setTitle("등록한 학생보기");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JPanel topPanel = new JPanel();
        courseComboBox = new JComboBox<>();
        loadCourses();
        topPanel.add(courseComboBox);
        viewButton = new JButton("학생보기");
        topPanel.add(viewButton);
        saveButton = new JButton("파일저장");
        topPanel.add(saveButton);
        panel.add(topPanel, BorderLayout.NORTH);

        inProgressListModel = new DefaultListModel<>();
        inProgressList = new JList<>(inProgressListModel);
        JScrollPane inProgressScrollPane = new JScrollPane(inProgressList);
        inProgressScrollPane.setBorder(BorderFactory.createTitledBorder("이수진행중"));

        completedListModel = new DefaultListModel<>();
        completedList = new JList<>(completedListModel);
        JScrollPane completedScrollPane = new JScrollPane(completedList);
        completedScrollPane.setBorder(BorderFactory.createTitledBorder("이수완료"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inProgressScrollPane, completedScrollPane);
        splitPane.setResizeWeight(0.5);
        panel.add(splitPane, BorderLayout.CENTER);

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadEnrolledStudents();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEnrolledStudentsToFile();
            }
        });
    }

    /**
     * 교사가 담당하는 과목을 로드하여 콤보박스에 추가하는 메서드.
     */
    private void loadCourses() {
        String query = "SELECT courseId, courseName FROM courses WHERE teacherId = ?";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, teacher.getTeacherId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courseComboBox.addItem(rs.getString("courseName") + " (" + rs.getString("courseId") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    /**
     * 선택된 과목의 수강신청 학생들을 로드하여 리스트에 표시하는 메서드.
     */
    private void loadEnrolledStudents() {
        inProgressListModel.clear();
        completedListModel.clear();
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        if (selectedCourse == null) return;
        String courseId = selectedCourse.substring(selectedCourse.lastIndexOf('(') + 1, selectedCourse.lastIndexOf(')'));

        String query = "SELECT s.name, s.studentId, s.department, s.email, e.status, e.grade " +
                "FROM students s " +
                "JOIN enrollments e ON s.studentId = e.studentId " +
                "WHERE e.courseId = ?";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String studentInfo = "Name: " + rs.getString("name") + ", Student ID: " + rs.getString("studentId") +
                        ", Department: " + rs.getString("department") + ", Email: " + rs.getString("email");
                if ("completed".equals(rs.getString("status"))) {
                    studentInfo += ", Grade: " + rs.getDouble("grade");
                    completedListModel.addElement(studentInfo);
                } else {
                    inProgressListModel.addElement(studentInfo);
                }
            }
        } catch (SQLException e) {
            System.out.println("학생불러오기 싫패: " + e.getMessage());
        }
    }

    /**
     * 수강신청 학생들을 파일로 저장하는 메서드.
     */
    private void saveEnrolledStudentsToFile() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        if (selectedCourse == null) return;
        String courseId = selectedCourse.substring(selectedCourse.lastIndexOf('(') + 1, selectedCourse.lastIndexOf(')'));

        String query = "SELECT s.name, s.studentId, s.department, s.email, e.status, e.grade " +
                "FROM students s " +
                "JOIN enrollments e ON s.studentId = e.studentId " +
                "WHERE e.courseId = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             PrintWriter writer = new PrintWriter("enrolled_students_" + courseId + ".txt")) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String studentInfo = "Name: " + rs.getString("name") + ", Student ID: " + rs.getString("studentId") +
                        ", Department: " + rs.getString("department") + ", Email: " + rs.getString("email");
                if ("completed".equals(rs.getString("status"))) {
                    studentInfo += ", Grade: " + rs.getDouble("grade");
                }
                writer.println(studentInfo);
            }
            JOptionPane.showMessageDialog(this, "등록된 학생의 정보를 파일로 저장하였습니다.");
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("Error saving students to file: " + e.getMessage());
        }
    }
}