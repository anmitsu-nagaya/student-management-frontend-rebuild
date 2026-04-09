package raisetech.student.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.dto.RegisterCourseDetailRequest;
import raisetech.student.management.dto.RegisterStudentDetailRequest;
import raisetech.student.management.dto.UpdateCourseDetailRequest;
import raisetech.student.management.dto.UpdateStudentDetailRequest;
import raisetech.student.management.dto.registerdata.RegisterCourseRequest;
import raisetech.student.management.dto.registerdata.RegisterStudentRequest;
import raisetech.student.management.dto.updatedata.UpdateStatusRequest;

class DomainDtoConverterTest {

  private DomainDtoConverter sut;

  @BeforeEach
  void Before() {
    sut = new DomainDtoConverter();
  }

  @Test
  void 登録リクエストされたデータを受講生詳細に変換する際値が正しくマッピングされること() {

    //事前準備
    RegisterStudentRequest student = new RegisterStudentRequest();
    student.setStudentFullName("山田太郎");
    student.setStudentFurigana("ヤマダタロウ");
    student.setStudentNickname("たろちゃん");
    student.setEmail("yamada.taro@example.com");
    student.setPrefecture("東京都");
    student.setCity("渋谷区");
    student.setAge(25);
    student.setGender("男性");
    student.setStudentRemark("積極的に質問する学生");

    RegisterCourseRequest course = new RegisterCourseRequest();
    course.setCourseName("Javaコース");

    RegisterCourseDetailRequest courseDetail = new RegisterCourseDetailRequest();
    courseDetail.setCourse(course);
    List<RegisterCourseDetailRequest> courseDetails = List.of(courseDetail);

    //実行
    StudentDetail actual = sut.toStudentDetailfromRegisterStudentDetail(
        new RegisterStudentDetailRequest(student, courseDetails));

    //検証

    Student actualStudent = actual.getStudent();
    CourseDetail actualCourseDetail = actual.getCourseList().getFirst();
    StudentCourse actualCourse = actualCourseDetail.getCourse();

    assertThat(actualStudent.getStudentFullName()).isEqualTo("山田太郎");
    assertThat(actualStudent.getStudentFurigana()).isEqualTo("ヤマダタロウ");
    assertThat(actualStudent.getStudentNickname()).isEqualTo("たろちゃん");
    assertThat(actualStudent.getEmail()).isEqualTo("yamada.taro@example.com");
    assertThat(actualStudent.getPrefecture()).isEqualTo("東京都");
    assertThat(actualStudent.getCity()).isEqualTo("渋谷区");
    assertThat(actualStudent.getAge()).isEqualTo(25);
    assertThat(actualStudent.getGender()).isEqualTo("男性");
    assertThat(actualStudent.getStudentRemark()).isEqualTo("積極的に質問する学生");

    assertThat(actualCourse.getCourseName()).isEqualTo("Javaコース");

  }


  @Test
  void 更新リクエストされたデータを受講生詳細に変換する際値が正しくマッピングされること() {

    //事前準備
    Student student = new Student();
    student.setStudentId("550e8400-e29b-41d4-a716-446655440001");
    student.setStudentFullName("山田太郎");
    student.setStudentFurigana("ヤマダタロウ");
    student.setStudentNickname("たろちゃん");
    student.setEmail("yamada.taro@example.com");
    student.setPrefecture("東京都");
    student.setCity("渋谷区");
    student.setAge(25);
    student.setGender("男性");
    student.setStudentRemark("積極的に質問する学生");
    student.setStudentIsDeleted(false);

    StudentCourse course = new StudentCourse();
    course.setCourseId(1);
    course.setStudentId("550e8400-e29b-41d4-a716-446655440001");
    course.setCourseName("Javaコース");

    UpdateStatusRequest status = new UpdateStatusRequest();
    status.setStatusId(1);
    status.setCourseId(1);
    status.setStatus(CourseStatus.本申込);

    UpdateCourseDetailRequest courseDetail = new UpdateCourseDetailRequest();
    courseDetail.setCourse(course);
    courseDetail.setStatus(status);
    List<UpdateCourseDetailRequest> courseDetails = List.of(courseDetail);

    //実行
    StudentDetail actual = sut.toStudentDetailfromUpdateStudentDetail(
        new UpdateStudentDetailRequest(student, courseDetails));

    //検証

    Student actualStudent = actual.getStudent();
    CourseDetail actualCourseDetail = actual.getCourseList().getFirst();
    StudentCourse actualCourse = actualCourseDetail.getCourse();
    StudentCourseStatus actualStatus = actualCourseDetail.getStatus();

    assertThat(actualStudent.getStudentId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
    assertThat(actualStudent.getStudentFullName()).isEqualTo("山田太郎");
    assertThat(actualStudent.getStudentFurigana()).isEqualTo("ヤマダタロウ");
    assertThat(actualStudent.getStudentNickname()).isEqualTo("たろちゃん");
    assertThat(actualStudent.getEmail()).isEqualTo("yamada.taro@example.com");
    assertThat(actualStudent.getPrefecture()).isEqualTo("東京都");
    assertThat(actualStudent.getCity()).isEqualTo("渋谷区");
    assertThat(actualStudent.getAge()).isEqualTo(25);
    assertThat(actualStudent.getGender()).isEqualTo("男性");
    assertThat(actualStudent.getStudentRemark()).isEqualTo("積極的に質問する学生");
    assertThat(actualStudent.getStudentIsDeleted()).isEqualTo(false);

    assertThat(actualCourse.getCourseId()).isEqualTo(1);
    assertThat(actualCourse.getStudentId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
    assertThat(actualCourse.getCourseName()).isEqualTo("Javaコース");

    assertThat(actualStatus.getStatusId()).isEqualTo(1);
    assertThat(actualStatus.getCourseId()).isEqualTo(1);
    assertThat(actualStatus.getStatus()).isEqualTo(CourseStatus.本申込);
  }

}
