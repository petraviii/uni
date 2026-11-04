-- =========================================================
-- seed_data.sql
-- Dummy/sample data for the grade_management database.
-- Run this AFTER schema.sql. Order matters: parent tables
-- (professors, secretaries, students) must be populated
-- before child tables (courses, grades) that reference them.
-- =========================================================

USE grade_management;

-- Professors
INSERT INTO professors (username, password, name, surname, department) VALUES
                                                                           ('mpapas',   'pass123', 'Maria',   'Papas',   'Computer Science'),
                                                                           ('gkostas',  'pass123', 'Giorgos', 'Kostas',  'Mathematics'),
                                                                           ('avasili',  'pass123', 'Anna',    'Vasiliou','Physics');

-- Secretaries
INSERT INTO secretaries (username, password, name, surname, department) VALUES
                                                                            ('sregistry', 'secpass1', 'Eleni',  'Nikou',   'Registrar'),
                                                                            ('sadmin',    'secpass2', 'Petros', 'Antoniou','Registrar');

-- Students
INSERT INTO students (username, password, name, surname, department, registration_number) VALUES
                                                                                              ('jdoe',   'stud123', 'John',  'Doe',    'Computer Science', 10001),
                                                                                              ('mklein', 'stud123', 'Maria', 'Klein',  'Mathematics',      10002),
                                                                                              ('bsmith', 'stud123', 'Bob',   'Smith',  'Physics',          10003),
                                                                                              ('acohen', 'stud123', 'Alice', 'Cohen',  'Computer Science', 10004);

-- Courses (one deliberately left unassigned to test that path)
INSERT INTO courses (course_name, professor_id) VALUES
                                                    ('Intro to Programming',      1), -- Maria Papas
                                                    ('Discrete Mathematics',      2), -- Giorgos Kostas
                                                    ('Classical Mechanics',       3), -- Anna Vasiliou
                                                    ('Data Structures',           NULL); -- unassigned on purpose

-- Grades
INSERT INTO grades (student_id, course_id, semester, score) VALUES
                                                                (1, 1, '2026-Spring', 8.5),
                                                                (1, 2, '2026-Spring', 7.0),
                                                                (2, 2, '2026-Spring', 9.0),
                                                                (3, 3, '2026-Spring', 6.5),
                                                                (4, 1, '2026-Spring', 10.0);