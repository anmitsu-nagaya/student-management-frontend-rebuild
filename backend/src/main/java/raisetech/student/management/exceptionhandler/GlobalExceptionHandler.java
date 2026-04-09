package raisetech.student.management.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 例外ハンドリングを管理します。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   *
   * NotFoundExceptionをハンドリングして、HTTP400のレスポンスを返します。存在しないURLにアクセスした場合に発生します。
   *
   * @param e 発生したNotFoundException
   * @return　エラーメッセージを含むResponseEntity
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  /**
   *
   * ConstraintViolationExceptionをハンドリングして、HTTP400のレスポンスを返します。HTTPメソッドに渡されたクエリパラメータがバリデーションルールに違反した場合に発生します。
   *
   * @param e 発生したConstraintViolationException
   * @return　エラーメッセージを含むResponseEntity
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) {
    return ResponseEntity.badRequest()
        .body(e.getMessage());
  }

  /**
   *
   * MethodArgumentNotValidExceptionをハンドリングして、HTTP400のレスポンスを返します。リクエストボディのフィールドがバリデーションルールに違反した場合に発生します。
   *
   * @param e 発生したMethodArgumentNotValidException
   * @return　エラー詳細をMapとしてまとめたResponseEntity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    List<Map<String, String>> errors = e.getBindingResult().getFieldErrors().stream()
        .map(error -> Map.of(
            "field", error.getField(),
            "message", error.getDefaultMessage()
        ))
        .toList();

    Map<String, Object> body = Map.of(
        "error", "入力値が不正です",
        "details", errors
    );

    return ResponseEntity.badRequest().body(body);
  }

  /**
   *
   * HttpMessageNotReadableExceptionをハンドリングして、HTTP400のレスポンスを返します。Json形式の入力に誤りがあった場合に発生します。
   *
   * @param e 発生したHttpMessageNotReadableException
   * @return　エラーメッセージを含むResponseEntity
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {

    return ResponseEntity.badRequest()
        .body("リクエストのJson文法に問題があります：" + e.getMessage());

  }
}
