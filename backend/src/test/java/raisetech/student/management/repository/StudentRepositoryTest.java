package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.searchStudentList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の単一検索が行えること() {
    Student actual = sut.searchStudent("550e8400-e29b-41d4-a716-446655440001");
    assertThat(actual.getStudentFullName()).isEqualTo("山田太郎");
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生コース情報の単一検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourse("550e8400-e29b-41d4-a716-446655440001");
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.getFirst().getCourseName()).isEqualTo("Javaコース");
  }

  @Test
  void 受講生コース申し込み状況の全件検索が行えること() {
    List<StudentCourseStatus> actual = sut.searchStudentCourseStatusList();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生コース申し込み状況の単一検索が行えること() {
    StudentCourseStatus actual = sut.searchStudentCourseStatus(1);
    assertThat(actual.getStatus()).isEqualTo(CourseStatus.本申込);
    LocalDateTime expected = LocalDateTime.parse("2025-10-01 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    assertThat(actual.getTemporaryAppliedAt()).isEqualTo(expected);
  }

  @Test
  void 受講生詳細の条件検索_実行されてStudentDetail型の空のリストが返ること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null, null, null, null, null,
        null, null,
        null, null, null, null, null, null);
    assertThat(actual).isNotNull();
  }

  @Test
  void 受講生詳細の条件検索_正しく条件検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList("550e8400-e29b-41d4-a716-446655440001",
        null, null, null, null,
        null, null,
        null, null, null, null, "Javaコース", null);

    assertThat(actual).hasSize(1);

    StudentDetail detail = actual.getFirst();
    Student student = detail.getStudent();
    CourseDetail courseDetail = detail.getCourseList().getFirst();

    // --- student情報の検証 ---
    assertThat(student.getStudentId()).isEqualTo("550e8400-e29b-41d4-a716-446655440001");
    assertThat(student.getStudentFullName()).isEqualTo("山田太郎");
    assertThat(student.getStudentFurigana()).isEqualTo("ヤマダタロウ");
    assertThat(student.getStudentNickname()).isEqualTo("たろちゃん");
    assertThat(student.getEmail()).isEqualTo("yamada.taro@example.com");
    assertThat(student.getPrefecture()).isEqualTo("東京都");
    assertThat(student.getCity()).isEqualTo("渋谷区");
    assertThat(student.getAge()).isEqualTo(25);
    assertThat(student.getGender()).isEqualTo("男性");
    assertThat(student.getStudentRemark()).isEqualTo("積極的に質問する学生");
    assertThat(student.getStudentIsDeleted()).isFalse();

    // --- course情報の検証 ---
    assertThat(courseDetail.getCourse().getCourseId()).isEqualTo(1);
    assertThat(courseDetail.getCourse().getCourseName()).isEqualTo("Javaコース");
    assertThat(courseDetail.getStatus().getCourseId()).isEqualTo(1);
    assertThat(courseDetail.getStatus().getStatus()).isEqualTo(CourseStatus.本申込);
    assertThat(courseDetail.getStatus().getTemporaryAppliedAt())
        .isEqualTo(LocalDateTime.parse("2025-10-01T09:00:00"));
    assertThat(courseDetail.getStatus().getOfficialAppliedAt())
        .isEqualTo(LocalDateTime.parse("2025-10-05T09:00:00"));
    assertThat(courseDetail.getStatus().getCourseStartedAt()).isNull();
    assertThat(courseDetail.getStatus().getCourseCompletedAt()).isNull();
  }

  @Test
  void 受講生詳細の条件検索_受講生データのパラメータ検索でコース詳細データが紐づいて検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList("550e8400-e29b-41d4-a716-446655440001",
        null, null, null, null,
        null, null,
        null, null, null, null, null, null);
    assertThat(actual.getFirst().getCourseList()).isNotNull();
  }

  @Test
  void 受講生詳細の条件検索_受講生コース情報データのパラメータ検索で受講生データと受講生申し込み状況データが紐づいて検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        null, null,
        null, null, null, null, "Javaコース", null);
    assertThat(actual.getFirst().getStudent()).isNotNull();
    assertThat(actual.getFirst().getCourseList().getFirst().getStatus()).isNotNull();
  }

  @Test
  void 受講生詳細の条件検索_コース申し込み状況データのパラメータ検索で受講生データと受講生コース情報データが紐づいて検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        null, null,
        null, null, null, null, "Javaコース", null);
    assertThat(actual.getFirst().getStudent()).isNotNull();
    assertThat(actual.getFirst().getCourseList().getFirst().getCourse()).isNotNull();
  }

  @Test
  void 受講生詳細の条件検索_同一の受講生に対し複数コースが検索されたときコースと同じ数の受講生が検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList("550e8400-e29b-41d4-a716-446655440001",
        null, null, null, null,
        null, null,
        null, null, null, null, null, null);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 受講生詳細の条件検索_受講生フルネームが部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        "山", null, null, null,
        null, null, null,
        null, null, null, null, null);
    assertThat(actual.getFirst().getStudent().getStudentFullName()).isEqualTo("山田太郎");
  }

  @Test
  void 受講生詳細の条件検索_受講生フリガナが部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, "ヤマ", null, null, null,
        null, null,
        null, null, null, null, null);
    assertThat(actual.getFirst().getStudent().getStudentFurigana()).isEqualTo("ヤマダタロウ");
  }

  @Test
  void 受講生詳細の条件検索_受講生ニックネームが部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, "たろ", null, null,
        null, null,
        null, null, null, null, null);
    assertThat(actual.getFirst().getStudent().getStudentNickname()).isEqualTo("たろちゃん");
  }

  @Test
  void 受講生詳細の条件検索_emailが部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, "taro", null,
        null, null,
        null, null, null, null, null);
    assertThat(actual.getFirst().getStudent().getEmail()).isEqualTo("yamada.taro@example.com");
  }

  @Test
  void 受講生詳細の条件検索_都道府県が部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        "県", null,
        null, null, null, null, null, null);
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生詳細の条件検索_市区町村が部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        null, "市",
        null, null, null, null, null, null);
    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生詳細の条件検索_年齢が指定した範囲で正しく検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        null, null,
        25, 30, null, null, null, null);
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生詳細の条件検索_性別が部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        null, null,
        null, null, "男", null, null, null);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 受講生詳細の条件検索_コース名が部分一致で検索されること() {
    List<StudentDetail> actual = sut.searchFilterStudentList(null,
        null, null, null, null,
        null, null,
        null, null, null, null, "コース", null);
    assertThat(actual.size()).isEqualTo(10);
  }


  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setStudentId(UUID.randomUUID().toString());
    student.setStudentFullName("井上花子");
    student.setStudentFurigana("イノウエハナコ");
    student.setStudentNickname("はな");
    student.setEmail("hana@example.com");
    student.setPrefecture("東京都");
    student.setCity("渋谷区");
    student.setAge(20);
    student.setGender("女性");
    student.setStudentRemark("");
    student.setStudentIsDeleted(false);
    sut.registerStudent(student);

    List<Student> actual = sut.searchStudentList();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(11);
    studentCourse.setStudentId(UUID.randomUUID().toString());
    studentCourse.setCourseName("Javaコース");
    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void コース申し込み状況の登録が行えること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setStatusId(11);
    studentCourseStatus.setCourseId(11);
    studentCourseStatus.setStatus(CourseStatus.仮申込);
    studentCourseStatus.setTemporaryAppliedAt(LocalDateTime.parse("2025-10-01 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    studentCourseStatus.setOfficialAppliedAt(null);
    studentCourseStatus.setCourseStartedAt(null);
    studentCourseStatus.setCourseCompletedAt(null);
    sut.registerStudentCourseStatus(studentCourseStatus);

    List<StudentCourseStatus> actual = sut.searchStudentCourseStatusList();

    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = new Student();
    student.setStudentId("550e8400-e29b-41d4-a716-446655440001");
    student.setStudentFullName("山田太郎");
    student.setStudentFurigana("ヤマダタロウ");
    student.setStudentNickname("たろちゃん");
    student.setEmail("yamada.taro@example.com");
    student.setPrefecture("東京都");
    student.setCity("新宿区");
    student.setAge(26);
    student.setGender("男性");
    student.setStudentRemark("積極的に質問する学生");
    student.setStudentIsDeleted(false);

    sut.updateStudent(student);

    Student actual = sut.searchStudent("550e8400-e29b-41d4-a716-446655440001");

    assertThat(actual.getAge()).isEqualTo(26);
    assertThat(actual.getCity()).isEqualTo("新宿区");
  }

  @Test
  void 受講生コース情報の更新が行えること() {

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(1);
    studentCourse.setStudentId("550e8400-e29b-41d4-a716-446655440001");
    studentCourse.setCourseName("AWSコース");
    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("550e8400-e29b-41d4-a716-446655440001");

    assertThat(actual.getFirst().getCourseName()).isEqualTo("AWSコース");
  }

  @Test
  void コース申し込み状況の更新が行えること() {

    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setStatusId(1);
    studentCourseStatus.setCourseId(1);
    studentCourseStatus.setStatus(CourseStatus.本申込);
    studentCourseStatus.setTemporaryAppliedAt(LocalDateTime.parse("2025-10-01 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    studentCourseStatus.setOfficialAppliedAt(LocalDateTime.parse("2025-10-05 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    studentCourseStatus.setCourseStartedAt(null);
    studentCourseStatus.setCourseCompletedAt(null);
    sut.updateStudentCourseStatus(studentCourseStatus);

    StudentCourseStatus actual = sut.searchStudentCourseStatus(1);

    assertThat(actual.getStatus()).isEqualTo(CourseStatus.本申込);
    assertThat(actual.getOfficialAppliedAt()).isEqualTo(LocalDateTime.parse("2025-10-05 09:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }
}
