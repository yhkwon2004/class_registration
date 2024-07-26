package org.example.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.model.Teacher;
import org.example.model.Student;
import org.example.service.StudentService;

/**
 * TeacherMainFrame 클래스는 교사의 메인 페이지를 위한 GUI 프레임입니다.
 */
public class TeacherMainFrame extends JFrame {
    private Teacher teacher;

    /**
     * TeacherMainFrame 생성자. 주어진 교사 객체를 사용하여 프레임을 초기화합니다.
     *
     * @param teacher 교사 객체
     */
    public TeacherMainFrame(Teacher teacher) {
        this.teacher = teacher;
        setTitle("교직원 페이지");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    /**
     * GUI 구성 요소를 배치하는 메서드.
     *
     * @param panel 구성 요소를 배치할 패널
     */
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("이름: " + teacher.getName());
        nameLabel.setBounds(10, 20, 300, 25);
        panel.add(nameLabel);

        JLabel genderLabel = new JLabel("성별: " + teacher.getGender());
        genderLabel.setBounds(10, 50, 300, 25);
        panel.add(genderLabel);

        JLabel phoneLabel = new JLabel("전화번호: " + teacher.getPhone());
        phoneLabel.setBounds(10, 80, 300, 25);
        panel.add(phoneLabel);

        JLabel emailLabel = new JLabel("이메일: " + teacher.getEmail());
        emailLabel.setBounds(10, 110, 300, 25);
        panel.add(emailLabel);

        JLabel departmentLabel = new JLabel("전공/소속: " + teacher.getDepartment());
        departmentLabel.setBounds(10, 140, 300, 25);
        panel.add(departmentLabel);

        JButton viewStudentButton = new JButton("학생조회");
        viewStudentButton.setBounds(10, 170, 150, 25);
        panel.add(viewStudentButton);

        JButton viewEnrollmentsButton = new JButton("등록한 학생보기");
        viewEnrollmentsButton.setBounds(10, 200, 150, 25);
        panel.add(viewEnrollmentsButton);

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setBounds(10, 230, 100, 25);
        panel.add(logoutButton);

        // 학생 정보 조회 버튼의 액션 리스너를 추가합니다.
        viewStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("학생 ID입력:");
                if (studentId != null) {
                    Student student = StudentService.getStudentById(studentId);
                    if (student != null) {
                        StudentInfoFrame studentInfoFrame = new StudentInfoFrame(student);
                        studentInfoFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "조회 불가");
                    }
                }
            }
        });

        // 수강 신청 조회 버튼의 액션 리스너를 추가합니다.
        viewEnrollmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TeacherEnrollmentFrame enrollmentFrame = new TeacherEnrollmentFrame(teacher);
                enrollmentFrame.setVisible(true);
            }
        });

        // 로그아웃 버튼의 액션 리스너를 추가합니다.
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }
}