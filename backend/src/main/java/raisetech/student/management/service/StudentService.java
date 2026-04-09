package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.converter.DataDomainConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private DataDomainConverter converter;

  /**
   * コンストラクタ
   *
   * @param repository 受講生テーブルと受講生コース情報テーブルと申し込み状況テーブルが紐づくリポジトリ
   * @param converter  受講生詳細を受講生やコース詳細、コース詳細をコース情報や申し込み状況、もしくはその逆の変換を行うコンバーター
   */
  @Autowired
  public StudentService(StudentRepository repository, DataDomainConverter converter
  ) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudentList();
    List<StudentCourse> courseList = repository.searchStudentCourseList();
    List<StudentCourseStatus> statusList = repository.searchStudentCourseStatusList();
    List<CourseDetail> courseDetails = converter.toCourseWithStatus(courseList,
        statusList);
    return converter.toStudentDetail(studentList, courseDetails);
  }

  /**
   * 受講生詳細検索です。
   *
   * @return 検索された受講生詳細
   */
  public List<StudentDetail> getFilteredStudents(
      String studentId,
      String studentFullName,
      String studentFurigana,
      String studentNickname,
      String email,
      String prefecture,
      String city,
      Integer ageFrom,
      Integer ageTo,
      String gender,
      Boolean studentIsDeleted,
      String courseName,
      CourseStatus status
  ) {

    List<StudentDetail> filterStudentDetailDB = repository.searchFilterStudentList(studentId,
        studentFullName,
        studentFurigana, studentNickname,
        email, prefecture, city, ageFrom, ageTo, gender,
        studentIsDeleted, courseName, status);

    List<Student> students = new ArrayList<>();
    List<CourseDetail> courseDetails = new ArrayList<>();
    for (StudentDetail studentDetail : filterStudentDetailDB) {
      students.add(studentDetail.getStudent());
      courseDetails.addAll(studentDetail.getCourseList());
    }

    return converter.toStudentDetailFromFilteringStudentDetail(students, courseDetails);

  }


  /**
   * 受講生詳細の登録を行います。 受講生とコース詳細を個別に登録し、コース詳細には受講生情報を紐づける値や日付情報・申し込み状況の初期値を設定します。
   * 受講生IDに対してUUIDの作成を行います。
   *
   * @param studentDetail リクエストされた登録内容を所持する受講生詳細
   * @return　登録情報を付与した受講生詳細
   */
  @Transactional
  public void registerStudentDetailList(StudentDetail studentDetail) {
    String id = UUID.randomUUID().toString();

    Student student = studentDetail.getStudent();
    student.setStudentId(id);
    repository.registerStudent(student);

    studentDetail.getCourseList().forEach(courseDetail -> {
      StudentCourse course = courseDetail.getCourse();
      course.setStudentId(id);
      repository.registerStudentCourse(course);

      StudentCourseStatus status = new StudentCourseStatus();
      status.setCourseId(course.getCourseId());
      status.setStatus(CourseStatus.仮申込);
      status.setTemporaryAppliedAt(LocalDateTime.now());
      repository.registerStudentCourseStatus(status);
    });
  }


  /**
   * 受講生詳細の更新を行います。 受講生とコース詳細をそれぞれ更新します。申し込み状況の更新時に日付情報を登録します。
   *
   * @param studentDetail 更新内容を所持する受講生詳細
   */
  @Transactional
  public void updateStudentDetailList(String studentId, StudentDetail studentDetail) {

    studentDetail.getStudent().setStudentId(studentId);
    repository.updateStudent(studentDetail.getStudent());

    studentDetail.getCourseList().forEach(courseDetail -> {
      StudentCourse course = courseDetail.getCourse();
      repository.updateStudentCourse(course);

      StudentCourseStatus status = courseDetail.getStatus();
      CourseStatus courseStatus = status.getStatus();
      LocalDateTime now = LocalDateTime.now();
      switch (courseStatus) {
        case 本申込 -> {
          status.setTemporaryAppliedAt(repository.searchStudentCourseStatus(status.getCourseId())
              .getTemporaryAppliedAt());
          status.setOfficialAppliedAt(now);
        }
        case 受講中 -> {
          status.setTemporaryAppliedAt(repository.searchStudentCourseStatus(status.getCourseId())
              .getTemporaryAppliedAt());
          status.setOfficialAppliedAt(repository.searchStudentCourseStatus(status.getCourseId())
              .getOfficialAppliedAt());
          status.setCourseStartedAt(now);
          status.setCourseCompletedAt(now.plusDays(300));
        }
        case 受講修了 -> {
          status.setTemporaryAppliedAt(repository.searchStudentCourseStatus(status.getCourseId())
              .getTemporaryAppliedAt());
          status.setOfficialAppliedAt(repository.searchStudentCourseStatus(status.getCourseId())
              .getOfficialAppliedAt());
          status.setCourseStartedAt(repository.searchStudentCourseStatus(status.getCourseId())
              .getCourseStartedAt());
          status.setCourseCompletedAt(now);
        }
      }
      repository.updateStudentCourseStatus(status);
    });
  }
}
