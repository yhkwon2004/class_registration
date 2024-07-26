package org.example.model;

/**
 * Teacher 클래스는 교직원의 정보를 저장하는 데이터 모델 클래스입니다.
 * 이 클래스는 교직원의 ID, 이름, 성별, 전화번호, 이메일, 소속 학과 정보를 저장합니다.
 *
 * 필드:
 * - teacherId: 교직원 ID
 * - name: 이름
 * - gender: 성별
 * - phone: 전화번호
 * - email: 이메일
 * - department: 소속 학과
 *
 * 생성자:
 * - Teacher(String teacherId, String name, String gender, String phone, String email, String department)
 *
 * 메소드:
 * - String getTeacherId(): 교직원 ID를 반환합니다.
 * - String getName(): 이름을 반환합니다.
 * - String getGender(): 성별을 반환합니다.
 * - String getPhone(): 전화번호를 반환합니다.
 * - String getEmail(): 이메일을 반환합니다.
 * - String getDepartment(): 소속 학과를 반환합니다.
 * - void setTeacherId(String teacherId): 교직원 ID를 설정합니다.
 * - void setName(String name): 이름을 설정합니다.
 * - void setGender(String gender): 성별을 설정합니다.
 * - void setPhone(String phone): 전화번호를 설정합니다.
 * - void setEmail(String email): 이메일을 설정합니다.
 * - void setDepartment(String department): 소속 학과를 설정합니다.
 */
public class Teacher {
    private String teacherId;  // 교직원 ID
    private String name;       // 이름
    private String gender;     // 성별
    private String phone;      // 전화번호
    private String email;      // 이메일
    private String department; // 소속 학과

    /**
     * Teacher 클래스의 생성자입니다.
     *
     * @param teacherId   교직원 ID
     * @param name        이름
     * @param gender      성별
     * @param phone       전화번호
     * @param email       이메일
     * @param department  소속 학과
     */
    public Teacher(String teacherId, String name, String gender, String phone, String email, String department) {
        this.teacherId = teacherId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.department = department;
    }

    // 각 필드에 대한 getter와 setter 메소드들
    public String getTeacherId() { return teacherId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
}