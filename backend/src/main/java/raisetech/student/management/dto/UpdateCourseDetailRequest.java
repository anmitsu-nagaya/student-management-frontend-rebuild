package raisetech.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.dto.updatedata.UpdateStatusRequest;

/**
 * 更新時にリクエストされた受講生コース情報と申し込み状況を扱うオブジェクト。
 */
@Schema(description = "更新リクエストコース詳細")
@Getter
@Setter
public class UpdateCourseDetailRequest {

  @Valid
  private StudentCourse course;
  @Valid
  private UpdateStatusRequest status;
}
