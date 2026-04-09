package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;

/**
 * 受講生コース情報と申し込み状況を扱うオブジェクト。
 */
@Schema(description = "コース詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetail {

  @Valid
  private StudentCourse course;
  @Valid
  private StudentCourseStatus status;

}

