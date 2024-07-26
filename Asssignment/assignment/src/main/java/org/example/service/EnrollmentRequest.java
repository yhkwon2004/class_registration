package org.example.service;

/**
 * EnrollmentRequest 클래스는 Runnable 인터페이스를 구현하여
 * 수강 신청 요청을 처리하는 스레드를 생성합니다.
 *
 * 필드:
 * - studentId: 학생 ID
 * - courseId: 과목 ID
 * - semester: 학기
 * - lock: 스레드 동기화를 위한 정적 객체
 *
 * 생성자:
 * - EnrollmentRequest(String studentId, String courseId, int semester)
 *
 * 메소드:
 * - void run(): 스레드가 실행될 때 호출되는 메소드로, 수강 신청을 처리합니다.
 */
public class EnrollmentRequest implements Runnable {
    private final String studentId;  // 학생 ID
    private final String courseId;   // 과목 ID
    private final int semester;      // 학기
    private static final Object lock = new Object();  // 스레드 동기화를 위한 정적 객체

    /**
     * EnrollmentRequest 생성자.
     *
     * @param studentId 학생 ID
     * @param courseId 과목 ID
     * @param semester 학기
     */
    public EnrollmentRequest(String studentId, String courseId, int semester) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
    }

    /**
     * 스레드가 실행될 때 호출되는 메소드로, 수강 신청을 처리합니다.
     * 스레드 동기화를 통해 동시에 여러 수강 신청 요청이 처리되지 않도록 합니다.
     */
    @Override
    public void run() {
        synchronized (lock) {  // 스레드 동기화
            System.out.println("Processing enrollment for student: " + studentId);
            boolean result = EnrollmentService.enrollStudent(studentId, courseId, semester);
            if (result) {
                System.out.println("Enrollment successful for student: " + studentId);
            } else {
                System.out.println("Enrollment failed for student: " + studentId);
            }
        }
    }
}