package raisetech.student.management.dto.registerdata;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterStudentRequestTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private RegisterStudentRequest registerStudent;

  @BeforeEach
  void before() {
    registerStudent = new RegisterStudentRequest();
    registerStudent.setStudentFullName("山田太郎");
    registerStudent.setStudentFurigana("ヤマダタロウ");
    registerStudent.setEmail("test@example.com");
  }

  @Test
  void 必須項目が空だとバリデーションエラーになること() {
    registerStudent.setStudentFullName("");
    registerStudent.setStudentFurigana("");
    registerStudent.setEmail("");

    Set<ConstraintViolation<RegisterStudentRequest>> violations =
        validator.validate(registerStudent);

    assertThat(violations.size()).isEqualTo(3);
    assertThat(violations).extracting("message").containsOnly("入力は必須です。");
  }

  @Test
  void 文字数超過でバリデーションエラーになること() {
    registerStudent.setStudentFullName("a".repeat(101));
    registerStudent.setStudentNickname("b".repeat(51));
    registerStudent.setStudentRemark("c".repeat(501));

    Set<ConstraintViolation<RegisterStudentRequest>> violations =
        validator.validate(registerStudent);

    assertThat(violations.size()).isEqualTo(3);
    assertThat(violations).extracting("message").containsOnly("文字数が超過しています。");
  }


  @Test
  void 年齢が1未満だとバリデーションエラーになること() {
    registerStudent.setAge(0);

    Set<ConstraintViolation<RegisterStudentRequest>> violations =
        validator.validate(registerStudent);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("値は1以上で入力してください。");
  }
  

}
