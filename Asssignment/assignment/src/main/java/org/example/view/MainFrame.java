package org.example.view;

import org.example.service.UpdateService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MainFrame 클래스는 사용자가 로그인 후에 접근할 수 있는 메인 화면을 제공하는 GUI 프레임이다.
 * 사용자는 자신의 연락처 정보를 업데이트할 수 있으며, 로그아웃 기능을 제공한다.
 */
public class MainFrame extends JFrame {
    private String userType;
    private String userId;

    /**
     * MainFrame 생성자. 메인 프레임을 초기화한다.
     *
     * @param userType 사용자 타입 (student 또는 teacher)
     * @param userId   사용자 ID
     */
    public MainFrame(String userType, String userId) {
        this.userType = userType;
        this.userId = userId;
        setTitle("Main");
        setSize(400, 300);
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

        JLabel phoneLabel = new JLabel("전화번호");
        phoneLabel.setBounds(10, 20, 80, 25);
        panel.add(phoneLabel);

        JTextField phoneField = new JTextField(20);
        phoneField.setBounds(100, 20, 165, 25);
        panel.add(phoneField);

        JLabel emailLabel = new JLabel("이메일");
        emailLabel.setBounds(10, 50, 80, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField(20);
        emailField.setBounds(100, 50, 165, 25);
        panel.add(emailField);

        JButton updateButton = new JButton("업데이트");
        updateButton.setBounds(10, 80, 80, 25);
        panel.add(updateButton);

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setBounds(100, 80, 100, 25);
        panel.add(logoutButton);

        // 업데이트 버튼 클릭 시 이벤트 처리
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText();
                String email = emailField.getText();
                boolean result;

                // 사용자 타입에 따라 정보 업데이트 처리
                if (userType.equals("student")) {
                    result = UpdateService.updateStudentInfo(userId, phone, email);
                } else {
                    result = UpdateService.updateTeacherInfo(userId, phone, email);
                }

                // 업데이트 결과에 따라 메시지 출력
                if (result) {
                    JOptionPane.showMessageDialog(null, "업데이트성공!");
                } else {
                    JOptionPane.showMessageDialog(null, "업데이트 실패!");
                }
            }
        });

        // 로그아웃 버튼 클릭 시 이벤트 처리
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }
}