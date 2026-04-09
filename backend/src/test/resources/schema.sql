CREATE SCHEMA IF NOT EXISTS StudentManagement;

CREATE TABLE StudentManagement.students (
  student_id char(36) PRIMARY KEY,
  student_full_name varchar(100) NOT NULL,
  student_furigana varchar(100) NOT NULL,
  student_nickname varchar(50) DEFAULT NULL,
  email varchar(254) NOT NULL UNIQUE,
  prefecture varchar(10),
  city varchar(50),
  age int,
  gender varchar(20),
  student_remark varchar(500),
  student_is_deleted boolean not null default false
);

CREATE TABLE StudentManagement.students_courses (
  course_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  student_id CHAR(36) NOT NULL,
  course_name VARCHAR(50) NOT NULL
);

CREATE TABLE StudentManagement.students_courses_status (
  status_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  course_id INT NOT NULL UNIQUE,
  status ENUM('仮申込', '本申込', '受講中', '受講修了') NOT NULL DEFAULT '仮申込',
  temporary_applied_at TIMESTAMP,
  official_applied_at TIMESTAMP,
  course_started_at TIMESTAMP,
  course_completed_at TIMESTAMP
);
