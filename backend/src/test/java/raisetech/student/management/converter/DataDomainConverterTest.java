package raisetech.student.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;

class DataDomainConverterTest {

  private DataDomainConverter sut;

  @BeforeEach
  void Before() {
    sut = new DataDomainConverter();
  }

  @Test
  void リポジトリ層から得た受講生コース情報と申し込み状況がコース詳細に正しくマッピングされた状態で変換されること() {
    StudentCourse course = new StudentCourse();
    course.setCourseId(1);
    course.setStudentId("550e8400-e29b-41d4-a716-446655440001");
    course.setCourseName("Javaコース");
    List<StudentCourse> courseList = List.of(course);
    StudentCourseStatus status = new StudentCourseStatus();
    status.setStatusId(1);
    status.setCourseId(1);
    status.setStatus(CourseStatus.仮申込);
    LocalDateTime testTime = LocalDateTime.parse("2025-10-01 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    status.setTemporaryAppliedAt(testTime);
    List<StudentCourseStatus> statusList = List.of(status);

    List<CourseDetail> actual = sut.toCourseWithStatus(courseList, statusList);

    assertThat(actual).hasSize(1);
    assertThat(actual.getFirst().getCourse().getCourseId()).isEqualTo(1);
    assertThat(actual.getFirst().getCourse().getStudentId()).isEqualTo(
        "550e8400-e29b-41d4-a716-446655440001");
    assertThat(actual.getFirst().getCourse().getCourseName()).isEqualTo("Javaコース");
    assertThat(actual.getFirst().getStatus().getStatusId()).isEqualTo(1);
    assertThat(actual.getFirst().getStatus().getCourseId()).isEqualTo(1);
    assertThat(actual.getFirst().getStatus().getStatus()).isEqualTo(CourseStatus.仮申込);
    assertThat(actual.getFirst().getStatus().getTemporaryAppliedAt()).isEqualTo(testTime);
  }

  @Test
  void 全件検索_レポジトリ層から得た受講生とコース詳細が受講生詳細に正しく変換されること() {

    //事前準備
    Student student = new Student();
    String id = "550e8400-e29b-41d4-a716-446655440001";
    student.setStudentId(id);
    List<Student> studentList = List.of(student);

    StudentCourse course1 = new StudentCourse();
    course1.setStudentId(id);
    StudentCourseStatus status1 = new StudentCourseStatus();
    CourseDetail courseDetail1 = new CourseDetail();
    courseDetail1.setCourse(course1);
    courseDetail1.setStatus(status1);

    StudentCourse course2 = new StudentCourse();
    course2.setStudentId(id);
    StudentCourseStatus status2 = new StudentCourseStatus();
    CourseDetail courseDetail2 = new CourseDetail();
    courseDetail2.setCourse(course2);
    courseDetail2.setStatus(status2);

    List<CourseDetail> courseDetails = List.of(courseDetail1, courseDetail2);

    //実行
    List<StudentDetail> actual = sut.toStudentDetail(studentList, courseDetails);

    //検証
    assertThat(actual).hasSize(1);
    assertThat(actual.getFirst().getCourseList()).hasSize(2);
    assertThat(actual.getFirst().getStudent()).isEqualTo(student);
    assertThat(actual.getFirst().getCourseList().getFirst()).isEqualTo(courseDetail1);

  }

  @Test
  void 条件検索_受講生詳細に変換する際値が正しくマッピングされること() {

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
    List<Student> studentList = List.of(student);

    StudentCourse course = new StudentCourse();
    course.setCourseId(1);
    course.setStudentId("550e8400-e29b-41d4-a716-446655440001");
    course.setCourseName("Javaコース");
    StudentCourseStatus status = new StudentCourseStatus();
    status.setStatusId(1);
    status.setCourseId(1);
    status.setStatus(CourseStatus.仮申込);
    status.setTemporaryAppliedAt(LocalDateTime.parse("2025-10-01 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    status.setOfficialAppliedAt(null);
    status.setCourseStartedAt(null);
    status.setCourseCompletedAt(null);

    CourseDetail courseDetail = new CourseDetail();
    courseDetail.setCourse(course);
    courseDetail.setStatus(status);
    List<CourseDetail> courseDetails = List.of(courseDetail);

    //実行
    List<StudentDetail> actual = sut.toStudentDetailFromFilteringStudentDetail(studentList,
        courseDetails);

    //検証
    assertThat(actual).hasSize(1);

    StudentDetail studentDetail = actual.getFirst();
    Student actualStudent = studentDetail.getStudent();
    CourseDetail actualCourseDetail = studentDetail.getCourseList().getFirst();
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
    assertThat(actualStudent.getStudentIsDeleted()).isFalse();

    assertThat(actualCourse.getCourseId()).isEqualTo(1);
    assertThat(actualCourse.getStudentId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
    assertThat(actualCourse.getCourseName()).isEqualTo("Javaコース");

    assertThat(actualStatus.getStatusId()).isEqualTo(1);
    assertThat(actualStatus.getCourseId()).isEqualTo(1);
    assertThat(actualStatus.getStatus()).isEqualTo(CourseStatus.仮申込);
    assertThat(actualStatus.getTemporaryAppliedAt()).isEqualTo(
        LocalDateTime.parse("2025-10-01 09:00:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    assertThat(actualStatus.getOfficialAppliedAt()).isNull();
    assertThat(actualStatus.getCourseStartedAt()).isNull();
    assertThat(actualStatus.getCourseCompletedAt()).isNull();

  }
}
