package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.converter.DomainDtoConverter;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.dto.RegisterStudentDetailRequest;
import raisetech.student.management.dto.UpdateStudentDetailRequest;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@CrossOrigin(originPatterns = {
    "https://app.mitsuyonagaya-dev.com",
    "http://localhost:*"})
@Validated
@RestController
public class StudentController {

  private StudentService service;
  private DomainDtoConverter converter;

  /**
   * コンストラクタ
   *
   * @param service 受講生サービス
   */
  @Autowired
  public StudentController(StudentService service, DomainDtoConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   *
   * 講座では頻繁に使用しましたが、以下の理由により、コメントアウトします。 1.全件取得と条件取得はRESTAPI設計の観点からまとめるべきである
   * 2.実際フロントでは論理削除されたテーブルのみ受け取っており、このGETエンドポイントは使用していない
   * 3.必要になった際は、クエリパラメータの値がすべてnullである条件処理を条件検索用のGET処理内で行い、service.searchStudentList();を呼び出す
   *
   * @return List<StudentDetail>
   */
//  @Operation(
//      summary = "一覧検索",
//      description = "受講生の一覧を検索します。全件検索を行うので、条件指定は行いません。",
//      operationId = "getStudentList",
//      responses = {
//          @ApiResponse(
//              responseCode = "200",
//              description = "一覧表示検索成功。検索した全受講生のデータを返します。",
//              content = @Content(
//                  mediaType = "application/json",
//                  array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
//              )
//          )
//      }
//  )
//  @GetMapping("/api/students")
//  public List<StudentDetail> getStudentList() {
//    return service.searchStudentList();
//  }
  @Operation(
      summary = "条件検索",
      description = "受講生の一覧を検索します。クエリパラメータによる条件検索を行います。",
      operationId = "getFilterStudentList",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "検索結果一覧表示成功。検索された受講生のデータを返します。",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "検索結果一覧表示失敗。クエリパラメータに不正な値が入力されています。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class),
                  examples = @ExampleObject(
                      value = "getFilterStudentList.studentId: UUIDの形式が正しくありません。")
              )
          )
      }
  )
  @GetMapping("/api/students")
  public List<StudentDetail> getFilterStudentList(
      @RequestParam(required = false) @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
          message = "UUIDの形式が正しくありません。") String studentId,
      @RequestParam(required = false) @Size(max = 100, message = "文字数が超過しています。") String studentFullName,
      @RequestParam(required = false) @Size(max = 100, message = "文字数が超過しています。") String studentFurigana,
      @RequestParam(required = false) @Size(max = 50, message = "文字数が超過しています。") String studentNickname,
      @RequestParam(required = false) @Email(message = "電子メールアドレスとして正しい形式にしてください")
      @Size(max = 254, message = "文字数が超過しています。") String email,
      @RequestParam(required = false) @Size(max = 10, message = "文字数が超過しています。") String prefecture,
      @RequestParam(required = false) @Size(max = 50, message = "文字数が超過しています。") String city,
      @RequestParam(required = false) @Min(value = 1, message = "値は1以上で入力してください。") Integer ageFrom,
      @RequestParam(required = false) @Min(value = 1, message = "値は1以上で入力してください。") Integer ageTo,
      @RequestParam(required = false) @Size(max = 20, message = "文字数が超過しています。") String gender,
      @RequestParam(required = false) Boolean studentIsDeleted,
      @RequestParam(required = false) @Size(max = 50, message = "文字数が超過しています。") String courseName,
      @RequestParam(required = false) CourseStatus status
  ) {
    return service.getFilteredStudents(studentId, studentFullName, studentFurigana, studentNickname,
        email, prefecture, city, ageFrom, ageTo, gender, studentIsDeleted, courseName, status);
  }


  @Operation(
      summary = "受講生登録",
      description = "受講生情報とコース情報を登録します。",
      operationId = "registerStudent",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "登録する受講生の詳細情報。受講生ID・コースID・申込状況IDを自動採番・自動登録します。",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = RegisterStudentDetailRequest.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "登録成功。登録された受講生情報を返します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "リクエストボディに不正な値が入力されています。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class),
                  examples = {
                      @ExampleObject(
                          name = "ValidationError",
                          value = """
                              {
                                  "details": [
                                      {
                                          "field": "student.studentFullName",
                                          "message": "入力は必須です。"
                                      },
                                      {
                                          "field": "student.email",
                                          "message": "電子メールアドレスとして正しい形式にしてください"
                                      }
                                  ],
                                  "error": "入力値が不正です"
                              }"""
                      ),
                      @ExampleObject(
                          name = "JsonParseError",
                          value = "リクエストのJson文法に問題があります：JSON parse error: Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"
                      )
                  }
              )
          )
      }
  )
  @PostMapping("/api/students")
  public ResponseEntity<String> registerStudent(
      @RequestBody @Valid RegisterStudentDetailRequest request) {
    StudentDetail studentDetail = converter.toStudentDetailfromRegisterStudentDetail(request);
    service.registerStudentDetailList(studentDetail);
    return ResponseEntity.ok("登録処理が成功しました。");
  }


  @Operation(
      summary = "受講生更新",
      description = "受講生情報とコース情報を更新します。受講生IDとコースIDが必要です。",
      operationId = "updateStudent",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "更新する受講生の詳細情報。申し込み状況を更新した日時を記録します。",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UpdateStudentDetailRequest.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "更新成功。更新された受講生情報を返します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "リクエストボディに不正な値が入力されています。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class),
                  examples = {
                      @ExampleObject(
                          name = "ValidationError",
                          value = """
                              {
                                  "details": [
                                      {
                                          "field": "student.studentFullName",
                                          "message": "入力は必須です。"
                                      },
                                      {
                                          "field": "student.email",
                                          "message": "電子メールアドレスとして正しい形式にしてください"
                                      }
                                  ],
                                  "error": "入力値が不正です"
                              }"""
                      ),
                      @ExampleObject(
                          name = "JsonParseError",
                          value = "リクエストのJson文法に問題があります：JSON parse error: Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"
                      )
                  }
              )
          )
      }
  )
  @PutMapping("/api/students/{studentId}")
  public ResponseEntity<String> updateStudent(
      @PathVariable String studentId, @RequestBody @Valid UpdateStudentDetailRequest request) {

    StudentDetail studentDetail = converter.toStudentDetailfromUpdateStudentDetail(request);

    service.updateStudentDetailList(studentId, studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * エラーハンドリングのテスト用APIです。
   * 常にNotFoundExceptionをスローします。
   *
   * @throws NotFoundException
   */
//  @GetMapping("/exception")
//  public ResponseEntity<String> throwException() throws NotFoundException {
//    throw new NotFoundException("このAPIは現在利用できません。古いURLとなっています。");
//  }

}


