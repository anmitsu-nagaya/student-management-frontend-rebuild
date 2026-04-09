package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生を扱うオブジェクト。
 */
@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  /**
   * 学生ID（主キー）。
   */
  @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
      message = "UUIDの形式が正しくありません。")
  private String studentId;
  /**
   * 学生のフルネーム。
   */
  @NotBlank(message = "入力は必須です。")
  @Size(max = 100, message = "文字数が超過しています。")
  private String studentFullName;
  /**
   * 学生のフリガナ（カタカナ）。
   */
  @NotBlank(message = "入力は必須です。")
  @Size(max = 100, message = "文字数が超過しています。")
  private String studentFurigana;
  /**
   * 学生のニックネーム。
   */
  @Size(max = 50, message = "文字数が超過しています。")
  private String studentNickname;
  /**
   * メールアドレス。
   */
  @NotBlank(message = "入力は必須です。")
  @Email(message = "電子メールアドレスとして正しい形式にしてください")
  @Size(max = 254, message = "文字数が超過しています。")
  private String email;
  /**
   * 地域（都道府県）。
   */
  @Size(max = 10, message = "文字数が超過しています。")
  private String prefecture;
  /**
   * 地域（市区町村）。
   */
  @Size(max = 50, message = "文字数が超過しています。")
  private String city;
  /**
   * 年齢。
   */
  @Min(value = 1, message = "値は1以上で入力してください。")
  private Integer age;
  /**
   * 性別。
   */
  @Size(max = 20, message = "文字数が超過しています。")
  private String gender;
  /**
   * 備考欄。
   */
  @Size(max = 500, message = "文字数が超過しています。")
  private String studentRemark;
  /**
   * 論理削除フラグ。
   */
  private Boolean studentIsDeleted;
}
