package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生コース情報を扱うオブジェクト。
 */
@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  /**
   * コースID。
   */
  private int courseId;
  /**
   * 受講生ID。
   */
  @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
      message = "UUIDの形式が正しくありません。")
  private String studentId;
  /**
   * コース名。
   */
  @NotBlank(message = "入力は必須です。")
  @Size(max = 50, message = "文字数が超過しています。")
  private String courseName;


}
