package raisetech.student.management.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentCourseTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private StudentCourse course;

  @BeforeEach
  void before() {
    course = new StudentCourse();
    course.setCourseName("Javaコース");
  }


  @Test
  void 正常系_全項目が正しい場合はバリデーションエラーにならないこと() {
    course.setCourseId(1);
    course.setStudentId("123e4567-e89b-12d3-a456-426614174000");
    course.setCourseName("Javaコース");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);
    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 異常系_UUID形式が不正な場合にエラーになること() {
    course.setStudentId("invalid-uuid");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);
    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("UUIDの形式が正しくありません。");
  }

  @Test
  void 異常系_必須項目が未入力の場合にエラーになること() {
    course.setCourseName("");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);
    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("入力は必須です。");
  }

}
