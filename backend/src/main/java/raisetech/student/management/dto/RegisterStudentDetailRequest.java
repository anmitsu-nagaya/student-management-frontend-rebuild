package raisetech.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.dto.registerdata.RegisterStudentRequest;

/**
 * 登録時にリクエストされた受講生とコース詳細を扱うオブジェクト。
 */
@Schema(description = "登録リクエスト受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentDetailRequest {

  @Valid
  private RegisterStudentRequest student;
  @Valid
  private List<RegisterCourseDetailRequest> courseList;

}
