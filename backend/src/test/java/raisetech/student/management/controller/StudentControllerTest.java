package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.converter.DomainDtoConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.enums.CourseStatus;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.dto.RegisterStudentDetailRequest;
import raisetech.student.management.dto.registerdata.RegisterCourseRequest;
import raisetech.student.management.dto.registerdata.RegisterStudentRequest;
import raisetech.student.management.dto.updatedata.UpdateStatusRequest;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @MockitoBean
  private DomainDtoConverter converter;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private Student student;
  private StudentCourse studentCourse;
  private StudentDetail studentDetail;

  private RegisterStudentRequest registerStudent;
  private RegisterCourseRequest registerCourse;

  private RegisterStudentRequest registerStudentRequest;
  private RegisterCourseRequest registerCourseRequest;
  private RegisterStudentDetailRequest registerRequest;

  private UpdateStatusRequest updateStatusRequest;

  @BeforeEach
  void before() {
    student = new Student();
    studentCourse = new StudentCourse();
    studentDetail = new StudentDetail();

    registerStudent = new RegisterStudentRequest();
    registerCourse = new RegisterCourseRequest();

    registerStudentRequest = new RegisterStudentRequest();
    registerCourseRequest = new RegisterCourseRequest();
    registerRequest = new RegisterStudentDetailRequest();

    updateStatusRequest = new UpdateStatusRequest();
  }

  /**
   * 全件取得エンドポイントをコメントアウトしたのに伴いテストもコメントアウトします
   *
   * @throws Exception
   */
//  @Test
//  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
//    mockMvc.perform(MockMvcRequestBuilders.get("/api/students"))
//        .andExpect(status().isOk());
//
//    verify(service, times(1)).searchStudentList();
//  }
  @Test
  void 受講生詳細の条件検索が実行できて空のリストが返ってくること() throws Exception {
    String id = UUID.randomUUID().toString();
    mockMvc.perform(MockMvcRequestBuilders.get("/api/students?studentId=" + id))
        .andExpect(status().isOk());
    verify(service, times(1)).getFilteredStudents(id,
        null,
        null, null,
        null, null, null, null, null, null,
        null, null, null);
  }


  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                            "student": {
                                "studentFullName": "山田太郎",
                                "studentFurigana": "ヤマダタロウ",
                                "studentNickname": "たろちゃん",
                                "email": "yamada.taro@example.com",
                                "prefecture": "東京都",
                                "city": "渋谷区",
                                "age": 25,
                                "gender": "男性",
                                "studentRemark": "積極的に質問する学生"
                            },
                            "courseList": [
                                {
                                    "course": {
                                        "courseName": "Javaコース"
                                    }
                                }
                            ]
                    }
                    """
            ))
        .andExpect(status().isOk());

    verify(converter, times(1)).toStudentDetailfromRegisterStudentDetail(any());
    verify(service, times(1)).registerStudentDetailList(any());

  }


  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/api/students/550e8400-e29b-41d4-a716-446655440001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "student":{
                            "studentId" : "550e8400-e29b-41d4-a716-446655440001",
                            "studentFullName": "山田太郎",
                            "studentFurigana": "ヤマダタロウ",
                            "studentNickname": "たろちゃん",
                            "email": "yamada.taro@example.com",
                            "prefecture": "東京都",
                            "city": "渋谷区",
                            "age": 25,
                            "gender": "男性",
                            "studentRemark": "積極的に質問する学生",
                            "studentIsDeleted" : false
                        },
                        "courseList": [
                                {
                                    "course": {
                                        "courseId": 1,
                                        "studentId" : "550e8400-e29b-41d4-a716-446655440001",
                                        "courseName": "Javaコース"
                                    }
                                },
                                {
                                    "status": {
                                        "statusId": 1,
                                        "courseId": 1,
                                        "status": "受講中"
                                    }
                                }
                        ]
                    }
                    """
            ))
        .andExpect(status().isOk());
    verify(converter, times(1)).toStudentDetailfromUpdateStudentDetail(any());
    verify(service, times(1)).updateStudentDetailList(student.getStudentId(), any());

  }

  /**
   * このエンドポイントはコメントアウトしたためテストでもコメントアウトします
   *
   * @throws Exception
   */
//  @Test
//  void 存在しないURLにアクセスしたときにエラーレスポンスが返ること()
//      throws Exception {
//    mockMvc.perform(MockMvcRequestBuilders.get("/exception"))
//        .andExpect(status().is4xxClientError())
//        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
//  }
  @Test
  void リクエストのクエリパラメータに不正な値が渡されたときにエラーレスポンスが返ること()
      throws Exception {
    String id = "ID";
    mockMvc.perform(MockMvcRequestBuilders.get("/api/students?studentId=" + id))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(
            "getFilterStudentList.studentId: UUIDの形式が正しくありません。"));
    String name = "a".repeat(101);
    mockMvc.perform(MockMvcRequestBuilders.get("/api/students?studentFullName=" + name))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(
            "getFilterStudentList.studentFullName: 文字数が超過しています。"));
    String email = "aaa";
    mockMvc.perform(MockMvcRequestBuilders.get("/api/students?email=" + email))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(
            "getFilterStudentList.email: 電子メールアドレスとして正しい形式にしてください"));
  }

  @Test
  void リクエストボディに不正な入力があるときにバリデーションエラーレスポンスが返ること()
      throws Exception {
    String invalidJson = """
        {
          "student": {
            "studentFullName": "",
            "studentFurigana": "",
            "email": "invalid-email"
          }
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("入力値が不正です"))
        .andExpect(jsonPath("$.details.length()").value(3))
        .andExpect(jsonPath("$.details[0].field").exists())
        .andExpect(jsonPath("$.details[0].message").exists());


  }

  @Test
  void リクエストのJSON文法に問題があるときにエラーレスポンスが返ること() throws Exception {
    String invalidJson = """
        {
          "student": {
            "studentFullName": "山田太郎"
            "email": "yamada.taro@example.com"
          }
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(
            "リクエストのJson文法に問題があります：JSON parse error: Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"));

  }


  @Test
  void 登録リクエストの受講生詳細の受講生で適切な値を入力したときに入力チェックが正しく実行されて異常が発生しないこと() {
    registerStudent.setStudentFullName("山田太郎");
    registerStudent.setStudentFurigana("ヤマダタロウ");
    registerStudent.setStudentNickname("たろちゃん");
    registerStudent.setEmail("yamada.taro@example.com");
    registerStudent.setPrefecture("東京都");
    registerStudent.setCity("渋谷区");
    registerStudent.setAge(25);
    registerStudent.setStudentRemark("積極的に質問する学生");

    Set<ConstraintViolation<RegisterStudentRequest>> violations = validator.validate(
        registerStudent);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 登録リクエストのコース詳細で適切な値を入力したときに入力チェックが正しく実行されて異常が発生しないこと() {
    registerCourse.setCourseName("Javaコース");

    Set<ConstraintViolation<RegisterCourseRequest>> violations = validator.validate(
        registerCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 更新リクエストの申し込み状況で適切な値を入力したときに入力チェックが正しく実行されて異常が発生しないこと() {
    updateStatusRequest.setStatusId(1);
    updateStatusRequest.setCourseId(1);
    updateStatusRequest.setStatus(CourseStatus.本申込);

    Set<ConstraintViolation<UpdateStatusRequest>> violations =
        validator.validate(updateStatusRequest);

    assertThat(violations.size()).isEqualTo(0);
  }


}
