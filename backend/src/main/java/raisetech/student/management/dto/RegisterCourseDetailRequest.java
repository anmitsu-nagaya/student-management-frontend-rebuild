package raisetech.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.dto.registerdata.RegisterCourseRequest;

/**
 * 登録時にリクエストされた受講生コース情報と申し込み状況を扱うオブジェクト。
 */
@Schema(description = "登録リクエストコース詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCourseDetailRequest {

  @Valid
  private RegisterCourseRequest course;


}
