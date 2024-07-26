package org.example.view;

import org.example.model.Student;
import org.example.model.Teacher;
import org.example.service.LoginService;
import org.example.service.StudentService;
import org.example.service.TeacherService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame 클래스는 로그인 화면을 제공하는 GUI 프레임이다.
 * 학생과 교수가 로그인할 수 있으며, 각각의 역할에 따라 다른 메인 페이지로 이동한다.
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton cancelButton;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;

    /**
     * LoginFrame 생성자. 로그인 프레임을 초기화한다.
     */
    public LoginFrame() {
        setTitle("로그인 페이지");
        setSize(300, 250);
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

        JLabel userLabel = new JLabel("아이디");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        usernameField.setText("12240001"); // 기본 유저 로그인용 초기값
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        studentRadioButton = new JRadioButton("학생");
        studentRadioButton.setBounds(100, 80, 80, 25);
        studentRadioButton.setEnabled(true);

        teacherRadioButton = new JRadioButton("교직원");
        teacherRadioButton.setBounds(180, 80, 80, 25);

        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(studentRadioButton);
        userTypeGroup.add(teacherRadioButton);

        panel.add(studentRadioButton);
        panel.add(teacherRadioButton);

        loginButton = new JButton("로그인");
        loginButton.setBounds(10, 110, 80, 25);
        panel.add(loginButton);

        signupButton = new JButton("신규");
        signupButton.setBounds(100, 110, 80, 25);
        panel.add(signupButton);

        cancelButton = new JButton("닫기");
        cancelButton.setBounds(190, 110, 80, 25);
        panel.add(cancelButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userType = studentRadioButton.isSelected() ? "student" : teacherRadioButton.isSelected() ? "teacher" : null;

                if (userType == null) {
                    JOptionPane.showMessageDialog(null, "학생/교직원 분류를 선택하시오.");
                    return;
                }

                if (userType.equals("student")) {
                    Student student = StudentService.getStudentById(userId);
                    if (student != null && LoginService.login(userId, password, userType)) {
                        JOptionPane.showMessageDialog(null, "로그인 성공");
                        StudentMainFrame studentMainFrame = new StudentMainFrame(student);
                        studentMainFrame.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패");
                    }
                } else if (userType.equals("teacher")) {
                    Teacher teacher = TeacherService.getTeacherById(userId);
                    if (teacher != null && LoginService.login(userId, password, userType)) {
                        JOptionPane.showMessageDialog(null, "로그인 성공");
                        TeacherMainFrame teacherMainFrame = new TeacherMainFrame(teacher);
                        teacherMainFrame.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패");
                    }
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignupFrame signupFrame = new SignupFrame();
                signupFrame.setVisible(true);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}