package org.example.model;

/**
 * Student 클래스는 학생의 정보를 저장하는 데이터 모델 클래스입니다.
 * 이 클래스는 학생의 ID, 이름, 성별, 전화번호, 이메일, 소속 학과, 학기, 성적 정보를 저장합니다.
 *
 * 필드:
 * - studentId: 학생 ID
 * - name: 이름
 * - gender: 성별
 * - phone: 전화번호
 * - email: 이메일
 * - department: 소속 학과
 * - semester: 학기
 * - grades: 성적
 *
 * 생성자:
 * - Student(String studentId, String name, String gender, String phone, String email, String department, int semester, double grades)
 *
 * 메소드:
 * - String getStudentId(): 학생 ID를 반환합니다.
 * - String getName(): 이름을 반환합니다.
 * - String getGender(): 성별을 반환합니다.
 * - String getPhone(): 전화번호를 반환합니다.
 * - String getEmail(): 이메일을 반환합니다.
 * - String getDepartment(): 소속 학과를 반환합니다.
 * - int getSemester(): 학기를 반환합니다.
 * - double getGrades(): 성적을 반환합니다.
 * - void setStudentId(String studentId): 학생 ID를 설정합니다.
 * - void setName(String name): 이름을 설정합니다.
 * - void setGender(String gender): 성별을 설정합니다.
 * - void setPhone(String phone): 전화번호를 설정합니다.
 * - void setEmail(String email): 이메일을 설정합니다.
 * - void setDepartment(String department): 소속 학과를 설정합니다.
 * - void setSemester(int semester): 학기를 설정합니다.
 * - void setGrades(double grades): 성적을 설정합니다.
 */
public class Student {
    private String studentId;  // 학생 ID
    private String name;       // 이름
    private String gender;     // 성별
    private String phone;      // 전화번호
    private String email;      // 이메일
    private String department; // 소속 학과
    private int semester;      // 학기
    private double grades;     // 성적

    public Student(String studentId, String name, String gender, String phone, String email, String department, int semester, double grades) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.semester = semester;
        this.grades = grades;
    }
    // 각 필드에 대한 getter와 setter 메소드들

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public int getSemester() { return semester; }
    public double getGrades() { return grades; }

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setGrades(double grades) { this.grades = grades; }
}