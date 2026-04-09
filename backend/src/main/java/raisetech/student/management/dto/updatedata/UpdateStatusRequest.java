package raisetech.student.management.dto.updatedata;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.enums.CourseStatus;

/**
 * 更新時にリクエストされた申し込み状況を扱うオブジェクト。
 */
@Schema(description = "更新リクエスト申し込み状況")
@Getter
@Setter
public class UpdateStatusRequest {

  /**
   * 申し込み状況ID。
   */
  @NotNull(message = "入力は必須です。")
  private int statusId;
  /**
   * コースID。
   */
  @NotNull(message = "入力は必須です。")
  private int courseId;
  /**
   * 申し込み状況。
   */
  @NotNull(message = "入力は必須です。")
  private CourseStatus status;

}
