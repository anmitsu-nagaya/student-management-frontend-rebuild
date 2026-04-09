package raisetech.student.management.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Student student;

  @BeforeEach
  void before() {
    student = new Student();
    student.setStudentFullName("山田太郎");
    student.setStudentFurigana("ヤマダタロウ");
    student.setEmail("test@example.com");
  }

  @Test
  void 正常系_全項目が正しい場合はバリデーションエラーにならないこと() {
    student.setStudentId("3b333f9d-993c-48c6-97ca-4a94bb7894b7");
    student.setStudentFullName("山田太郎");
    student.setStudentFurigana("ヤマダタロウ");
    student.setStudentNickname("たろちゃん");
    student.setEmail("yamada.taro@example.com");
    student.setPrefecture("東京都");
    student.setCity("渋谷区");
    student.setAge(25);
    student.setStudentRemark("積極的に質問する学生");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 異常系_UUID形式が不正な場合にエラーになること() {
    student.setStudentId("1234");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("UUIDの形式が正しくありません。");
  }

  @Test
  void 異常系_必須項目が未入力の場合にエラーになること() {
    student.setStudentFullName("");
    student.setStudentFurigana("");
    student.setEmail("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(3);
    assertThat(violations).extracting("message")
        .containsOnly("入力は必須です。");
  }

  @Test
  void 異常系_メールアドレスの形式が不正な場合にエラーになること() {
    student.setEmail("invalid-email");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("電子メールアドレスとして正しい形式にしてください");
  }

  @Test
  void 異常系_文字数超過でバリデーションエラーになること() {
    student.setStudentFullName("a".repeat(101));
    student.setCity("b".repeat(51));
    student.setStudentRemark("c".repeat(501));

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(3);
    assertThat(violations).extracting("message")
        .containsOnly("文字数が超過しています。");
  }

  @Test
  void 異常系_年齢が1未満の場合にエラーになること() {
    student.setAge(0);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("値は1以上で入力してください。");
  }
}
