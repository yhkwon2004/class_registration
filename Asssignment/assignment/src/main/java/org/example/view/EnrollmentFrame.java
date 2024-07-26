package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.model.Student;
import org.example.service.EnrollmentRequest;
import org.example.service.EnrollmentService;
import org.example.util.DatabaseUtil;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * EnrollmentFrame 클래스는 수강 신청을 위한 GUI 프레임을 제공한다.
 * 학생이 수강 신청, 수강 취소, 신청한 과목 목록을 볼 수 있는 기능을 포함한다.
 */
public class EnrollmentFrame extends JFrame {
    private Student student;
    private JList<String> courseList;
    private DefaultListModel<String> listModel;
    private JButton enrollButton;
    private JButton cancelEnrollmentButton;
    private JButton closeButton;
    private JList<String> enrolledCourseList;
    private DefaultListModel<String> enrolledListModel;

    /**
     * EnrollmentFrame 생성자. 주어진 학생 객체를 사용하여 프레임을 초기화한다.
     *
     * @param student 수강 신청을 할 학생 객체
     */
    public EnrollmentFrame(Student student) {
        this.student = student;
        setTitle("수강신청 페이지");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);
        placeComponents(panel);

        // 창이 닫힐 때 수강 신청 상태를 해제하는 리스너 추가
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                EnrollmentService.endEnrollment();
            }
        });
    }

    /**
     * GUI 컴포넌트를 배치하는 메서드.
     *
     * @param panel 컴포넌트를 배치할 JPanel
     */
    private void placeComponents(JPanel panel) {
        JPanel topPanel = new JPanel(new GridLayout(1, 2));

        listModel = new DefaultListModel<>();
        courseList = new JList<>(listModel);
        loadCourses();
        JScrollPane scrollPane = new JScrollPane(courseList);
        topPanel.add(scrollPane);

        enrolledListModel = new DefaultListModel<>();
        enrolledCourseList = new JList<>(enrolledListModel);
        loadEnrolledCourses();
        JScrollPane enrolledScrollPane = new JScrollPane(enrolledCourseList);
        topPanel.add(enrolledScrollPane);

        panel.add(topPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        enrollButton = new JButton("수강신청");
        cancelEnrollmentButton = new JButton("수강신청 취소");
        closeButton = new JButton("닫기");

        buttonPanel.add(enrollButton);
        buttonPanel.add(cancelEnrollmentButton);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        enrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!EnrollmentService.startEnrollment(student.getStudentId())) {
                    JOptionPane.showMessageDialog(null, "다른 사용자가 사용중임니다.기다려주세요");
                    return;
                }

                String selectedCourse = courseList.getSelectedValue();
                if (selectedCourse != null) {
                    String courseId = getCourseId(selectedCourse);
                    boolean result = sendEnrollmentRequest(student.getStudentId(), courseId, student.getSemester());
                    JOptionPane.showMessageDialog(null, result ? "수강신청 성공" : "수강신청 실패");
                    if (result) {
                        loadEnrolledCourses();
                    }
                }
            }
        });

        cancelEnrollmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = enrolledCourseList.getSelectedValue();
                if (selectedCourse != null) {
                    String courseId = getCourseId(selectedCourse);
                    boolean cancelResult = EnrollmentService.cancelEnrollment(student.getStudentId(), courseId);
                    if (cancelResult) {
                        JOptionPane.showMessageDialog(null, "수강신청 취소 성공");
                        loadEnrolledCourses(); // 신청 과목 목록 갱신
                    } else {
                        JOptionPane.showMessageDialog(null, "수강신청 취소 실패");
                    }
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnrollmentService.endEnrollment();
                dispose();
            }
        });
    }

    /**
     * 데이터베이스에서 수강 가능한 과목 목록을 불러와 리스트 모델에 추가한다.
     */
    private void loadCourses() {
        String query = "SELECT courseId, courseName FROM courses WHERE courseId NOT IN " +
                "(SELECT courseId FROM enrollments WHERE studentId = ? AND status = 'completed')";
        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getStudentId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String courseId = rs.getString("courseId");
                String courseName = rs.getString("courseName");
                listModel.addElement(courseName + " (" + courseId + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    /**
     * 데이터베이스에서 이미 수강 신청한 과목 목록을 불러와 리스트 모델에 추가한다.
     */
    private void loadEnrolledCourses() {
        enrolledListModel.clear();
        List<String> enrolledCourses = EnrollmentService.getEnrolledCourses(student.getStudentId(), student.getSemester());
        for (String course : enrolledCourses) {
            enrolledListModel.addElement(course);
        }
    }

    /**
     * 과목 문자열에서 과목 ID를 추출한다.
     *
     * @param courseString 과목 문자열 (예: "Course Name (courseId)")
     * @return 과목 ID
     */
    private String getCourseId(String courseString) {
        return courseString.substring(courseString.lastIndexOf('(') + 1, courseString.lastIndexOf(')'));
    }

    /**
     * 서버로 수강 신청 요청을 보내고 응답을 받는다.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @param semester 학기
     * @return 수강 신청 성공 시 true, 실패 시 false
     */
    private boolean sendEnrollmentRequest(String studentId, String courseId, int semester) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Sending data to server...");
            out.writeObject(studentId);
            out.writeObject(courseId);
            out.writeInt(semester);
            out.flush(); // Ensure data is sent

            String response = (String) in.readObject();
            System.out.println("Response from server: " + response);
            return response.equals("Enrollment successful");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}