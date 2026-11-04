-- =========================================================
-- migrate_password_hashing.sql
-- Replaces plain-text password columns with salt + hashed
-- password columns, across all three user tables.
-- Run this once, after schema.sql/seed_data.sql have already run.
-- =========================================================

USE grade_management;

ALTER TABLE professors
    ADD COLUMN salt VARCHAR(255) NOT NULL DEFAULT '',
    ADD COLUMN password_hash VARCHAR(255) NOT NULL DEFAULT '',
DROP COLUMN password;

ALTER TABLE secretaries
    ADD COLUMN salt VARCHAR(255) NOT NULL DEFAULT '',
    ADD COLUMN password_hash VARCHAR(255) NOT NULL DEFAULT '',
DROP COLUMN password;

ALTER TABLE students
    ADD COLUMN salt VARCHAR(255) NOT NULL DEFAULT '',
    ADD COLUMN password_hash VARCHAR(255) NOT NULL DEFAULT '',
DROP COLUMN password;