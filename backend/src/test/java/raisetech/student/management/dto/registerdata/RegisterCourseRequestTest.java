package raisetech.student.management.dto.registerdata;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterCourseRequestTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private RegisterCourseRequest registerCourse;

  @BeforeEach
  void before() {
    registerCourse = new RegisterCourseRequest();
  }

  @Test
  void コース名が未入力だとバリデーションエラーになること() {
    registerCourse.setCourseName("");

    Set<ConstraintViolation<RegisterCourseRequest>> violations =
        validator.validate(registerCourse);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("入力は必須です。");
  }

  @Test
  void コース名が文字数超過でバリデーションエラーになること() {
    registerCourse.setCourseName("a".repeat(51));

    Set<ConstraintViolation<RegisterCourseRequest>> violations =
        validator.validate(registerCourse);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("文字数が超過しています。");
  }


}
