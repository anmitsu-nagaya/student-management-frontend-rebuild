package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生テーブルと受講生コース情報テーブルと申し込み状況テーブルが紐づくリポジトリです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return　受講生一覧(全件)
   */
  List<Student> searchStudentList();

  /**
   * 受講生の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  Student searchStudent(@Param("studentId") String studentId);


  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報(全件)
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(@Param("studentId") String studentId);

  /**
   * 受講生のコース申し込み状況の全件検索を行います。
   *
   * @return　コース申し込み状況(全件)
   */
  List<StudentCourseStatus> searchStudentCourseStatusList();

  /**
   * コースIDに紐づくコース申し込み状況を検索します。
   *
   * @param courseId コースID
   * @return コースIDに紐づくコース申し込み状況
   */
  StudentCourseStatus searchStudentCourseStatus(@Param("courseId") Integer courseId);

  /**
   * 受講生詳細の条件検索を行います。
   *
   * @return 検索された受講生詳細一覧
   */
  List<StudentDetail> searchFilterStudentList(
      @Param("studentId") String studentId,
      @Param("studentFullName") String studentFullName,
      @Param("studentFurigana") String studentFurigana,
      @Param("studentNickname") String studentNickname,
      @Param("email") String email,
      @Param("prefecture") String prefecture,
      @Param("city") String city,
      @Param("ageFrom") Integer ageFrom,
      @Param("ageTo") Integer ageTo,
      @Param("gender") String gender,
      @Param("studentIsDeleted") Boolean studentIsDeleted,
      @Param("courseName") String courseName,
      @Param("status") CourseStatus status
  );

  /**
   * 受講生を新規登録します。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行います。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * コース申し込み状況を新規登録します。IDに関しては自動採番を行います。
   *
   * @param studentCourseStatus コース申し込み状況
   */
  void registerStudentCourseStatus(StudentCourseStatus studentCourseStatus);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * コース申し込み状況の申し込み状況を更新します。
   *
   * @param studentCourseStatus コース申し込み状況
   */
  void updateStudentCourseStatus(StudentCourseStatus studentCourseStatus);

}
