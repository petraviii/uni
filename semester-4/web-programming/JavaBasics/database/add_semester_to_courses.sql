-- =========================================================
-- add_semester_to_courses.sql
-- Adds a semester column to courses, satisfying Phase 3's
-- requirement that Secretary's course view shows title,
-- semester, and responsible professor.
-- =========================================================

USE grade_management;

ALTER TABLE courses
    ADD COLUMN semester VARCHAR(20) NOT NULL DEFAULT '2026-Spring';

-- Optional: adjust individual courses if they should show a
-- different semester than the default. All your seeded courses
-- currently only have grades in 2026-Spring, so the default fits.