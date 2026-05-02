-- Big planning seed for Schedule module
-- Target DB: Activity_Management (MySQL)
-- All schedules are assigned to tutor user_id = 2
-- Week used for testing: 2026-04-13 to 2026-04-19

USE Activity_Management;

-- Rooms
INSERT INTO room (room_id, name, capacity, level, is_available) VALUES
  (10, 'R-C001', 18, 1, 1),
  (11, 'R-C002', 20, 1, 1),
  (12, 'R-B101', 22, 2, 1),
  (13, 'R-B102', 25, 2, 1),
  (14, 'R-A201', 30, 3, 1),
  (15, 'R-A202', 35, 3, 1)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  capacity = VALUES(capacity),
  level = VALUES(level),
  is_available = VALUES(is_available);

-- Classes
INSERT INTO `class` (class_id, name, number_students, level) VALUES
  (10, 'C001', 16, 'A1'),
  (11, 'C002', 19, 'A2'),
  (12, 'C003', 21, 'B1'),
  (13, 'C004', 24, 'B2'),
  (14, 'C005', 26, 'C1'),
  (15, 'C006', 28, 'C2')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  number_students = VALUES(number_students),
  level = VALUES(level);

-- Schedules (all for tutor user_id = 2)
-- Keep times non-overlapping for tutor 2 in same week for realistic planning
INSERT INTO schedule (schedule_id, title, type, start_time, end_time, user_id, course_id, room_id, class_id) VALUES
  (2001, 'Course Study: A1 Grammar Foundations', 'COURSE', '2026-04-13 08:00:00', '2026-04-13 11:00:00', 2, 301, 10, 10),
  (2002, 'Course Study: A2 Speaking Practice',   'COURSE', '2026-04-13 12:00:00', '2026-04-13 15:00:00', 2, 302, 11, 11),
  (2003, 'Course Study: B1 Listening Lab',       'COURSE', '2026-04-13 16:00:00', '2026-04-13 19:00:00', 2, 303, 12, 12),

  (2004, 'Course Study: B2 Writing Workshop',    'COURSE', '2026-04-14 08:00:00', '2026-04-14 11:00:00', 2, 304, 13, 13),
  (2005, 'Course Study: C1 Business English',    'COURSE', '2026-04-14 12:00:00', '2026-04-14 15:00:00', 2, 305, 14, 14),
  (2006, 'Course Study: C2 Debate & Fluency',    'COURSE', '2026-04-14 16:00:00', '2026-04-14 19:00:00', 2, 306, 15, 15),

  (2007, 'Course Study: A1 Vocabulary Sprint',   'COURSE', '2026-04-15 08:00:00', '2026-04-15 11:00:00', 2, 307, 10, 10),
  (2008, 'Course Study: A2 Pronunciation Clinic','COURSE', '2026-04-15 12:00:00', '2026-04-15 15:00:00', 2, 308, 11, 11),
  (2009, 'Course Study: B1 Reading Skills',      'COURSE', '2026-04-15 16:00:00', '2026-04-15 19:00:00', 2, 309, 12, 12),

  (2010, 'Course Study: B2 Advanced Grammar',    'COURSE', '2026-04-16 08:00:00', '2026-04-16 11:00:00', 2, 310, 13, 13),
  (2011, 'Course Study: C1 Academic Writing',    'COURSE', '2026-04-16 12:00:00', '2026-04-16 15:00:00', 2, 311, 14, 14),
  (2012, 'Course Study: C2 Presentation Skills', 'COURSE', '2026-04-16 16:00:00', '2026-04-16 19:00:00', 2, 312, 15, 15),

  (2013, 'Course Study: A1 Communication Basics','COURSE', '2026-04-17 08:00:00', '2026-04-17 11:00:00', 2, 313, 10, 10),
  (2014, 'Course Study: A2 Interactive Speaking', 'COURSE','2026-04-17 12:00:00', '2026-04-17 15:00:00', 2, 314, 11, 11),
  (2015, 'Course Study: B1 Listening & Notes',   'COURSE', '2026-04-17 16:00:00', '2026-04-17 19:00:00', 2, 315, 12, 12),

  (2016, 'Course Study: B2 Practice Session',    'COURSE', '2026-04-18 08:00:00', '2026-04-18 11:00:00', 2, 316, 13, 13),
  (2017, 'Course Study: C1 Language Lab',        'COURSE', '2026-04-18 12:00:00', '2026-04-18 15:00:00', 2, 317, 14, 14),
  (2018, 'Course Study: C2 Mock Assessment',     'COURSE', '2026-04-18 16:00:00', '2026-04-18 19:00:00', 2, 318, 15, 15)
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  type = VALUES(type),
  start_time = VALUES(start_time),
  end_time = VALUES(end_time),
  user_id = VALUES(user_id),
  course_id = VALUES(course_id),
  room_id = VALUES(room_id),
  class_id = VALUES(class_id);

-- Quick checks
SELECT COUNT(*) AS rooms_seeded FROM room WHERE room_id BETWEEN 10 AND 15;
SELECT COUNT(*) AS classes_seeded FROM `class` WHERE class_id BETWEEN 10 AND 15;
SELECT COUNT(*) AS schedules_seeded_for_tutor2
FROM schedule
WHERE schedule_id BETWEEN 2001 AND 2018
  AND user_id = 2;

-- Optional cleanup
-- DELETE FROM schedule WHERE schedule_id BETWEEN 2001 AND 2018;
-- DELETE FROM room WHERE room_id BETWEEN 10 AND 15;
-- DELETE FROM `class` WHERE class_id BETWEEN 10 AND 15;
