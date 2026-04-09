package raisetech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.converter.DataDomainConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private DataDomainConverter dataDomainConverter;


  @Captor
  ArgumentCaptor<StudentCourse> courseCaptor;

  @Captor
  ArgumentCaptor<StudentCourseStatus> statusCaptor;

  private StudentService sut;
  private Student student;
  private StudentCourse studentCourse;
  private StudentCourseStatus studentCourseStatus;
  private String id;
  private StudentDetail studentDetail;
  private CourseDetail courseDetail;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, dataDomainConverter);
    student = new Student();
    studentCourse = new StudentCourse();
    studentCourseStatus = new StudentCourseStatus();
    studentDetail = new StudentDetail();
    courseDetail = new CourseDetail();
    id = "test-id";
  }


  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーター処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> courseList = new ArrayList<>();
    List<StudentCourseStatus> statusList = new ArrayList<>();
    List<CourseDetail> courseDetails = new ArrayList<>();
    List<StudentDetail> studentDetails = new ArrayList<>();
    when(repository.searchStudentList()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(courseList);
    when(repository.searchStudentCourseStatusList()).thenReturn(statusList);
    when(dataDomainConverter.toCourseWithStatus(courseList, statusList)).thenReturn(courseDetails);
    when(dataDomainConverter.toStudentDetail(studentList, courseDetails)).thenReturn(
        studentDetails);

    sut.searchStudentList();

    verify(repository, times(1)).searchStudentList();
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchStudentCourseStatusList();
    verify(dataDomainConverter, times(1)).toCourseWithStatus(courseList, statusList);
    verify(dataDomainConverter, times(1)).toStudentDetail(studentList, courseDetails);

  }

  @Test
  void 受講生詳細の条件検索_リポジトリとマッパーの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<CourseDetail> courseDetails = new ArrayList<>();
    List<StudentDetail> studentDetails = new ArrayList<>();
    when(repository.searchFilterStudentList(null,
        null,
        null, null,
        null, null, null, null, null, null,
        null, null, null)).thenReturn(new ArrayList<>());
    when(dataDomainConverter.toStudentDetailFromFilteringStudentDetail(studentList,
        courseDetails)).thenReturn(
        studentDetails);

    sut.getFilteredStudents(null,
        null,
        null, null,
        null, null, null, null, null, null,
        null, null, null);

    verify(repository, times(1)).searchFilterStudentList(null,
        null,
        null, null,
        null, null, null, null, null, null,
        null, null, null);
    verify(dataDomainConverter, times(1)).toStudentDetailFromFilteringStudentDetail(studentList,
        courseDetails);
  }

  @Test
  void 受講生詳細の条件検索_正しく検索が行われていること() {
    student.setStudentId(id);
    studentCourse.setCourseName("test");
    studentCourseStatus.setStatus(CourseStatus.本申込);
    courseDetail.setCourse(studentCourse);
    courseDetail.setStatus(studentCourseStatus);
    List<Student> studentList = List.of(student);
    List<CourseDetail> courseDetails = List.of(courseDetail);
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseDetails);
    List<StudentDetail> studentDetails = List.of(studentDetail);
    when(repository.searchFilterStudentList(id,
        null,
        null, null,
        null, null, null, null, null, null,
        null, "test", CourseStatus.本申込)).thenReturn(studentDetails);
    when(dataDomainConverter.toStudentDetailFromFilteringStudentDetail(studentList,
        courseDetails)).thenReturn(
        studentDetails);

    List<StudentDetail> actual = sut.getFilteredStudents(id,
        null,
        null, null,
        null, null, null, null, null, null,
        null, "test", CourseStatus.本申込);
    List<StudentDetail> expected = List.of(studentDetail);

    verify(repository, times(1)).searchFilterStudentList(id,
        null,
        null, null,
        null, null, null, null, null, null,
        null, "test", CourseStatus.本申込);
    verify(dataDomainConverter, times(1)).toStudentDetailFromFilteringStudentDetail(studentList,
        courseDetails);

    StudentDetail actualDetail = actual.getFirst();
    StudentDetail expectedDetail = expected.getFirst();

    Student actualStudent = actualDetail.getStudent();
    Student expectedStudent = expectedDetail.getStudent();

    assertThat(actualStudent.getStudentId()).isEqualTo(expectedStudent.getStudentId());
    assertThat(actualStudent.getStudentFullName()).isEqualTo(expectedStudent.getStudentFullName());
    assertThat(actualStudent.getStudentFurigana()).isEqualTo(expectedStudent.getStudentFurigana());
    assertThat(actualStudent.getStudentNickname()).isEqualTo(expectedStudent.getStudentNickname());
    assertThat(actualStudent.getEmail()).isEqualTo(expectedStudent.getEmail());
    assertThat(actualStudent.getPrefecture()).isEqualTo(expectedStudent.getPrefecture());
    assertThat(actualStudent.getCity()).isEqualTo(expectedStudent.getCity());
    assertThat(actualStudent.getAge()).isEqualTo(expectedStudent.getAge());
    assertThat(actualStudent.getGender()).isEqualTo(expectedStudent.getGender());
    assertThat(actualStudent.getStudentRemark()).isEqualTo(expectedStudent.getStudentRemark());
    assertThat(actualStudent.getStudentIsDeleted()).isEqualTo(
        expectedStudent.getStudentIsDeleted());

    CourseDetail actualCourseDetail = actualDetail.getCourseList().getFirst();
    CourseDetail expectedCourseDetail = expectedDetail.getCourseList().getFirst();

    StudentCourse actualCourse = actualCourseDetail.getCourse();
    StudentCourse expectedCourse = expectedCourseDetail.getCourse();

    assertThat(actualCourse.getCourseId()).isEqualTo(expectedCourse.getCourseId());
    assertThat(actualCourse.getStudentId()).isEqualTo(expectedCourse.getStudentId());
    assertThat(actualCourse.getCourseName()).isEqualTo(expectedCourse.getCourseName());

    StudentCourseStatus actualStatus = actualCourseDetail.getStatus();
    StudentCourseStatus expectedStatus = expectedCourseDetail.getStatus();

    assertThat(actualStatus.getStatusId()).isEqualTo(expectedStatus.getStatusId());
    assertThat(actualStatus.getCourseId()).isEqualTo(expectedStatus.getCourseId());
    assertThat(actualStatus.getStatus()).isEqualTo(expectedStatus.getStatus());
    assertThat(actualStatus.getTemporaryAppliedAt()).isEqualTo(
        expectedStatus.getTemporaryAppliedAt());
    assertThat(actualStatus.getOfficialAppliedAt()).isEqualTo(
        expectedStatus.getOfficialAppliedAt());
    assertThat(actualStatus.getCourseStartedAt()).isEqualTo(expectedStatus.getCourseStartedAt());
    assertThat(actualStatus.getCourseCompletedAt()).isEqualTo(
        expectedStatus.getCourseCompletedAt());
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること() {
    registerExtracted();
    sut.registerStudentDetailList(studentDetail);

    verify(repository, times(1)).registerStudent(any(Student.class));
    verify(repository, times(1)).registerStudentCourse(any(StudentCourse.class));
    verify(repository, times(1)).registerStudentCourseStatus(any(StudentCourseStatus.class));
  }


  @Test
  void 受講生詳細の登録_受講生IDに正しいIDが設定されること() {
    registerExtracted();
    sut.registerStudentDetailList(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);

    assertThat(studentDetail.getStudent().getStudentId()).isNotNull();
    assertThat(studentDetail.getStudent().getStudentId()).matches(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    assertThat(studentDetail.getCourseList().getFirst().getCourse().getStudentId()).isNotNull();
    assertThat(studentDetail.getCourseList().getFirst().getCourse().getStudentId()).matches(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

  }

  @Test
  void 受講生詳細の登録_コース詳細のデータマッピングが正しいこと() {
    registerExtracted();
    sut.registerStudentDetailList(studentDetail);

    verify(repository, times(1)).registerStudentCourse(courseCaptor.capture());
    verify(repository, times(1)).registerStudentCourseStatus(statusCaptor.capture());

    StudentCourse courseCaptured = courseCaptor.getValue();
    assertThat(courseCaptured.getStudentId()).isEqualTo(studentDetail.getStudent().getStudentId());
    assertThat(courseCaptured.getCourseName()).isEqualTo(courseDetail.getCourse().getCourseName());
    StudentCourseStatus statusCaptured = statusCaptor.getValue();
    assertThat(statusCaptured.getCourseId()).isEqualTo(studentCourse.getCourseId());
    assertThat(statusCaptured.getStatus()).isEqualTo(CourseStatus.仮申込);
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime actual = statusCaptured.getTemporaryAppliedAt();
    assertThat(Duration.between(actual, now).abs().getSeconds()).isLessThan(3);
  }

  private void registerExtracted() {
    studentDetail.setStudent(student);
    courseDetail.setCourse(studentCourse);
    courseDetail.setStatus(studentCourseStatus);
    List<CourseDetail> courseDetails = List.of(courseDetail);
    studentDetail.setCourseList(courseDetails);
  }

  @Test
  void 受講生詳細の更新_リポジトリが適切に呼び出せていること() {
    updateExtracted();
    when(repository.searchStudentCourseStatus(any(Integer.class))).thenReturn(studentCourseStatus);

    sut.updateStudentDetailList(student.getStudentId(), studentDetail);

    verify(repository, times(1)).updateStudent(any(Student.class));
    verify(repository, times(1)).updateStudentCourse(any(StudentCourse.class));
    verify(repository, times(2)).searchStudentCourseStatus(any(Integer.class));
    verify(repository, times(1)).updateStudentCourseStatus(any(StudentCourseStatus.class));

  }

  @Test
  void 受講生詳細の更新_コース詳細のデータマッピングが正しいこと() {
    updateExtracted();
    studentCourseStatus.setCourseId(3);
    when(repository.searchStudentCourseStatus(3)).thenReturn(studentCourseStatus);

    sut.updateStudentDetailList(student.getStudentId(), studentDetail);

    verify(repository, times(1)).updateStudentCourse(courseCaptor.capture());
    verify(repository, times(2)).searchStudentCourseStatus(3);
    verify(repository, times(1)).updateStudentCourseStatus(statusCaptor.capture());

    assertThat(courseCaptor.getValue()).isEqualTo(studentCourse);

    StudentCourseStatus statusCaptured = statusCaptor.getValue();
    assertThat(statusCaptured.getStatusId()).isEqualTo(studentCourseStatus.getStatusId());
    assertThat(statusCaptured.getCourseId()).isEqualTo(studentCourseStatus.getCourseId());
    assertThat(statusCaptured.getStatus()).isEqualTo(studentCourseStatus.getStatus());

    assertThat(statusCaptured.getTemporaryAppliedAt()).isEqualTo(
        studentCourseStatus.getTemporaryAppliedAt());
    assertThat(statusCaptured.getOfficialAppliedAt()).isEqualTo(
        studentCourseStatus.getOfficialAppliedAt());

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime courseStartedAtActual = statusCaptured.getCourseStartedAt();
    assertThat(Duration.between(courseStartedAtActual, now).abs().getSeconds()).isLessThan(3);
    LocalDateTime courseCompletedAtActual = statusCaptured.getCourseCompletedAt();
    assertThat(Duration.between(courseCompletedAtActual, now.plusDays(300)).abs()
        .getSeconds()).isLessThan(3);


  }

  private void updateExtracted() {
    studentDetail.setStudent(student);
    courseDetail.setCourse(studentCourse);
    studentCourseStatus.setStatus(CourseStatus.受講中);
    courseDetail.setStatus(studentCourseStatus);
    List<CourseDetail> courseDetails = List.of(courseDetail);
    studentDetail.setCourseList(courseDetails);
  }
}
