package raisetech.student.management.dto.registerdata;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 登録時にリクエストされた受講生コース情報を扱うオブジェクト。
 */
@Schema(description = "登録リクエスト受講生コース情報")
@Getter
@Setter
public class RegisterCourseRequest {

  /**
   * コース名。
   */
  @NotBlank(message = "入力は必須です。")
  @Size(max = 50, message = "文字数が超過しています。")
  private String courseName;

}
