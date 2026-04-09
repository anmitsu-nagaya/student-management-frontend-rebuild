package raisetech.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;

/**
 * 更新時にリクエストされた受講生とコース詳細を扱うオブジェクト。
 */
@Schema(description = "更新リクエスト受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentDetailRequest {

  @Valid
  private Student student;
  @Valid
  private List<UpdateCourseDetailRequest> courseList;

}
