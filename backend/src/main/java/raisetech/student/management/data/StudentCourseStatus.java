package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.enums.CourseStatus;

/**
 * 申し込み状況を扱うオブジェクト。
 */
@Schema(description = "受講生コース申し込み状況")
@Getter
@Setter
public class StudentCourseStatus {

  /**
   * 申し込み状況ID。
   */
  private int statusId;
  /**
   * コースID。
   */
  private int courseId;
  /**
   * 申し込み状況。
   */
  @NotNull(message = "入力は必須です。")
  private CourseStatus status;
  /**
   * 仮申込日。
   */
  private LocalDateTime temporaryAppliedAt;
  /**
   * 本申込日。
   */
  private LocalDateTime officialAppliedAt;
  /**
   * 受講開始日。
   */
  private LocalDateTime courseStartedAt;
  /**
   * 受講終了日。
   */
  private LocalDateTime courseCompletedAt;
}
