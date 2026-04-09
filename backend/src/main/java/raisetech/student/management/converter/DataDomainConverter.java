package raisetech.student.management.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生・受講生コース情報・申し込み状況のデータ層オブジェクトを、 ドメイン層の受講生詳細およびコース詳細オブジェクトへ変換、 もしくはその逆変換を行うコンバーターです。
 */
@Component
public class DataDomainConverter {


  /**
   * コースに紐づく申し込み状況をマッピングします。
   *
   * @param courseList 受講生コース情報一覧
   * @param statusList 申し込み状況一覧
   * @return　コース詳細情報のリスト
   */
  public List<CourseDetail> toCourseWithStatus(List<StudentCourse> courseList,
      List<StudentCourseStatus> statusList) {
    Map<Integer, StudentCourseStatus> statusMap = statusList.stream()
        .collect(Collectors.toMap(StudentCourseStatus::getCourseId, s -> s));

    List<CourseDetail> courseDetails = new ArrayList<>();

    for (StudentCourse course : courseList) {
      CourseDetail courseDetail = new CourseDetail();
      courseDetail.setCourse(course);
      courseDetail.setStatus(statusMap.get(course.getCourseId()));
      courseDetails.add(courseDetail);
    }

    return courseDetails;
  }


  /**
   * 受講生に紐づくコース詳細をマッピングします。 コース詳細は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てます。
   *
   * @param studentList      受講生一覧
   * @param courseDetailList コース詳細のリスト
   * @return　受講生詳細情報のリスト
   */
  public List<StudentDetail> toStudentDetail(List<Student> studentList,
      List<CourseDetail> courseDetailList) {
    List<Student> students = studentList.stream()
        .filter(student -> student.getStudentId() != null)
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(Student::getStudentId, s -> s, (a, b) -> a),
            map -> new ArrayList<>(map.values())
        ));

    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<CourseDetail> convertStudentCourseList = courseDetailList.stream()
          .filter(studentCourse -> student.getStudentId()
              .equals(studentCourse.getCourse().getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  /**
   * 受講生に紐づくコース詳細をマッピングします。 コース詳細は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てます。
   * 受講生：コース情報＝1:Nであることから、検索結果に同じIDの受講生が存在する可能性があるため、重複削除を行います。
   *
   * @param studentList      受講生一覧
   * @param courseDetailList コース詳細のリスト
   * @return　受講生詳細情報のリスト
   */
  public List<StudentDetail> toStudentDetailFromFilteringStudentDetail(List<Student> studentList,
      List<CourseDetail> courseDetailList) {
    List<Student> students = studentList.stream()
        .filter(student -> student.getStudentId() != null)
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(Student::getStudentId, s -> s, (a, b) -> a),
            map -> new ArrayList<>(map.values())
        ));

    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<CourseDetail> convertStudentCourseList = courseDetailList.stream()
          .filter(studentCourse -> student.getStudentId()
              .equals(studentCourse.getCourse().getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

}
