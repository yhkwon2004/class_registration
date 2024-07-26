package org.example.view;

import org.example.service.SignupService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SignupFrame 클래스는 사용자 등록 화면을 제공하는 GUI 프레임입니다.
 * 사용자는 이름, 이메일, 전화번호, ID, 학과, 학기, 성별 및 비밀번호를 입력하고
 * 학생 또는 교사로 등록할 수 있습니다.
 */
public class SignupFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField studentIdField;
    private JTextField departmentField;
    private JPasswordField passwordField;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JComboBox<String> semesterComboBox;
    private JButton signupButton;
    private JButton cancelButton;

    /**
     * SignupFrame 생성자. 등록 프레임을 초기화합니다.
     */
    public SignupFrame() {
        setTitle("회원가입 페이지");
        setSize(400, 450);
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

        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(100, 20, 165, 25);
        panel.add(nameField);

        JLabel emailLabel = new JLabel("이메일");
        emailLabel.setBounds(10, 50, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(100, 50, 165, 25);
        panel.add(emailField);

        JLabel phoneLabel = new JLabel("전화번호");
        phoneLabel.setBounds(10, 80, 80, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField(20);
        phoneField.setBounds(100, 80, 165, 25);
        panel.add(phoneField);

        JLabel studentIdLabel = new JLabel("학생/교직원 ID");
        studentIdLabel.setBounds(10, 110, 150, 25);
        panel.add(studentIdLabel);

        studentIdField = new JTextField(20);
        studentIdField.setBounds(160, 110, 165, 25);
        panel.add(studentIdField);

        JLabel departmentLabel = new JLabel("전공");
        departmentLabel.setBounds(10, 140, 80, 25);
        panel.add(departmentLabel);

        departmentField = new JTextField(20);
        departmentField.setBounds(100, 140, 165, 25);
        panel.add(departmentField);

        JLabel semesterLabel = new JLabel("이수학기");
        semesterLabel.setBounds(10, 170, 80, 25);
        panel.add(semesterLabel);

        String[] semesters = {"1", "2", "3", "4", "5", "6", "7", "8"};
        semesterComboBox = new JComboBox<>(semesters);
        semesterComboBox.setBounds(100, 170, 165, 25);
        panel.add(semesterComboBox);

        JLabel genderLabel = new JLabel("성별");
        genderLabel.setBounds(10, 200, 80, 25);
        panel.add(genderLabel);

        maleButton = new JRadioButton("남성");
        maleButton.setBounds(100, 200, 80, 25);
        femaleButton = new JRadioButton("여성");
        femaleButton.setBounds(180, 200, 80, 25);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        panel.add(maleButton);
        panel.add(femaleButton);

        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setBounds(10, 230, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 230, 165, 25);
        panel.add(passwordField);

        JLabel userTypeLabel = new JLabel("가입분류");
        userTypeLabel.setBounds(10, 260, 80, 25);
        panel.add(userTypeLabel);

        studentRadioButton = new JRadioButton("학생");
        studentRadioButton.setBounds(100, 260, 80, 25);
        teacherRadioButton = new JRadioButton("교직원");
        teacherRadioButton.setBounds(180, 260, 80, 25);

        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(studentRadioButton);
        userTypeGroup.add(teacherRadioButton);

        panel.add(studentRadioButton);
        panel.add(teacherRadioButton);

        signupButton = new JButton("가입");
        signupButton.setBounds(10, 300, 80, 25);
        panel.add(signupButton);

        cancelButton = new JButton("취소");
        cancelButton.setBounds(100, 300, 80, 25);
        panel.add(cancelButton);

        // 회원가입 버튼 클릭 시 이벤트 처리
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String studentId = studentIdField.getText();
                String department = departmentField.getText();
                String semester = (String) semesterComboBox.getSelectedItem();
                String gender = maleButton.isSelected() ? "Male" : "Female";
                String password = new String(passwordField.getPassword());
                String userType = studentRadioButton.isSelected() ? "student" : teacherRadioButton.isSelected() ? "teacher" : null;

                // 모든 필드가 채워졌는지 확인
                if (userType == null) {
                    JOptionPane.showMessageDialog(null, "가입분류를 선택하시오.");
                    return;
                }

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || studentId.isEmpty() || department.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "모든 정보를 입력하십시오");
                    return;
                }

                // 회원가입 요청 처리
                boolean result = SignupService.signup(studentId, password, name, gender, phone, email, department, semester, userType);

                if (result) {
                    JOptionPane.showMessageDialog(null, "가입성공");
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "가입실패");
                }
            }
        });

        // 취소 버튼 클릭 시 이벤트 처리
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }
}