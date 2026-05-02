-- Seed data for weekly schedule PDF testing (MySQL)
-- Database: Activity_Management
-- Run this script, then in admin planning choose the same weekStart date.
-- Student export requires a real student in learner service with classId = 1.

USE Activity_Management;

-- Rooms
INSERT INTO room (room_id, name, capacity, level, is_available)
VALUES
  (1, 'R-C001', 30, 1, 1),
  (2, 'R-B101', 24, 2, 1),
  (3, 'R-A201', 20, 1, 1)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  capacity = VALUES(capacity),
  level = VALUES(level),
  is_available = VALUES(is_available);

-- Classes (entity table name is class)
INSERT INTO `class` (class_id, name, number_students, level)
VALUES
  (1, 'C001', 22, 'A1'),
  (2, 'C002', 18, 'B1')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  number_students = VALUES(number_students),
  level = VALUES(level);

-- Schedules for week starting Monday 2026-04-13
-- Tutor IDs here: 5 and 7 (adjust to IDs that exist in learner service)
INSERT INTO schedule (schedule_id, title, type, start_time, end_time, user_id, course_id, room_id, class_id)
VALUES
  (1001, 'Course Study: English Grammar Basics', 'COURSE', '2026-04-13 09:00:00', '2026-04-13 12:00:00', 5, 101, 1, 1),
  (1002, 'Course Study: Speaking Workshop',      'COURSE', '2026-04-14 14:00:00', '2026-04-14 17:00:00', 5, 102, 2, 1),
  (1003, 'Course Study: Listening Lab',          'COURSE', '2026-04-16 10:00:00', '2026-04-16 13:00:00', 5, 103, 1, 1),
  (1004, 'Course Study: Business English',       'COURSE', '2026-04-15 09:00:00', '2026-04-15 12:00:00', 7, 104, 3, 2),
  (1005, 'Course Study: TOEIC Prep',             'COURSE', '2026-04-17 13:00:00', '2026-04-17 16:00:00', 7, 105, 2, 2)
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  type = VALUES(type),
  start_time = VALUES(start_time),
  end_time = VALUES(end_time),
  user_id = VALUES(user_id),
  course_id = VALUES(course_id),
  room_id = VALUES(room_id),
  class_id = VALUES(class_id);

-- Optional cleanup command:
-- DELETE FROM schedule WHERE schedule_id BETWEEN 1001 AND 1005;
