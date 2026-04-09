package raisetech.student.management.dto.updatedata;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateStatusRequestTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private UpdateStatusRequest status;

  @BeforeEach
  void before() {
    status = new UpdateStatusRequest();
  }

  @Test
  void ステータスが未入力の場合にバリデーションエラーになること() {
    status.setStatus(null);

    Set<ConstraintViolation<UpdateStatusRequest>> violations =
        validator.validate(status);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("入力は必須です。");
  }

}
