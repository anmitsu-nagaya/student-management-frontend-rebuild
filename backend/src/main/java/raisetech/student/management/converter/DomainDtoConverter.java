package raisetech.student.management.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.domain.CourseDetail;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.dto.RegisterCourseDetailRequest;
import raisetech.student.management.dto.RegisterStudentDetailRequest;
import raisetech.student.management.dto.UpdateCourseDetailRequest;
import raisetech.student.management.dto.UpdateStudentDetailRequest;
import raisetech.student.management.dto.registerdata.RegisterStudentRequest;

/**
 * リクエストされた受講生詳細DTOオブジェクトを、 ドメイン層の受講生詳細オブジェクトへ変換、 もしくはその逆変換を行うコンバーターです。
 */
@Component
public class DomainDtoConverter {

  /**
   * 登録リクエストされた受講生詳細をマッピングします。申し込み状況のリクエストについては、登録時は不要なため、引数に加えていません。
   *
   * @param request 登録リクエストされた受講生詳細一覧
   * @return　ドメインで扱うための受講生詳細のリスト
   */
  public StudentDetail toStudentDetailfromRegisterStudentDetail(
      RegisterStudentDetailRequest request) {

    Student student = getStudent(request.getStudent());

    List<CourseDetail> courseDetails = new ArrayList<>();
    for (RegisterCourseDetailRequest registerCourseRequest : request.getCourseList()) {
      StudentCourse course = new StudentCourse();
      course.setCourseName(registerCourseRequest.getCourse().getCourseName());
      CourseDetail courseDetail = new CourseDetail();
      courseDetail.setCourse(course);
      courseDetails.add(courseDetail);
    }
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseDetails);

    return studentDetail;

  }

  private static Student getStudent(RegisterStudentRequest requestStudentList) {
    Student student = new Student();
    student.setStudentFullName(requestStudentList.getStudentFullName());
    student.setStudentFurigana(requestStudentList.getStudentFurigana());
    student.setStudentNickname(requestStudentList.getStudentNickname());
    student.setEmail(requestStudentList.getEmail());
    student.setPrefecture(requestStudentList.getPrefecture());
    student.setCity(requestStudentList.getCity());
    student.setAge(requestStudentList.getAge());
    student.setGender(requestStudentList.getGender());
    student.setStudentRemark(requestStudentList.getStudentRemark());
    return student;
  }

  /**
   * 更新リクエストされた受講生詳細をマッピングします。
   *
   * @param request 更新リクエストされた受講生詳細一覧
   * @return　ドメインで扱うための受講生詳細のリスト
   */
  public StudentDetail toStudentDetailfromUpdateStudentDetail(UpdateStudentDetailRequest request) {

    Student student = request.getStudent();

    List<CourseDetail> courseDetails = new ArrayList<>();
    for (UpdateCourseDetailRequest updateCourseRequest : request.getCourseList()) {

      StudentCourseStatus status = new StudentCourseStatus();
      status.setStatusId(updateCourseRequest.getStatus().getStatusId());
      status.setCourseId(updateCourseRequest.getStatus().getCourseId());
      status.setStatus(updateCourseRequest.getStatus().getStatus());

      CourseDetail courseDetail = new CourseDetail();
      courseDetail.setCourse(updateCourseRequest.getCourse());
      courseDetail.setStatus(status);
      courseDetails.add(courseDetail);
    }
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseDetails);

    return studentDetail;

  }

}
