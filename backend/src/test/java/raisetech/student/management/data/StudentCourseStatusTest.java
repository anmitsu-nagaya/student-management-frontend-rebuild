package raisetech.student.management.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.enums.CourseStatus;

class StudentCourseStatusTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private StudentCourseStatus status;

  @BeforeEach
  void before() {
    status = new StudentCourseStatus();
  }

  @Test
  void 正常系_ステータスが正常な場合にバリデーションエラーが発生しないこと() {
    status.setStatus(CourseStatus.本申込);

    Set<ConstraintViolation<StudentCourseStatus>> violations =
        validator.validate(status);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void ステータスが未入力の場合にバリデーションエラーになること() {
    status.setStatus(null);

    Set<ConstraintViolation<StudentCourseStatus>> violations =
        validator.validate(status);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("入力は必須です。");
  }
}
