-- =========================================================
-- schema.sql
-- Grade Management System — Database Schema
-- Run this in MySQL Workbench to create the database and
-- all tables, before running seed_data.sql.
-- =========================================================

CREATE DATABASE IF NOT EXISTS grade_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE grade_management;

-- -----------------------------------------------------
-- Table: professors
-- -----------------------------------------------------
CREATE TABLE professors (
                            professor_id INT AUTO_INCREMENT PRIMARY KEY,
                            username     VARCHAR(50)  NOT NULL UNIQUE,
                            password     VARCHAR(255) NOT NULL, -- replace with password_hash + salt
                            name         VARCHAR(50)  NOT NULL,
                            surname      VARCHAR(50)  NOT NULL,
                            department   VARCHAR(100) NOT NULL
);

-- -----------------------------------------------------
-- Table: secretaries
-- -----------------------------------------------------
CREATE TABLE secretaries (
                             secretary_id INT AUTO_INCREMENT PRIMARY KEY,
                             username     VARCHAR(50)  NOT NULL UNIQUE,
                             password     VARCHAR(255) NOT NULL, -- replace with password_hash + salt
                             name         VARCHAR(50)  NOT NULL,
                             surname      VARCHAR(50)  NOT NULL,
                             department   VARCHAR(100) NOT NULL
);

-- -----------------------------------------------------
-- Table: students
-- -----------------------------------------------------
CREATE TABLE students (
                          student_id          INT AUTO_INCREMENT PRIMARY KEY,
                          username             VARCHAR(50)  NOT NULL UNIQUE,
                          password             VARCHAR(255) NOT NULL, -- replace with password_hash + salt
                          name                 VARCHAR(50)  NOT NULL,
                          surname              VARCHAR(50)  NOT NULL,
                          department           VARCHAR(100) NOT NULL,
                          registration_number  INT          NOT NULL UNIQUE
);

-- -----------------------------------------------------
-- Table: courses
-- professor_id is NULLABLE: a course can exist before a
-- professor is assigned to teach it.
-- -----------------------------------------------------
CREATE TABLE courses (
                         course_id    INT AUTO_INCREMENT PRIMARY KEY,
                         course_name  VARCHAR(100) NOT NULL,
                         professor_id INT NULL,
                         CONSTRAINT fk_courses_professor
                             FOREIGN KEY (professor_id) REFERENCES professors(professor_id)
                                 ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Table: grades
-- Both foreign keys are required: a grade cannot exist
-- without a student and a course.
-- -----------------------------------------------------
CREATE TABLE grades (
                        grade_id   INT AUTO_INCREMENT PRIMARY KEY,
                        student_id INT NOT NULL,
                        course_id  INT NOT NULL,
                        semester   VARCHAR(20) NOT NULL,
                        score      DECIMAL(4,2) NOT NULL,
                        CONSTRAINT fk_grades_student
                            FOREIGN KEY (student_id) REFERENCES students(student_id)
                                ON DELETE CASCADE,
                        CONSTRAINT fk_grades_course
                            FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                ON DELETE CASCADE
);