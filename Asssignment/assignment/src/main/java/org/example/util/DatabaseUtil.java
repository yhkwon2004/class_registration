package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * DatabaseUtil 클래스는 SQLite 데이터베이스와의 연결을 관리하고
 * 필요한 테이블을 생성하는 유틸리티 메서드를 제공합니다.
 */
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:university.db";

    /**
     * 데이터베이스에 연결합니다.
     *
     * @return Connection 객체를 반환합니다. 연결에 실패한 경우 null을 반환합니다.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Connection to SQLite has failed: " + e.getMessage());
        }
        return conn;
    }

    /**
     * 필요한 테이블을 생성합니다. 이미 테이블이 존재하는 경우 테이블을 생성하지 않습니다.
     */
    public static void createTables() {
        String studentTable = "CREATE TABLE IF NOT EXISTS students (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " studentId TEXT NOT NULL,\n"
                + " password TEXT NOT NULL,\n"
                + " name TEXT NOT NULL,\n"
                + " gender TEXT,\n"
                + " phone TEXT,\n"
                + " email TEXT,\n"
                + " department TEXT,\n"
                + " semester INTEGER\n"
                + ");";

        String teacherTable = "CREATE TABLE IF NOT EXISTS teachers (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " teacherId TEXT NOT NULL,\n"
                + " password TEXT NOT NULL,\n"
                + " name TEXT NOT NULL,\n"
                + " gender TEXT,\n"
                + " phone TEXT,\n"
                + " email TEXT,\n"
                + " department TEXT,\n"
                + " semester INTEGER\n"
                + ");";

        String enrollmentTable = "CREATE TABLE IF NOT EXISTS enrollments (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " studentId TEXT NOT NULL,\n"
                + " courseName TEXT NOT NULL,\n"
                + " grade REAL\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(studentTable);
                stmt.execute(teacherTable);
                stmt.execute(enrollmentTable);
            } else {
                System.out.println("Failed to create tables: no database connection.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 메인 메서드로, 프로그램 실행 시 테이블을 생성합니다.
     *
     * @param args 명령줄 인자
     */
    public static void main(String[] args) {
        createTables();
    }
}