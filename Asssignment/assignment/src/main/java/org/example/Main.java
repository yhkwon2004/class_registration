package org.example;

import org.example.server.EnrollmentServer;
import org.example.util.DatabaseUtil;
import org.example.view.LoginFrame;

/**
 * Main 클래스는 프로그램의 진입점을 제공하며, 데이터베이스 초기화, 서버 시작 및 클라이언트 GUI 실행을 담당합니다.
 */
public class Main {
    public static void main(String[] args) {
        // 접근성 기술 비활성화 설정
        System.setProperty("javax.accessibility.assistive_technologies", "");

        // 데이터베이스 테이블을 생성합니다.
        try {
            DatabaseUtil.createTables();
        } catch (Exception e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }

        // 서버를 별도의 스레드에서 실행
        Thread serverThread = new Thread(new EnrollmentServer());
        serverThread.start();

        // 클라이언트 GUI 실행
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}