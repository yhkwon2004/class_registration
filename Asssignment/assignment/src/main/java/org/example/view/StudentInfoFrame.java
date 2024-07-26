package org.example.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.model.Student;

/**
 * StudentInfoFrame 클래스는 학생의 정보를 표시하는 GUI 프레임입니다.
 * 주어진 학생 객체를 사용하여 정보를 표시합니다.
 */
public class StudentInfoFrame extends JFrame {
    private Student student;

    /**
     * StudentInfoFrame 생성자. 주어진 학생 객체를 사용하여 정보 프레임을 초기화합니다.
     *
     * @param student 학생 객체
     */
    public StudentInfoFrame(Student student) {
        this.student = student;
        setTitle("학생 페이지");
        setSize(400, 400);
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

        JLabel departmentLabel = new JLabel("전공: " + student.getDepartment());
        departmentLabel.setBounds(10, 140, 300, 25);
        panel.add(departmentLabel);

        JLabel semesterLabel = new JLabel("이수학기: " + student.getSemester());
        semesterLabel.setBounds(10, 170, 300, 25);
        panel.add(semesterLabel);

        JLabel studentIdLabel = new JLabel("ID: " + student.getStudentId());
        studentIdLabel.setBounds(10, 200, 300, 25);
        panel.add(studentIdLabel);

        JLabel gradesLabel = new JLabel("성적: " + student.getGrades());
        gradesLabel.setBounds(10, 230, 300, 25);
        panel.add(gradesLabel);

        JButton closeButton = new JButton("닫기");
        closeButton.setBounds(10, 270, 80, 25);
        panel.add(closeButton);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}