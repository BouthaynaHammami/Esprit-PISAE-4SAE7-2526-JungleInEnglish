-- Insert Sample Business English Courses
INSERT IGNORE INTO course (course_id, title, description, level, type, price, lessons_number, is_hidden, image_url) VALUES
(1, 'Business Communication Essentials', 'Master professional communication skills for the workplace', 'A1', 'BUSINESS_ENGLISH', 99.99, 12, false, 'https://images.unsplash.com/photo-1552664730-d307ca884978?w=800'),
(2, 'Corporate Email Writing', 'Learn to write effective and professional business emails', 'A2', 'BUSINESS_ENGLISH', 79.99, 8, false, 'https://images.unsplash.com/photo-1596526131083-e8c633c948d2?w=800'),
(3, 'Business Presentations & Public Speaking', 'Develop confidence in delivering business presentations', 'B1', 'BUSINESS_ENGLISH', 149.99, 15, false, 'https://images.unsplash.com/photo-1475721027785-f74eccf877e2?w=800'),
(4, 'Negotiation Skills in English', 'Master negotiation techniques for international business', 'B2', 'BUSINESS_ENGLISH', 129.99, 10, false, 'https://images.unsplash.com/photo-1521791136064-7986c2920216?w=800'),
(5, 'Business Meeting Management', 'Learn to conduct and participate in professional meetings', 'C1', 'BUSINESS_ENGLISH', 119.99, 10, false, 'https://images.unsplash.com/photo-1556761175-b413da4baf72?w=800'),
(6, 'Advanced Business Writing', 'Master reports, proposals, and business documents', 'C2', 'BUSINESS_ENGLISH', 179.99, 14, false, 'https://images.unsplash.com/photo-1450101499163-c8848c66ca85?w=800');

-- Insert Sample General English Courses
INSERT IGNORE INTO course (course_id, title, description, level, type, price, lessons_number, is_hidden, image_url) VALUES
(7, 'English for Beginners', 'Start your English learning journey from scratch', 'A1', 'GENERAL_ENGLISH', 49.99, 20, false, 'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=800'),
(8, 'Everyday Conversation Skills', 'Learn practical English for daily situations', 'A2', 'GENERAL_ENGLISH', 59.99, 15, false, 'https://images.unsplash.com/photo-1573164713714-d95e436ab8d6?w=800'),
(9, 'Intermediate English Grammar', 'Strengthen your grammar foundation', 'B1', 'GENERAL_ENGLISH', 89.99, 18, false, 'https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=800'),
(10, 'Advanced English Fluency', 'Achieve native-like fluency and confidence', 'C2', 'GENERAL_ENGLISH', 139.99, 16, false, 'https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=800');

INSERT IGNORE INTO certification_question
(question_text, optiona, optionb, optionc, optiond, correct_answer, level, category, points, active)
VALUES
    ('She ___ a student.','is','are','am','be','A','A1','Grammar',5,1),
    ('I ___ to school every day.','go','goes','going','went','A','A1','Grammar',5,1),
    ('Opposite of big?','small','tall','large','heavy','A','A1','Vocabulary',5,1),
    ('There ___ a book on the table.','is','are','am','be','A','A1','Grammar',5,1),

    ('He ___ not like coffee.','does','do','is','are','A','A2','Grammar',5,1),
    ('I wake up ___ 7am.','at','in','on','by','A','A2','Grammar',5,1),
    ('What does beautiful mean?','beau/belle','laid/laide','grand/grande','petit/petite','A','A2','Vocabulary',5,1),
    ('She ___ been to Paris.','has','have','had','is','A','A2','Grammar',5,1),

    ('If I ___ rich, I would travel.','were','was','am','be','A','B1','Grammar',10,1),
    ('The meeting was ___ due to the storm.','postponed','proposed','promoted','proceeded','A','B1','Vocabulary',10,1),
    ('She suggested ___ to the cinema.','going','go','to go','gone','A','B1','Grammar',10,1),
    ('Unless you study, you ___ fail.','will','would','shall','should','A','B1','Grammar',10,1),

    ('I wish I ___ speak French fluently.','could','can','will','would','A','B2','Grammar',10,1),
    ('What does meticulous mean?','very careful and precise','very fast','very loud','very creative','A','B2','Vocabulary',10,1),
    ('The book ___ you lent me was excellent.','that','who','whom','what','A','B2','Grammar',10,1),
    ('To procrastinate means?','to delay doing things','to organize','to complete quickly','to prioritize','A','B2','Vocabulary',10,1),

    ('Had I known, I ___ helped.','would have','will have','had','should','A','C1','Grammar',15,1),
    ('What does sycophant mean?','flatters to gain favor','very honest','very brave','very smart','A','C1','Vocabulary',15,1),
    ('Not only ___ he arrive late, but he forgot documents.','did','had','was','has','A','C1','Grammar',15,1),
    ('What does laconic mean?','using very few words','too many words','complex words','incorrect words','A','C1','Vocabulary',15,1),

    ('It is high time he ___ his behavior.','changed','changes','change','would change','A','C2','Grammar',15,1),
    ('What does perspicacious mean?','having ready insight','poor judgment','no opinion','extreme views','A','C2','Vocabulary',15,1),
    ('Correct formal letter opening?','I am writing to express my concern','I wanna talk about a problem','Hey I have an issue','Just wanted to say something','A','C2','Register',15,1),
    ('What does ineffable mean?','too great to express in words','easy to express','very common','very simple','A','C2','Vocabulary',15,1);