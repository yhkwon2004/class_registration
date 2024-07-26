# class_registration
수강신청구현 연습용 프로그램
1.개발환경
Java Open JDK 17 Version
Local DB(SQLite3) / SQLiteSudio (데이터 관리용도)

2.기법적용
객체지향, 추상 클래스, 상속, 인터페이스, 스윙, 스레드, 파일I/O

3.프로그램 설명
학생, 교직원 정보의 GUI를 통해 출력 (구현)
수강신청 기능 (구현)
 패키지와 클래스의 트리 구조
로그인 기능
로그인 기능을 위한 Table 2개
Students, Teachers


로그인 서버 클래스 code

  
학생 / 교직원 로그인 창

 
학생 테이블 데이터


교직원 테이블데이터

User ID와 Password 입력하고 Students, Teacher을 선택 후 로그인 버튼을 누르면 Service 패키지의 Login Sevice 클래스가 실행되어 Students 및 Teachers 테이블에서 ID와 Password를 매칭하여 속성 값을 확인 후 로그인을 진행, Student Main Page와 Teachers Main Page에 각각 접속할 수 있게 구현 

Students Service 의 getStudentById 및 Teachers Service의 getTeacherById 메서드를 이용해 개인의 정보(이름, 성별, 전화번호, 이메일, 학과 등)를 화면에 표기
학생페이지

학생의 View Grades와 Enroll 버튼을 각각 누르면 각각의 화면으로 접속

Student Grades에서 이미 수강 완료한 과목은 “completed”라는 상태 및 Grade의 
성적으로확인 할 수 있음

이번 학기에서 수강 신청한 과목은 Grade 및 Status의 “in_progress”를 통해 확인 

또한 수강신청 화면에서는 왼쪽에는 개설된 과목과 오른쪽에는 현 학기에 수강신청한 과목을 각각표기

Enroll 버튼을 클릭시 Service 패키지의 EnrollmentService클래스 내에 enrollStudent 메서드가 동작하여 수강신청이 진행

Cancel Enrollment 버튼을 클릭시 동일한 클래스 내에 cancleEnrollment 메서드가 동작하여 수강신청이 삭제됨.
 수강신청 table
 개설과목 table
수강 신청&취소 구현을 위한 코드

수강신청 코드


수강취소신청 코드









교직원 페이지

교직원 계정으로 로그인시, 학생의 정보를 볼수있는 기능과, 수강신청목록을 확인할수 있는 기능을 구현

학생조회 버튼을 클릭시 actionPerformed 메서드가 동작하여 학생의 학번을 입력하면, student table에서 studentid를 기반으로 학생의 정보를 출력하는 기능


actionPerformed 메소드
 
학번입력 화면

또한 “등록한 학생보기”버튼을 누를경우 LoadCourses 메서드가 실행되어 위의 해당 교수의 개설과목이 표기되며, “학생보기”를 누를경우 LoadEnrolledStudents 메서드가 실행되어 수강이 완료된 학생과, 수강신청이 진행된 학생을 구분하여 확인가능

“파일저장”버튼을 클릭시 saveEnrolledStudnetsToFile 메서드가 실행되어 해당 파일이 txt.파일로 저장됨

oadCours, loadEnrollStudents코드

 
해당 파일저장 코드



DB를 이용하여 수강 신청 시간중복 방지 구현


과목별 시간표

수강신청시 동일한시간대의 수업을 수강신청 할 경우 우선 enrollStudnet 메서드가 실행되고 isScheduleConfict 메서드가 실행되어 동일한 시간대에 수업이 있을 경우 수강신청이 진행되지 않음


// DB수정을 통해 수강신청 과목 변경가능


스레드개념 적용

우선 수강신청중인 학생이 있을경우, 다른 학생의 계정으로 로그인하고, 수강신청을 진행하면 아래의 코드에 따라서 False가 반환되며, actionPerformed 메서드에서 false를 받아들여 수강신청을 대기시키는 메세지 발생





도용방지용 20232301권용현
클래스 다이어그램 (과제 파일에 원본 같이 첨부)

추가 수강신청 디테일 적용

 - DB연동을 통해서 수강신청 프로그램 작성

 - 학생이 수강신청을 할 경우, 이미 수강한 과목은 표기하지 않으며, 금번 학기에 신청한 과목은 따로 표기되도록 설정(EnrollmentFrame)

 - 수강 신청시 학점 평균이 4점 이상일 경우 21점까지 수강이 가능하고, 4점 이하일 경우 18학점까지 수강이 가능하도록 구현



 - 교직원의 수강신청 화면에서 본인의 개설 과목에 대해서 조회가 가능하도록 설정(TeacherEnrollmentFrame)

 - 수강신청인원 조회 화면에서는 이미 수강한 학생과, 수강 신청한 학생이 조회가능하도록 구분(TeacherEnrollmentFrame)
