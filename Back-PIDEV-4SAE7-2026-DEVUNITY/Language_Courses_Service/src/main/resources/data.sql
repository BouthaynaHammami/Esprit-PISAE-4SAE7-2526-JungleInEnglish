-- Insert Level Children (Age Groups)
INSERT IGNORE INTO level_children (level_id, name, min_age, max_age, description, order_index) VALUES
(1, 'Little Learners', 4, 6, 'Perfect for preschool and kindergarten kids', 1),
(2, 'Young Explorers', 7, 9, 'Elementary school level activities', 2),
(3, 'Junior Masters', 10, 12, 'Advanced activities for pre-teens', 3);

-- Insert Courses
INSERT IGNORE INTO course (course_id, title, description, level_id, price) VALUES
(1, 'English Basics for Kids', 'Learn basic English vocabulary and grammar', 1, 0.0),
(2, 'Fun with Words', 'Expand vocabulary through fun activities', 2, 0.0),
(3, 'Grammar Adventures', 'Master English grammar rules', 3, 0.0);

-- Insert Activities
INSERT IGNORE INTO activity (activity_id, title, description, category, difficulty, type, content_url, image_url, icon_class, xp_reward, estimated_time, course_id) VALUES
-- Vocabulary Activities
(1, 'Animal Names', 'Learn the names of common animals', 'VOCABULARY', 'BEGINNER', 'QUIZ', '/content/animals', '/images/animals.jpg', 'bi-emoji-smile', 50, 10, 1),
(2, 'Colors and Shapes', 'Identify different colors and shapes', 'VOCABULARY', 'BEGINNER', 'GAME', '/content/colors', '/images/colors.jpg', 'bi-palette', 50, 15, 1),
(3, 'Food and Drinks', 'Learn vocabulary about food and beverages', 'VOCABULARY', 'BEGINNER', 'QUIZ', '/content/food', '/images/food.jpg', 'bi-cup-straw', 60, 12, 1),

-- Grammar Activities
(4, 'Simple Sentences', 'Build basic English sentences', 'GRAMMAR', 'BEGINNER', 'QUIZ', '/content/sentences', '/images/grammar.jpg', 'bi-pencil', 70, 20, 1),
(5, 'Verb Tenses', 'Learn present, past, and future tenses', 'GRAMMAR', 'INTERMEDIATE', 'QUIZ', '/content/verbs', '/images/verbs.jpg', 'bi-clock', 80, 25, 2),

-- Reading Activities
(6, 'Short Stories', 'Read and understand simple stories', 'READING', 'BEGINNER', 'VIDEO', '/content/stories', '/images/reading.jpg', 'bi-book', 90, 30, 1),
(7, 'Comprehension Practice', 'Answer questions about what you read', 'READING', 'INTERMEDIATE', 'QUIZ', '/content/comprehension', '/images/comprehension.jpg', 'bi-question-circle', 100, 25, 2),

-- Listening Activities
(8, 'Listen and Repeat', 'Practice pronunciation by listening', 'LISTENING', 'BEGINNER', 'GAME', '/content/listen', '/images/listening.jpg', 'bi-headphones', 60, 15, 1),
(9, 'Sound Recognition', 'Identify different English sounds', 'LISTENING', 'INTERMEDIATE', 'QUIZ', '/content/sounds', '/images/sounds.jpg', 'bi-music-note', 70, 20, 2);

-- Insert Questions for Activity 1 (Animal Names)
INSERT IGNORE INTO question (question_id, activity_id, question_text, question_type, options, correct_answer, image_url, audio_url, explanation, points) VALUES
(1, 1, 'What animal says "Meow"?', 'MULTIPLE_CHOICE', 'Dog,Cat,Cow,Bird', 'Cat', '/images/cat.jpg', '/audio/cat.mp3', 'Cats say meow!', 10),
(2, 1, 'What animal says "Woof"?', 'MULTIPLE_CHOICE', 'Cat,Dog,Pig,Horse', 'Dog', '/images/dog.jpg', '/audio/dog.mp3', 'Dogs say woof!', 10),
(3, 1, 'What animal says "Moo"?', 'MULTIPLE_CHOICE', 'Sheep,Cow,Duck,Chicken', 'Cow', '/images/cow.jpg', '/audio/cow.mp3', 'Cows say moo!', 10),
(4, 1, 'A bird can fly. True or False?', 'TRUE_FALSE', 'True,False', 'True', '/images/bird.jpg', '/audio/bird.mp3', 'Most birds can fly!', 10),
(5, 1, 'What animal has a long trunk?', 'MULTIPLE_CHOICE', 'Lion,Elephant,Tiger,Bear', 'Elephant', '/images/elephant.jpg', '/audio/elephant.mp3', 'Elephants have long trunks!', 10);

-- Insert Questions for Activity 3 (Food and Drinks)
INSERT IGNORE INTO question (question_id, activity_id, question_text, question_type, options, correct_answer, image_url, audio_url, explanation, points) VALUES
(6, 3, 'What color is an apple?', 'MULTIPLE_CHOICE', 'Blue,Red,Yellow,Purple', 'Red', '/images/apple.jpg', '/audio/apple.mp3', 'Apples are usually red!', 10),
(7, 3, 'What do you drink in the morning?', 'MULTIPLE_CHOICE', 'Juice,Soup,Ice cream,Cake', 'Juice', '/images/juice.jpg', '/audio/juice.mp3', 'Many people drink juice for breakfast!', 10),
(8, 3, 'Bananas are yellow. True or False?', 'TRUE_FALSE', 'True,False', 'True', '/images/banana.jpg', '/audio/banana.mp3', 'Ripe bananas are yellow!', 10);

-- Insert Questions for Activity 4 (Simple Sentences)
INSERT IGNORE INTO question (question_id, activity_id, question_text, question_type, options, correct_answer, image_url, audio_url, explanation, points) VALUES
(9, 4, 'Complete: I ___ a student.', 'MULTIPLE_CHOICE', 'am,is,are,be', 'am', null, '/audio/sentence1.mp3', 'Use "am" with "I"', 15),
(10, 4, 'Complete: She ___ happy.', 'MULTIPLE_CHOICE', 'am,is,are,be', 'is', null, '/audio/sentence2.mp3', 'Use "is" with "she"', 15),
(11, 4, 'Complete: They ___ friends.', 'MULTIPLE_CHOICE', 'am,is,are,be', 'are', null, '/audio/sentence3.mp3', 'Use "are" with "they"', 15);

-- Insert Questions for Activity 7 (Comprehension Practice)
INSERT IGNORE INTO question (question_id, activity_id, question_text, question_type, options, correct_answer, image_url, audio_url, explanation, points) VALUES
(12, 7, 'Tom has a red ball. What color is Tom''s ball?', 'MULTIPLE_CHOICE', 'Blue,Red,Green,Yellow', 'Red', null, null, 'The story says Tom has a red ball', 20),
(13, 7, 'The cat is sleeping. What is the cat doing?', 'MULTIPLE_CHOICE', 'Running,Eating,Sleeping,Playing', 'Sleeping', null, null, 'The story says the cat is sleeping', 20);

-- Insert Rewards/Badges
INSERT IGNORE INTO reward (reward_id, name, type, points_required) VALUES
(1, 'First Steps', 'BADGES', 50),
(2, 'Quick Learner', 'BADGES', 100),
(3, 'Word Master', 'BADGES', 200),
(4, 'Grammar Guru', 'BADGES', 300),
(5, 'Reading Star', 'BADGES', 500);
