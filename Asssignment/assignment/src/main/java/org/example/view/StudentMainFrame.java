package org.example.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.model.Student;
import org.example.service.StudentService;

/**
 * StudentMainFrame 클래스는 학생의 메인 페이지를 표시하는 GUI 프레임입니다.
 * 주어진 학생 객체를 사용하여 정보를 표시하고, 다양한 기능을 수행할 수 있습니다.
 */
public class StudentMainFrame extends JFrame {
    private Student student;

    /**
     * StudentMainFrame 생성자. 주어진 학생 객체를 사용하여 메인 프레임을 초기화합니다.
     *
     * @param student 학생 객체
     */
    public StudentMainFrame(Student student) {
        this.student = student;
        setTitle("학생 페이지");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("이름: " + student.getName());
        nameLabel.setBounds(10, 20, 300, 25);
        panel.add(nameLabel);

        JLabel genderLabel = new JLabel("성별: " + student.getGender());
        genderLabel.setBounds(10, 50, 300, 25);
        panel.add(genderLabel);

        JLabel phoneLabel = new JLabel("전화번호: " + student.getPhone());
        phoneLabel.setBounds(10, 80, 300, 25);
        panel.add(phoneLabel);

        JLabel emailLabel = new JLabel("이메일: " + student.getEmail());
        emailLabel.setBounds(10, 110, 300, 25);
        panel.add(emailLabel);

        JLabel departmentLabel = new JLabel("전공/소속: " + student.getDepartment());
        departmentLabel.setBounds(10, 140, 300, 25);
        panel.add(departmentLabel);

        JLabel semesterLabel = new JLabel("이수학기: " + student.getSemester());
        semesterLabel.setBounds(10, 170, 300, 25);
        panel.add(semesterLabel);

        JLabel studentIdLabel = new JLabel("ID: " + student.getStudentId());
        studentIdLabel.setBounds(10, 200, 300, 25);
        panel.add(studentIdLabel);

        double averageGrade = StudentService.getAverageGrade(student.getStudentId());
        JLabel gradesLabel = new JLabel("성적: " + String.format("%.2f", averageGrade));
        gradesLabel.setBounds(10, 230, 300, 25);
        panel.add(gradesLabel);

        JButton viewGradesButton = new JButton("과목보기");
        viewGradesButton.setBounds(10, 270, 120, 25);
        panel.add(viewGradesButton);

        JButton enrollButton = new JButton("수강신청");
        enrollButton.setBounds(150, 270, 120, 25);
        panel.add(enrollButton);

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setBounds(300, 270, 100, 25);
        panel.add(logoutButton);

        viewGradesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentGradesFrame studentGradesFrame = new StudentGradesFrame(student.getStudentId());
                studentGradesFrame.setVisible(true);
            }
        });

        enrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnrollmentFrame enrollmentFrame = new EnrollmentFrame(student);
                enrollmentFrame.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }
}