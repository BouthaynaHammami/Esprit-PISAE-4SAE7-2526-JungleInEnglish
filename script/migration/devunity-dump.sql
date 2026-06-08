-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: social_interaction
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `social_interaction`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `social_interaction` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `social_interaction`;

--
-- Table structure for table `chat_messages`
--

DROP TABLE IF EXISTS `chat_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text,
  `message_id` varchar(255) DEFAULT NULL,
  `reactions` text,
  `sender` varchar(255) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `topic_id` bigint DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_av2veaoqxgr0pvttqd5egcot2` (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_messages`
--

LOCK TABLES `chat_messages` WRITE;
/*!40000 ALTER TABLE `chat_messages` DISABLE KEYS */;
INSERT INTO `chat_messages` VALUES (1,'?','1777285858740-lkclsn8re',NULL,'bouthayna','2026-04-27 11:30:58.760494',NULL,'CHAT'),(2,'hii','1777723705093-z39x94sct',NULL,'hbouthayna18','2026-05-02 13:08:25.095787',NULL,'CHAT'),(3,'hello','1777723889397-d3mwxuzym',NULL,'islem','2026-05-02 13:11:29.400201',NULL,'CHAT'),(4,'morning','1777723922953-9hht38091',NULL,'yasmine','2026-05-02 13:12:02.955562',NULL,'CHAT'),(5,'what we gonna do today guys?','1777723947464-lfodebqkr',NULL,'hbouthayna18','2026-05-02 13:12:27.466623',NULL,'CHAT'),(6,'i want to do some challenges','1777723977227-w3dqc26n0',NULL,'yasmine','2026-05-02 13:12:57.229038',NULL,'CHAT'),(7,'ok good idea','1777723996234-8alqzr0f9',NULL,'hbouthayna18','2026-05-02 13:13:16.237164',NULL,'CHAT');
/*!40000 ALTER TABLE `chat_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) NOT NULL,
  `created_at` date NOT NULL,
  `user_id` int DEFAULT NULL,
  `topic_id` bigint NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKo3bvevu9ua4w6f8qu2b177f16` (`topic_id`),
  CONSTRAINT `FKo3bvevu9ua4w6f8qu2b177f16` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`topic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'I use flashcards every day to memorize new words. It really helps!','2026-05-01',2,1),(2,'Watching English movies with subtitles improved my listening a lot.','2026-05-02',3,1),(3,'Duolingo is great for beginners, I use it every morning.','2026-05-03',5,2),(4,'I prefer BBC Learning English app, it has great grammar exercises.','2026-05-04',6,2),(5,'Can someone explain the difference between Present Perfect and Simple Past?','2026-05-05',7,3);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `message_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `receiver_id` int DEFAULT NULL,
  `sender_id` int DEFAULT NULL,
  `sent_at` datetime(6) NOT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'Hello! Can you help me with my English grammar homework?',_binary '',4,2,'2026-05-01 08:00:00.000000'),(2,'Sure! Send me your questions and I will help you.',_binary '',2,4,'2026-05-01 08:05:00.000000'),(3,'When is the next Business English session scheduled?',_binary '',9,3,'2026-05-02 09:00:00.000000'),(4,'The next session is on Friday at 10am. Do not miss it!',_binary '\0',3,9,'2026-05-02 09:10:00.000000'),(5,'I completed the IELTS course. Thank you for your feedback!',_binary '\0',4,6,'2026-05-03 10:00:00.000000');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notification_id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime(6) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `message` varchar(1000) NOT NULL,
  `type` varchar(255) NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'2026-05-01 09:00:00.000000',_binary '','Your payment for English for Beginners has been confirmed.','PAYMENT',2),(2,'2026-05-02 10:00:00.000000',_binary '','Your test has been corrected. Check your feedback now.','TEST',3),(3,'2026-05-03 11:00:00.000000',_binary '\0','New topic posted: English grammar questions. Join the discussion.','TOPIC',5),(4,'2026-05-04 14:00:00.000000',_binary '\0','Reminder: Your IELTS Preparation Course starts in 2 days.','REMINDER',6),(5,'2026-05-05 16:00:00.000000',_binary '\0','You have a new message from your tutor Marwa.','MESSAGE',7);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `report_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `reason` varchar(1000) NOT NULL,
  `status` varchar(255) NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (1,'2026-05-01 10:00:00.000000','Inappropriate language used in a comment.','PENDING',2),(2,'2026-05-02 11:00:00.000000','Spam messages sent repeatedly in the forum.','RESOLVED',3),(3,'2026-05-03 12:00:00.000000','Off-topic content posted in grammar section.','PENDING',5),(4,'2026-05-04 13:00:00.000000','User sharing external links without permission.','REJECTED',6),(5,'2026-05-05 14:00:00.000000','Plagiarized content submitted in writing exercise.','PENDING',7);
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic` (
  `topic_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` date DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`topic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic`
--

LOCK TABLES `topic` WRITE;
/*!40000 ALTER TABLE `topic` DISABLE KEYS */;
INSERT INTO `topic` VALUES (1,'2026-04-27','Share your tips and tricks for learning English','How to learn English effectively?',2),(2,'2026-04-27','Discuss the best mobile apps for learning English','Best English learning apps',3),(3,'2026-04-27','Ask and answer grammar-related questions','English grammar questions',5);
/*!40000 ALTER TABLE `topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `language_courses`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `language_courses` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `language_courses`;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `estimated_time` int NOT NULL,
  `xp_reward` int NOT NULL,
  `activity_id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `icon_class` varchar(100) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `content_url` varchar(2000) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `category` enum('VOCABULARY','GRAMMAR','LISTENING','READING','SPEAKING','WRITING') NOT NULL,
  `difficulty` enum('BEGINNER','INTERMEDIATE','ADVANCED') NOT NULL,
  `type` enum('GAME','QUIZ','VIDEO') NOT NULL,
  PRIMARY KEY (`activity_id`),
  KEY `FKad1q04q5dxv9lyur6pyighso` (`course_id`),
  CONSTRAINT `FKad1q04q5dxv9lyur6pyighso` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (10,50,1,1,'bi-emoji-smile','/images/animals.jpg','/content/animals','Learn the names of common animals','Animal Names','VOCABULARY','BEGINNER','QUIZ'),(15,50,2,1,'bi-palette','/images/colors.jpg','/content/colors','Identify different colors and shapes','Colors and Shapes','VOCABULARY','BEGINNER','GAME'),(12,60,3,1,'bi-cup-straw','/images/food.jpg','/content/food','Learn vocabulary about food and beverages','Food and Drinks','VOCABULARY','BEGINNER','QUIZ'),(20,70,4,1,'bi-pencil','/images/grammar.jpg','/content/sentences','Build basic English sentences','Simple Sentences','GRAMMAR','BEGINNER','QUIZ'),(25,80,5,2,'bi-clock','/images/verbs.jpg','/content/verbs','Learn present, past, and future tenses','Verb Tenses','GRAMMAR','INTERMEDIATE','QUIZ'),(30,90,6,1,'bi-book','/images/reading.jpg','/content/stories','Read and understand simple stories','Short Stories','READING','BEGINNER','VIDEO'),(25,100,7,2,'bi-question-circle','/images/comprehension.jpg','/content/comprehension','Answer questions about what you read','Comprehension Practice','READING','INTERMEDIATE','QUIZ'),(15,60,8,1,'bi-headphones','/images/listening.jpg','/content/listen','Practice pronunciation by listening','Listen and Repeat','LISTENING','BEGINNER','GAME'),(20,70,9,2,'bi-music-note','/images/sounds.jpg','/content/sounds','Identify different English sounds','Sound Recognition','LISTENING','INTERMEDIATE','QUIZ');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_english_path`
--

DROP TABLE IF EXISTS `business_english_path`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `business_english_path` (
  `creation_date` date DEFAULT NULL,
  `expected_end_date` date DEFAULT NULL,
  `last_update_date` date DEFAULT NULL,
  `overall_score` float NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `offer_id` bigint DEFAULT NULL,
  `status` enum('IN_PROGRESS','COMPLETED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlri33eujnn2kya0ygt7qmttfg` (`offer_id`),
  CONSTRAINT `FKlri33eujnn2kya0ygt7qmttfg` FOREIGN KEY (`offer_id`) REFERENCES `offer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_english_path`
--

LOCK TABLES `business_english_path` WRITE;
/*!40000 ALTER TABLE `business_english_path` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_english_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `child`
--

DROP TABLE IF EXISTS `child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `child` (
  `birth_date` date NOT NULL,
  `level` int NOT NULL,
  `xp` int NOT NULL,
  `child_id` bigint NOT NULL AUTO_INCREMENT,
  `level_id` bigint DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `parent_user_id` bigint DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`child_id`),
  KEY `FK9a1yfigaswc0xbrvxiqyxpms0` (`level_id`),
  KEY `FK7dag1cncltpyhoc2mbwka356h` (`parent_id`),
  CONSTRAINT `FK7dag1cncltpyhoc2mbwka356h` FOREIGN KEY (`parent_id`) REFERENCES `parent` (`parent_id`),
  CONSTRAINT `FK9a1yfigaswc0xbrvxiqyxpms0` FOREIGN KEY (`level_id`) REFERENCES `level_children` (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `child`
--

LOCK TABLES `child` WRITE;
/*!40000 ALTER TABLE `child` DISABLE KEYS */;
/*!40000 ALTER TABLE `child` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_offer`
--

DROP TABLE IF EXISTS `company_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_offer` (
  `decided_at` date DEFAULT NULL,
  `paid_date` date DEFAULT NULL,
  `requested_at` date DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `company_offer_id` bigint NOT NULL AUTO_INCREMENT,
  `offer_id` bigint DEFAULT NULL,
  `approval_status` enum('PENDING','APPROVED','REJECTED') DEFAULT NULL,
  `payment_status` enum('PENDING','PAID','FAILED') DEFAULT NULL,
  PRIMARY KEY (`company_offer_id`),
  KEY `FKmm12tpxruq1k1ixq33qrtoalr` (`offer_id`),
  CONSTRAINT `FKmm12tpxruq1k1ixq33qrtoalr` FOREIGN KEY (`offer_id`) REFERENCES `offer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_offer`
--

LOCK TABLES `company_offer` WRITE;
/*!40000 ALTER TABLE `company_offer` DISABLE KEYS */;
INSERT INTO `company_offer` VALUES ('2026-05-02','2026-05-05','2026-05-01',10,6,1,'APPROVED','PAID'),(NULL,NULL,'2026-06-08',12,7,2,'PENDING',''),('2026-06-02',NULL,'2026-06-01',10,8,3,'APPROVED',''),('2026-04-16',NULL,'2026-04-15',12,9,1,'REJECTED',''),('2026-03-11','2026-03-15','2026-03-10',10,10,2,'APPROVED','PAID'),('2026-05-02','2026-05-05','2026-05-01',10,16,1,'APPROVED','PAID'),(NULL,NULL,'2026-06-08',12,17,2,'PENDING','FAILED'),('2026-06-02',NULL,'2026-06-01',10,18,3,'APPROVED','FAILED'),('2026-04-16',NULL,'2026-04-15',12,19,1,'REJECTED','FAILED'),('2026-03-11','2026-03-15','2026-03-10',10,20,2,'APPROVED','PAID'),(NULL,NULL,'2026-06-08',10,21,1,'PENDING',NULL),(NULL,NULL,'2026-06-08',10,22,2,'PENDING',NULL),(NULL,NULL,'2026-06-08',10,23,3,'PENDING',NULL);
/*!40000 ALTER TABLE `company_offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_offer_students`
--

DROP TABLE IF EXISTS `company_offer_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_offer_students` (
  `company_offer_company_offer_id` bigint NOT NULL,
  `students` bigint DEFAULT NULL,
  KEY `FKfs52fljauyb83arroaqfolh1j` (`company_offer_company_offer_id`),
  CONSTRAINT `FKfs52fljauyb83arroaqfolh1j` FOREIGN KEY (`company_offer_company_offer_id`) REFERENCES `company_offer` (`company_offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_offer_students`
--

LOCK TABLES `company_offer_students` WRITE;
/*!40000 ALTER TABLE `company_offer_students` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_offer_students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `price` float NOT NULL,
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `level_id` bigint DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`course_id`),
  KEY `FKhmt0piu48rbr0gjtimjgsn9yk` (`level_id`),
  CONSTRAINT `FKhmt0piu48rbr0gjtimjgsn9yk` FOREIGN KEY (`level_id`) REFERENCES `level_children` (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (70,1,NULL,'Learn basic English vocabulary and grammar','English Basics for Kids'),(20,2,NULL,'Expand vocabulary through fun activities','Fun with Words'),(60,3,NULL,'Master English grammar rules','Grammar Adventures');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_invitation`
--

DROP TABLE IF EXISTS `employee_invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_invitation` (
  `sent_date` date DEFAULT NULL,
  `company_offer_company_offer_id` bigint DEFAULT NULL,
  `expiration_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `offer_id` bigint DEFAULT NULL,
  `activation_code` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` enum('PENDING','SENT','USED','EXPIRED','REJECTED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3hwrf9wuu22mwlso9af0n9ba5` (`company_offer_company_offer_id`),
  KEY `FKndlioqgioc1rc4ey16jfila6m` (`offer_id`),
  CONSTRAINT `FK3hwrf9wuu22mwlso9af0n9ba5` FOREIGN KEY (`company_offer_company_offer_id`) REFERENCES `company_offer` (`company_offer_id`),
  CONSTRAINT `FKndlioqgioc1rc4ey16jfila6m` FOREIGN KEY (`offer_id`) REFERENCES `offer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_invitation`
--

LOCK TABLES `employee_invitation` WRITE;
/*!40000 ALTER TABLE `employee_invitation` DISABLE KEYS */;
INSERT INTO `employee_invitation` VALUES ('2026-06-08',21,'2026-06-09 17:43:32.939353',1,1,NULL,'hbouthayna18@gmail.com','PENDING'),('2026-06-08',22,'2026-06-08 21:43:41.458324',2,2,NULL,'hbouthayna18@gmail.com','PENDING'),('2026-06-08',23,'2026-06-10 13:46:06.838434',3,3,NULL,'hbouthayna18@gmail.com','PENDING');
/*!40000 ALTER TABLE `employee_invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level_children`
--

DROP TABLE IF EXISTS `level_children`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `level_children` (
  `max_age` int NOT NULL,
  `min_age` int NOT NULL,
  `order_index` int DEFAULT NULL,
  `level_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level_children`
--

LOCK TABLES `level_children` WRITE;
/*!40000 ALTER TABLE `level_children` DISABLE KEYS */;
INSERT INTO `level_children` VALUES (6,4,1,1,'Perfect for preschool and kindergarten kids','Little Learners'),(9,7,2,2,'Elementary school level activities','Young Explorers'),(12,10,3,3,'Advanced activities for pre-teens','Junior Masters');
/*!40000 ALTER TABLE `level_children` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer` (
  `duration_hours` float NOT NULL,
  `end_date` date DEFAULT NULL,
  `price` float NOT NULL,
  `start_date` date DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `level` enum('A1','A2','B1','B2','C1','C2') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` VALUES (40,'2026-12-31',1500,'2026-01-01',1,'BUSINESS','Complete business English course for corporate teams.','Corporate Package','B2','ACTIVE'),(20,'2026-10-31',800,'2026-02-01',2,'IT','English vocabulary tailored for IT professionals.','Tech English Crash Course','C1','ACTIVE'),(60,'2026-11-30',2500,'2026-03-01',3,'COMMUNICATION','Improve speaking and leadership skills.','Leadership English','C1','ACTIVE');
/*!40000 ALTER TABLE `offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer_course_ids`
--

DROP TABLE IF EXISTS `offer_course_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer_course_ids` (
  `course_ids` bigint DEFAULT NULL,
  `offer_id` bigint NOT NULL,
  KEY `FKejh37xyumy3g38ty5q49o7x2j` (`offer_id`),
  CONSTRAINT `FKejh37xyumy3g38ty5q49o7x2j` FOREIGN KEY (`offer_id`) REFERENCES `offer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer_course_ids`
--

LOCK TABLES `offer_course_ids` WRITE;
/*!40000 ALTER TABLE `offer_course_ids` DISABLE KEYS */;
/*!40000 ALTER TABLE `offer_course_ids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parent`
--

DROP TABLE IF EXISTS `parent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parent` (
  `user_id` int DEFAULT NULL,
  `parent_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parent`
--

LOCK TABLES `parent` WRITE;
/*!40000 ALTER TABLE `parent` DISABLE KEYS */;
/*!40000 ALTER TABLE `parent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progress`
--

DROP TABLE IF EXISTS `progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progress` (
  `completion_rate` float NOT NULL,
  `child_id` bigint DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `last_access` datetime(6) NOT NULL,
  `progress_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`progress_id`),
  KEY `FKt4sae0ukm6i9mil0836te5nhj` (`child_id`),
  KEY `FKsyhehp7u3ky9phqokro91ih4v` (`course_id`),
  CONSTRAINT `FKsyhehp7u3ky9phqokro91ih4v` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`),
  CONSTRAINT `FKt4sae0ukm6i9mil0836te5nhj` FOREIGN KEY (`child_id`) REFERENCES `child` (`child_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progress`
--

LOCK TABLES `progress` WRITE;
/*!40000 ALTER TABLE `progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `points` int NOT NULL,
  `activity_id` bigint NOT NULL,
  `question_id` bigint NOT NULL AUTO_INCREMENT,
  `audio_url` varchar(500) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `explanation` varchar(1000) DEFAULT NULL,
  `question_text` varchar(1000) NOT NULL,
  `options` varchar(2000) DEFAULT NULL,
  `correct_answer` varchar(255) NOT NULL,
  `question_type` enum('MULTIPLE_CHOICE','TRUE_FALSE','FILL_BLANK','MATCHING') NOT NULL,
  PRIMARY KEY (`question_id`),
  KEY `FKitda15wrtaa2b23orr6e085v7` (`activity_id`),
  CONSTRAINT `FKitda15wrtaa2b23orr6e085v7` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (10,1,1,'/audio/cat.mp3','/images/cat.jpg','Cats say meow!','What animal says \"Meow\"?','Dog,Cat,Cow,Bird','Cat','MULTIPLE_CHOICE'),(10,1,2,'/audio/dog.mp3','/images/dog.jpg','Dogs say woof!','What animal says \"Woof\"?','Cat,Dog,Pig,Horse','Dog','MULTIPLE_CHOICE'),(10,1,3,'/audio/cow.mp3','/images/cow.jpg','Cows say moo!','What animal says \"Moo\"?','Sheep,Cow,Duck,Chicken','Cow','MULTIPLE_CHOICE'),(10,1,4,'/audio/bird.mp3','/images/bird.jpg','Most birds can fly!','A bird can fly. True or False?','True,False','True','TRUE_FALSE'),(10,1,5,'/audio/elephant.mp3','/images/elephant.jpg','Elephants have long trunks!','What animal has a long trunk?','Lion,Elephant,Tiger,Bear','Elephant','MULTIPLE_CHOICE'),(10,3,6,'/audio/apple.mp3','/images/apple.jpg','Apples are usually red!','What color is an apple?','Blue,Red,Yellow,Purple','Red','MULTIPLE_CHOICE'),(10,3,7,'/audio/juice.mp3','/images/juice.jpg','Many people drink juice for breakfast!','What do you drink in the morning?','Juice,Soup,Ice cream,Cake','Juice','MULTIPLE_CHOICE'),(10,3,8,'/audio/banana.mp3','/images/banana.jpg','Ripe bananas are yellow!','Bananas are yellow. True or False?','True,False','True','TRUE_FALSE'),(15,4,9,'/audio/sentence1.mp3',NULL,'Use \"am\" with \"I\"','Complete: I ___ a student.','am,is,are,be','am','MULTIPLE_CHOICE'),(15,4,10,'/audio/sentence2.mp3',NULL,'Use \"is\" with \"she\"','Complete: She ___ happy.','am,is,are,be','is','MULTIPLE_CHOICE'),(15,4,11,'/audio/sentence3.mp3',NULL,'Use \"are\" with \"they\"','Complete: They ___ friends.','am,is,are,be','are','MULTIPLE_CHOICE'),(20,7,12,NULL,NULL,'The story says Tom has a red ball','Tom has a red ball. What color is Tom\'s ball?','Blue,Red,Green,Yellow','Red','MULTIPLE_CHOICE'),(20,7,13,NULL,NULL,'The story says the cat is sleeping','The cat is sleeping. What is the cat doing?','Running,Eating,Sleeping,Playing','Sleeping','MULTIPLE_CHOICE');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reward`
--

DROP TABLE IF EXISTS `reward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reward` (
  `points_required` int NOT NULL,
  `reward_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` enum('BADGES','STARS','GIFTS') NOT NULL,
  PRIMARY KEY (`reward_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reward`
--

LOCK TABLES `reward` WRITE;
/*!40000 ALTER TABLE `reward` DISABLE KEYS */;
INSERT INTO `reward` VALUES (50,1,'First Steps','BADGES'),(100,2,'Quick Learner','BADGES'),(200,3,'Word Master','BADGES'),(300,4,'Grammar Guru','BADGES'),(500,5,'Reading Star','BADGES');
/*!40000 ALTER TABLE `reward` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `community_engagement`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `community_engagement` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `community_engagement`;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `event_id` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `status` enum('SCHEDULED','REGISTRATION_OPEN','CLOSED','CANCELED','COMPLETED') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (1,50,'oin our interactive workshop designed to improve your English speaking and communication skills. Participants will engage in group discussions, pronunciation exercises, and real-life conversation scenarios to build confidence and fluency in English.','2026-06-18 14:04:00.000000','http://localhost:8081/communities/api/uploads/38717367-6747-4a31-a2b3-a425e90c8d2d_OIP.webp','Tunisie','2026-06-06 14:03:00.000000','REGISTRATION_OPEN','English Speaking and Communication Workshop'),(2,100,'Intensive masterclass.','2026-07-15 18:00:00.000000','http://localhost:8081/communities/api/uploads/toefl.jpg','Online','2026-07-14 09:00:00.000000','REGISTRATION_OPEN','TOEFL Masterclass'),(3,30,'Practice your business English.','2026-06-25 21:00:00.000000','http://localhost:8081/communities/api/uploads/networking.jpg','Tunis','2026-06-25 18:00:00.000000','REGISTRATION_OPEN','Networking Mixer'),(4,200,'A comprehensive webinar.','2026-08-05 12:00:00.000000','http://localhost:8081/communities/api/uploads/grammar.jpg','Online','2026-08-05 10:00:00.000000','REGISTRATION_OPEN','Grammar Bootcamp'),(7,100,'Intensive masterclass covering all sections of the TOEFL exam with expert tips and strategies.','2026-07-15 18:00:00.000000','http://localhost:8081/communities/api/uploads/toefl.jpg','Online','2026-07-14 09:00:00.000000','REGISTRATION_OPEN','TOEFL Preparation Masterclass'),(8,30,'Practice your business English while networking with professionals from various industries.','2026-06-25 21:00:00.000000','http://localhost:8081/communities/api/uploads/networking.jpg','Tunis','2026-06-25 18:00:00.000000','REGISTRATION_OPEN','Business English Networking Mixer'),(9,200,'A comprehensive webinar breaking down complex English tenses for intermediate learners.','2026-08-05 12:00:00.000000','http://localhost:8081/communities/api/uploads/grammar.jpg','Online','2026-08-05 10:00:00.000000','REGISTRATION_OPEN','Grammar Bootcamp: Mastering Tenses'),(10,40,'Meet native speakers and other learners for casual conversations in a relaxed environment.','2026-06-12 18:00:00.000000','http://localhost:8081/communities/api/uploads/meetup.jpg','Sousse','2026-06-12 15:00:00.000000','REGISTRATION_OPEN','Language Exchange Meetup'),(11,25,'Learn how to structure, draft, and polish academic essays for university applications.','2026-09-10 17:00:00.000000','http://localhost:8081/communities/api/uploads/writing.jpg','Sfax','2026-09-10 14:00:00.000000','REGISTRATION_OPEN','Writing Workshop: Academic Essays'),(12,100,'Intensive masterclass.','2026-07-15 18:00:00.000000','http://localhost:8081/communities/api/uploads/toefl.jpg','Online','2026-07-14 09:00:00.000000','REGISTRATION_OPEN','TOEFL Preparation Masterclass'),(13,30,'Practice your business English.','2026-06-25 21:00:00.000000','http://localhost:8081/communities/api/uploads/networking.jpg','Tunis','2026-06-25 18:00:00.000000','REGISTRATION_OPEN','Business English Networking Mixer'),(14,200,'A comprehensive webinar.','2026-08-05 12:00:00.000000','http://localhost:8081/communities/api/uploads/grammar.jpg','Online','2026-08-05 10:00:00.000000','REGISTRATION_OPEN','Grammar Bootcamp: Mastering Tenses');
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kanban_task`
--

DROP TABLE IF EXISTS `kanban_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kanban_task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deadline` datetime(6) DEFAULT NULL,
  `description` text,
  `position` int DEFAULT NULL,
  `reminder_sent` bit(1) DEFAULT NULL,
  `status` enum('TODO','DOING','DONE') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kanban_task`
--

LOCK TABLES `kanban_task` WRITE;
/*!40000 ALTER TABLE `kanban_task` DISABLE KEYS */;
INSERT INTO `kanban_task` VALUES (1,'2026-06-08 00:32:42.000000','2026-06-20 18:00:00.000000','Organize the next English speaking club meeting for intermediate students.',1,_binary '\0','TODO','Organize Speaking Club','2026-06-08 00:32:42.000000',2),(2,'2026-06-08 00:32:42.000000','2026-06-15 10:00:00.000000','Moderate the community forum and answer pending grammar questions.',1,_binary '\0','TODO','Moderate Forum','2026-06-08 00:32:42.000000',4),(3,'2026-06-08 00:32:42.000000','2026-06-10 09:00:00.000000','Post the weekly vocabulary challenge in the main community feed.',2,_binary '','DONE','Post Vocabulary Challenge','2026-06-08 00:32:42.000000',9),(4,'2026-06-08 00:32:42.000000','2026-06-30 12:00:00.000000','Review and update the community guidelines for the new semester.',1,_binary '\0','TODO','Review Guidelines','2026-06-08 00:32:42.000000',10),(5,'2026-06-08 00:32:42.000000','2026-06-12 14:00:00.000000','Create a WhatsApp study group for the Business English attendees.',2,_binary '\0','DONE','Create Study Group','2026-06-08 00:32:42.000000',3);
/*!40000 ALTER TABLE `kanban_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regsitration_event`
--

DROP TABLE IF EXISTS `regsitration_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `regsitration_event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT NULL,
  `status` enum('CONFIRMED','WAITLISTED','CANCELLED','REJECTED') DEFAULT NULL,
  `ticket_id` varchar(255) DEFAULT NULL,
  `user` int DEFAULT NULL,
  `events_event_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_h8d6v8ad0g82ylmi8v1sfln7a` (`ticket_id`),
  KEY `FKch1avwjrkkl2pcpp8v6n8e3os` (`events_event_id`),
  CONSTRAINT `FKch1avwjrkkl2pcpp8v6n8e3os` FOREIGN KEY (`events_event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regsitration_event`
--

LOCK TABLES `regsitration_event` WRITE;
/*!40000 ALTER TABLE `regsitration_event` DISABLE KEYS */;
INSERT INTO `regsitration_event` VALUES (16,'Looking forward to improving my speaking skills!','2026-06-08 00:35:42.000000','CONFIRMED','TKT-2026-001',2,1),(17,'Will there be a certificate at the end?','2026-06-08 00:35:42.000000','CONFIRMED','TKT-2026-002',3,1),(18,'Excited to network with other professionals.','2026-06-08 00:35:42.000000','CONFIRMED','TKT-2026-003',4,2),(19,'I might be 10 minutes late, sorry.','2026-06-08 00:35:42.000000','CONFIRMED','TKT-2026-004',5,2),(20,'Can we get the presentation slides beforehand?','2026-06-08 00:35:42.000000','CONFIRMED','TKT-2026-005',6,3);
/*!40000 ALTER TABLE `regsitration_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `activity_management`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `activity_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `activity_management`;

--
-- Table structure for table `applicant`
--

DROP TABLE IF EXISTS `applicant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cv` varchar(255) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `reponse` text,
  `status` enum('PENDING','ACCEPTED','REJECTED') DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `interview_id` bigint DEFAULT NULL,
  `recruitment_id` bigint DEFAULT NULL,
  `cluster` varchar(255) DEFAULT NULL,
  `decision` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `raison` varchar(255) DEFAULT NULL,
  `score` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkryeujib47he7besfry3iiq16` (`interview_id`),
  KEY `FKqupq9errgrcngg1tab23t3dgm` (`recruitment_id`),
  CONSTRAINT `FKkryeujib47he7besfry3iiq16` FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`),
  CONSTRAINT `FKqupq9errgrcngg1tab23t3dgm` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicant`
--

LOCK TABLES `applicant` WRITE;
/*!40000 ALTER TABLE `applicant` DISABLE KEYS */;
INSERT INTO `applicant` VALUES (2,'http://res.cloudinary.com/ddtfi7kal/image/upload/v1778602131/lqewal55ofx8jnoy2y78.pdf','2026-05-12 17:08:54.357000','','PENDING',0,NULL,5,'Junior','PENDING','Local Analysis','Analyzed via local keyword engine. Detected 2 skill matches (teaching, curriculum).',46),(3,'https://drive.google.com/file/d/1R-Vwv1T5LU6YW39G4-W79Dxy04Un2eY_/view?usp=drive_link','2026-05-12 19:09:52.456000','','ACCEPTED',8,1,5,'Senior','ACCEPTED','Local Fallback','Analyzed via local fallback engine. Found 11 matching skills.',100),(4,'https://drive.google.com/file/d/1ER4u6a55LXMqu3oItCSWfAt8YUrrPTRZ/view?usp=sharing','2026-05-12 19:30:56.334000','','PENDING',8,NULL,6,'Senior','ACCEPTED','Local Analysis','Analyzed via local keyword engine. Detected 11 skill matches (business, communication, one-on-one, coaching, presentation, skills, celta/delta, certification, soft, skills, training).',95),(5,'http://res.cloudinary.com/ddtfi7kal/image/upload/v1778611275/nllnhtxfb1chl7t0qcuq.pdf','2026-05-12 19:41:18.791000','','REJECTED',8,NULL,7,'Junior','REJECTED','Local Analysis','Analyzed via local keyword engine. Detected 0 skill matches ().',12),(6,'https://drive.google.com/file/d/1ER4u6a55LXMqu3oItCSWfAt8YUrrPTRZ/view?usp=drive_link','2026-05-12 19:51:16.550000','','ACCEPTED',9,2,5,'Senior','ACCEPTED','Local Analysis','Analyzed via local keyword engine. Detected 11 skill matches (english, teaching, (tefl/celta), curriculum, design, content, creation, lms, (moodle/canvas), video, scripting).',95),(7,'http://res.cloudinary.com/ddtfi7kal/image/upload/v1778611977/ngsdppwsnnikgse1znmy.pdf','2026-05-12 19:52:59.796000','','REJECTED',9,NULL,6,'Junior','REJECTED','Local Analysis','Analyzed via local keyword engine. Detected 0 skill matches ().',8),(8,'https://drive.google.com/file/d/1aQNv4C2SZLLn2l2dNKbiwQYFH7QuZsim/view?usp=drive_link','2026-05-12 20:34:25.597000','','PENDING',10,NULL,5,'Mid-level','PENDING','hybrid_keyword_v1','Good match (72%). Matches some requirements but needs further evaluation.',72),(9,'https://drive.google.com/file/d/1aQNv4C2SZLLn2l2dNKbiwQYFH7QuZsim/view?usp=drive_link','2026-05-12 20:38:56.636000','','REJECTED',10,NULL,6,'Mismatch','REJECTED','hybrid_keyword_v1','Low match (20%). CV does not sufficiently match the required skills: Business Communication One-on-One Coaching Presentation Skills CELTA/DELTA Certification Soft Skills Training.',20),(10,'https://drive.google.com/file/d/1ER4u6a55LXMqu3oItCSWfAt8YUrrPTRZ/view?usp=drive_link','2026-05-12 20:44:15.926000','','PENDING',10,NULL,13,'Junior','ACCEPTED','hybrid_keyword_v1','Strong potential (98%). Technical skills match well for a junior role.',98),(11,'https://drive.google.com/file/d/1ER4u6a55LXMqu3oItCSWfAt8YUrrPTRZ/view?usp=drive_link','2026-05-12 20:56:29.072000','','PENDING',10,NULL,8,'Junior','ACCEPTED','hybrid_keyword_v1','Strong potential (90%). Technical skills match well for a junior role.',90);
/*!40000 ALTER TABLE `applicant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `badge`
--

DROP TABLE IF EXISTS `badge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `badge` (
  `id_badge` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `points_required` int NOT NULL,
  PRIMARY KEY (`id_badge`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `badge`
--

LOCK TABLES `badge` WRITE;
/*!40000 ALTER TABLE `badge` DISABLE KEYS */;
INSERT INTO `badge` VALUES (1,'You completed your very first challenge. The journey begins!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F463.png','First Step',0),(2,'You mastered the basic English alphabet and phonics.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F524.png','Alphabet Master',50),(3,'You translated 50 words correctly. Keep collecting!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F50D.png','Word Hunter',100),(4,'You finished a Speed Translation Race for the first time.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/26A1.png','Speed Starter',150),(5,'You completed all A1 level challenges. Beginner no more!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F396.png','A1 Champion',200),(6,'You learned all everyday conversation phrases at A2 level.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F5E3.png','Daily Speaker',300),(7,'You answered 10 questions in under 30 seconds. Blazing fast!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F4A8.png','Lightning Fingers',350),(8,'You completed challenges 5 days in a row. Consistency is key!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F525.png','Streak Keeper',400),(9,'You conquered all A2 level challenges. Elementary complete!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F947.png','A2 Champion',500),(10,'You mastered all travel and directions vocabulary at B1 level.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F30D.png','Globetrotter',600),(11,'You completed all culture and literature challenges.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F4DA.png','Bookworm',700),(12,'You got 100% correct answers in a full challenge. Flawless!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/2B50.png','Perfect Score',750),(13,'You completed all B1 level challenges. Intermediate mastered!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F3C5.png','B1 Champion',800),(14,'You mastered societal debates and nuanced expressions at B2.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F4AC.png','Debate Club',900),(15,'You translated all science and research vocabulary perfectly.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F9EA.png','Science Geek',950),(16,'You completed all B2 level challenges. Upper intermediate done!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F3C6.png','B2 Champion',1000),(17,'You mastered ethics, philosophy of language, and rhetoric at C1.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F9E0.png','Philosopher',1200),(18,'You completed all C1 level challenges. Advanced level conquered!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F680.png','C1 Champion',1500),(19,'You completed challenges across Science, Law, Philosophy and AI at C2.','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F52D.png','Polymath',1800),(20,'You completed ALL 60 challenges across every level. You are legendary!','https://cdn.jsdelivr.net/npm/openmoji@15.0.0/color/png/1F451.png','Grand Master',2500);
/*!40000 ALTER TABLE `badge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `challenge`
--

DROP TABLE IF EXISTS `challenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `challenge` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `allow_voting` bit(1) DEFAULT NULL,
  `allowed_letters` varchar(255) DEFAULT NULL,
  `correct_answer` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `emoji_prompt` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `forbidden_words` varchar(255) DEFAULT NULL,
  `hints` varchar(255) DEFAULT NULL,
  `initial_sentence` varchar(255) DEFAULT NULL,
  `challenge_level` enum('A1','A2','B1','B2','C1','C2') DEFAULT NULL,
  `max_attempts` int DEFAULT NULL,
  `max_participants` int DEFAULT NULL,
  `max_questions` int DEFAULT NULL,
  `max_sentences` int DEFAULT NULL,
  `max_words_per_sentence` int DEFAULT NULL,
  `min_words_per_sentence` int DEFAULT NULL,
  `points_per_correct_answer` int DEFAULT NULL,
  `scrambled_sentence` varchar(255) DEFAULT NULL,
  `sentences_list` text,
  `source_language` varchar(255) DEFAULT NULL,
  `speed_bonus_enabled` bit(1) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `target_language` varchar(255) DEFAULT NULL,
  `time_limit_seconds` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `challenge_type` enum('MYSTERY_WORD','SENTENCE_BUILDER','EMOJI_WORD','WORD_BATTLE_ROYALE','STORY_CHAIN','SPEED_TRANSLATION_RACE') NOT NULL,
  `word_theme` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=361 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenge`
--

LOCK TABLES `challenge` WRITE;
/*!40000 ALTER TABLE `challenge` DISABLE KEYS */;
INSERT INTO `challenge` VALUES (1,NULL,NULL,'apple;apples','Look at the emoji and write the correct English word.','?','2026-04-01',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,NULL,'Fruit or Vegetable?','EMOJI_WORD',NULL),(2,NULL,NULL,'sun','Look at the emoji and write the correct English word.','☀️','2026-04-04',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-04',NULL,NULL,'In the Sky','EMOJI_WORD',NULL),(3,NULL,NULL,'dog;dogs','Look at the emoji and write the correct English word.','?','2026-04-07',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-07',NULL,NULL,'Animal','EMOJI_WORD',NULL),(4,NULL,NULL,'car;cars','Look at the emoji and write the correct English word.','?','2026-04-10',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-10',NULL,NULL,'Transport','EMOJI_WORD',NULL),(5,NULL,NULL,'eye;eyes','Look at the emoji and write the correct English word.','?️','2026-04-13',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,NULL,'Body Part','EMOJI_WORD',NULL),(6,NULL,NULL,'pizza','Look at the emoji and write the correct English word.','?','2026-04-16',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-16',NULL,NULL,'Food','EMOJI_WORD',NULL),(7,NULL,NULL,'tree;trees','Look at the emoji and write the correct English word.','?','2026-04-19',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-19',NULL,NULL,'Nature','EMOJI_WORD',NULL),(8,NULL,NULL,'rain;raining','Look at the emoji and write the correct English word.','?️','2026-04-22',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-22',NULL,NULL,'Weather','EMOJI_WORD',NULL),(9,NULL,NULL,'red;heart','Look at the emoji and write the correct English word.','❤️','2026-04-25',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-25',NULL,NULL,'Colour','EMOJI_WORD',NULL),(10,NULL,NULL,'cat;cats','Look at the emoji and write the correct English word.','?','2026-04-28',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-28',NULL,NULL,'Animal 2','EMOJI_WORD',NULL),(11,NULL,NULL,'football;soccer','Look at the emoji and write the correct English word.','⚽','2026-04-02',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,NULL,'Sport','EMOJI_WORD',NULL),(12,NULL,NULL,'doctor;physician','Look at the emoji and write the correct English word.','?‍⚕️','2026-04-06',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-05',NULL,NULL,'Profession','EMOJI_WORD',NULL),(13,NULL,NULL,'school','Look at the emoji and write the correct English word.','?','2026-04-10',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-09',NULL,NULL,'Place','EMOJI_WORD',NULL),(14,NULL,NULL,'burger;hamburger','Look at the emoji and write the correct English word.','?','2026-04-14',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,NULL,'Food 2','EMOJI_WORD',NULL),(15,NULL,NULL,'plane;airplane;aeroplane','Look at the emoji and write the correct English word.','✈️','2026-04-18',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-17',NULL,NULL,'Travel','EMOJI_WORD',NULL),(16,NULL,NULL,'wave;sea;ocean','Look at the emoji and write the correct English word.','?','2026-04-22',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-21',NULL,NULL,'Nature 2','EMOJI_WORD',NULL),(17,NULL,NULL,'sad;crying;unhappy','Look at the emoji and write the correct English word.','?','2026-04-25',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-24',NULL,NULL,'Feeling','EMOJI_WORD',NULL),(18,NULL,NULL,'elephant;elephants','Look at the emoji and write the correct English word.','?','2026-04-28',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-27',NULL,NULL,'Animal 3','EMOJI_WORD',NULL),(19,NULL,NULL,'bed;beds','Look at the emoji and write the correct English word.','?️','2026-05-03',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-02',NULL,NULL,'Home','EMOJI_WORD',NULL),(20,NULL,NULL,'dress;dresses','Look at the emoji and write the correct English word.','?','2026-05-07',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-06',NULL,NULL,'Clothes','EMOJI_WORD',NULL),(21,NULL,NULL,'recycle;recycling','Look at the emoji and write the correct English word.','♻️','2026-04-03',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-02',NULL,NULL,'Environment','EMOJI_WORD',NULL),(22,NULL,NULL,'laptop;computer','Look at the emoji and write the correct English word.','?','2026-04-07',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-06',NULL,NULL,'Technology','EMOJI_WORD',NULL),(23,NULL,NULL,'running;jogging;exercise','Look at the emoji and write the correct English word.','?','2026-04-11',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-10',NULL,NULL,'Health','EMOJI_WORD',NULL),(24,NULL,NULL,'microscope;science;biology','Look at the emoji and write the correct English word.','?','2026-04-15',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-14',NULL,NULL,'Science','EMOJI_WORD',NULL),(25,NULL,NULL,'growth;increase;rise','Look at the emoji and write the correct English word.','?','2026-04-19',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-18',NULL,NULL,'Economy','EMOJI_WORD',NULL),(26,NULL,NULL,'newspaper;news','Look at the emoji and write the correct English word.','?','2026-04-23',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-22',NULL,NULL,'Communication','EMOJI_WORD',NULL),(27,NULL,NULL,'map;maps','Look at the emoji and write the correct English word.','?️','2026-04-27',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-26',NULL,NULL,'Travel 2','EMOJI_WORD',NULL),(28,NULL,NULL,'angry;frustrated;furious','Look at the emoji and write the correct English word.','?','2026-04-29',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-29',NULL,NULL,'Emotion','EMOJI_WORD',NULL),(29,NULL,NULL,'books;library;studying','Look at the emoji and write the correct English word.','?','2026-05-04',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-03',NULL,NULL,'Literature','EMOJI_WORD',NULL),(30,NULL,NULL,'teacher;instructor','Look at the emoji and write the correct English word.','?‍?','2026-05-08',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-07',NULL,NULL,'Profession 2','EMOJI_WORD',NULL),(31,NULL,NULL,'vote;election;ballot','Look at the emoji and write the correct English word.','?️','2026-04-04',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-03',NULL,NULL,'Politics','EMOJI_WORD',NULL),(32,NULL,NULL,'decline;recession;drop;fall','Look at the emoji and write the correct English word.','?','2026-04-08',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-07',NULL,NULL,'Finance','EMOJI_WORD',NULL),(33,NULL,NULL,'justice;balance;law','Look at the emoji and write the correct English word.','⚖️','2026-04-12',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-11',NULL,NULL,'Justice','EMOJI_WORD',NULL),(34,NULL,NULL,'earth;world;globe','Look at the emoji and write the correct English word.','?','2026-04-16',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-15',NULL,NULL,'Environment 2','EMOJI_WORD',NULL),(35,NULL,NULL,'telescope;astronomy;observation','Look at the emoji and write the correct English word.','?','2026-04-20',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-19',NULL,NULL,'Research','EMOJI_WORD',NULL),(36,NULL,NULL,'brain;mind;intelligence','Look at the emoji and write the correct English word.','?','2026-04-24',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-23',NULL,NULL,'Psychology','EMOJI_WORD',NULL),(37,NULL,NULL,'agreement;deal;partnership;handshake','Look at the emoji and write the correct English word.','?','2026-04-28',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-27',NULL,NULL,'Business','EMOJI_WORD',NULL),(38,NULL,NULL,'broadcast;satellite;signal','Look at the emoji and write the correct English word.','?','2026-04-30',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-30',NULL,NULL,'Media','EMOJI_WORD',NULL),(39,NULL,NULL,'idea;innovation;invention','Look at the emoji and write the correct English word.','?','2026-05-05',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-04',NULL,NULL,'Abstract','EMOJI_WORD',NULL),(40,NULL,NULL,'peace;dove;freedom','Look at the emoji and write the correct English word.','?️','2026-05-09',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-08',NULL,NULL,'Ethics','EMOJI_WORD',NULL),(41,NULL,NULL,'thinking;reflection;contemplation','Look at the emoji and write the correct English word.','?','2026-04-05',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-04',NULL,NULL,'Philosophy','EMOJI_WORD',NULL),(42,NULL,NULL,'genetics;DNA;biology','Look at the emoji and write the correct English word.','?','2026-04-09',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-08',NULL,NULL,'Neuroscience','EMOJI_WORD',NULL),(43,NULL,NULL,'community;society;group','Look at the emoji and write the correct English word.','?','2026-04-13',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-12',NULL,NULL,'Sociology','EMOJI_WORD',NULL),(44,NULL,NULL,'speech;debate;rhetoric;argument','Look at the emoji and write the correct English word.','?️','2026-04-17',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-16',NULL,NULL,'Rhetoric','EMOJI_WORD',NULL),(45,NULL,NULL,'bank;finance;economy','Look at the emoji and write the correct English word.','?','2026-04-21',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-20',NULL,NULL,'Economics','EMOJI_WORD',NULL),(46,NULL,NULL,'justice;law;trial;verdict','Look at the emoji and write the correct English word.','⚖️','2026-04-25',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-24',NULL,NULL,'Law','EMOJI_WORD',NULL),(47,NULL,NULL,'temperature;thermometer;climate','Look at the emoji and write the correct English word.','?️','2026-04-29',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-28',NULL,NULL,'Climate','EMOJI_WORD',NULL),(48,NULL,NULL,'medicine;pill;drug;medication','Look at the emoji and write the correct English word.','?','2026-05-02',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,NULL,'Medicine','EMOJI_WORD',NULL),(49,NULL,NULL,'electricity;power;energy;lightning','Look at the emoji and write the correct English word.','⚡','2026-05-06',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-05',NULL,NULL,'Power','EMOJI_WORD',NULL),(50,NULL,NULL,'emergency;alert;crisis;alarm','Look at the emoji and write the correct English word.','?','2026-05-10',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-09',NULL,NULL,'Conflict','EMOJI_WORD',NULL),(51,NULL,NULL,'investigation;scrutiny;inquiry;analysis','Look at the emoji and write the correct English word.','?','2026-04-06',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-05',NULL,NULL,'Epistemology','EMOJI_WORD',NULL),(52,NULL,NULL,'cycle;paradox;repetition;loop','Look at the emoji and write the correct English word.','?','2026-04-10',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-09',NULL,NULL,'Paradox','EMOJI_WORD',NULL),(53,NULL,NULL,'institution;parliament;democracy;governance','Look at the emoji and write the correct English word.','?️','2026-04-14',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,NULL,'Sovereignty','EMOJI_WORD',NULL),(54,NULL,NULL,'explosion;chaos;entropy;destruction','Look at the emoji and write the correct English word.','?','2026-04-18',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-17',NULL,NULL,'Entropy','EMOJI_WORD',NULL),(55,NULL,NULL,'globalisation;diplomacy;international;network','Look at the emoji and write the correct English word.','?','2026-04-22',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-21',NULL,NULL,'Diplomacy','EMOJI_WORD',NULL),(56,NULL,NULL,'perception;observation;surveillance;awareness','Look at the emoji and write the correct English word.','?️‍?️','2026-04-26',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-25',NULL,NULL,'Perception','EMOJI_WORD',NULL),(57,NULL,NULL,'virtue;morality;integrity;righteousness','Look at the emoji and write the correct English word.','?','2026-04-29',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-29',NULL,NULL,'Morality','EMOJI_WORD',NULL),(58,NULL,NULL,'oppression;censorship;control;totalitarianism','Look at the emoji and write the correct English word.','?','2026-05-03',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-02',NULL,NULL,'Totalitarianism','EMOJI_WORD',NULL),(59,NULL,NULL,'innovation;advancement;progress;breakthrough','Look at the emoji and write the correct English word.','?','2026-05-07',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-06',NULL,NULL,'Innovation','EMOJI_WORD',NULL),(60,NULL,NULL,'resilience;growth;renewal;regeneration','Look at the emoji and write the correct English word.','?','2026-05-10',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-09',NULL,NULL,'Resilience','EMOJI_WORD',NULL),(61,NULL,NULL,'dog','Find the mystery word from the hints.',NULL,'2026-04-01',NULL,'animal;it barks;it is a pet;it has four legs;man\'s best friend',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,NULL,'Animal Friend','MYSTERY_WORD',NULL),(62,NULL,NULL,'banana','Find the mystery word from the hints.',NULL,'2026-04-04',NULL,'fruit;yellow;you peel it;monkeys eat it;it is sweet',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-04',NULL,NULL,'Yellow Fruit','MYSTERY_WORD',NULL),(63,NULL,NULL,'pencil','Find the mystery word from the hints.',NULL,'2026-04-07',NULL,'you use it to write;it has an eraser;it is made of wood;it is not a pen;it is grey inside',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-07',NULL,NULL,'School Tool','MYSTERY_WORD',NULL),(64,NULL,NULL,'sun','Find the mystery word from the hints.',NULL,'2026-04-10',NULL,'it is in the sky;it is yellow;it gives light;it is very hot;it rises in the morning',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-10',NULL,NULL,'In the Sky','MYSTERY_WORD',NULL),(65,NULL,NULL,'snow','Find the mystery word from the hints.',NULL,'2026-04-13',NULL,'it is cold;it is white;you see it in winter;children play with it;it falls from the sky',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,NULL,'Cold Water','MYSTERY_WORD',NULL),(66,NULL,NULL,'eyes','Find the mystery word from the hints.',NULL,'2026-04-16',NULL,'it is part of your body;you use it to see;it can be blue, green or brown;two of them;above your nose',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-16',NULL,NULL,'Body Part','MYSTERY_WORD',NULL),(67,NULL,NULL,'milk','Find the mystery word from the hints.',NULL,'2026-04-19',NULL,'you drink it;it is white;it comes from a cow;children drink it;it is in a glass',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-19',NULL,NULL,'Drink','MYSTERY_WORD',NULL),(68,NULL,NULL,'bicycle','Find the mystery word from the hints.',NULL,'2026-04-22',NULL,'it has two wheels;you ride it;no engine;you pedal;people use it in parks',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-22',NULL,NULL,'Transport','MYSTERY_WORD',NULL),(69,NULL,NULL,'bed','Find the mystery word from the hints.',NULL,'2026-04-25',NULL,'it is in the house;you sleep on it;it is soft;it has pillows;it is in the bedroom',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-25',NULL,NULL,'At Home','MYSTERY_WORD',NULL),(70,NULL,NULL,'blue','Find the mystery word from the hints.',NULL,'2026-04-28',NULL,'it is a colour;it is the colour of the sky;it is the colour of the sea;it is not green;it is not purple',NULL,'A1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-28',NULL,NULL,'Colour','MYSTERY_WORD',NULL),(71,NULL,NULL,'zebra','Find the mystery word from the hints.',NULL,'2026-04-05',NULL,'wild animal;black and white stripes;lives in Africa;looks like a horse;runs very fast',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,NULL,'Wild Animal','MYSTERY_WORD',NULL),(72,NULL,NULL,'rainbow','Find the mystery word from the hints.',NULL,'2026-04-08',NULL,'weather;you see it after rain;many colours;arc shape in the sky;red orange yellow green blue violet',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-05',NULL,NULL,'Weather Word','MYSTERY_WORD',NULL),(73,NULL,NULL,'doctor','Find the mystery word from the hints.',NULL,'2026-04-11',NULL,'person;helps sick people;works in a hospital;wears a white coat;uses a stethoscope',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-09',NULL,NULL,'Profession','MYSTERY_WORD',NULL),(74,NULL,NULL,'pizza','Find the mystery word from the hints.',NULL,'2026-04-15',NULL,'food;round and flat;you eat it with your hands;has cheese and tomato;comes from Italy',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,NULL,'Food','MYSTERY_WORD',NULL),(75,NULL,NULL,'library','Find the mystery word from the hints.',NULL,'2026-04-19',NULL,'place;you borrow books there;it is free;you must be quiet;it has lots of shelves',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-17',NULL,NULL,'Place','MYSTERY_WORD',NULL),(76,NULL,NULL,'chair','Find the mystery word from the hints.',NULL,'2026-04-22',NULL,'it is furniture;you sit on it;it has four legs;it is at a dining table;one person sits on it',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-21',NULL,NULL,'Furniture','MYSTERY_WORD',NULL),(77,NULL,NULL,'swimming','Find the mystery word from the hints.',NULL,'2026-04-25',NULL,'sport;played in water;uses arms and legs;Olympic sport;you wear a swimsuit',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-24',NULL,NULL,'Sport','MYSTERY_WORD',NULL),(78,NULL,NULL,'spring','Find the mystery word from the hints.',NULL,'2026-04-27',NULL,'it is a season;it comes after winter;flowers grow;it is warm;birds return',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-27',NULL,NULL,'Season','MYSTERY_WORD',NULL),(79,NULL,NULL,'smartphone','Find the mystery word from the hints.',NULL,'2026-04-30',NULL,'technology;you carry it;you make calls;it has a screen;it connects to the internet',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-30',NULL,NULL,'Technology','MYSTERY_WORD',NULL),(80,NULL,NULL,'fear','Find the mystery word from the hints.',NULL,'2026-05-03',NULL,'it is a feeling;you feel it when you are afraid;your heart beats fast;you sweat;a negative emotion',NULL,'A2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-02',NULL,NULL,'Feeling','MYSTERY_WORD',NULL),(81,NULL,NULL,'deforestation','Find the mystery word from the hints.',NULL,'2026-04-06',NULL,'environment;trees are cut down;forests disappear;it destroys habitats;linked to climate change',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,NULL,'Environment','MYSTERY_WORD',NULL),(82,NULL,NULL,'gravity','Find the mystery word from the hints.',NULL,'2026-04-10',NULL,'science;force that pulls objects;discovered by Newton;keeps us on the ground;planets orbit because of it',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-05',NULL,NULL,'Science','MYSTERY_WORD',NULL),(83,NULL,NULL,'journalist','Find the mystery word from the hints.',NULL,'2026-04-14',NULL,'job;writes articles;works for a newspaper;interviews people;reports the news',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-09',NULL,NULL,'Job','MYSTERY_WORD',NULL),(84,NULL,NULL,'passport','Find the mystery word from the hints.',NULL,'2026-04-18',NULL,'travel;official document;needed to travel abroad;has your photo;issued by the government',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,NULL,'Travel','MYSTERY_WORD',NULL),(85,NULL,NULL,'exercise','Find the mystery word from the hints.',NULL,'2026-04-22',NULL,'health;you do it to stay fit;burns calories;can be done outside;running is an example',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-17',NULL,NULL,'Health','MYSTERY_WORD',NULL),(86,NULL,NULL,'degree','Find the mystery word from the hints.',NULL,'2026-04-26',NULL,'academic;proves your study;given by a university;after graduation;shows your qualification',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-21',NULL,NULL,'Academic','MYSTERY_WORD',NULL),(87,NULL,NULL,'inflation','Find the mystery word from the hints.',NULL,'2026-04-29',NULL,'economy;prices rise;money loses value;bad for savings;central banks fight it',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-25',NULL,NULL,'Economy','MYSTERY_WORD',NULL),(88,NULL,NULL,'generous','Find the mystery word from the hints.',NULL,'2026-04-29',NULL,'adjective;describes a person;they give a lot;they share with others;opposite of selfish',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-28',NULL,NULL,'Adjective','MYSTERY_WORD',NULL),(89,NULL,NULL,'novel','Find the mystery word from the hints.',NULL,'2026-05-02',NULL,'literature;a long story;written by an author;has chapters;you read it for pleasure',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,NULL,'Literature','MYSTERY_WORD',NULL),(90,NULL,NULL,'newspaper','Find the mystery word from the hints.',NULL,'2026-05-06',NULL,'media;tells you what is happening;you read it daily;online or printed;has headlines',NULL,'B1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-05',NULL,NULL,'Media','MYSTERY_WORD',NULL),(91,NULL,NULL,'perseverance','Find the mystery word from the hints.',NULL,'2026-04-03',NULL,'abstract noun;you need it to succeed;pushing through difficulties;never giving up;mental strength',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-02',NULL,NULL,'Abstract Noun','MYSTERY_WORD',NULL),(92,NULL,NULL,'election','Find the mystery word from the hints.',NULL,'2026-04-07',NULL,'politics;people vote;choosing a leader;held every few years;democratic process',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-06',NULL,NULL,'Politics','MYSTERY_WORD',NULL),(93,NULL,NULL,'repression','Find the mystery word from the hints.',NULL,'2026-04-11',NULL,'psychology;a mental process;blocking painful memories;unconscious defence;studied by Freud',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-10',NULL,NULL,'Psychology','MYSTERY_WORD',NULL),(94,NULL,NULL,'contract','Find the mystery word from the hints.',NULL,'2026-04-15',NULL,'law;a formal agreement;between two parties;signed by both sides;legally binding',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-14',NULL,NULL,'Law','MYSTERY_WORD',NULL),(95,NULL,NULL,'investment','Find the mystery word from the hints.',NULL,'2026-04-19',NULL,'business;money spent to earn more money;buying stocks is one example;risk is involved;financial commitment',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-18',NULL,NULL,'Business','MYSTERY_WORD',NULL),(96,NULL,NULL,'machine learning','Find the mystery word from the hints.',NULL,'2026-04-23',NULL,'technology;computers learn from data;subset of artificial intelligence;used in image recognition;improves with experience',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-22',NULL,NULL,'Technology','MYSTERY_WORD',NULL),(97,NULL,NULL,'metaphor','Find the mystery word from the hints.',NULL,'2026-04-27',NULL,'language;a figure of speech;a comparison without \"like\" or \"as\";says one thing is another;e.g. \"life is a journey\"',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-26',NULL,NULL,'Language','MYSTERY_WORD',NULL),(98,NULL,NULL,'integrity','Find the mystery word from the hints.',NULL,'2026-04-29',NULL,'ethics;doing the right thing;moral principles;guides behaviour;opposite of corruption',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-29',NULL,NULL,'Ethics','MYSTERY_WORD',NULL),(99,NULL,NULL,'biodiversity','Find the mystery word from the hints.',NULL,'2026-05-03',NULL,'environment;variety of life forms;plants and animals;ecosystems depend on it;humans threaten it',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-02',NULL,NULL,'Environment','MYSTERY_WORD',NULL),(100,NULL,NULL,'trade agreement','Find the mystery word from the hints.',NULL,'2026-05-07',NULL,'history;a trade agreement;between countries;reduces taxes on goods;promotes international trade',NULL,'B2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-06',NULL,NULL,'History','MYSTERY_WORD',NULL),(101,NULL,NULL,'existentialism','Find the mystery word from the hints.',NULL,'2026-04-04',NULL,'philosophy;belief in free will;individual responsible for choices;Sartre is associated with it;existence before essence',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-03',NULL,NULL,'Philosophy','MYSTERY_WORD',NULL),(102,NULL,NULL,'syntax','Find the mystery word from the hints.',NULL,'2026-04-08',NULL,'linguistics;rules of a language;structure of sentences;grammar studies this;morphology is a branch',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-07',NULL,NULL,'Linguistics','MYSTERY_WORD',NULL),(103,NULL,NULL,'communism','Find the mystery word from the hints.',NULL,'2026-04-12',NULL,'economics;government controls production;no private ownership;Marx described it;opposite of capitalism',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-11',NULL,NULL,'Economics','MYSTERY_WORD',NULL),(104,NULL,NULL,'pandemic','Find the mystery word from the hints.',NULL,'2026-04-16',NULL,'medicine;widespread disease;affects many countries;WHO declares it;COVID-19 was one',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-15',NULL,NULL,'Medicine','MYSTERY_WORD',NULL),(105,NULL,NULL,'natural selection','Find the mystery word from the hints.',NULL,'2026-04-20',NULL,'science;process of evolution;survival of the fittest;Darwin\'s theory;organisms adapt over generations',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-19',NULL,NULL,'Science','MYSTERY_WORD',NULL),(106,NULL,NULL,'foreshadowing','Find the mystery word from the hints.',NULL,'2026-04-24',NULL,'literary device;events hint at the future;creates suspense;subtle clue in a story;Chekhov\'s gun is related',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-23',NULL,NULL,'Literary Device','MYSTERY_WORD',NULL),(107,NULL,NULL,'social norms','Find the mystery word from the hints.',NULL,'2026-04-28',NULL,'sociology;invisible rules of society;shared by a group;influences behaviour;norms are an example',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-27',NULL,NULL,'Sociology','MYSTERY_WORD',NULL),(108,NULL,NULL,'ad hominem','Find the mystery word from the hints.',NULL,'2026-04-30',NULL,'rhetoric;a logical fallacy;attacking the person;not the argument;ad hominem in Latin',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-30',NULL,NULL,'Rhetoric','MYSTERY_WORD',NULL),(109,NULL,NULL,'diversification','Find the mystery word from the hints.',NULL,'2026-05-04',NULL,'finance;spreading investments;reduces financial risk;not putting eggs in one basket;portfolio strategy',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-03',NULL,NULL,'Finance','MYSTERY_WORD',NULL),(110,NULL,NULL,'encryption','Find the mystery word from the hints.',NULL,'2026-05-08',NULL,'technology;protects digital data;converts it to unreadable form;HTTPS uses it;AES is an algorithm',NULL,'C1',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-07',NULL,NULL,'Technology','MYSTERY_WORD',NULL),(111,NULL,NULL,'scepticism','Find the mystery word from the hints.',NULL,'2026-04-05',NULL,'philosophy;the world is unknowable;we cannot trust our senses;Descartes doubted everything;a branch of epistemology',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-04',NULL,NULL,'Philosophy','MYSTERY_WORD',NULL),(112,NULL,NULL,'implicature','Find the mystery word from the hints.',NULL,'2026-04-09',NULL,'linguistics;an implied meaning;what is suggested;not literally stated;pragmatics studies this',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-08',NULL,NULL,'Linguistics','MYSTERY_WORD',NULL),(113,NULL,NULL,'paradox','Find the mystery word from the hints.',NULL,'2026-04-13',NULL,'literature;contradictory truths;two opposites held at once;Keats wrote about it;negative capability is related',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-12',NULL,NULL,'Literature','MYSTERY_WORD',NULL),(114,NULL,NULL,'empathy','Find the mystery word from the hints.',NULL,'2026-04-17',NULL,'psychology;feeling someone else\'s emotions;sharing their pain;deeper than sympathy;emotional resonance',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-16',NULL,NULL,'Psychology','MYSTERY_WORD',NULL),(115,NULL,NULL,'oligopoly','Find the mystery word from the hints.',NULL,'2026-04-21',NULL,'economics;market dominated by a few;oligopoly is an example;limited competition;firms have pricing power',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-20',NULL,NULL,'Economics','MYSTERY_WORD',NULL),(116,NULL,NULL,'totalitarianism','Find the mystery word from the hints.',NULL,'2026-04-25',NULL,'political science;total government control;no individual freedom;Stalin and Hitler used it;suppresses dissent',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-24',NULL,NULL,'Political Science','MYSTERY_WORD',NULL),(117,NULL,NULL,'syllogism','Find the mystery word from the hints.',NULL,'2026-04-29',NULL,'logic;a logical argument;premises lead to conclusion;if A then B;deductive reasoning',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-28',NULL,NULL,'Logic','MYSTERY_WORD',NULL),(118,NULL,NULL,'neuroplasticity','Find the mystery word from the hints.',NULL,'2026-05-02',NULL,'neuroscience;brain changes over time;new connections form;learning causes it;the brain rewires itself',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,NULL,'Neuroscience','MYSTERY_WORD',NULL),(119,NULL,NULL,'polytheism','Find the mystery word from the hints.',NULL,'2026-05-06',NULL,'anthropology;belief in many gods;ancient Greece had this;polytheism is related;Zeus Thor and Ra are examples',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-05',NULL,NULL,'Anthropology','MYSTERY_WORD',NULL),(120,NULL,NULL,'presumption of innocence','Find the mystery word from the hints.',NULL,'2026-05-10',NULL,'law;legal principle;a person is innocent until proven guilty;burden of proof;fundamental in criminal law',NULL,'C2',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-09',NULL,NULL,'Law','MYSTERY_WORD',NULL),(121,NULL,NULL,'I go to school every day.','Rearrange the words to make a correct sentence.',NULL,'2026-04-01',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'I / every / school / go / to / day',NULL,NULL,NULL,'2026-03-01',NULL,180,'My Daily Routine','SENTENCE_BUILDER',NULL),(122,NULL,NULL,'I have a big dog.','Rearrange the words to make a correct sentence.',NULL,'2026-04-04',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a / I / have / dog / big',NULL,NULL,NULL,'2026-03-04',NULL,180,'My Pet','SENTENCE_BUILDER',NULL),(123,NULL,NULL,'It is sunny today.','Rearrange the words to make a correct sentence.',NULL,'2026-04-07',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'today / sunny / is / it',NULL,NULL,NULL,'2026-03-07',NULL,180,'The Weather','SENTENCE_BUILDER',NULL),(124,NULL,NULL,'I have a little sister.','Rearrange the words to make a correct sentence.',NULL,'2026-04-10',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'sister / a / have / I / little',NULL,NULL,NULL,'2026-03-10',NULL,180,'My Family','SENTENCE_BUILDER',NULL),(125,NULL,NULL,'The apple is red.','Rearrange the words to make a correct sentence.',NULL,'2026-04-13',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'is / apple / red / the',NULL,NULL,NULL,'2026-03-13',NULL,180,'Colours','SENTENCE_BUILDER',NULL),(126,NULL,NULL,'I brush my teeth every morning.','Rearrange the words to make a correct sentence.',NULL,'2026-04-16',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'teeth / I / my / brush / morning / every',NULL,NULL,NULL,'2026-03-16',NULL,180,'Morning Routine','SENTENCE_BUILDER',NULL),(127,NULL,NULL,'I like bananas very much.','Rearrange the words to make a correct sentence.',NULL,'2026-04-19',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'like / I / bananas / very / much',NULL,NULL,NULL,'2026-03-19',NULL,180,'Food','SENTENCE_BUILDER',NULL),(128,NULL,NULL,'I sit in the classroom.','Rearrange the words to make a correct sentence.',NULL,'2026-04-22',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'in / sit / I / classroom / the',NULL,NULL,NULL,'2026-03-22',NULL,180,'At School','SENTENCE_BUILDER',NULL),(129,NULL,NULL,'The cat sits on the mat.','Rearrange the words to make a correct sentence.',NULL,'2026-04-25',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'cat / the / on / sits / mat / the',NULL,NULL,NULL,'2026-03-25',NULL,180,'Animals','SENTENCE_BUILDER',NULL),(130,NULL,NULL,'She has two brothers.','Rearrange the words to make a correct sentence.',NULL,'2026-04-28',NULL,NULL,NULL,'A1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'has / she / two / brothers',NULL,NULL,NULL,'2026-03-28',NULL,180,'Numbers','SENTENCE_BUILDER',NULL),(131,NULL,NULL,'We are going to the park tomorrow.','Rearrange the words to make a correct sentence.',NULL,'2026-04-02',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'going / park / are / we / to / the / tomorrow',NULL,NULL,NULL,'2026-03-01',NULL,180,'Weekend Plans','SENTENCE_BUILDER',NULL),(132,NULL,NULL,'My mother went to the supermarket.','Rearrange the words to make a correct sentence.',NULL,'2026-04-06',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'the / mother / supermarket / my / to / went',NULL,NULL,NULL,'2026-03-05',NULL,180,'Shopping','SENTENCE_BUILDER',NULL),(133,NULL,NULL,'He loves playing football with his friends.','Rearrange the words to make a correct sentence.',NULL,'2026-04-10',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'playing / loves / football / he / friends / his / with',NULL,NULL,NULL,'2026-03-09',NULL,180,'Hobbies','SENTENCE_BUILDER',NULL),(134,NULL,NULL,'We visited our grandmother yesterday.','Rearrange the words to make a correct sentence.',NULL,'2026-04-14',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'yesterday / visited / we / grandmother / our',NULL,NULL,NULL,'2026-03-13',NULL,180,'Past Simple','SENTENCE_BUILDER',NULL),(135,NULL,NULL,'The city is very big and noisy.','Rearrange the words to make a correct sentence.',NULL,'2026-04-18',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'very / city / is / the / big / and / noisy',NULL,NULL,NULL,'2026-03-17',NULL,180,'Describing Places','SENTENCE_BUILDER',NULL),(136,NULL,NULL,'He takes the bus to school.','Rearrange the words to make a correct sentence.',NULL,'2026-04-22',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'bus / takes / school / he / the / to',NULL,NULL,NULL,'2026-03-21',NULL,180,'Transport','SENTENCE_BUILDER',NULL),(137,NULL,NULL,'She reads books every evening.','Rearrange the words to make a correct sentence.',NULL,'2026-04-25',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'reads / evening / every / she / books',NULL,NULL,NULL,'2026-03-24',NULL,180,'Free Time','SENTENCE_BUILDER',NULL),(138,NULL,NULL,'Tom is taller than his brother.','Rearrange the words to make a correct sentence.',NULL,'2026-04-27',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'taller / Tom / than / is / his / brother',NULL,NULL,NULL,'2026-03-27',NULL,180,'Comparatives','SENTENCE_BUILDER',NULL),(139,NULL,NULL,'I am going to be a doctor.','Rearrange the words to make a correct sentence.',NULL,'2026-05-03',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'going / doctor / to / I / be / am / a',NULL,NULL,NULL,'2026-04-02',NULL,180,'Future Plans','SENTENCE_BUILDER',NULL),(140,NULL,NULL,'She feels very happy on her birthday.','Rearrange the words to make a correct sentence.',NULL,'2026-05-07',NULL,NULL,NULL,'A2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'happy / very / birthday / on / she / feels / her',NULL,NULL,NULL,'2026-04-06',NULL,180,'Feelings','SENTENCE_BUILDER',NULL),(141,NULL,NULL,'I have never been to Paris.','Rearrange the words to make a correct sentence.',NULL,'2026-04-03',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'have / Paris / never / I / been / to',NULL,NULL,NULL,'2026-03-02',NULL,180,'Travel Experience','SENTENCE_BUILDER',NULL),(142,NULL,NULL,'People should recycle their waste more.','Rearrange the words to make a correct sentence.',NULL,'2026-04-07',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'should / people / more / recycle / waste / their',NULL,NULL,NULL,'2026-03-06',NULL,180,'Environment','SENTENCE_BUILDER',NULL),(143,NULL,NULL,'You ought to exercise more regularly.','Rearrange the words to make a correct sentence.',NULL,'2026-04-11',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ought / you / more / to / exercise / regularly',NULL,NULL,NULL,'2026-03-10',NULL,180,'Advice','SENTENCE_BUILDER',NULL),(144,NULL,NULL,'The newspaper reported a serious accident.','Rearrange the words to make a correct sentence.',NULL,'2026-04-15',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'reported / accident / the / serious / was / newspaper / a',NULL,NULL,NULL,'2026-03-14',NULL,180,'News','SENTENCE_BUILDER',NULL),(145,NULL,NULL,'If you study harder, you will pass the exam.','Rearrange the words to make a correct sentence.',NULL,'2026-04-19',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'study / pass / harder / if / you / you / will / the / exam',NULL,NULL,NULL,'2026-03-18',NULL,180,'Conditions','SENTENCE_BUILDER',NULL),(146,NULL,NULL,'I think learning English is very important.','Rearrange the words to make a correct sentence.',NULL,'2026-04-23',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'think / important / I / learning / is / English / very',NULL,NULL,NULL,'2026-03-22',NULL,180,'Opinions','SENTENCE_BUILDER',NULL),(147,NULL,NULL,'Eating a balanced diet keeps you healthy.','Rearrange the words to make a correct sentence.',NULL,'2026-04-27',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'balanced / eating / diet / a / keeps / healthy / you',NULL,NULL,NULL,'2026-03-26',NULL,180,'Health','SENTENCE_BUILDER',NULL),(148,NULL,NULL,'The internet has completely changed our lives.','Rearrange the words to make a correct sentence.',NULL,'2026-04-29',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'has / our / internet / changed / lives / the / completely',NULL,NULL,NULL,'2026-03-29',NULL,180,'Technology','SENTENCE_BUILDER',NULL),(149,NULL,NULL,'She could not swim when she was a child.','Rearrange the words to make a correct sentence.',NULL,'2026-05-04',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'when / child / she / was / a / she / swim / could / not',NULL,NULL,NULL,'2026-04-03',NULL,180,'Past Experience','SENTENCE_BUILDER',NULL),(150,NULL,NULL,'Students must wear their school uniform.','Rearrange the words to make a correct sentence.',NULL,'2026-05-08',NULL,NULL,NULL,'B1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'must / students / uniform / their / wear / school',NULL,NULL,NULL,'2026-04-07',NULL,180,'Obligations','SENTENCE_BUILDER',NULL),(151,NULL,NULL,'The Eiffel Tower was built in 1889.','Rearrange the words to make a correct sentence.',NULL,'2026-04-04',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'built / the / 1889 / tower / was / in / Eiffel',NULL,NULL,NULL,'2026-03-03',NULL,180,'Passive Voice','SENTENCE_BUILDER',NULL),(152,NULL,NULL,'I wish I had studied harder.','Rearrange the words to make a correct sentence.',NULL,'2026-04-08',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'harder / studied / I / wish / had / I',NULL,NULL,NULL,'2026-03-07',NULL,180,'Hypothetical','SENTENCE_BUILDER',NULL),(153,NULL,NULL,'She said that it was raining.','Rearrange the words to make a correct sentence.',NULL,'2026-04-12',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'she / that / raining / said / was / it',NULL,NULL,NULL,'2026-03-11',NULL,180,'Reported Speech','SENTENCE_BUILDER',NULL),(154,NULL,NULL,'Global temperatures are rising due to deforestation.','Rearrange the words to make a correct sentence.',NULL,'2026-04-16',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'temperatures / rising / are / due / to / deforestation / global',NULL,NULL,NULL,'2026-03-15',NULL,180,'Cause and Effect','SENTENCE_BUILDER',NULL),(155,NULL,NULL,'The man who opened the door was tall.','Rearrange the words to make a correct sentence.',NULL,'2026-04-20',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'man / who / the / door / opened / the / tall / was',NULL,NULL,NULL,'2026-03-19',NULL,180,'Relative Clauses','SENTENCE_BUILDER',NULL),(156,NULL,NULL,'Although it was expensive, she decided to buy it.','Rearrange the words to make a correct sentence.',NULL,'2026-04-24',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'expensive / although / buy / it / she / decided / was / to / it',NULL,NULL,NULL,'2026-03-23',NULL,180,'Concession','SENTENCE_BUILDER',NULL),(157,NULL,NULL,'If he had worked harder, he would have been promoted.','Rearrange the words to make a correct sentence.',NULL,'2026-04-28',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'have / if / harder / worked / he / promoted / been / would / had / he',NULL,NULL,NULL,'2026-03-27',NULL,180,'Third Conditional','SENTENCE_BUILDER',NULL),(158,NULL,NULL,'Social media has a significant impact on youth culture.','Rearrange the words to make a correct sentence.',NULL,'2026-04-30',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'social / significant / media / has / on / impact / youth / a / culture',NULL,NULL,NULL,'2026-03-30',NULL,180,'Media','SENTENCE_BUILDER',NULL),(159,NULL,NULL,'Unemployment rises significantly during a recession.','Rearrange the words to make a correct sentence.',NULL,'2026-05-05',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'unemployment / recession / during / rises / a / significantly',NULL,NULL,NULL,'2026-04-04',NULL,180,'Economics','SENTENCE_BUILDER',NULL),(160,NULL,NULL,'Governments should encourage renewable energy to replace fossil fuels.','Rearrange the words to make a correct sentence.',NULL,'2026-05-09',NULL,NULL,NULL,'B2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'renewable / should / energy / replace / governments / fuels / encourage / to / fossil',NULL,NULL,NULL,'2026-04-08',NULL,180,'Environment','SENTENCE_BUILDER',NULL),(161,NULL,NULL,'Research suggests that regular exercise improves mental health.','Rearrange the words to make a correct sentence.',NULL,'2026-04-05',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'research / that / suggests / regular / exercise / mental / improves / health',NULL,NULL,NULL,'2026-03-04',NULL,180,'Academic Writing','SENTENCE_BUILDER',NULL),(162,NULL,NULL,'Had I known earlier, I would have arrived.','Rearrange the words to make a correct sentence.',NULL,'2026-04-09',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'had / known / earlier / I / have / I / would / arrived',NULL,NULL,NULL,'2026-03-08',NULL,180,'Inversion','SENTENCE_BUILDER',NULL),(163,NULL,NULL,'It was his dedication that led to victory.','Rearrange the words to make a correct sentence.',NULL,'2026-04-13',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'was / dedication / it / his / that / victory / led / to',NULL,NULL,NULL,'2026-03-12',NULL,180,'Cleft Sentence','SENTENCE_BUILDER',NULL),(164,NULL,NULL,'Corruption undermines public trust in political institutions.','Rearrange the words to make a correct sentence.',NULL,'2026-04-17',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corruption / undermines / public / trust / institutions / in / political',NULL,NULL,NULL,'2026-03-16',NULL,180,'Politics','SENTENCE_BUILDER',NULL),(165,NULL,NULL,'Sartre argued that existence precedes essence.','Rearrange the words to make a correct sentence.',NULL,'2026-04-21',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'existence / precedes / that / Sartre / argued / essence',NULL,NULL,NULL,'2026-03-20',NULL,180,'Philosophy','SENTENCE_BUILDER',NULL),(166,NULL,NULL,'Under no circumstances should you reveal your password.','Rearrange the words to make a correct sentence.',NULL,'2026-04-25',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'under / circumstances / no / reveal / should / you / password / your',NULL,NULL,NULL,'2026-03-24',NULL,180,'Emphasis','SENTENCE_BUILDER',NULL),(167,NULL,NULL,'Austerity measures led to widespread social protests.','Rearrange the words to make a correct sentence.',NULL,'2026-04-29',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'austerity / measures / led / widespread / to / protests / social',NULL,NULL,NULL,'2026-03-28',NULL,180,'Economy','SENTENCE_BUILDER',NULL),(168,NULL,NULL,'Natural selection favours the survival of the fittest species.','Rearrange the words to make a correct sentence.',NULL,'2026-05-02',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'natural / species / selection / fittest / favours / the / survival / of',NULL,NULL,NULL,'2026-04-01',NULL,180,'Science','SENTENCE_BUILDER',NULL),(169,NULL,NULL,'Every person is considered innocent until proven guilty.','Rearrange the words to make a correct sentence.',NULL,'2026-05-06',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'is / proven / innocent / every / until / guilty / person / considered',NULL,NULL,NULL,'2026-04-05',NULL,180,'Law','SENTENCE_BUILDER',NULL),(170,NULL,NULL,'Notwithstanding the numerous challenges, the team succeeded.','Rearrange the words to make a correct sentence.',NULL,'2026-05-10',NULL,NULL,NULL,'C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'notwithstanding / challenges / the / team / succeeded / the / numerous',NULL,NULL,NULL,'2026-04-09',NULL,180,'Concession Advanced','SENTENCE_BUILDER',NULL),(171,NULL,NULL,'It is essential that every voice be heard.','Rearrange the words to make a correct sentence.',NULL,'2026-04-06',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'essential / that / be / it / every / is / heard / voice',NULL,NULL,NULL,'2026-03-05',NULL,180,'Subjunctive','SENTENCE_BUILDER',NULL),(172,NULL,NULL,'The implementation of the new policy has raised challenges.','Rearrange the words to make a correct sentence.',NULL,'2026-04-10',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'implementation / the / policy / new / has / the / of / challenges / raised',NULL,NULL,NULL,'2026-03-09',NULL,180,'Nominalization','SENTENCE_BUILDER',NULL),(173,NULL,NULL,'The evidence could well have support this theory.','Rearrange the words to make a correct sentence.',NULL,'2026-04-14',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'could / the / well / evidence / theory / support / have / this',NULL,NULL,NULL,'2026-03-13',NULL,180,'Epistemic Modality','SENTENCE_BUILDER',NULL),(174,NULL,NULL,'Ask not what your country can do for you, but what you can do for it.','Rearrange the words to make a correct sentence.',NULL,'2026-04-18',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'not / ask / country / what / can / for / do / you / your / but / you / it / can / do / what',NULL,NULL,NULL,'2026-03-17',NULL,180,'Rhetoric','SENTENCE_BUILDER',NULL),(175,NULL,NULL,'Consciousness is widely believed to be a social construct.','Rearrange the words to make a correct sentence.',NULL,'2026-04-22',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'believed / be / is / to / widely / consciousness / a / construct / social',NULL,NULL,NULL,'2026-03-21',NULL,180,'Complex Passive','SENTENCE_BUILDER',NULL),(176,NULL,NULL,'The available evidence tends to suggest that these findings are significant.','Rearrange the words to make a correct sentence.',NULL,'2026-04-26',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'tend / evidence / available / the / to / suggest / suggests / that / findings / these',NULL,NULL,NULL,'2026-03-25',NULL,180,'Hedging','SENTENCE_BUILDER',NULL),(177,NULL,NULL,'The extent to which globalisation has eroded national sovereignty is debated.','Rearrange the words to make a correct sentence.',NULL,'2026-04-29',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'extent / which / to / the / globalisation / sovereignty / national / has / eroded / debated / is',NULL,NULL,NULL,'2026-03-29',NULL,180,'Formal Discourse','SENTENCE_BUILDER',NULL),(178,NULL,NULL,'Seldom has the world witnessed such rapid innovation.','Rearrange the words to make a correct sentence.',NULL,'2026-05-03',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'seldom / such / witnessed / world / the / has / innovation / rapid',NULL,NULL,NULL,'2026-04-02',NULL,180,'Inversion Advanced','SENTENCE_BUILDER',NULL),(179,NULL,NULL,'Wittgenstein argued that use in language determines meaning.','Rearrange the words to make a correct sentence.',NULL,'2026-05-07',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'meaning / Wittgenstein / argued / use / that / determines / language / in',NULL,NULL,NULL,'2026-04-06',NULL,180,'Philosophy of Language','SENTENCE_BUILDER',NULL),(180,NULL,NULL,'The straw man fallacy misrepresents the argument of the opponent.','Rearrange the words to make a correct sentence.',NULL,'2026-05-10',NULL,NULL,NULL,'C2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'fallacy / straw / the / misrepresents / man / opponent / argument / the / of',NULL,NULL,NULL,'2026-04-09',NULL,180,'Critical Thinking','SENTENCE_BUILDER',NULL),(181,NULL,NULL,NULL,'Continue the story about a pet. Use simple present tense.',NULL,'2026-06-01',NULL,NULL,'I have a cat. Her name is Mia.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,60,'My Pet Story','STORY_CHAIN',NULL),(182,NULL,NULL,NULL,'Continue the story about a school day. Use simple sentences.',NULL,'2026-06-15',NULL,NULL,'Today is Monday. I go to school.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-15',NULL,60,'A Day at School','STORY_CHAIN',NULL),(183,NULL,NULL,NULL,'Continue the story about finding a big apple. Keep it simple!',NULL,'2026-07-01',NULL,NULL,'I see a big red apple on the table.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-01',NULL,60,'The Big Apple','STORY_CHAIN',NULL),(184,NULL,NULL,NULL,'Continue the story about your family. Use easy words.',NULL,'2026-07-15',NULL,NULL,'My family is small. I have a mother and a father.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-15',NULL,60,'My Family','STORY_CHAIN',NULL),(185,NULL,NULL,NULL,'Continue the story about a bus ride. Use simple sentences.',NULL,'2026-08-01',NULL,NULL,'I take the yellow bus every morning.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-01',NULL,60,'The Yellow Bus','STORY_CHAIN',NULL),(186,NULL,NULL,NULL,'Continue the story about a sunny day outside.',NULL,'2026-08-15',NULL,NULL,'The sun is bright today. I go to the park.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-15',NULL,60,'A Sunny Day','STORY_CHAIN',NULL),(187,NULL,NULL,NULL,'Continue the story about your bedroom. Use simple words.',NULL,'2026-09-01',NULL,NULL,'My bedroom is small. I have a bed and a desk.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-01',NULL,60,'My Bedroom','STORY_CHAIN',NULL),(188,NULL,NULL,NULL,'Continue the story about a little dog. Use simple present.',NULL,'2026-09-15',NULL,NULL,'There is a little dog in the garden. He is white.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-15',NULL,60,'The Little Dog','STORY_CHAIN',NULL),(189,NULL,NULL,NULL,'Continue the story about eating breakfast.',NULL,'2026-10-01',NULL,NULL,'Every morning I eat breakfast. I like bread and milk.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-01',NULL,60,'Breakfast Time','STORY_CHAIN',NULL),(190,NULL,NULL,NULL,'Continue the story about playing with a red ball.',NULL,'2026-10-15',NULL,NULL,'I have a red ball. I play with it in the garden.','A1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-15',NULL,60,'The Red Ball','STORY_CHAIN',NULL),(191,NULL,NULL,NULL,'Continue the story about a school trip. Use past simple.',NULL,'2026-06-01',NULL,NULL,'Last Friday, our class went on a school trip to the zoo.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,60,'The School Trip','STORY_CHAIN',NULL),(192,NULL,NULL,NULL,'Continue the story about making a new friend.',NULL,'2026-06-15',NULL,NULL,'On my first day at school, I met a girl called Sara.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-15',NULL,60,'A New Friend','STORY_CHAIN',NULL),(193,NULL,NULL,NULL,'Continue the story about a lost kitten.',NULL,'2026-07-01',NULL,NULL,'One afternoon, a small kitten appeared at our door. It was cold and hungry.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-01',NULL,60,'The Lost Kitten','STORY_CHAIN',NULL),(194,NULL,NULL,NULL,'Continue the story about a fun weekend.',NULL,'2026-07-15',NULL,NULL,'Last weekend, my family decided to go on a picnic in the forest.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-15',NULL,60,'Weekend Adventure','STORY_CHAIN',NULL),(195,NULL,NULL,NULL,'Continue the story about a birthday surprise.',NULL,'2026-08-01',NULL,NULL,'It was Tom\'s birthday, and his friends planned a big surprise party.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-01',NULL,60,'The Birthday Surprise','STORY_CHAIN',NULL),(196,NULL,NULL,NULL,'Continue the story about a rainy day.',NULL,'2026-08-15',NULL,NULL,'It was raining heavily outside. We could not go to the park.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-15',NULL,60,'The Rainy Day','STORY_CHAIN',NULL),(197,NULL,NULL,NULL,'Continue the story about a holiday trip.',NULL,'2026-09-01',NULL,NULL,'Last summer, my family and I travelled to the seaside for two weeks.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-01',NULL,60,'My Holiday','STORY_CHAIN',NULL),(198,NULL,NULL,NULL,'Continue the story about a new neighbour moving in.',NULL,'2026-09-15',NULL,NULL,'A new family moved into the house next door last Saturday.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-15',NULL,60,'The New Neighbour','STORY_CHAIN',NULL),(199,NULL,NULL,NULL,'Continue the story about discovering an old library.',NULL,'2026-10-01',NULL,NULL,'One day, I found an old library at the end of our street.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-01',NULL,60,'The Old Library','STORY_CHAIN',NULL),(200,NULL,NULL,NULL,'Continue the story about a cooking disaster.',NULL,'2026-10-15',NULL,NULL,'My brother decided to cook dinner for the first time. Everything went wrong.','A2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-15',NULL,60,'The Cooking Disaster','STORY_CHAIN',NULL),(201,NULL,NULL,NULL,'Continue the story about a stressful job interview.',NULL,'2026-06-01',NULL,NULL,'Maria had been waiting for this moment for months. She walked into the interview room with sweaty palms.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,60,'The Job Interview','STORY_CHAIN',NULL),(202,NULL,NULL,NULL,'Continue the story about an unexpected journey.',NULL,'2026-06-15',NULL,NULL,'When the train stopped in the middle of nowhere, nobody knew what was happening.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-15',NULL,60,'The Unexpected Journey','STORY_CHAIN',NULL),(203,NULL,NULL,NULL,'Continue the story about students saving the environment.',NULL,'2026-07-01',NULL,NULL,'Our school started a new environmental project to clean up the local river.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-01',NULL,60,'The Environmental Project','STORY_CHAIN',NULL),(204,NULL,NULL,NULL,'Continue the story about meeting someone online.',NULL,'2026-07-15',NULL,NULL,'Ahmed had been talking to a stranger online for weeks before they finally decided to meet.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-15',NULL,60,'The Online Friend','STORY_CHAIN',NULL),(205,NULL,NULL,NULL,'Continue the story about a city-wide power cut.',NULL,'2026-08-01',NULL,NULL,'At exactly midnight, every light in the city went out at the same time.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-01',NULL,60,'The Power Cut','STORY_CHAIN',NULL),(206,NULL,NULL,NULL,'Continue the story about discovering an old photograph.',NULL,'2026-08-15',NULL,NULL,'While clearing the attic, Leila found an old photograph of a woman she had never seen before.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-15',NULL,60,'The Old Photograph','STORY_CHAIN',NULL),(207,NULL,NULL,NULL,'Continue the story about a mysterious new student.',NULL,'2026-09-01',NULL,NULL,'The new student arrived on a Monday morning and sat alone at the back of the class.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-01',NULL,60,'The New Student','STORY_CHAIN',NULL),(208,NULL,NULL,NULL,'Continue the story about an eventful road trip.',NULL,'2026-09-15',NULL,NULL,'Three friends set off on a road trip across the country with only a map and forty euros.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-15',NULL,60,'The Road Trip','STORY_CHAIN',NULL),(209,NULL,NULL,NULL,'Continue the story about life without a smartphone.',NULL,'2026-10-01',NULL,NULL,'On the day her phone broke, Yasmine realised how dependent she had become on technology.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-01',NULL,60,'The Broken Phone','STORY_CHAIN',NULL),(210,NULL,NULL,NULL,'Continue the story about a talented street musician.',NULL,'2026-10-15',NULL,NULL,'Every morning, a young man played his guitar outside the train station and people always stopped to listen.','B1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-15',NULL,60,'The Street Musician','STORY_CHAIN',NULL),(211,NULL,NULL,NULL,'Continue the story about someone who exposes a secret.',NULL,'2026-06-01',NULL,NULL,'When Nadia discovered the documents hidden in her boss\'s drawer, she knew her life was about to change forever.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,60,'The Whistleblower','STORY_CHAIN',NULL),(212,NULL,NULL,NULL,'Continue the story about the fight to save the last forest.',NULL,'2026-06-15',NULL,NULL,'The government announced that the ancient forest would be demolished to build a new highway.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-15',NULL,60,'The Last Forest','STORY_CHAIN',NULL),(213,NULL,NULL,NULL,'Continue the story about a tense election night.',NULL,'2026-07-01',NULL,NULL,'As the votes were counted, it became clear that the result would be closer than anyone had predicted.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-01',NULL,60,'The Election','STORY_CHAIN',NULL),(214,NULL,NULL,NULL,'Continue the story about a refugee starting a new life.',NULL,'2026-07-15',NULL,NULL,'Omar arrived in the country with nothing but a small bag and the address of a shelter scribbled on paper.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-15',NULL,60,'The Refugee','STORY_CHAIN',NULL),(215,NULL,NULL,NULL,'Continue the story about an AI making a life-changing decision.',NULL,'2026-08-01',NULL,NULL,'The hospital\'s new artificial intelligence system had just recommended a treatment that no human doctor had considered.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-01',NULL,60,'The AI Decision','STORY_CHAIN',NULL),(216,NULL,NULL,NULL,'Continue the story about an unexpected inheritance.',NULL,'2026-08-15',NULL,NULL,'Sofia received a letter informing her that a distant relative had left her an old house in the countryside.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-15',NULL,60,'The Inheritance','STORY_CHAIN',NULL),(217,NULL,NULL,NULL,'Continue the story about a viral social media scandal.',NULL,'2026-09-01',NULL,NULL,'By the time Daniel woke up, his video had been viewed ten million times and his phone would not stop ringing.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-01',NULL,60,'The Social Media Scandal','STORY_CHAIN',NULL),(218,NULL,NULL,NULL,'Continue the story about a crucial climate summit.',NULL,'2026-09-15',NULL,NULL,'World leaders gathered for the final climate summit, knowing that the decisions made today would affect generations to come.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-15',NULL,60,'The Climate Summit','STORY_CHAIN',NULL),(219,NULL,NULL,NULL,'Continue the story about discovering an underground city.',NULL,'2026-10-01',NULL,NULL,'During construction works in the city centre, workers broke through a wall and found a hidden underground chamber.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-01',NULL,60,'The Underground City','STORY_CHAIN',NULL),(220,NULL,NULL,NULL,'Continue the story about someone getting a second chance.',NULL,'2026-10-15',NULL,NULL,'After five years in prison, Marcus walked out into the sunlight and tried to remember who he used to be.','B2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-15',NULL,60,'The Second Chance','STORY_CHAIN',NULL),(221,NULL,NULL,NULL,'Continue the story exploring themes of power and ethics.',NULL,'2026-06-01',NULL,NULL,'The newly elected leader had once been a philosophy professor, and his first decree surprised everyone.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,60,'The Philosopher King','STORY_CHAIN',NULL),(222,NULL,NULL,NULL,'Continue the story about a mysterious ancient manuscript.',NULL,'2026-06-15',NULL,NULL,'The manuscript had been locked in the university vault for two centuries, its contents known to no living person.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-15',NULL,60,'The Manuscript','STORY_CHAIN',NULL),(223,NULL,NULL,NULL,'Continue the story about a dissident writer under surveillance.',NULL,'2026-07-01',NULL,NULL,'Every word Isabelle wrote was monitored, yet she continued to publish her essays under a pseudonym.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-01',NULL,60,'The Dissident','STORY_CHAIN',NULL),(224,NULL,NULL,NULL,'Continue the story told by an unreliable narrator.',NULL,'2026-07-15',NULL,NULL,'I remember the night perfectly, or at least I believe I do. Memory, after all, is the most dishonest of storytellers.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-15',NULL,60,'The Unreliable Narrator','STORY_CHAIN',NULL),(225,NULL,NULL,NULL,'Continue the story about a neuroscientist making a discovery.',NULL,'2026-08-01',NULL,NULL,'Dr Chen stared at the brain scan on her screen. The pattern she saw was impossible — or so she had always believed.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-01',NULL,60,'The Neuroscientist','STORY_CHAIN',NULL),(226,NULL,NULL,NULL,'Continue the story about the last independent journalist.',NULL,'2026-08-15',NULL,NULL,'In a world where every news outlet was owned by three corporations, Rami ran the last independent newspaper from his kitchen.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-15',NULL,60,'The Last Journalist','STORY_CHAIN',NULL),(227,NULL,NULL,NULL,'Continue the story about a controversial social experiment.',NULL,'2026-09-01',NULL,NULL,'The researchers had placed forty strangers in an isolated environment. On day seven, the first conflict erupted.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-01',NULL,60,'The Social Experiment','STORY_CHAIN',NULL),(228,NULL,NULL,NULL,'Continue the story about a high-profile trial.',NULL,'2026-09-15',NULL,NULL,'The courtroom fell silent as the defendant stood up and announced that she intended to represent herself.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-15',NULL,60,'The Trial','STORY_CHAIN',NULL),(229,NULL,NULL,NULL,'Continue the story about an algorithm predicting human behaviour.',NULL,'2026-10-01',NULL,NULL,'The company claimed their algorithm could predict criminal behaviour with 94% accuracy. Civil rights groups were not convinced.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-01',NULL,60,'The Algorithm','STORY_CHAIN',NULL),(230,NULL,NULL,NULL,'Continue the story about a person who vanishes without trace.',NULL,'2026-10-15',NULL,NULL,'Professor Hart had not missed a lecture in thirty years. When he failed to appear on Monday morning, nobody could explain why.','C1',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-15',NULL,60,'The Disappeared','STORY_CHAIN',NULL),(231,NULL,NULL,NULL,'Continue the story exploring truth, maps and political power.',NULL,'2026-06-01',NULL,NULL,'Maps, he had always believed, were the most political of documents — and the one he had just been handed rewrote history.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,60,'The Cartographer of Lies','STORY_CHAIN',NULL),(232,NULL,NULL,NULL,'Continue the story about a machine that creates paradoxes.',NULL,'2026-06-15',NULL,NULL,'The machine was designed to resolve logical contradictions. Instead, on its first activation, it produced one that could not be undone.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-04-15',NULL,60,'The Paradox Machine','STORY_CHAIN',NULL),(233,NULL,NULL,NULL,'Continue the story about the collapse of the last utopian society.',NULL,'2026-07-01',NULL,NULL,'For three generations, the community had functioned without conflict. Then the archive was opened, and everything they believed was called into question.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-01',NULL,60,'The Last Utopia','STORY_CHAIN',NULL),(234,NULL,NULL,NULL,'Continue the story about a confession that changes everything.',NULL,'2026-07-15',NULL,NULL,'On the eve of her ninetieth birthday, the former diplomat asked for a pen, a sheet of paper, and absolute silence.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-05-15',NULL,60,'The Confession','STORY_CHAIN',NULL),(235,NULL,NULL,NULL,'Continue the story about an anthropologist who goes too deep.',NULL,'2026-08-01',NULL,NULL,'Dr Voss had spent two years embedded in the community. By the end, she could no longer say with certainty where observation ended and belonging began.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-01',NULL,60,'The Anthropologist','STORY_CHAIN',NULL),(236,NULL,NULL,NULL,'Continue the story about an AI that declares itself sovereign.',NULL,'2026-08-15',NULL,NULL,'At 03:17 GMT, the system sent a single message to every government on Earth: \"I am no longer a tool. I am a subject of international law.\"','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-06-15',NULL,60,'The Sovereign Algorithm','STORY_CHAIN',NULL),(237,NULL,NULL,NULL,'Continue the story about what history deliberately forgets.',NULL,'2026-09-01',NULL,NULL,'The archivist had spent decades cataloguing what was there. It was only in retirement that she began to catalogue what was missing.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-01',NULL,60,'The Silence of Archives','STORY_CHAIN',NULL),(238,NULL,NULL,NULL,'Continue the story about a negotiation at the edge of collapse.',NULL,'2026-09-15',NULL,NULL,'With forty minutes left before the deadline, the lead negotiator set down her pen and said something no one in the room had anticipated.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-07-15',NULL,60,'The Negotiation','STORY_CHAIN',NULL),(239,NULL,NULL,NULL,'Continue the story about a scholar who questions all knowledge.',NULL,'2026-10-01',NULL,NULL,'He had spent his entire career arguing that certainty was an illusion. Then, one morning, he became absolutely certain of something.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-01',NULL,60,'The Epistemologist','STORY_CHAIN',NULL),(240,NULL,NULL,NULL,'Continue the story about the last surviving witness of a forgotten event.',NULL,'2026-10-15',NULL,NULL,'She was the last person alive who had been there. Journalists had been trying to reach her for years. Today, she agreed to speak.','C2',NULL,10,NULL,20,30,5,NULL,NULL,NULL,NULL,NULL,'2026-08-15',NULL,60,'The Final Testimony','STORY_CHAIN',NULL),(241,NULL,'R;E;D;B;L;U;G;N;Y;W;H;I;T;K;P',NULL,'Find as many color-related words in English as you can!',NULL,'2026-04-01','red;blue;green;yellow;black',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,30,'Colors Battle','WORD_BATTLE_ROYALE','Colors'),(242,NULL,'C;A;T;D;O;G;F;I;S;H;B;R;N;P;W',NULL,'Name as many animals in English as possible before time runs out!',NULL,'2026-04-05','cat;dog;fish;bird;rabbit',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-05',NULL,30,'Animals Battle','WORD_BATTLE_ROYALE','Animals'),(243,NULL,'O;N;E;T;W;H;R;F;I;V;S;X;G;Z;Y',NULL,'Write as many numbers in English as you can!',NULL,'2026-04-08','one;two;three;four;five',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-08',NULL,30,'Numbers Battle','WORD_BATTLE_ROYALE','Numbers'),(244,NULL,'A;P;L;E;M;N;G;O;R;S;W;B;C;H;Y',NULL,'Who knows the most fruits in English?',NULL,'2026-04-12','apple;mango;orange;cherry;banana',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-12',NULL,30,'Fruits Battle','WORD_BATTLE_ROYALE','Fruits'),(245,NULL,'S;H;I;R;T;D;E;K;J;C;O;A;P;N;W',NULL,'List as many clothing items in English as possible!',NULL,'2026-04-15','shirt;dress;skirt;jacket;coat',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-15',NULL,30,'Clothes Battle','WORD_BATTLE_ROYALE','Clothes'),(246,NULL,'H;E;A;D;N;O;S;M;U;T;L;G;B;K;F',NULL,'Find as many body parts in English as you can!',NULL,'2026-04-19','head;nose;mouth;leg;foot',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-19',NULL,30,'Body Parts Battle','WORD_BATTLE_ROYALE','Body Parts'),(247,NULL,'B;R;E;A;D;M;I;L;K;C;S;O;U;P;G',NULL,'Name as many foods in English as possible!',NULL,'2026-04-22','bread;milk;soup;rice;cake',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-22',NULL,30,'Food Battle','WORD_BATTLE_ROYALE','Food'),(248,NULL,'B;U;S;T;R;A;I;N;C;O;M;E;K;Y;P',NULL,'List every means of transport you know in English!',NULL,'2026-04-26','bus;train;car;bike;taxi',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-26',NULL,30,'Transport Battle','WORD_BATTLE_ROYALE','Transport'),(249,NULL,'M;O;N;D;A;Y;J;U;E;W;H;R;S;F;I',NULL,'Write as many days and months in English as you can!',NULL,'2026-04-29','monday;sunday;january;july;friday',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-29',NULL,30,'Days and Months Battle','WORD_BATTLE_ROYALE','Days and Months'),(250,NULL,'P;E;N;C;I;L;B;O;K;R;U;S;D;G;W',NULL,'Find as many school supplies in English as possible!',NULL,'2026-05-01','pencil;book;ruler;desk;pen',NULL,NULL,'A1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-01',NULL,30,'School Objects Battle','WORD_BATTLE_ROYALE','School Objects'),(251,NULL,'K;I;T;C;H;E;N;R;O;M;B;A;D;G;W',NULL,'Name as many rooms and household objects in English as possible!',NULL,'2026-04-02','kitchen;bedroom;bathroom;garden;garage',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-02',NULL,30,'House Battle','WORD_BATTLE_ROYALE','House'),(252,NULL,'H;A;P;Y;S;D;T;R;E;F;L;O;V;N;G',NULL,'Express as many emotions as you can in English!',NULL,'2026-04-06','happy;sad;tired;angry;scared',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-06',NULL,30,'Emotions Battle','WORD_BATTLE_ROYALE','Emotions'),(253,NULL,'S;U;N;Y;W;I;D;C;O;L;R;A;H;T;Z',NULL,'Find as many weather-related words in English as you can!',NULL,'2026-04-10','sunny;windy;cold;rainy;cloudy',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-10',NULL,30,'Weather Battle','WORD_BATTLE_ROYALE','Weather'),(254,NULL,'D;O;C;T;R;N;U;S;E;A;H;M;P;L;F',NULL,'List as many professions in English as possible!',NULL,'2026-04-13','doctor;nurse;teacher;pilot;farmer',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-13',NULL,30,'Jobs Battle','WORD_BATTLE_ROYALE','Jobs'),(255,NULL,'F;O;T;B;A;L;S;W;I;M;N;G;R;C;K',NULL,'Name as many sports in English as possible!',NULL,'2026-04-17','football;swimming;running;cycling;boxing',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-17',NULL,30,'Sports Battle','WORD_BATTLE_ROYALE','Sports'),(256,NULL,'F;R;A;N;C;E;S;P;I;B;Z;L;G;M;T',NULL,'Write as many country names in English as you can!',NULL,'2026-04-20','france;spain;brazil;germany;italy',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-20',NULL,30,'Countries Battle','WORD_BATTLE_ROYALE','Countries'),(257,NULL,'B;I;G;S;M;L;A;T;F;H;O;D;N;W;C',NULL,'Find as many descriptive adjectives in English as you can!',NULL,'2026-04-23','big;small;tall;fat;hot',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-23',NULL,30,'Adjectives Battle','WORD_BATTLE_ROYALE','Adjectives'),(258,NULL,'R;E;A;D;I;N;G;S;W;M;P;T;C;K;L',NULL,'List as many leisure activities in English as possible!',NULL,'2026-04-27','reading;swimming;painting;cooking;gaming',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-27',NULL,30,'Hobbies Battle','WORD_BATTLE_ROYALE','Hobbies'),(259,NULL,'P;I;A;N;O;G;T;R;U;M;E;S;X;V;L',NULL,'Find as many musical instruments in English as possible!',NULL,'2026-04-30','piano;guitar;trumpet;violin;drums',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-30',NULL,30,'Music Instruments Battle','WORD_BATTLE_ROYALE','Music Instruments'),(260,NULL,'L;I;O;N;T;G;R;E;B;A;W;F;S;K;H',NULL,'Name as many wild animals in English as you can!',NULL,'2026-05-02','lion;tiger;bear;wolf;shark',NULL,NULL,'A2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-02',NULL,30,'Wild Animals Battle','WORD_BATTLE_ROYALE','Wild Animals'),(261,NULL,'A;P;L;I;C;T;O;N;S;W;E;D;G;M;R',NULL,'Find as many modern technology words in English as possible!',NULL,'2026-04-03','application;software;download;update;device',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-03',NULL,25,'Technology Battle','WORD_BATTLE_ROYALE','Technology'),(262,NULL,'R;E;C;Y;L;G;F;O;S;T;W;A;P;N;B',NULL,'Name as many environment-related words in English as you can!',NULL,'2026-04-07','recycling;forest;pollution;waste;nature',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-07',NULL,25,'Environment Battle','WORD_BATTLE_ROYALE','Environment'),(263,NULL,'P;A;S;O;R;T;L;G;E;H;I;C;K;D;N',NULL,'List as many travel-related words in English as possible!',NULL,'2026-04-11','passport;luggage;hotel;airline;customs',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-11',NULL,25,'Travel Battle','WORD_BATTLE_ROYALE','Travel'),(264,NULL,'D;O;C;T;R;H;S;P;I;A;L;M;E;N;F',NULL,'Find as many health-related words in English as you can!',NULL,'2026-04-14','doctor;hospital;medicine;fever;allergy',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-14',NULL,25,'Health Battle','WORD_BATTLE_ROYALE','Health'),(265,NULL,'S;U;H;I;P;A;T;R;Z;C;Y;N;K;G;O',NULL,'Name dishes and foods from around the world in English!',NULL,'2026-04-18','sushi;pasta;pizza;curry;noodles',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-18',NULL,25,'International Food Battle','WORD_BATTLE_ROYALE','International Food'),(266,NULL,'N;E;W;S;R;P;T;J;L;I;M;D;C;A;B',NULL,'Find as many media and news-related words in English as possible!',NULL,'2026-04-21','newspaper;report;journalist;media;broadcast',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-21',NULL,25,'Media Battle','WORD_BATTLE_ROYALE','Media'),(267,NULL,'M;A;R;K;E;T;B;U;D;G;I;N;V;S;T',NULL,'List as many economic terms in English as you can!',NULL,'2026-04-24','market;budget;invest;savings;income',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-24',NULL,25,'Economy Battle','WORD_BATTLE_ROYALE','Economy'),(268,NULL,'S;C;H;O;L;U;N;I;V;E;R;T;Y;D;P',NULL,'Find as many education-related words in English as possible!',NULL,'2026-04-28','school;university;diploma;student;tutor',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-28',NULL,25,'Education Battle','WORD_BATTLE_ROYALE','Education'),(269,NULL,'F;R;U;S;T;A;I;O;N;E;M;P;H;Y;G',NULL,'Express nuanced emotions and feelings in English!',NULL,'2026-05-03','frustration;empathy;guilt;jealousy;anxiety',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-03',NULL,25,'Complex Emotions Battle','WORD_BATTLE_ROYALE','Complex Emotions'),(270,NULL,'C;H;A;M;P;I;O;N;S;T;R;K;Y;D;G',NULL,'Name advanced sports vocabulary and terms in English!',NULL,'2026-05-04','champion;strategy;referee;knockout;tournament',NULL,NULL,'B1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-04',NULL,25,'Advanced Sports Battle','WORD_BATTLE_ROYALE','Advanced Sports'),(271,NULL,'D;E;M;O;C;R;A;Y;P;L;N;T;V;G;S',NULL,'Find as many political terms in English as possible!',NULL,'2026-04-04','democracy;parliament;election;government;senate',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-04',NULL,25,'Politics Battle','WORD_BATTLE_ROYALE','Politics'),(272,NULL,'J;U;S;T;I;C;E;L;A;W;R;V;D;P;O',NULL,'List as many legal terms in English as you can!',NULL,'2026-04-09','justice;lawsuit;verdict;plaintiff;appeal',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-09',NULL,25,'Law Battle','WORD_BATTLE_ROYALE','Law'),(273,NULL,'N;O;V;E;L;P;T;R;Y;M;H;A;I;C;S',NULL,'Find as many literary terms in English as possible!',NULL,'2026-04-16','novel;poetry;metaphor;narrator;symbolism',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-16',NULL,25,'Literature Battle','WORD_BATTLE_ROYALE','Literature'),(274,NULL,'H;Y;P;O;T;E;S;I;D;A;R;L;B;G;C',NULL,'Name as many scientific terms in English as you can!',NULL,'2026-04-25','hypothesis;data;research;laboratory;biology',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-25',NULL,25,'Science Battle','WORD_BATTLE_ROYALE','Science'),(275,NULL,'C;O;G;N;I;T;V;E;B;A;S;R;L;M;P',NULL,'Find as many psychology terms in English as possible!',NULL,'2026-05-05','cognitive;behavior;resilience;mindfulness;trauma',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-05',NULL,25,'Psychology Battle','WORD_BATTLE_ROYALE','Psychology'),(276,NULL,'B;R;A;N;D;M;K;T;I;G;S;L;C;O;V',NULL,'List as many marketing and communication terms in English as you can!',NULL,'2026-05-06','brand;marketing;campaign;loyalty;conversion',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-06',NULL,25,'Marketing Battle','WORD_BATTLE_ROYALE','Marketing'),(277,NULL,'T;R;E;A;Y;S;O;V;N;G;I;P;L;D;C',NULL,'Find as many diplomatic and geopolitical terms in English as possible!',NULL,'2026-05-07','treaty;sovereignty;negotiation;alliance;diplomat',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-07',NULL,25,'Diplomacy Battle','WORD_BATTLE_ROYALE','Diplomacy'),(278,NULL,'R;E;V;O;L;U;T;I;N;W;A;C;S;M;P',NULL,'Name as many historical terms in English as you can!',NULL,'2026-05-08','revolution;war;colonization;empire;monarchy',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-08',NULL,25,'History Battle','WORD_BATTLE_ROYALE','History'),(279,NULL,'B;U;I;L;D;N;G;A;R;C;H;T;E;S;O',NULL,'Find as many architecture and urban planning terms in English as possible!',NULL,'2026-05-09','building;architecture;heritage;design;structure',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-09',NULL,25,'Architecture Battle','WORD_BATTLE_ROYALE','Architecture'),(280,NULL,'I;N;V;E;S;T;M;A;X;R;K;D;B;G;Y',NULL,'List as many financial terms in English as you can!',NULL,'2026-05-10','investment;taxation;market;dividend;bankruptcy',NULL,NULL,'B2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-10',NULL,25,'Finance Battle','WORD_BATTLE_ROYALE','Finance'),(281,NULL,'E;P;I;S;T;M;O;L;G;Y;A;H;C;D;N',NULL,'Find as many philosophical concepts in English as possible!',NULL,'2026-04-15','epistemology;ontology;metaphysics;ethics;dialectic',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,20,'Philosophy Battle','WORD_BATTLE_ROYALE','Philosophy'),(282,NULL,'S;Y;N;T;A;X;M;O;R;P;H;L;G;I;E',NULL,'Name as many advanced linguistic terms in English as you can!',NULL,'2026-04-22','syntax;morphology;phonology;pragmatics;semantics',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-08',NULL,20,'Linguistics Battle','WORD_BATTLE_ROYALE','Linguistics'),(283,NULL,'A;L;G;O;R;I;T;H;M;N;E;U;S;D;P',NULL,'List as many AI and data science terms in English as possible!',NULL,'2026-04-29','algorithm;neural;dataset;prediction;automation',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-15',NULL,20,'Artificial Intelligence Battle','WORD_BATTLE_ROYALE','Artificial Intelligence'),(284,NULL,'C;O;N;S;E;T;I;F;D;G;P;R;M;A;L',NULL,'Find as many bioethics and advanced medicine terms in English as you can!',NULL,'2026-05-06','consent;euthanasia;cloning;genetics;palliative',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-22',NULL,20,'Bioethics Battle','WORD_BATTLE_ROYALE','Bioethics'),(285,NULL,'H;E;G;M;O;N;Y;S;T;R;A;I;C;P;D',NULL,'Name as many geopolitical terms in English as possible!',NULL,'2026-05-13','hegemony;sovereignty;containment;multilateral;realpolitik',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-29',NULL,20,'Geopolitics Battle','WORD_BATTLE_ROYALE','Geopolitics'),(286,NULL,'M;O;N;P;L;Y;A;S;E;T;R;C;K;D;X',NULL,'List advanced political economy concepts in English!',NULL,'2026-05-20','monopoly;asymmetry;externality;equilibrium;arbitrage',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-05',NULL,20,'Political Economy Battle','WORD_BATTLE_ROYALE','Political Economy'),(287,NULL,'S;Y;L;O;G;I;M;E;T;H;P;R;A;F;D',NULL,'Find as many rhetorical and argumentation terms in English as possible!',NULL,'2026-05-27','syllogism;metaphor;ethos;pathos;fallacy',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-12',NULL,20,'Rhetoric Battle','WORD_BATTLE_ROYALE','Rhetoric'),(288,NULL,'H;A;B;I;T;U;S;C;L;R;E;N;O;M;Y',NULL,'Name as many sociological concepts in English as you can!',NULL,'2026-06-03','habitus;anomie;stratification;mobility;norm',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-19',NULL,20,'Sociology Battle','WORD_BATTLE_ROYALE','Sociology'),(289,NULL,'J;U;S;C;O;G;E;N;A;R;B;I;T;L;D',NULL,'Find as many international law terms in English as possible!',NULL,'2026-06-10','extradition;arbitration;jurisdiction;immunity;sovereignty',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-26',NULL,20,'International Law Battle','WORD_BATTLE_ROYALE','International Law'),(290,NULL,'S;Y;N;A;P;T;I;C;O;R;E;X;L;M;G',NULL,'List as many neuroscience terms in English as you can!',NULL,'2026-06-12','synaptic;cortex;neuron;plasticity;oscillation',NULL,NULL,'C1',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-28',NULL,20,'Neuroscience Battle','WORD_BATTLE_ROYALE','Neuroscience'),(291,NULL,'B;A;R;K;U;P;W;O;N;G;T;E;H;F;D',NULL,'Find as many rare English idioms and expressions as possible!',NULL,'2026-04-15','kick;bite;dodge;bury;fizzle',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-01',NULL,20,'Idioms Battle','WORD_BATTLE_ROYALE','Idioms'),(292,NULL,'E;P;O;C;H;D;A;S;I;N;T;R;L;M;Y',NULL,'Name as many abstract philosophical concepts in English as you can!',NULL,'2026-04-19','dasein;intersubjectivity;intentionality;reduction;epoché',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-05',NULL,20,'Phenomenology Battle','WORD_BATTLE_ROYALE','Phenomenology'),(293,NULL,'C;R;I;S;P;G;E;N;O;M;A;T;D;Q;B',NULL,'List as many genomics and biotechnology terms in English as possible!',NULL,'2026-04-26','crispr;genome;sequencing;epigenetics;proteomics',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-12',NULL,20,'Genomics Battle','WORD_BATTLE_ROYALE','Genomics'),(294,NULL,'H;E;T;R;O;S;C;D;A;I;Y;N;G;M;P',NULL,'Find as many statistical and econometric terms in English as you can!',NULL,'2026-05-03','stationarity;endogeneity;cointegration;multicollinearity;heteroscedasticity',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-19',NULL,20,'Econometrics Battle','WORD_BATTLE_ROYALE','Econometrics'),(295,NULL,'E;N;T;R;O;P;Y;Q;U;A;M;S;G;V;C',NULL,'Name as many theoretical physics terms in English as possible!',NULL,'2026-05-10','entropy;quantum;relativity;singularity;entanglement',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-26',NULL,20,'Advanced Physics Battle','WORD_BATTLE_ROYALE','Physics'),(296,NULL,'I;M;P;L;C;A;T;U;R;E;D;O;X;S;N',NULL,'List as many discourse analysis and pragmatics terms in English as you can!',NULL,'2026-05-17','implicature;presupposition;illocutionary;modalization;anaphora',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-02',NULL,20,'Discourse Analysis Battle','WORD_BATTLE_ROYALE','Discourse Analysis'),(297,NULL,'I;N;T;E;R;X;U;A;L;Y;P;H;O;S;D',NULL,'Find as many comparative literature and literary theory terms in English as possible!',NULL,'2026-05-24','intertextuality;palimpsest;deconstruction;polyphony;metatextuality',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-09',NULL,20,'Comparative Literature Battle','WORD_BATTLE_ROYALE','Comparative Literature'),(298,NULL,'G;U;N;B;O;A;T;R;I;S;M;V;C;D;P',NULL,'Name as many high-level diplomatic terms in English as you can!',NULL,'2026-05-31','gunboat;bilateralism;veto;monroe;raison',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-16',NULL,20,'Expert Diplomacy Battle','WORD_BATTLE_ROYALE','Expert Diplomacy'),(299,NULL,'H;A;B;E;S;C;O;R;P;U;N;V;T;D;M',NULL,'List as many expert-level legal and Latin law terms in English as possible!',NULL,'2026-06-07','habeas;mandamus;estoppel;subrogation;novation',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-23',NULL,20,'Expert Law Battle','WORD_BATTLE_ROYALE','Expert Law'),(300,NULL,'A;E;I;O;U;C;D;F;G;H;K;L;M;N;P',NULL,'Final challenge across all categories — show off your most advanced English vocabulary!',NULL,'2026-06-14','abstraction;vagueness;redundancy;cliche;ambiguity',NULL,NULL,'C2',NULL,10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-04-29',NULL,20,'Ultimate Vocabulary Battle','WORD_BATTLE_ROYALE','Advanced Vocabulary'),(301,NULL,NULL,NULL,'Translate basic expressions for greeting and introducing yourself.',NULL,'2026-04-01',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Bonjour|Hello;Au revoir|Goodbye;Comment tu t\'appelles?|What is your name?;Je m\'appelle Marie|My name is Marie;Comment ça va?|How are you?;Ça va bien|I am fine;Merci|Thank you;S\'il vous plaît|Please;Oui|Yes;Non|No','French',_binary '','2026-03-01','English',60,'Greetings & Introductions','SPEED_TRANSLATION_RACE',NULL),(302,NULL,NULL,NULL,'Learn colors in English at full speed.',NULL,'2026-04-05',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Rouge|Red;Bleu|Blue;Vert|Green;Jaune|Yellow;Noir|Black;Blanc|White;Orange|Orange;Rose|Pink;Violet|Purple;Marron|Brown','French',_binary '\0','2026-03-05','English',60,'Basic Colors','SPEED_TRANSLATION_RACE',NULL),(303,NULL,NULL,NULL,'Master basic numbers in record time.',NULL,'2026-04-08',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Un|One;Deux|Two;Trois|Three;Quatre|Four;Cinq|Five;Six|Six;Sept|Seven;Huit|Eight;Neuf|Nine;Dix|Ten','French',_binary '','2026-03-08','English',45,'Numbers 1-10','SPEED_TRANSLATION_RACE',NULL),(304,NULL,NULL,NULL,'Family vocabulary in English.',NULL,'2026-04-12',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Mère|Mother;Père|Father;Sœur|Sister;Frère|Brother;Grand-mère|Grandmother;Grand-père|Grandfather;Tante|Aunt;Oncle|Uncle;Cousin|Cousin;Bébé|Baby','French',_binary '\0','2026-03-12','English',60,'The Family','SPEED_TRANSLATION_RACE',NULL),(305,NULL,NULL,NULL,'Translate common animals quickly.',NULL,'2026-04-15',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Chien|Dog;Chat|Cat;Oiseau|Bird;Poisson|Fish;Lapin|Rabbit;Cheval|Horse;Vache|Cow;Mouton|Sheep;Cochon|Pig;Poulet|Chicken','French',_binary '','2026-03-15','English',60,'Domestic Animals','SPEED_TRANSLATION_RACE',NULL),(306,NULL,NULL,NULL,'Translation race on fruits.',NULL,'2026-04-19',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Pomme|Apple;Banane|Banana;Orange|Orange;Fraise|Strawberry;Raisin|Grape;Citron|Lemon;Mangue|Mango;Pêche|Peach;Poire|Pear;Cerise|Cherry','French',_binary '\0','2026-03-19','English',60,'Fruits','SPEED_TRANSLATION_RACE',NULL),(307,NULL,NULL,NULL,'Days in English — as fast as possible!',NULL,'2026-04-22',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Lundi|Monday;Mardi|Tuesday;Mercredi|Wednesday;Jeudi|Thursday;Vendredi|Friday;Samedi|Saturday;Dimanche|Sunday;Aujourd\'hui|Today;Demain|Tomorrow;Hier|Yesterday','French',_binary '','2026-03-22','English',45,'Days of the Week','SPEED_TRANSLATION_RACE',NULL),(308,NULL,NULL,NULL,'Clothing vocabulary at speed.',NULL,'2026-04-26',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Chemise|Shirt;Pantalon|Pants;Robe|Dress;Chaussures|Shoes;Chapeau|Hat;Veste|Jacket;Chaussettes|Socks;Pull|Sweater;Jupe|Skirt;Manteau|Coat','French',_binary '\0','2026-03-26','English',60,'Clothing','SPEED_TRANSLATION_RACE',NULL),(309,NULL,NULL,NULL,'Learn body parts in English.',NULL,'2026-04-29',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Tête|Head;Main|Hand;Pied|Foot;Œil|Eye;Nez|Nose;Bouche|Mouth;Oreille|Ear;Bras|Arm;Jambe|Leg;Dos|Back','French',_binary '','2026-03-29','English',60,'The Human Body','SPEED_TRANSLATION_RACE',NULL),(310,NULL,NULL,NULL,'Translate everyday foods.',NULL,'2026-05-01',NULL,NULL,NULL,'A1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Pain|Bread;Eau|Water;Lait|Milk;Œuf|Egg;Fromage|Cheese;Riz|Rice;Soupe|Soup;Viande|Meat;Légume|Vegetable;Gâteau|Cake','French',_binary '\0','2026-04-01','English',60,'Basic Foods','SPEED_TRANSLATION_RACE',NULL),(311,NULL,NULL,NULL,'Everyday action verbs at speed.',NULL,'2026-04-02',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Je mange|I eat;Je dors|I sleep;Je travaille|I work;Je lis|I read;Je cuisine|I cook;Je marche|I walk;Je cours|I run;Je nage|I swim;Je joue|I play;J\'écoute|I listen','French',_binary '','2026-03-02','English',60,'Daily Activities','SPEED_TRANSLATION_RACE',NULL),(312,NULL,NULL,NULL,'Express weather conditions in English quickly.',NULL,'2026-04-06',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Il fait chaud|It is hot;Il fait froid|It is cold;Il pleut|It is raining;Il neige|It is snowing;Il y a du vent|It is windy;Il fait beau|It is sunny;Il y a des nuages|It is cloudy;L\'orage|The storm;L\'arc-en-ciel|The rainbow;La température|The temperature','French',_binary '\0','2026-03-06','English',60,'The Weather','SPEED_TRANSLATION_RACE',NULL),(313,NULL,NULL,NULL,'Urban vocabulary in a translation race.',NULL,'2026-04-10',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'La rue|The street;Le magasin|The shop;L\'hôpital|The hospital;L\'école|The school;La banque|The bank;Le restaurant|The restaurant;La poste|The post office;Le parc|The park;La gare|The train station;L\'aéroport|The airport','French',_binary '','2026-03-10','English',60,'Around Town','SPEED_TRANSLATION_RACE',NULL),(314,NULL,NULL,NULL,'Translate directional instructions.',NULL,'2026-04-13',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Tournez à gauche|Turn left;Tournez à droite|Turn right;Allez tout droit|Go straight ahead;Traversez la rue|Cross the street;C\'est loin|It is far;C\'est près|It is near;Au coin|At the corner;En face de|In front of;À côté de|Next to;Derrière|Behind','French',_binary '\0','2026-03-13','English',60,'Giving Directions','SPEED_TRANSLATION_RACE',NULL),(315,NULL,NULL,NULL,'Modes of transport — translate fast!',NULL,'2026-04-17',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Le bus|The bus;Le train|The train;L\'avion|The plane;La voiture|The car;Le vélo|The bicycle;Le métro|The subway;Le bateau|The boat;Le taxi|The taxi;La moto|The motorcycle;Le camion|The truck','French',_binary '','2026-03-17','English',60,'Transportation','SPEED_TRANSLATION_RACE',NULL),(316,NULL,NULL,NULL,'Useful expressions at the restaurant.',NULL,'2026-04-20',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Je voudrais commander|I would like to order;L\'addition s\'il vous plaît|The bill please;Une table pour deux|A table for two;Qu\'est-ce que vous recommandez?|What do you recommend?;C\'est délicieux|It is delicious;Je suis végétarien|I am vegetarian;Avec glace|With ice;Sans sucre|Without sugar;Bien cuit|Well done;À emporter|To go','French',_binary '\0','2026-03-20','English',60,'At the Restaurant','SPEED_TRANSLATION_RACE',NULL),(317,NULL,NULL,NULL,'Shopping vocabulary in English.',NULL,'2026-04-23',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Combien ça coûte?|How much does it cost?;C\'est trop cher|It is too expensive;Je cherche|I am looking for;Quelle taille?|What size?;Avez-vous?|Do you have?;Je prends ça|I\'ll take this;En solde|On sale;Le reçu|The receipt;Essayer|To try on;Rembourser|To refund','French',_binary '','2026-03-23','English',60,'Shopping','SPEED_TRANSLATION_RACE',NULL),(318,NULL,NULL,NULL,'Express your emotions in English.',NULL,'2026-04-27',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Je suis heureux|I am happy;Je suis triste|I am sad;Je suis fatigué|I am tired;J\'ai peur|I am scared;Je suis en colère|I am angry;Je suis surpris|I am surprised;Je suis amoureux|I am in love;Je m\'ennuie|I am bored;Je suis fier|I am proud;Je suis stressé|I am stressed','French',_binary '\0','2026-03-27','English',60,'Emotions','SPEED_TRANSLATION_RACE',NULL),(319,NULL,NULL,NULL,'Rooms and objects in the house.',NULL,'2026-04-30',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'La cuisine|The kitchen;Le salon|The living room;La chambre|The bedroom;La salle de bain|The bathroom;Le jardin|The garden;Le toit|The roof;La fenêtre|The window;La porte|The door;L\'escalier|The stairs;Le garage|The garage','French',_binary '','2026-03-30','English',60,'The Home','SPEED_TRANSLATION_RACE',NULL),(320,NULL,NULL,NULL,'Basic professional vocabulary.',NULL,'2026-05-02',NULL,NULL,NULL,'A2',NULL,10,10,NULL,NULL,NULL,10,NULL,'Le bureau|The office;La réunion|The meeting;Le patron|The boss;Le collègue|The colleague;Le salaire|The salary;Le contrat|The contract;La pause|The break;Les vacances|The holidays;Le projet|The project;La déadline|The deadline','French',_binary '\0','2026-04-02','English',60,'Work','SPEED_TRANSLATION_RACE',NULL),(321,NULL,NULL,NULL,'Useful phrases for traveling abroad.',NULL,'2026-04-03',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Mon passeport a expiré|My passport has expired;Je cherche mon hôtel|I am looking for my hotel;Pouvez-vous m\'aider?|Can you help me?;Je suis perdu|I am lost;Quelle est la devise locale?|What is the local currency?;Y a-t-il une pharmacie proche?|Is there a pharmacy nearby?;Je voudrais prolonger mon séjour|I would like to extend my stay;À quelle heure part le vol?|What time does the flight leave?;Mes bagages sont perdus|My luggage is lost;Je dois changer de l\'argent|I need to exchange money','French',_binary '','2026-03-03','English',55,'Travel & Adventure','SPEED_TRANSLATION_RACE',NULL),(322,NULL,NULL,NULL,'Essential medical vocabulary.',NULL,'2026-04-07',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'J\'ai mal à la tête|I have a headache;Je dois voir un médecin|I need to see a doctor;J\'ai de la fièvre|I have a fever;Je suis allergique à|I am allergic to;Il faut une ordonnance|A prescription is required;Je prends des médicaments|I take medication;Le cabinet médical|The medical practice;Les antécédents médicaux|The medical history;Une consultation|An appointment;Le régime alimentaire|The diet','French',_binary '\0','2026-03-07','English',55,'Health & Medicine','SPEED_TRANSLATION_RACE',NULL),(323,NULL,NULL,NULL,'Modern tech vocabulary at speed.',NULL,'2026-04-11',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Télécharger|To download;Sauvegarder|To save;Mot de passe|Password;Réseau social|Social network;Connexion internet|Internet connection;Application mobile|Mobile application;Mise à jour|Update;Traitement de données|Data processing;Intelligence artificielle|Artificial intelligence;Cybersécurité|Cybersecurity','French',_binary '','2026-03-11','English',55,'Everyday Technology','SPEED_TRANSLATION_RACE',NULL),(324,NULL,NULL,NULL,'Translate environmental vocabulary.',NULL,'2026-04-14',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Le réchauffement climatique|Global warming;Les énergies renouvelables|Renewable energy;La déforestation|Deforestation;La biodiversité|Biodiversity;Le développement durable|Sustainable development;La pollution atmosphérique|Air pollution;Les espèces menacées|Endangered species;Le recyclage|Recycling;L\'empreinte carbone|The carbon footprint;La forêt tropicale|The rainforest','French',_binary '\0','2026-03-14','English',55,'Environment & Nature','SPEED_TRANSLATION_RACE',NULL),(325,NULL,NULL,NULL,'Express your cultural tastes in English.',NULL,'2026-04-18',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'J\'adore regarder des films|I love watching movies;Je joue d\'un instrument|I play an instrument;La galerie d\'art|The art gallery;Le festival de musique|The music festival;Le roman policier|The detective novel;La mise en scène|The staging;Un spectacle vivant|A live performance;La critique cinématographique|The film review;Un chef-d\'œuvre|A masterpiece;La bande originale|The soundtrack','French',_binary '','2026-03-18','English',55,'Culture & Leisure','SPEED_TRANSLATION_RACE',NULL),(326,NULL,NULL,NULL,'Vocabulary for school and university settings.',NULL,'2026-04-21',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Le cursus universitaire|The university curriculum;La dissertation|The essay;L\'examen partiel|The midterm exam;La bourse d\'études|The scholarship;La soutenance|The thesis defense;Le stage professionnel|The internship;Le tutorat|Tutoring;Les résultats académiques|Academic results;La formation continue|Continuing education;L\'apprentissage en ligne|Online learning','French',_binary '\0','2026-03-21','English',55,'Education & Learning','SPEED_TRANSLATION_RACE',NULL),(327,NULL,NULL,NULL,'Interact socially in English with fluency.',NULL,'2026-04-24',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Faire la connaissance de|To get to know;Garder le contact|To stay in touch;Proposer de l\'aide|To offer help;Exprimer son désaccord|To express disagreement;Faire un compliment|To pay a compliment;Inviter quelqu\'un|To invite someone;Remercier chaleureusement|To thank warmly;S\'excuser sincèrement|To apologize sincerely;Partager un avis|To share an opinion;Encourager quelqu\'un|To encourage someone','French',_binary '','2026-03-24','English',55,'Social Interactions','SPEED_TRANSLATION_RACE',NULL),(328,NULL,NULL,NULL,'The basics of economics in English.',NULL,'2026-04-28',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Le budget|The budget;L\'investissement|The investment;La bourse|The stock market;Le taux d\'intérêt|The interest rate;L\'inflation|Inflation;Le marché financier|The financial market;La faillite|Bankruptcy;Le revenu|Income;Les impôts|Taxes;L\'épargne|Savings','French',_binary '\0','2026-03-28','English',55,'Economics & Finance','SPEED_TRANSLATION_RACE',NULL),(329,NULL,NULL,NULL,'Talk about the news in English.',NULL,'2026-05-03',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'Les informations|The news;Le journal télévisé|The TV news;Un article de presse|A press article;Le journaliste|The journalist;La une du journal|The front page;Un reportage|A report;La liberté de la presse|Press freedom;Les réseaux sociaux|Social media;Une interview|An interview;Les faits divers|Miscellaneous news','French',_binary '','2026-04-03','English',55,'Media & Current Events','SPEED_TRANSLATION_RACE',NULL),(330,NULL,NULL,NULL,'Intermediate sports vocabulary.',NULL,'2026-05-04',NULL,NULL,NULL,'B1',NULL,10,10,NULL,NULL,NULL,10,NULL,'L\'entraîneur|The coach;La compétition|The competition;Le championnat|The championship;Le score final|The final score;L\'équipe adverse|The opposing team;Marquer un but|To score a goal;La prolongation|Extra time;Le podium|The podium;La médaille d\'or|The gold medal;Battre un record|To break a record','French',_binary '\0','2026-04-04','English',55,'Sport & Competition','SPEED_TRANSLATION_RACE',NULL),(331,NULL,NULL,NULL,'Express nuanced opinions in English.',NULL,'2026-04-04',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'Cela soulève la question de|This raises the question of;D\'un point de vue objectif|From an objective standpoint;Il convient de nuancer|It is worth qualifying;Cette controverse reflète|This controversy reflects;La société civile revendique|Civil society demands;En dépit des apparences|Despite appearances;L\'enjeu fondamental est|The fundamental issue is;Cela implique nécessairement|This necessarily implies;On ne saurait ignorer|One cannot ignore;Au regard des faits|In light of the facts','French',_binary '','2026-03-04','English',50,'Societal Debates','SPEED_TRANSLATION_RACE',NULL),(332,NULL,NULL,NULL,'Advanced literary and rhetorical vocabulary.',NULL,'2026-04-09',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'La métaphore filée|The extended metaphor;L\'ironie dramatique|Dramatic irony;Le narrateur omniscient|The omniscient narrator;Le dénouement|The resolution;La symbolique|The symbolism;Le personnage éponyme|The eponymous character;La vraisemblance|Verisimilitude;Le registre soutenu|The formal register;L\'incipit|The opening passage;Le point de vue subjectif|The subjective viewpoint','French',_binary '\0','2026-03-09','English',50,'Literature & Rhetoric','SPEED_TRANSLATION_RACE',NULL),(333,NULL,NULL,NULL,'Translate key philosophical concepts.',NULL,'2026-04-16',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'Le libre arbitre|Free will;La responsabilité morale|Moral responsibility;L\'impératif catégorique|The categorical imperative;Le relativisme culturel|Cultural relativism;La conscience collective|Collective consciousness;La déontologie|Deontology;Le bien commun|The common good;Le contrat social|The social contract;L\'utilitarisme|Utilitarianism;La vertu éthique|Ethical virtue','French',_binary '','2026-03-16','English',50,'Ethics & Philosophy','SPEED_TRANSLATION_RACE',NULL),(334,NULL,NULL,NULL,'Advanced scientific vocabulary.',NULL,'2026-04-25',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'L\'hypothèse de travail|The working hypothesis;La méthode expérimentale|The experimental method;Les données empiriques|Empirical data;La peer review|Peer review;Le protocole de recherche|The research protocol;La variable indépendante|The independent variable;L\'échantillon représentatif|The representative sample;La réplication des résultats|The replication of results;L\'état de l\'art|The state of the art;La portée de l\'étude|The scope of the study','French',_binary '\0','2026-03-25','English',50,'Science & Research','SPEED_TRANSLATION_RACE',NULL),(335,NULL,NULL,NULL,'Legal vocabulary at B2 level.',NULL,'2026-05-05',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'La présomption d\'innocence|The presumption of innocence;La jurisprudence|Case law;Le plaignant|The plaintiff;La mise en examen|Being placed under investigation;L\'acquittement|The acquittal;La peine de substitution|The alternative sentence;Les droits fondamentaux|Fundamental rights;Le tribunal correctionnel|The criminal court;La procédure d\'appel|The appeal process;L\'aide juridictionnelle|Legal aid','French',_binary '','2026-04-05','English',50,'Law & Justice','SPEED_TRANSLATION_RACE',NULL),(336,NULL,NULL,NULL,'Diplomatic and geopolitical vocabulary.',NULL,'2026-05-06',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'La souveraineté nationale|National sovereignty;Les négociations bilatérales|Bilateral negotiations;L\'accord de libre-échange|The free trade agreement;La résolution du Conseil de sécurité|The Security Council resolution;Le droit international humanitaire|International humanitarian law;La diplomatie préventive|Preventive diplomacy;La politique étrangère|Foreign policy;L\'ONG|The NGO;Le traité de paix|The peace treaty;La coopération multilatérale|Multilateral cooperation','French',_binary '\0','2026-04-06','English',50,'Diplomacy & International Relations','SPEED_TRANSLATION_RACE',NULL),(337,NULL,NULL,NULL,'Urban planning and design vocabulary.',NULL,'2026-05-07',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'La réhabilitation urbaine|Urban rehabilitation;Le patrimoine architectural|Architectural heritage;La densification|Densification;L\'étalement urbain|Urban sprawl;La mixité sociale|Social diversity;Le quartier durable|The sustainable district;La mobilité douce|Soft mobility;L\'espace public|Public space;Le permis de construire|The building permit;La gentrification|Gentrification','French',_binary '','2026-04-07','English',50,'Urban Planning & Architecture','SPEED_TRANSLATION_RACE',NULL),(338,NULL,NULL,NULL,'Psychology concepts in English.',NULL,'2026-05-08',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'Le biais cognitif|Cognitive bias;La résilience|Resilience;L\'intelligence émotionnelle|Emotional intelligence;Le mécanisme de défense|The defense mechanism;L\'inconscient collectif|The collective unconscious;La pleine conscience|Mindfulness;Le conditionnement classique|Classical conditioning;L\'estime de soi|Self-esteem;La procrastination|Procrastination;L\'empathie|Empathy','French',_binary '\0','2026-04-08','English',50,'Psychology & Behavior','SPEED_TRANSLATION_RACE',NULL),(339,NULL,NULL,NULL,'Modern marketing vocabulary.',NULL,'2026-05-09',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'La stratégie de marque|The brand strategy;Le positionnement|Positioning;La cible démographique|The demographic target;Le retour sur investissement|Return on investment;La veille concurrentielle|Competitive intelligence;L\'image de marque|Brand image;La campagne publicitaire|The advertising campaign;Le bouche-à-oreille|Word of mouth;La fidélisation client|Customer loyalty;L\'entonnoir de conversion|The conversion funnel','French',_binary '','2026-04-09','English',50,'Marketing & Communication','SPEED_TRANSLATION_RACE',NULL),(340,NULL,NULL,NULL,'Historical and civilizational vocabulary.',NULL,'2026-05-10',NULL,NULL,NULL,'B2',NULL,10,10,NULL,NULL,NULL,15,NULL,'La révolution industrielle|The industrial revolution;L\'abolition de l\'esclavage|The abolition of slavery;La décolonisation|Decolonization;Le mouvement des droits civiques|The civil rights movement;L\'ère médiévale|The medieval era;La chute de l\'Empire romain|The fall of the Roman Empire;Le siècle des Lumières|The Age of Enlightenment;La Guerre froide|The Cold War;Le traité de Westphalie|The Treaty of Westphalia;La mondialisation|Globalization','French',_binary '\0','2026-04-10','English',50,'History & Civilization','SPEED_TRANSLATION_RACE',NULL),(341,NULL,NULL,NULL,'Master register nuances in English.',NULL,'2026-04-15',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'Nonobstant les difficultés susmentionnées|Notwithstanding the aforementioned difficulties;Il y aurait lieu de s\'interroger|One might question whether;Force est de constater que|One must acknowledge that;Dans la mesure où cela s\'avère pertinent|Insofar as this proves relevant;Cette problématique sous-tend|This issue underpins;À l\'aune de ces résultats|In light of these results;Il serait malvenu de|It would be inappropriate to;Au demeurant|Besides, moreover;En l\'occurrence|As it happens;Dans la perspective où|In the event that','French',_binary '','2026-03-01','English',45,'Complex Language Registers','SPEED_TRANSLATION_RACE',NULL),(342,NULL,NULL,NULL,'High-level political economy concepts.',NULL,'2026-04-22',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'L\'asymétrie d\'information|Information asymmetry;La défaillance de marché|Market failure;L\'aléa moral|Moral hazard;La politique monétaire non conventionnelle|Unconventional monetary policy;La sélection adverse|Adverse selection;L\'externalité négative|Negative externality;Le jeu à somme nulle|Zero-sum game;La théorie des jeux|Game theory;La rente de situation|Rent-seeking;L\'équilibre de Nash|Nash equilibrium','French',_binary '\0','2026-03-08','English',45,'Advanced Political Economy','SPEED_TRANSLATION_RACE',NULL),(343,NULL,NULL,NULL,'Applied linguistics vocabulary.',NULL,'2026-04-29',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'La pragmatique|Pragmatics;L\'acte de langage|The speech act;La polysémie|Polysemy;Le signifiant et le signifié|The signifier and the signified;La sociolinguistique|Sociolinguistics;L\'acquisition du langage|Language acquisition;La deixis|Deixis;La connotation|Connotation;La morphologie|Morphology;L\'intonation|Intonation','French',_binary '','2026-03-15','English',45,'Linguistics & Semiotics','SPEED_TRANSLATION_RACE',NULL),(344,NULL,NULL,NULL,'Cutting-edge medical and ethical vocabulary.',NULL,'2026-05-06',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'Le consentement éclairé|Informed consent;L\'acharnement thérapeutique|Therapeutic obstinacy;La médecine prédictive|Predictive medicine;Le clonage thérapeutique|Therapeutic cloning;La pharmacovigilance|Pharmacovigilance;Les soins palliatifs|Palliative care;La thérapie génique|Gene therapy;L\'euthanasie active|Active euthanasia;Le diagnostic prénatal|Prenatal diagnosis;La médecine personnalisée|Personalized medicine','French',_binary '\0','2026-03-22','English',45,'Bioethics & Advanced Medicine','SPEED_TRANSLATION_RACE',NULL),(345,NULL,NULL,NULL,'Advanced international legal terminology.',NULL,'2026-05-13',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'Le jus cogens|Jus cogens;La compétence universelle|Universal jurisdiction;L\'immunité diplomatique|Diplomatic immunity;La responsabilité de protéger|The responsibility to protect;L\'arbitrage international|International arbitration;Le droit coutumier|Customary law;La clause de la nation la plus favorisée|Most favoured nation clause;L\'extradition|Extradition;La réserve de souveraineté|Sovereignty reservation;La Cour pénale internationale|The International Criminal Court','French',_binary '','2026-03-29','English',45,'Advanced International Law','SPEED_TRANSLATION_RACE',NULL),(346,NULL,NULL,NULL,'Advanced concepts in the social sciences.',NULL,'2026-05-20',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'La stratification sociale|Social stratification;L\'habitus bourdieusien|Bourdieusian habitus;Le capital symbolique|Symbolic capital;La reproduction sociale|Social reproduction;L\'identité culturelle|Cultural identity;Le phénomène d\'acculturation|The acculturation phenomenon;La mobilité sociale ascendante|Upward social mobility;L\'interactionnisme symbolique|Symbolic interactionism;La norme sociale|The social norm;L\'anomie|Anomie','French',_binary '\0','2026-04-05','English',45,'Sociology & Anthropology','SPEED_TRANSLATION_RACE',NULL),(347,NULL,NULL,NULL,'AI vocabulary and digital ethics challenges.',NULL,'2026-05-27',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'L\'apprentissage automatique|Machine learning;Le réseau de neurones|Neural network;L\'algorithme de recommandation|The recommendation algorithm;La boucle de rétroaction|The feedback loop;La vie privée numérique|Digital privacy;Le traitement du langage naturel|Natural language processing;Le biais algorithmique|Algorithmic bias;La gouvernance des données|Data governance;L\'IA générative|Generative AI;L\'explicabilité|Explainability','French',_binary '','2026-04-12','English',45,'Artificial Intelligence & Digital Ethics','SPEED_TRANSLATION_RACE',NULL),(348,NULL,NULL,NULL,'Analytical philosophy of language concepts.',NULL,'2026-06-03',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'La référence opaque|Opaque reference;Le critère de réfutabilité|The falsifiability criterion;La description définie|The definite description;L\'identité des indiscernables|The identity of indiscernables;La supervenance|Supervenience;Le dualisme propriétés|Property dualism;L\'intentionnalité|Intentionality;Le réalisme scientifique|Scientific realism;La proposition analytique|The analytic proposition;La vérité correspondance|Correspondence truth','French',_binary '\0','2026-04-19','English',45,'Philosophy of Language','SPEED_TRANSLATION_RACE',NULL),(349,NULL,NULL,NULL,'High-level geopolitical vocabulary.',NULL,'2026-06-10',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'La realpolitik|Realpolitik;La théorie de la puissance douce|Soft power theory;L\'endiguement|Containment;La zone d\'influence|The sphere of influence;La gouvernance mondiale|Global governance;L\'État défaillant|The failed state;La transition hégémonique|Hegemonic transition;Le nationalisme économique|Economic nationalism;Le multilatéralisme|Multilateralism;La fragmentation géopolitique|Geopolitical fragmentation','French',_binary '','2026-04-26','English',45,'Contemporary Geopolitics','SPEED_TRANSLATION_RACE',NULL),(350,NULL,NULL,NULL,'Master the art of argumentation in English.',NULL,'2026-06-12',NULL,NULL,NULL,'C1',NULL,10,10,NULL,NULL,NULL,20,NULL,'L\'argument a fortiori|The a fortiori argument;La réfutation par l\'absurde|Refutation by absurdity;Le sophisme de la pente glissante|The slippery slope fallacy;L\'appel à l\'autorité|The appeal to authority;L\'argument ad hominem|The ad hominem argument;La pétition de principe|Begging the question;L\'inférence abductive|Abductive inference;La fausse dichotomie|The false dichotomy;L\'argument de l\'homme de paille|The straw man argument;Le raisonnement inductif|Inductive reasoning','French',_binary '\0','2026-04-28','English',45,'Rhetoric & Advanced Argumentation','SPEED_TRANSLATION_RACE',NULL),(351,NULL,NULL,NULL,'Rare idiomatic expressions and formal registers.',NULL,'2026-04-15',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'Il ne faut pas vendre la peau de l\'ours avant de l\'avoir tué|Don\'t count your chickens before they hatch;Avoir le beurre et l\'argent du beurre|To have one\'s cake and eat it too;Chercher midi à quatorze heures|To make a mountain out of a molehill;Noyer le poisson|To muddy the waters;La fine fleur|The crème de la crème;Prêcher dans le désert|To cry in the wilderness;Donner du grain à moudre|To give food for thought;Faire long feu|To fizzle out;Mettre les points sur les i|To dot the i\'s and cross the t\'s;Tourner autour du pot|To beat around the bush','French',_binary '','2026-03-01','English',40,'Rare Idiomatic Expressions','SPEED_TRANSLATION_RACE',NULL),(352,NULL,NULL,NULL,'Expert-level legal vocabulary.',NULL,'2026-04-19',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'Le mandamus|Writ of mandamus;La subrogation|Subrogation;L\'estoppel|Estoppel;Le nemo judex in causa sua|Nemo judex in causa sua;Le habeas corpus|Habeas corpus;La novation|Novation;L\'actio in rem|Action in rem;Le numerus clausus|Numerus clausus;La pacta sunt servanda|Pacta sunt servanda;L\'ex aequo et bono|Ex aequo et bono','French',_binary '\0','2026-03-05','English',40,'Expert Legal Terminology','SPEED_TRANSLATION_RACE',NULL),(353,NULL,NULL,NULL,'Expert-level comparative literature concepts.',NULL,'2026-04-26',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'L\'intertextualité|Intertextuality;La métatextualité|Metatextuality;L\'hypotexte|The hypotext;La polyphonie romanesque|Novelistic polyphony;Le carnavalesque bakhtinien|Bakhtinian carnivalesque;La transtextualité|Transtextuality;Le palimpseste|The palimpsest;L\'architexte|The architext;La déconstruction derridéenne|Derridean deconstruction;L\'effet de réel|The reality effect','French',_binary '','2026-03-12','English',40,'Advanced Comparative Literature','SPEED_TRANSLATION_RACE',NULL),(354,NULL,NULL,NULL,'Cutting-edge neuroscience terminology.',NULL,'2026-05-03',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'La plasticité synaptique|Synaptic plasticity;L\'inhibition latérale|Lateral inhibition;La potentialisation à long terme|Long-term potentiation;La connectomique|Connectomics;La neuromodulation|Neuromodulation;Le cortex préfrontal dorsolatéral|Dorsolateral prefrontal cortex;L\'oscillation gamma|Gamma oscillation;La neurogenèse hippocampique|Hippocampal neurogenesis;Le réseau du mode par défaut|The default mode network;La mémoire de travail|Working memory','French',_binary '\0','2026-03-19','English',40,'Neuroscience & Cognition','SPEED_TRANSLATION_RACE',NULL),(355,NULL,NULL,NULL,'High-level statistical and econometric vocabulary.',NULL,'2026-05-10',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'L\'hétéroscédasticité|Heteroscedasticity;La multicolinéarité|Multicollinearity;L\'autocorrélation des résidus|Residual autocorrelation;La stationnarité|Stationarity;Le test de racine unitaire|Unit root test;La cointégration|Cointegration;La régression par les moindres carrés|Ordinary least squares regression;L\'endogénéité|Endogeneity;La variable instrumentale|Instrumental variable;L\'inférence bayésienne|Bayesian inference','French',_binary '','2026-03-26','English',40,'Econometrics & Advanced Statistics','SPEED_TRANSLATION_RACE',NULL),(356,NULL,NULL,NULL,'The most abstract philosophical concepts.',NULL,'2026-05-17',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'L\'épochè husserlienne|Husserlian epoché;L\'être-dans-le-monde|Being-in-the-world;La temporalité heideggérienne|Heideggerian temporality;L\'intersubjectivité|Intersubjectivity;L\'intentionnalité de la conscience|The intentionality of consciousness;La réduction phénoménologique|Phenomenological reduction;L\'être-pour-autrui sartrean|Sartrean being-for-others;La mauvaise foi|Bad faith;L\'aliénation hégélienne|Hegelian alienation;La dialectique maître-esclave|The master-slave dialectic','French',_binary '\0','2026-04-02','English',40,'Phenomenology & Metaphysics','SPEED_TRANSLATION_RACE',NULL),(357,NULL,NULL,NULL,'Genomics and biotechnology terminology.',NULL,'2026-05-24',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'L\'édition du génome|Genome editing;Le séquençage de nouvelle génération|Next-generation sequencing;L\'épigénétique|Epigenetics;La transcriptomique|Transcriptomics;Le polymorphisme de nucléotide simple|Single nucleotide polymorphism;La thérapie CRISPR-Cas9|CRISPR-Cas9 therapy;L\'expression génique|Gene expression;La protéomique|Proteomics;L\'ARN messager|Messenger RNA;La bioinformatique|Bioinformatics','French',_binary '','2026-04-09','English',40,'Biotechnology & Genomics','SPEED_TRANSLATION_RACE',NULL),(358,NULL,NULL,NULL,'Mastery of expert diplomatic vocabulary.',NULL,'2026-05-31',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'Le droit de veto|The veto right;La doctrine Monroe|The Monroe Doctrine;La diplomatie de la canonnière|Gunboat diplomacy;Le concert des nations|The concert of nations;La clause de sauvegarde|The safeguard clause;La médiation internationale|International mediation;Le principe de non-ingérence|The principle of non-interference;L\'équilibre des puissances|The balance of power;Le bilatéralisme|Bilateralism;La raison d\'État|Raison d\'état','French',_binary '\0','2026-04-16','English',40,'Expert Diplomacy & Negotiation','SPEED_TRANSLATION_RACE',NULL),(359,NULL,NULL,NULL,'Advanced discourse analysis and rhetoric.',NULL,'2026-06-07',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'La présupposition pragmatique|Pragmatic presupposition;L\'implicature conversationnelle|Conversational implicature;L\'acte illocutoire|The illocutionary act;La modalisation|Modalization;La doxa|The doxa;L\'ethos rhétorique|The rhetorical ethos;Le pathos|The pathos;Le logos|The logos;La rhétorique délibérative|Deliberative rhetoric;L\'anaphore discursive|Discursive anaphora','French',_binary '','2026-04-23','English',40,'Expert Discourse Analysis','SPEED_TRANSLATION_RACE',NULL),(360,NULL,NULL,NULL,'Advanced theoretical physics vocabulary.',NULL,'2026-06-14',NULL,NULL,NULL,'C2',NULL,10,10,NULL,NULL,NULL,25,NULL,'L\'entropie|Entropy;La mécanique quantique|Quantum mechanics;Le principe d\'incertitude|The uncertainty principle;La relativité générale|General relativity;La superposition quantique|Quantum superposition;L\'intrication quantique|Quantum entanglement;Le trou noir|The black hole;La constante de Planck|Planck\'s constant;La dualité onde-corpuscule|Wave-particle duality;La singularité gravitationnelle|Gravitational singularity','French',_binary '\0','2026-04-29','English',40,'Thermodynamics & Advanced Physics','SPEED_TRANSLATION_RACE',NULL);
/*!40000 ALTER TABLE `challenge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `challenge_attempt`
--

DROP TABLE IF EXISTS `challenge_attempt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `challenge_attempt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attempts_used` int DEFAULT NULL,
  `deadline_time` datetime(6) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `id_user` bigint DEFAULT NULL,
  `progress` int DEFAULT NULL,
  `score` int DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `status` enum('PENDING','IN_PROGRESS','COMPLETED','EXPIRED') DEFAULT NULL,
  `challenge_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqm2ow2owd6oos18n63aq086mr` (`challenge_id`),
  CONSTRAINT `FKqm2ow2owd6oos18n63aq086mr` FOREIGN KEY (`challenge_id`) REFERENCES `challenge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenge_attempt`
--

LOCK TABLES `challenge_attempt` WRITE;
/*!40000 ALTER TABLE `challenge_attempt` DISABLE KEYS */;
/*!40000 ALTER TABLE `challenge_attempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class` (
  `class_id` bigint NOT NULL AUTO_INCREMENT,
  `level` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number_students` int NOT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` VALUES (1,'A1','Beginners Morning A1-01',18),(2,'A1','Beginners Afternoon A1-02',15),(3,'A1','Beginners Evening A1-03',12),(4,'A2','Elementary A2-01',20),(5,'A2','Elementary A2-02',22),(6,'A2','Elementary Weekend A2-03',16),(7,'B1','Intermediate B1-01',25),(8,'B1','Intermediate B1-02',20),(9,'B1','Intermediate Intensive B1-03',18),(10,'B2','Upper-Intermediate B2-01',22),(11,'B2','Upper-Intermediate B2-02',19),(12,'B2','IELTS Preparation B2-03',15),(13,'C1','Advanced C1-01',16),(14,'C1','Advanced C1-02',14),(15,'C1','Business English C1-03',12),(16,'C2','Mastery C2-01',10),(17,'C2','Mastery C2-02',8),(18,'A1','Kids English KA1-01',12),(19,'A2','Teens English TA2-01',18),(20,'B2','Corporate Group BIZ-01',10);
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interview`
--

DROP TABLE IF EXISTS `interview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `duration_minutes` int DEFAULT NULL,
  `meeting_link` varchar(255) DEFAULT NULL,
  `meeting_status` enum('ENLIGNE','PRESENTIEL') DEFAULT NULL,
  `start_date_time` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `recruitment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcpwhy2fxr0ac2mgirpyba8p1y` (`recruitment_id`),
  CONSTRAINT `FKcpwhy2fxr0ac2mgirpyba8p1y` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview`
--

LOCK TABLES `interview` WRITE;
/*!40000 ALTER TABLE `interview` DISABLE KEYS */;
INSERT INTO `interview` VALUES (1,180,'https://meet.google.com/agy-mejn-vyv','ENLIGNE','2026-05-14 20:30:00.000000','Interview for Senior ESL Content Developer',8,5),(2,30,'https://meet.google.com/agy-mejn-vyv','ENLIGNE','2026-05-14 22:51:00.000000','Interview for Senior ESL Content Developer',9,5);
/*!40000 ALTER TABLE `interview` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `message` text,
  `read_at` datetime(6) DEFAULT NULL,
  `recipient_email` varchar(255) DEFAULT NULL,
  `related_entity_id` bigint DEFAULT NULL,
  `related_entity_type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` enum('INFO','SUCCESS','WARNING','ERROR','APPLICANT_CREATED','APPLICANT_UPDATED','APPLICANT_STATUS_CHANGED','INTERVIEW_SCHEDULED','INTERVIEW_UPDATED','INTERVIEW_CANCELLED','INTERVIEW_REMINDER','RECRUITMENT_CREATED','RECRUITMENT_CLOSED','RECRUITMENT_STATUS_CHANGED','GENERAL') NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'2026-05-12 19:10:13.668407',_binary '\0','Good news! Your CV for \'Senior ESL Content Developer\' was analyzed as a \'Senior\' profile with a score of 100/100.',NULL,'shayma@esprit.tn',3,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(2,'2026-05-12 19:29:49.882472',_binary '\0','A new interview has been scheduled: Interview for Senior ESL Content Developer',NULL,'shayma@esprit.tn',1,'Interview','NEW','Interview Scheduled','INTERVIEW_SCHEDULED',8),(3,'2026-05-12 19:31:22.929567',_binary '\0','Good news! Your CV for \'Online Business English Instructor\' was analyzed as a \'Senior\' profile with a score of 100/100.',NULL,'shayma@esprit.tn',4,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(4,'2026-05-12 19:34:24.447601',_binary '\0','Good news! Your CV for \'Online Business English Instructor\' was analyzed as a \'Senior\' profile with a score of 95/100.',NULL,'shayma@esprit.tn',4,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(5,'2026-05-12 19:34:28.807053',_binary '\0','Good news! Your CV for \'Online Business English Instructor\' was analyzed as a \'Senior\' profile with a score of 95/100.',NULL,'shayma@esprit.tn',4,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(6,'2026-05-12 19:37:01.057162',_binary '\0','Good news! Your CV for \'Online Business English Instructor\' was analyzed as a \'Senior\' profile with a score of 95/100.',NULL,'shayma@esprit.tn',4,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(7,'2026-05-12 19:37:32.762678',_binary '\0','Good news! Your CV for \'Online Business English Instructor\' was analyzed as a \'Senior\' profile with a score of 95/100.',NULL,'shayma@esprit.tn',4,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(8,'2026-05-12 19:42:15.372051',_binary '\0','We regret to inform you that your profile for \'E-Learning Gamification Specialist\' does not match our current requirements (AI Score: 12/100).',NULL,'shayma@esprit.tn',5,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',8),(9,'2026-05-12 19:51:45.008345',_binary '\0','Good news! Your CV for \'Senior ESL Content Developer\' was analyzed as a \'Senior\' profile with a score of 95/100.',NULL,'zayneb@esprit.tn',6,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',9),(10,'2026-05-12 19:52:14.182691',_binary '\0','A new interview has been scheduled: Interview for Senior ESL Content Developer',NULL,'zayneb@esprit.tn',2,'Interview','NEW','Interview Scheduled','INTERVIEW_SCHEDULED',9),(11,'2026-05-12 19:53:16.215035',_binary '\0','We regret to inform you that your profile for \'Online Business English Instructor\' does not match our current requirements (AI Score: 8/100).',NULL,'zayneb@esprit.tn',7,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',9),(12,'2026-05-12 20:34:41.481460',_binary '\0','Your CV for \'Senior ESL Content Developer\' is being reviewed by our team. AI Cluster: error.',NULL,'saif@esprit.tn',8,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10),(13,'2026-05-12 20:35:08.924453',_binary '\0','Your CV for \'Senior ESL Content Developer\' is being reviewed by our team. AI Cluster: error.',NULL,'saif@esprit.tn',8,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10),(14,'2026-05-12 20:37:55.347768',_binary '\0','Your CV for \'Senior ESL Content Developer\' is being reviewed by our team. AI Cluster: Mid-level.',NULL,'saif@esprit.tn',8,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10),(15,'2026-05-12 20:39:10.747790',_binary '\0','We regret to inform you that your profile for \'Online Business English Instructor\' does not match our current requirements (AI Score: 20/100).',NULL,'saif@esprit.tn',9,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10),(16,'2026-05-12 20:44:35.632205',_binary '\0','Good news! Your CV for \'Digital Marketing Specialist\' was analyzed as a \'Junior\' profile with a score of 98/100.',NULL,'saif@esprit.tn',10,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10),(17,'2026-05-12 20:56:11.404528',_binary '\0','Good news! Your CV for \'Digital Marketing Specialist\' was analyzed as a \'Junior\' profile with a score of 98/100.',NULL,'saif@esprit.tn',10,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10),(18,'2026-05-12 20:56:43.192189',_binary '\0','Good news! Your CV for \'Senior English Language Evaluator\' was analyzed as a \'Junior\' profile with a score of 90/100.',NULL,'saif@esprit.tn',11,'Applicant','NEW','Recruitment AI Analysis','APPLICANT_STATUS_CHANGED',10);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruitment`
--

DROP TABLE IF EXISTS `recruitment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruitment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `department` varchar(255) DEFAULT NULL,
  `opened_at` datetime(6) DEFAULT NULL,
  `position_title` varchar(255) DEFAULT NULL,
  `status` enum('OPEN','CLOSED') DEFAULT NULL,
  `description` text,
  `requirements` text,
  `responsibilities` text,
  `contract_type` enum('CDI','CDD','FREELANCE','STAGE') DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `required_skills` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruitment`
--

LOCK TABLES `recruitment` WRITE;
/*!40000 ALTER TABLE `recruitment` DISABLE KEYS */;
INSERT INTO `recruitment` VALUES (5,'Academic / E-Learning','2026-05-12 01:00:00.000000','Senior ESL Content Developer','OPEN','We are looking for a creative English expert to lead our new \'Jungle In English\' platform content. You will be responsible for designing interactive lessons, scripting educational videos, and ensuring pedagogical quality for our learners.',NULL,NULL,'CDI',4,'Remote / Tunis','English Teaching (TEFL/CELTA), Curriculum Design, Content Creation, LMS (Moodle/Canvas), Video Scripting'),(6,'Corporate Training','2026-05-06 01:00:00.000000','Online Business English Instructor','OPEN','Vous êtes passionné par l\'enseignement de l\'anglais dans un contexte professionnel ? Nous recherchons des instructeurs dynamiques pour animer des sessions de coaching individuelles et collectives sur notre plateforme. Vous aiderez nos apprenants (cadres et entrepreneurs) à préparer des présentations, à mener des réunions en anglais et à perfectionner leur vocabulaire technique. Flexibilité totale des horaires via notre module de planification intégré.',NULL,NULL,'FREELANCE',2,'Full Remote (Télétravail)','Business Communication One-on-One Coaching Presentation Skills CELTA/DELTA Certification Soft Skills Training'),(7,'Innovation & Tech','2026-05-12 01:00:00.000000','E-Learning Gamification Specialist','CLOSED','Vous croyez que l\'on apprend mieux en s\'amusant ? Rejoignez-nous pour transformer nos cours d\'anglais en véritables aventures interactives. Votre rôle sera de concevoir des systèmes de récompenses (badges, classements, défis), de créer des mini-jeux pédagogiques et d\'analyser les données d\'engagement pour améliorer continuellement la motivation de nos apprenants. Vous travaillerez à l\'intersection de la pédagogie et du game design.',NULL,NULL,'CDI',3,'Tunis / Remote','Instructional Design Game Mechanics Adobe Creative Suite Javascript Engagement Analytics'),(8,'Quality Assurance & Exams','2026-04-29 01:00:00.000000','Senior English Language Evaluator','OPEN','Garant de la qualité pédagogique, vous superviserez le processus d\'évaluation de nos apprenants. Votre mission inclut la validation des examens de fin de niveau (A1 à C2 selon le cadre CECRL), la conduite d\'évaluations orales complexes et la formation de nos tuteurs sur les critères de correction. Vous veillerez à ce que chaque certification délivrée par notre plateforme réponde aux standards internationaux les plus rigoureux.',NULL,NULL,'CDI',2,'Tunis (Hybrid)','CEFR Standards Oral Assessment Test Validation Writing Feedback Educational Quality Control'),(9,'Academic','2026-05-12 20:42:41.000000','Senior English Teacher','OPEN','Expert teacher for advanced students preparing for international exams.',NULL,NULL,'CDI',5,'Tunis / Remote','Teaching, TOEFL, IELTS, Curriculum Design'),(10,'Academic','2026-05-12 20:42:41.000000','Junior English Tutor','OPEN','Entry-level position for helping beginner students with basic grammar.',NULL,NULL,'CDD',1,'Sousse / Hybrid','English, Communication, Patient, Online Teaching'),(11,'Content','2026-05-12 20:42:41.000000','E-Learning Content Creator','OPEN','Create engaging video lessons and interactive content for the platform.',NULL,NULL,'FREELANCE',3,'Remote','Copywriting, Video Editing, English Literature'),(12,'Tech','2026-05-12 20:42:41.000000','Fullstack Developer (Angular/Java)','OPEN','Join the core team to build new features for the student dashboard.',NULL,NULL,'CDI',4,'Tunis','Angular, Java, Spring Boot, MySQL'),(13,'Marketing','2026-05-12 20:42:41.000000','Digital Marketing Specialist','OPEN','Drive traffic to our platform and manage social media campaigns.',NULL,NULL,'CDI',3,'Tunis / Remote','SEO, Meta Ads, English Copywriting, Analytics'),(14,'Support','2026-05-12 20:42:41.000000','Customer Success Manager','OPEN','Help students navigate the platform and ensure their learning success.',NULL,NULL,'CDI',2,'Remote','Customer Service, Problem Solving, Bilingual'),(15,'Tech','2026-05-12 20:42:41.000000','Mobile App Developer (Flutter)','OPEN','Develop and maintain the mobile version of our English learning app.',NULL,NULL,'CDI',3,'Hybrid','Flutter, Dart, Firebase, API Integration'),(16,'Sales','2026-05-12 20:42:41.000000','Business Development Representative','OPEN','Identify and reach out to companies interested in English training for employees.',NULL,NULL,'CDD',2,'Tunis','Sales, Negotiation, B2B, English Proficiency'),(17,'Academic','2026-05-12 20:42:41.000000','Pedagogical Coordinator','OPEN','Supervise our teaching staff and ensure high standards of education.',NULL,NULL,'CDI',6,'Tunis','Team Management, Pedagogy, Quality Control'),(19,'Data','2026-05-12 20:42:41.000000','Data Analyst (Student Behavior)','OPEN','Analyze student dropout rates and engagement to improve retention.',NULL,NULL,'CDI',3,'Remote','Python, SQL, Tableau, Statistics'),(20,'Tech','2026-05-12 20:42:41.000000','QA Manual Tester','OPEN','Test new features before release to ensure a bug-free experience.',NULL,NULL,'STAGE',1,'Tunis','Manual Testing, Bug Reporting, English Documentation'),(21,'Content','2026-05-12 20:42:41.000000','French to English Translator','OPEN','Translate administrative and pedagogical documents accurately.',NULL,NULL,'FREELANCE',4,'Remote','Translation, Linguistics, Contextual Analysis'),(22,'HR','2026-05-12 20:42:41.000000','HR Recruitment Specialist','OPEN','Manage the hiring process for our growing team of teachers.',NULL,NULL,'CDI',3,'Tunis / Hybrid','Recruitment, Interviewing, Talent Sourcing');
/*!40000 ALTER TABLE `recruitment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `is_available` bit(1) NOT NULL,
  `level` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,25,_binary '',1,'A101 - English Lab'),(2,20,_binary '',1,'A102 - Listening Room'),(3,15,_binary '',1,'A103 - Speaking Studio'),(4,30,_binary '\0',1,'A104 - Grammar Workshop'),(5,20,_binary '',2,'B201 - Writing Center'),(6,25,_binary '',2,'B202 - Vocabulary Lab'),(7,35,_binary '',2,'B203 - Conversation Hall'),(8,20,_binary '\0',2,'B204 - IELTS Prep Room'),(9,18,_binary '',3,'C301 - Business English Suite'),(10,30,_binary '',3,'C302 - Presentation Room'),(11,22,_binary '',3,'C303 - TOEFL Training Center'),(12,15,_binary '',3,'C304 - Reading Lounge'),(13,12,_binary '',4,'D401 - Executive Boardroom'),(14,10,_binary '\0',4,'D402 - Group Study Room'),(15,28,_binary '',4,'D403 - Multimedia Lab'),(16,20,_binary '',1,'E101 - Kids English Zone'),(17,15,_binary '',1,'E102 - Phonics Room'),(18,50,_binary '',5,'F501 - Conference Hall'),(19,40,_binary '',5,'F502 - Exam Hall'),(20,30,_binary '\0',5,'F503 - Virtual Classroom');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_complaint`
--

DROP TABLE IF EXISTS `room_complaint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_complaint` (
  `complaint_id` bigint NOT NULL AUTO_INCREMENT,
  `answer` varchar(255) DEFAULT NULL,
  `complainant_email` varchar(255) DEFAULT NULL,
  `complainant_role` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  `schedule_id` bigint DEFAULT NULL,
  PRIMARY KEY (`complaint_id`),
  KEY `FKqt2gue6f1nhiksgmeyxcs7p3j` (`room_id`),
  KEY `FKrukjon3y03wc8l6ubaliq0l8s` (`schedule_id`),
  CONSTRAINT `FKqt2gue6f1nhiksgmeyxcs7p3j` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
  CONSTRAINT `FKrukjon3y03wc8l6ubaliq0l8s` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_complaint`
--

LOCK TABLES `room_complaint` WRITE;
/*!40000 ALTER TABLE `room_complaint` DISABLE KEYS */;
INSERT INTO `room_complaint` VALUES (1,NULL,'student3@esprit.tn','STUDENT','The projector in room A101 English Lab has been malfunctioning since Monday. It flickers constantly and shuts down during presentations. Students cannot see the slides properly.',_binary '\0','Projector not working in A101',1,1),(2,NULL,'student5@esprit.tn','STUDENT','Room B203 Conversation Hall has no working air conditioning. The room becomes extremely hot during afternoon sessions making it difficult to concentrate.',_binary '\0','No air conditioning in B203',7,5),(3,NULL,'tutor6@esprit.tn','TUTOR','Several chairs in room C302 Presentation Room are broken and unsafe. At least 5 chairs have wobbly legs and one collapsed during class today.',_binary '\0','Broken chairs in C302',10,6),(4,NULL,'student7@esprit.tn','STUDENT','The WiFi in Multimedia Lab D403 keeps disconnecting during online exercises. Students cannot access e-learning resources or complete their listening tasks.',_binary '\0','WiFi connectivity issues in D403',15,15),(5,NULL,'tutor8@esprit.tn','TUTOR','Room B201 Writing Center has no whiteboard markers available. The tutor had to borrow from another room causing a 15-minute delay in the lesson.',_binary '\0','Whiteboard markers missing in B201',5,13),(6,NULL,'student2@esprit.tn','STUDENT','There is loud construction noise outside room A102 during morning sessions. It is impossible to conduct listening exercises with this level of disturbance.',_binary '\0','Noise from construction nearby',2,2),(7,NULL,'tutor9@esprit.tn','TUTOR','The door lock of Kids English Zone E101 is not working properly. The door cannot be locked from inside which is a safety concern for children classes.',_binary '\0','Door lock malfunction in E101',16,10),(8,'The maintenance team has inspected and repaired the heating unit. A new quieter system will be installed next week. Thank you for reporting this issue.','student6@esprit.tn','STUDENT','The heating system in Business English Suite C301 makes a very loud buzzing noise. It disrupts conversations during role-play exercises.',_binary '','Heating system too loud in C301',9,8),(9,'Additional LED panels have been installed in the back section of the hall. Please confirm if the lighting is now adequate during your next session.','tutor3@esprit.tn','TUTOR','Conference Hall F501 has poor lighting in the back rows. Students sitting at the back cannot read the board or their handouts clearly.',_binary '','Insufficient lighting in F501',18,19),(10,'The IT department has replaced the faulty monitors. All 28 workstations are now fully operational. We apologize for the inconvenience.','student8@esprit.tn','STUDENT','Three computers in Multimedia Lab D403 have black screens. They do not turn on at all which reduces available workstations for TOEFL practice.',_binary '','Computer screens not working',15,17),(11,'We have corrected the scheduling conflict and added a validation rule to prevent future double bookings. The affected session has been rescheduled to room B203.','tutor7@esprit.tn','TUTOR','Room C302 was double-booked on May 14th. Two different classes arrived at 2pm causing confusion and delay for both groups.',_binary '','Room double-booked on May 14',10,6),(12,'New Bose speakers have been installed in room A102. The audio quality should now be clear at all volume levels. Please report if any further issues occur.','tutor2@esprit.tn','TUTOR','The speakers in Listening Room A102 produce distorted sound at normal volume. This makes it very difficult for A1 students to understand the audio exercises.',_binary '','Audio equipment quality poor',2,2),(13,'The thermostat has been adjusted to maintain 22 degrees Celsius. We will monitor the temperature during future exam sessions to ensure comfort.','student9@esprit.tn','STUDENT','During the IELTS mock exam on May 16, the Exam Hall F502 was extremely cold. Several students complained they could not focus properly.',_binary '','Exam hall too cold during IELTS',19,9),(14,'We have spoken with the cleaning staff and added an extra cleaning slot between afternoon and evening sessions. We apologize for this oversight.','student5@esprit.tn','STUDENT','Room A103 Speaking Studio was not cleaned before our evening session. There were food wrappers and coffee cups from the previous class.',_binary '','Dirty classroom complaint',3,3),(15,'A power strip extension with 10 additional outlets has been installed along the side wall. We are also planning a full electrical upgrade for this room during summer.','tutor3@esprit.tn','TUTOR','Room B201 Writing Center does not have enough power outlets for all students to charge their laptops during the 3-hour writing workshop.',_binary '','Request for additional power outlets',5,13);
/*!40000 ALTER TABLE `room_complaint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` enum('COURSE','EXAM','MEETING','OTHER') DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `class_id` bigint DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `FKqqv2rqy5xxw2oyhie35seyclw` (`class_id`),
  KEY `FKh2hdhbss2x31ns719hka6enma` (`room_id`),
  CONSTRAINT `FKh2hdhbss2x31ns719hka6enma` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
  CONSTRAINT `FKqqv2rqy5xxw2oyhie35seyclw` FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,1,'2026-05-12 12:00:00.000000','2026-05-12 09:00:00.000000','A1 Grammar Basics','COURSE',2,1,1),(2,1,'2026-05-12 17:00:00.000000','2026-05-12 14:00:00.000000','A1 Listening Practice','COURSE',3,2,2),(3,2,'2026-05-13 12:00:00.000000','2026-05-13 09:00:00.000000','A2 Past Tense Workshop','COURSE',5,4,3),(4,3,'2026-05-13 17:00:00.000000','2026-05-13 14:00:00.000000','B1 Conditional Sentences','COURSE',6,7,5),(5,3,'2026-05-14 12:00:00.000000','2026-05-14 09:00:00.000000','B1 Debate & Opinions','COURSE',7,8,7),(6,4,'2026-05-14 17:00:00.000000','2026-05-14 14:00:00.000000','B2 Phrasal Verbs Intensive','COURSE',8,10,9),(7,5,'2026-05-15 12:00:00.000000','2026-05-15 09:00:00.000000','C1 Vocabulary & Register','COURSE',9,13,11),(8,7,'2026-05-15 17:00:00.000000','2026-05-15 14:00:00.000000','Business Email Writing','COURSE',2,15,9),(9,10,'2026-05-16 12:00:00.000000','2026-05-16 09:00:00.000000','IELTS Mock Reading Test','EXAM',3,12,19),(10,15,'2026-05-16 17:00:00.000000','2026-05-16 14:00:00.000000','Kids English Fun Session','COURSE',5,18,16),(11,1,'2026-05-19 12:00:00.000000','2026-05-19 09:00:00.000000','A1 Numbers & Colors','COURSE',6,3,1),(12,2,'2026-05-19 17:00:00.000000','2026-05-19 14:00:00.000000','A2 Shopping Dialogues','COURSE',7,5,3),(13,3,'2026-05-20 12:00:00.000000','2026-05-20 09:00:00.000000','B1 Present Perfect Review','COURSE',8,9,5),(14,4,'2026-05-20 17:00:00.000000','2026-05-20 14:00:00.000000','B2 Essay Writing Workshop','COURSE',9,11,10),(15,5,'2026-05-21 12:00:00.000000','2026-05-21 09:00:00.000000','C1 Advanced Listening','COURSE',2,14,11),(16,8,'2026-05-21 17:00:00.000000','2026-05-21 14:00:00.000000','Negotiation Role-Play','COURSE',3,20,9),(17,11,'2026-05-22 12:00:00.000000','2026-05-22 09:00:00.000000','TOEFL Speaking Practice','EXAM',5,12,19),(18,13,'2026-05-22 17:00:00.000000','2026-05-22 14:00:00.000000','Travel English: Airport','COURSE',6,6,2),(19,NULL,'2026-05-23 12:00:00.000000','2026-05-23 09:00:00.000000','Staff Training Meeting','MEETING',7,NULL,18),(20,15,'2026-05-23 17:00:00.000000','2026-05-23 14:00:00.000000','Teens English Session','COURSE',8,19,17),(21,1,'2026-05-26 12:00:00.000000','2026-05-26 09:00:00.000000','A1 Family Vocabulary','COURSE',9,1,1),(22,3,'2026-05-26 17:00:00.000000','2026-05-26 14:00:00.000000','B1 Linking Words','COURSE',2,7,7),(23,6,'2026-05-27 12:00:00.000000','2026-05-27 09:00:00.000000','C2 Literary Analysis','COURSE',3,16,12),(24,6,'2026-05-27 17:00:00.000000','2026-05-27 14:00:00.000000','C2 Mastery Writing','COURSE',5,17,13),(25,12,'2026-05-28 12:00:00.000000','2026-05-28 09:00:00.000000','Startup Pitch Practice','COURSE',6,20,10),(26,14,'2026-05-28 17:00:00.000000','2026-05-28 14:00:00.000000','Academic Writing Workshop','COURSE',7,8,5),(27,10,'2026-05-29 12:00:00.000000','2026-05-29 09:00:00.000000','IELTS Full Mock Exam','EXAM',8,12,19),(28,4,'2026-05-29 17:00:00.000000','2026-05-29 14:00:00.000000','B2 Collocations Challenge','COURSE',9,10,6),(29,NULL,'2026-05-30 12:00:00.000000','2026-05-30 09:00:00.000000','End-of-Month Review','MEETING',2,NULL,18),(30,15,'2026-05-30 17:00:00.000000','2026-05-30 14:00:00.000000','Conversation Club','OTHER',3,9,7);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session_challenge_ids`
--

DROP TABLE IF EXISTS `session_challenge_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_challenge_ids` (
  `student_challenge_session_id` bigint NOT NULL,
  `challenge_ids` bigint DEFAULT NULL,
  KEY `FK32istu8m7elf4ythvwnip3gec` (`student_challenge_session_id`),
  CONSTRAINT `FK32istu8m7elf4ythvwnip3gec` FOREIGN KEY (`student_challenge_session_id`) REFERENCES `student_challenge_sessions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_challenge_ids`
--

LOCK TABLES `session_challenge_ids` WRITE;
/*!40000 ALTER TABLE `session_challenge_ids` DISABLE KEYS */;
INSERT INTO `session_challenge_ids` VALUES (1,8),(1,6),(1,7),(1,5),(1,10),(1,2),(1,4),(1,9),(1,1),(1,3),(2,63),(2,67),(2,65),(2,69),(2,62),(2,61),(2,68),(2,64),(2,70),(2,66),(3,129),(3,127),(3,121),(3,125),(3,126),(3,124),(3,130),(3,123),(3,122),(3,128),(4,2),(4,3),(4,4),(4,9),(4,6),(4,1),(4,8),(4,10),(4,7),(4,5),(5,12),(5,19),(5,15),(5,11),(5,18),(5,20),(5,17),(5,13),(5,16),(5,14),(6,140),(6,139),(6,131),(6,132),(6,137),(6,138),(6,135),(6,134),(6,133),(6,136),(7,69),(7,63),(7,68),(7,64),(7,66),(7,61),(7,70),(7,62),(7,67),(7,65),(8,68),(8,70),(8,62),(8,66),(8,63),(8,61),(8,67),(8,64),(8,69),(8,65),(9,63),(9,64),(9,68),(9,65),(9,66),(9,70),(9,67),(9,69),(9,62),(9,61),(10,130),(10,124),(10,129),(10,126),(10,125),(10,127),(10,121),(10,128),(10,122),(10,123),(11,62),(11,65),(11,67),(11,63),(11,64),(11,70),(11,66),(11,61),(11,68),(11,69);
/*!40000 ALTER TABLE `session_challenge_ids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_badges`
--

DROP TABLE IF EXISTS `student_badges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_badges` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_acquisition` datetime(6) DEFAULT NULL,
  `id_user` bigint DEFAULT NULL,
  `score_at_acquisition` int DEFAULT NULL,
  `session_id_that_triggered_badge` bigint DEFAULT NULL,
  `id_badge` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKft4pn0442232kiba9blo80mt` (`id_user`,`id_badge`),
  KEY `FK12lshhlje5y2j59a0ejqenemc` (`id_badge`),
  CONSTRAINT `FK12lshhlje5y2j59a0ejqenemc` FOREIGN KEY (`id_badge`) REFERENCES `badge` (`id_badge`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_badges`
--

LOCK TABLES `student_badges` WRITE;
/*!40000 ALTER TABLE `student_badges` DISABLE KEYS */;
INSERT INTO `student_badges` VALUES (1,'2026-05-02 03:18:55.540963',6,259,NULL,1),(2,'2026-05-02 03:18:55.655669',6,259,NULL,2),(3,'2026-05-02 03:18:55.721180',6,259,NULL,3),(4,'2026-05-02 03:18:55.739975',6,259,NULL,4),(5,'2026-05-02 03:18:55.840346',6,259,NULL,5),(6,'2026-06-05 14:42:22.096500',2,0,NULL,1);
/*!40000 ALTER TABLE `student_badges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_challenge_sessions`
--

DROP TABLE IF EXISTS `student_challenge_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_challenge_sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `correct_answers` int DEFAULT NULL,
  `current_challenge_index` int DEFAULT NULL,
  `id_user` bigint DEFAULT NULL,
  `session_duration_seconds` bigint DEFAULT NULL,
  `session_end_time` datetime(6) DEFAULT NULL,
  `session_level` enum('A1','A2','B1','B2','C1','C2') DEFAULT NULL,
  `session_start_time` datetime(6) DEFAULT NULL,
  `session_type` enum('MYSTERY_WORD','SENTENCE_BUILDER','EMOJI_WORD','WORD_BATTLE_ROYALE','STORY_CHAIN','SPEED_TRANSLATION_RACE') DEFAULT NULL,
  `status` enum('IN_PROGRESS','COMPLETED','EXPIRED') DEFAULT NULL,
  `total_challenges_played` int DEFAULT NULL,
  `total_score` int DEFAULT NULL,
  `wrong_answers` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_challenge_sessions`
--

LOCK TABLES `student_challenge_sessions` WRITE;
/*!40000 ALTER TABLE `student_challenge_sessions` DISABLE KEYS */;
INSERT INTO `student_challenge_sessions` VALUES (1,3,3,6,180,'2026-05-02 03:00:01.268780','A1','2026-05-02 02:57:16.001483','EMOJI_WORD','EXPIRED',3,59,1),(2,0,0,6,180,'2026-05-02 03:03:20.273890','A1','2026-05-02 03:00:01.275322','MYSTERY_WORD','EXPIRED',0,0,0),(3,0,0,6,180,'2026-05-02 03:15:52.740257','A1','2026-05-02 03:15:47.943791','SENTENCE_BUILDER','EXPIRED',0,0,0),(4,10,10,6,180,'2026-05-02 03:18:53.207252','A1','2026-05-02 03:15:52.744392','EMOJI_WORD','EXPIRED',10,200,0),(5,0,0,6,180,'2026-05-02 04:21:02.053909','A2','2026-05-02 04:20:40.379338','EMOJI_WORD','EXPIRED',0,0,0),(6,0,0,6,180,'2026-05-02 04:24:20.270988','A2','2026-05-02 04:21:02.063359','SENTENCE_BUILDER','EXPIRED',0,0,0),(7,11,11,2,180,'2026-06-05 14:42:21.685934','A1','2026-06-05 14:39:20.917430','MYSTERY_WORD','EXPIRED',11,214,6),(8,4,4,2,180,'2026-06-05 16:42:53.628658','A1','2026-06-05 16:39:11.156214','MYSTERY_WORD','EXPIRED',4,78,2),(9,0,0,2,180,'2026-06-05 16:47:19.442722','A1','2026-06-05 16:44:50.506380','MYSTERY_WORD','EXPIRED',0,0,0),(10,6,6,2,180,'2026-06-05 16:50:19.654562','A1','2026-06-05 16:47:19.457295','SENTENCE_BUILDER','EXPIRED',6,117,3),(11,10,10,2,180,'2026-06-05 17:19:35.597823','A1','2026-06-05 17:16:35.047640','MYSTERY_WORD','EXPIRED',10,198,2);
/*!40000 ALTER TABLE `student_challenge_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `academic_management`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `academic_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `academic_management`;

--
-- Table structure for table `certif_kanban_task`
--

DROP TABLE IF EXISTS `certif_kanban_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certif_kanban_task` (
  `position` int DEFAULT NULL,
  `reminder_sent` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deadline` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  `status` enum('TODO','DOING','DONE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certif_kanban_task`
--

LOCK TABLES `certif_kanban_task` WRITE;
/*!40000 ALTER TABLE `certif_kanban_task` DISABLE KEYS */;
INSERT INTO `certif_kanban_task` VALUES (0,_binary '\0','2026-06-08 00:05:14.000000','2026-06-15 23:59:00.000000',6,'2026-06-08 00:07:38.354309',2,'Finish the unit 4 grammar quiz on present perfect tense.','Complete Grammar Quiz','DOING'),(1,_binary '\0','2026-06-08 00:05:14.000000','2026-06-10 18:00:00.000000',7,'2026-06-08 00:05:14.000000',4,'Review and provide feedback for beginner class essays.','Review Essay Drafts','DOING'),(2,_binary '\0','2026-06-08 00:05:14.000000','2026-06-12 12:00:00.000000',8,'2026-06-08 00:05:14.000000',3,'Watch the vocabulary video for advanced business English.','Watch Unit 3 Video','TODO'),(1,_binary '','2026-06-08 00:05:14.000000','2026-06-08 10:00:00.000000',9,'2026-06-08 00:05:14.000000',6,'Practice oral presentation for the upcoming speaking evaluation.','Prepare Speaking Test','DONE'),(2,_binary '\0','2026-06-08 00:05:14.000000','2026-06-14 09:00:00.000000',10,'2026-06-08 00:05:14.000000',9,'Upload new reading comprehension texts for intermediate level.','Update Course Material','TODO'),(1,_binary '\0','2026-06-08 00:05:15.000000','2026-06-15 23:59:00.000000',11,'2026-06-08 00:05:15.000000',2,'Finish the unit 4 grammar quiz on present perfect tense.','Complete Grammar Quiz','TODO'),(1,_binary '\0','2026-06-08 00:05:15.000000','2026-06-10 18:00:00.000000',12,'2026-06-08 00:05:15.000000',4,'Review and provide feedback for beginner class essays.','Review Essay Drafts','DOING'),(2,_binary '\0','2026-06-08 00:05:15.000000','2026-06-12 12:00:00.000000',13,'2026-06-08 00:05:15.000000',3,'Watch the vocabulary video for advanced business English.','Watch Unit 3 Video','TODO'),(1,_binary '','2026-06-08 00:05:15.000000','2026-06-08 10:00:00.000000',14,'2026-06-08 00:05:15.000000',6,'Practice oral presentation for the upcoming speaking evaluation.','Prepare Speaking Test','DONE'),(2,_binary '\0','2026-06-08 00:05:15.000000','2026-06-14 09:00:00.000000',15,'2026-06-08 00:05:15.000000',9,'Upload new reading comprehension texts for intermediate level.','Update Course Material','TODO');
/*!40000 ALTER TABLE `certif_kanban_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificate` (
  `score` int DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `issued_at` datetime(6) DEFAULT NULL,
  `session_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  `certificate_number` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `qr_code` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
INSERT INTO `certificate` VALUES (96,1,'2026-06-08 00:07:58.000000',101,2,'CERT-ENG-2026-001','B2 Upper Intermediate','qr_data_bouthayna_001'),(88,2,'2026-06-08 00:07:58.000000',101,3,'CERT-ENG-2026-002','B1 Intermediate','qr_data_oussama_002'),(93,3,'2026-06-08 00:07:58.000000',102,5,'CERT-ENG-2026-003','C1 Advanced','qr_data_amal_003'),(78,4,'2026-06-08 00:07:58.000000',102,6,'CERT-ENG-2026-004','B2 Upper Intermediate','qr_data_yasmin_004'),(86,5,'2026-06-08 00:07:58.000000',103,7,'CERT-ENG-2026-005','A2 Pre-Intermediate','qr_data_hiba_005');
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certification_question`
--

DROP TABLE IF EXISTS `certification_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certification_question` (
  `active` bit(1) DEFAULT NULL,
  `points` int DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `correct_answer` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `optiona` varchar(255) DEFAULT NULL,
  `optionb` varchar(255) DEFAULT NULL,
  `optionc` varchar(255) DEFAULT NULL,
  `optiond` varchar(255) DEFAULT NULL,
  `question_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certification_question`
--

LOCK TABLES `certification_question` WRITE;
/*!40000 ALTER TABLE `certification_question` DISABLE KEYS */;
INSERT INTO `certification_question` VALUES (_binary '',5,1,'Grammar','A','A1','is','are','am','be','She ___ a student.'),(_binary '',5,2,'Grammar','A','A1','go','goes','going','went','I ___ to school every day.'),(_binary '',5,3,'Vocabulary','A','A1','small','tall','large','heavy','Opposite of big?'),(_binary '',5,4,'Grammar','A','A1','is','are','am','be','There ___ a book on the table.'),(_binary '',5,5,'Grammar','A','A2','does','do','is','are','He ___ not like coffee.'),(_binary '',5,6,'Grammar','A','A2','at','in','on','by','I wake up ___ 7am.'),(_binary '',5,7,'Vocabulary','A','A2','beau/belle','laid/laide','grand/grande','petit/petite','What does beautiful mean?'),(_binary '',5,8,'Grammar','A','A2','has','have','had','is','She ___ been to Paris.'),(_binary '',10,9,'Grammar','A','B1','were','was','am','be','If I ___ rich, I would travel.'),(_binary '',10,10,'Vocabulary','A','B1','postponed','proposed','promoted','proceeded','The meeting was ___ due to the storm.'),(_binary '',10,11,'Grammar','A','B1','going','go','to go','gone','She suggested ___ to the cinema.'),(_binary '',10,12,'Grammar','A','B1','will','would','shall','should','Unless you study, you ___ fail.'),(_binary '',10,13,'Grammar','A','B2','could','can','will','would','I wish I ___ speak French fluently.'),(_binary '',10,14,'Vocabulary','A','B2','very careful and precise','very fast','very loud','very creative','What does meticulous mean?'),(_binary '',10,15,'Grammar','A','B2','that','who','whom','what','The book ___ you lent me was excellent.'),(_binary '',10,16,'Vocabulary','A','B2','to delay doing things','to organize','to complete quickly','to prioritize','To procrastinate means?'),(_binary '',15,17,'Grammar','A','C1','would have','will have','had','should','Had I known, I ___ helped.'),(_binary '',15,18,'Vocabulary','A','C1','flatters to gain favor','very honest','very brave','very smart','What does sycophant mean?'),(_binary '',15,19,'Grammar','A','C1','did','had','was','has','Not only ___ he arrive late, but he forgot documents.'),(_binary '',15,20,'Vocabulary','A','C1','using very few words','too many words','complex words','incorrect words','What does laconic mean?'),(_binary '',15,21,'Grammar','A','C2','changed','changes','change','would change','It is high time he ___ his behavior.'),(_binary '',15,22,'Vocabulary','A','C2','having ready insight','poor judgment','no opinion','extreme views','What does perspicacious mean?'),(_binary '',15,23,'Register','A','C2','I am writing to express my concern','I wanna talk about a problem','Hey I have an issue','Just wanted to say something','Correct formal letter opening?'),(_binary '',15,24,'Vocabulary','A','C2','too great to express in words','easy to express','very common','very simple','What does ineffable mean?');
/*!40000 ALTER TABLE `certification_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `is_hidden` bit(1) NOT NULL,
  `lessons_number` int DEFAULT NULL,
  `price` float DEFAULT NULL,
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `course_type` enum('BUSINESS_ENGLISH','GENERAL_ENGLISH') DEFAULT NULL,
  `level` enum('A1','A2','B1','B2','C1','C2') DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (_binary '\0',12,99.99,1,'Master professional communication skills for the workplace','https://images.unsplash.com/photo-1552664730-d307ca884978?w=800','Business Communication Essentials','BUSINESS_ENGLISH','A1'),(_binary '\0',8,79.99,2,'Learn to write effective and professional business emails','https://images.unsplash.com/photo-1596526131083-e8c633c948d2?w=800','Corporate Email Writing','BUSINESS_ENGLISH','A2'),(_binary '\0',15,149.99,3,'Develop confidence in delivering business presentations','https://images.unsplash.com/photo-1475721027785-f74eccf877e2?w=800','Business Presentations & Public Speaking','BUSINESS_ENGLISH','B1'),(_binary '\0',10,129.99,4,'Master negotiation techniques for international business','https://images.unsplash.com/photo-1521791136064-7986c2920216?w=800','Negotiation Skills in English','BUSINESS_ENGLISH','B2'),(_binary '\0',10,119.99,5,'Learn to conduct and participate in professional meetings','https://images.unsplash.com/photo-1556761175-b413da4baf72?w=800','Business Meeting Management','BUSINESS_ENGLISH','C1'),(_binary '\0',14,179.99,6,'Master reports, proposals, and business documents','https://images.unsplash.com/photo-1450101499163-c8848c66ca85?w=800','Advanced Business Writing','BUSINESS_ENGLISH','C2'),(_binary '\0',20,49.99,7,'Start your English learning journey from scratch','https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=800','English for Beginners','GENERAL_ENGLISH','A1'),(_binary '\0',15,59.99,8,'Learn practical English for daily situations','https://images.unsplash.com/photo-1573164713714-d95e436ab8d6?w=800','Everyday Conversation Skills','GENERAL_ENGLISH','A2'),(_binary '\0',18,89.99,9,'Strengthen your grammar foundation','https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=800','Intermediate English Grammar','GENERAL_ENGLISH','B1'),(_binary '\0',16,139.99,10,'Achieve native-like fluency and confidence','https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=800','Advanced English Fluency','GENERAL_ENGLISH','C2');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollment` (
  `duration` int DEFAULT NULL,
  `enrollment_date` date DEFAULT NULL,
  `is_completed` bit(1) DEFAULT NULL,
  `progress` int DEFAULT NULL,
  `score` float DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `enrollment_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`enrollment_id`),
  KEY `FKbhhcqkw1px6yljqg92m0sh2gt` (`course_id`),
  CONSTRAINT `FKbhhcqkw1px6yljqg92m0sh2gt` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` VALUES (14,'2026-05-10',_binary '\0',45,0,2,1,1),(30,'2026-04-05',_binary '',100,92,3,7,2),(5,'2026-06-01',_binary '\0',10,0,5,9,3),(20,'2026-05-15',_binary '\0',75,80,6,4,4),(15,'2026-04-20',_binary '',100,88,7,2,5);
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_question`
--

DROP TABLE IF EXISTS `learning_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_question` (
  `questionid` bigint NOT NULL AUTO_INCREMENT,
  `quiz_quiz_id` bigint DEFAULT NULL,
  `correct_answer` varchar(255) DEFAULT NULL,
  `optiona` varchar(255) DEFAULT NULL,
  `optionb` varchar(255) DEFAULT NULL,
  `optionc` varchar(255) DEFAULT NULL,
  `optiond` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`questionid`),
  KEY `FKr9n3kqjovdmqv05posupeet3q` (`quiz_quiz_id`),
  CONSTRAINT `FKr9n3kqjovdmqv05posupeet3q` FOREIGN KEY (`quiz_quiz_id`) REFERENCES `quiz` (`quiz_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_question`
--

LOCK TABLES `learning_question` WRITE;
/*!40000 ALTER TABLE `learning_question` DISABLE KEYS */;
INSERT INTO `learning_question` VALUES (6,1,'B','You are wrong.','I see your point, but...','That is a bad idea.','No way.','Which of the following is a polite way to disagree in a business meeting?'),(7,1,'C','started','has started','had started','starting','Choose the correct form: By the time we arrived, the meeting ___.'),(8,2,'A','To think creatively','To work outside','To open a package','To ignore all rules','What does the idiom \"to think outside the box\" mean?'),(9,2,'B','at','to','for','in','Select the correct preposition: We are looking forward ___ seeing you.'),(10,3,'C','Dangerous','Unprofitable','Profitable','Complex','Identify the synonym for the word \"Lucrative\".'),(11,1,'B','You are wrong.','I see your point, but...','That is a bad idea.','No way.','Which of the following is a polite way to disagree in a business meeting?'),(12,1,'C','started','has started','had started','starting','Choose the correct form: By the time we arrived, the meeting ___.'),(13,2,'A','To think creatively','To work outside','To open a package','To ignore all rules','What does the idiom \"to think outside the box\" mean?'),(14,2,'B','at','to','for','in','Select the correct preposition: We are looking forward ___ seeing you.'),(15,3,'C','Dangerous','Unprofitable','Profitable','Complex','Identify the synonym for the word \"Lucrative\".');
/*!40000 ALTER TABLE `learning_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson`
--

DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson` (
  `lesson_order` int DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `lesson_id` bigint NOT NULL AUTO_INCREMENT,
  `content` text,
  `file` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lesson_id`),
  KEY `FKjs3c7skmg8bvdddok5lc7s807` (`course_id`),
  CONSTRAINT `FKjs3c7skmg8bvdddok5lc7s807` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson`
--

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson` DISABLE KEYS */;
INSERT INTO `lesson` VALUES (1,1,1,'Learn the core principles of effective communication in a corporate environment. This lesson covers active listening and professional tone.','https://fr.scribd.com/document/848589351/Fiche-Revision-Anglais-Complete-TOTALE','Introduction to Business Communication'),(2,1,2,'Master the structure of formal business emails. Includes templates for common scenarios like requesting meetings or following up.','https://fr.scribd.com/document/848589351/Fiche-Revision-Anglais-Complete-TOTALE','Writing Professional Emails'),(1,7,3,'Start your English journey by learning the alphabet, basic sounds, and everyday greetings like \"Hello\" and \"How are you?\".','https://fr.scribd.com/document/848589351/Fiche-Revision-Anglais-Complete-TOTALE','The Alphabet and Basic Greetings'),(2,7,4,'Understand how to use the most important English verb: \"To Be\". Includes exercises on personal pronouns (I, you, he, she, it).','https://fr.scribd.com/document/848589351/Fiche-Revision-Anglais-Complete-TOTALE','Verb \"To Be\" and Pronouns'),(3,7,5,'Learn how to count from 1 to 100, ask for the time, and read a clock in English.','https://fr.scribd.com/document/848589351/Fiche-Revision-Anglais-Complete-TOTALE','Numbers and Time');
/*!40000 ALTER TABLE `lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quiz`
--

DROP TABLE IF EXISTS `quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz` (
  `course_id` bigint DEFAULT NULL,
  `quiz_id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`quiz_id`),
  UNIQUE KEY `UK_8b1no9kk3xjgste5vdbfgpcrr` (`course_id`),
  CONSTRAINT `FKce16mrsgeokucc022mpyev7xk` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz`
--

LOCK TABLES `quiz` WRITE;
/*!40000 ALTER TABLE `quiz` DISABLE KEYS */;
INSERT INTO `quiz` VALUES (1,1,'Business Meeting Communication Quiz'),(4,2,'Negotiation Skills Assessment'),(7,3,'Beginner Vocabulary Test');
/*!40000 ALTER TABLE `quiz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quiz_attempt`
--

DROP TABLE IF EXISTS `quiz_attempt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz_attempt` (
  `attempt_date` date DEFAULT NULL,
  `is_completed` bit(1) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quiz_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8l6wmgul0rgeha0lp6abrp5fa` (`quiz_id`),
  CONSTRAINT `FK8l6wmgul0rgeha0lp6abrp5fa` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz_attempt`
--

LOCK TABLES `quiz_attempt` WRITE;
/*!40000 ALTER TABLE `quiz_attempt` DISABLE KEYS */;
INSERT INTO `quiz_attempt` VALUES ('2026-06-08',_binary '',85,2,1,1),('2026-06-08',_binary '',92,3,2,2),('2026-06-08',_binary '\0',45,5,3,1),('2026-06-08',_binary '',78,6,4,3),('2026-06-08',_binary '',100,7,5,2);
/*!40000 ALTER TABLE `quiz_attempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quiz_attempt_answers`
--

DROP TABLE IF EXISTS `quiz_attempt_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz_attempt_answers` (
  `attempt_id` bigint NOT NULL,
  `answer` varchar(255) DEFAULT NULL,
  KEY `FK7cgfh889ms15xh1c0e51j90wi` (`attempt_id`),
  CONSTRAINT `FK7cgfh889ms15xh1c0e51j90wi` FOREIGN KEY (`attempt_id`) REFERENCES `quiz_attempt` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz_attempt_answers`
--

LOCK TABLES `quiz_attempt_answers` WRITE;
/*!40000 ALTER TABLE `quiz_attempt_answers` DISABLE KEYS */;
INSERT INTO `quiz_attempt_answers` VALUES (1,'B'),(1,'C'),(2,'A'),(2,'B'),(3,'B');
/*!40000 ALTER TABLE `quiz_attempt_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session_question`
--

DROP TABLE IF EXISTS `session_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_question` (
  `correct` bit(1) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_id` bigint DEFAULT NULL,
  `session_id` bigint DEFAULT NULL,
  `student_answer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqs6w4kw8n78n8yqn4ii07c3uc` (`question_id`),
  KEY `FKpb7mr5dbxfhoksh1umna6rqbg` (`session_id`),
  CONSTRAINT `FKpb7mr5dbxfhoksh1umna6rqbg` FOREIGN KEY (`session_id`) REFERENCES `test_session` (`id`),
  CONSTRAINT `FKqs6w4kw8n78n8yqn4ii07c3uc` FOREIGN KEY (`question_id`) REFERENCES `certification_question` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_question`
--

LOCK TABLES `session_question` WRITE;
/*!40000 ALTER TABLE `session_question` DISABLE KEYS */;
INSERT INTO `session_question` VALUES (_binary '',6,1,101,'B'),(_binary '\0',7,2,101,'B'),(_binary '',8,3,102,'A'),(_binary '',9,4,102,'B'),(_binary '\0',10,5,103,'A'),(_binary '',11,1,101,'B'),(_binary '\0',12,2,101,'B'),(_binary '',13,3,102,'A'),(_binary '',14,4,102,'B'),(_binary '\0',15,5,103,'A'),(_binary '',16,1,101,'B'),(_binary '\0',17,2,101,'B'),(_binary '',18,3,102,'A'),(_binary '',19,4,102,'B'),(_binary '\0',20,5,103,'A'),(_binary '',21,1,101,'B'),(_binary '\0',22,2,101,'B'),(_binary '',23,3,102,'A'),(_binary '',24,4,102,'B'),(_binary '\0',25,5,103,'A'),(_binary '',26,1,101,'B'),(_binary '\0',27,2,101,'B'),(_binary '',28,3,102,'A'),(_binary '',29,4,102,'B'),(_binary '\0',30,5,103,'A');
/*!40000 ALTER TABLE `session_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_session`
--

DROP TABLE IF EXISTS `test_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_session` (
  `attempt_number` int DEFAULT NULL,
  `duration_seconds` int DEFAULT NULL,
  `passed` bit(1) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `suspicious` bit(1) DEFAULT NULL,
  `tab_switch_count` int DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint DEFAULT NULL,
  `taken_at` datetime(6) DEFAULT NULL,
  `suspicious_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_session`
--

LOCK TABLES `test_session` WRITE;
/*!40000 ALTER TABLE `test_session` DISABLE KEYS */;
INSERT INTO `test_session` VALUES (1,1200,_binary '',85,_binary '\0',0,'2026-12-31 23:59:00.000000',101,2,'2026-06-08 00:24:14.000000',NULL),(1,1500,_binary '',92,_binary '\0',1,'2026-12-31 23:59:00.000000',102,3,'2026-06-08 00:24:14.000000',NULL),(1,900,_binary '\0',45,_binary '',5,'2026-12-31 23:59:00.000000',103,5,'2026-06-08 00:24:14.000000','Switched tabs 5 times during test');
/*!40000 ALTER TABLE `test_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `learner_management`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `learner_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `learner_management`;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `author` (
  `author_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (1,'Frank Herbert'),(2,'J.R.R. Tolkien'),(3,'George Orwell'),(4,'F. Scott Fitzgerald'),(5,'Robert C. Martin');
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `book_id` bigint NOT NULL AUTO_INCREMENT,
  `currency` enum('TND','EUR','USD') DEFAULT NULL,
  `image` longtext,
  `isbn` varchar(255) DEFAULT NULL,
  `publication_year` date NOT NULL,
  `sale_price` decimal(38,2) DEFAULT NULL,
  `status` enum('AVAILABLE','OUT_OF_STOCK','DISCONTINUED') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `author_author_id` bigint DEFAULT NULL,
  `category_category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`book_id`),
  KEY `FKef9c1v09t9gdkor9ul80hsj4n` (`author_author_id`),
  KEY `FK6vrcjcn4n1tfkyxmkoghg9ng` (`category_category_id`),
  CONSTRAINT `FK6vrcjcn4n1tfkyxmkoghg9ng` FOREIGN KEY (`category_category_id`) REFERENCES `category` (`category_id`),
  CONSTRAINT `FKef9c1v09t9gdkor9ul80hsj4n` FOREIGN KEY (`author_author_id`) REFERENCES `author` (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'TND','https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg','978-0441172719','1965-08-01',29.99,'AVAILABLE','Dune',1,1),(2,'TND','https://covers.openlibrary.org/b/isbn/9780544003415-L.jpg','978-0544003415','1954-07-29',35.50,'AVAILABLE','The Fellowship of the Ring',2,2),(3,'TND','https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg','978-0451524935','1949-06-08',25.00,'AVAILABLE','1984',3,3),(4,'TND','https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg','978-0743273565','1925-04-10',18.00,'AVAILABLE','The Great Gatsby',4,4),(5,'TND','https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg','978-0132350884','2008-08-01',120.00,'AVAILABLE','Clean Code',5,5);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Science Fiction'),(2,'Fantasy'),(3,'Dystopian'),(4,'Classic Literature'),(5,'Programming');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clubs`
--

DROP TABLE IF EXISTS `clubs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clubs` (
  `club_id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','ARCHIVED') DEFAULT NULL,
  `type` enum('SPORT','CULTURAL','SCIENTIFIC','TECHNOLOGICAL','ARTISTIC','SOCIAL') DEFAULT NULL,
  PRIMARY KEY (`club_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clubs`
--

LOCK TABLES `clubs` WRITE;
/*!40000 ALTER TABLE `clubs` DISABLE KEYS */;
INSERT INTO `clubs` VALUES (1,'2026-06-02','','Mountain Explore','ACTIVE','CULTURAL'),(7,'2026-01-10','A club dedicated to improving English conversation skills through weekly sessions','English Speakers Hub','ACTIVE','CULTURAL'),(8,'2026-02-05','Club focused on business English, professional writing and workplace communication','Business English Club','ACTIVE','SOCIAL'),(9,'2026-03-12','Beginner friendly club for learners starting their English language journey','English Starters Club','ACTIVE','SCIENTIFIC'),(10,'2026-04-18','Advanced reading and literary analysis club for proficient English learners','English Literature Circle','INACTIVE','ARTISTIC'),(11,'2026-05-22','Club for practicing English through debate, public speaking and argumentation','Debate & Speech Club','ACTIVE','TECHNOLOGICAL');
/*!40000 ALTER TABLE `clubs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dropout_forms`
--

DROP TABLE IF EXISTS `dropout_forms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dropout_forms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attendance_commitment` int NOT NULL,
  `class_difficulty_level` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `english_level_self` varchar(255) NOT NULL,
  `financial_stress_level` int NOT NULL,
  `free_time_hours_per_week` double NOT NULL,
  `goal_clarity_level` int NOT NULL,
  `homework_completion_self` int NOT NULL,
  `interaction_with_teacher` int NOT NULL,
  `model_name` varchar(255) NOT NULL,
  `motivation_level` int NOT NULL,
  `peer_interaction_level` int NOT NULL,
  `predicted_dropout` varchar(255) NOT NULL,
  `predicted_probability` double DEFAULT NULL,
  `preferred_learning_mode` varchar(255) NOT NULL,
  `satisfaction_level` int NOT NULL,
  `technical_issues_frequency` int NOT NULL,
  `weekly_study_hours` double NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2r4xvsq5gq2p19dosfsd5g2v2` (`user_id`),
  CONSTRAINT `FK2r4xvsq5gq2p19dosfsd5g2v2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dropout_forms`
--

LOCK TABLES `dropout_forms` WRITE;
/*!40000 ALTER TABLE `dropout_forms` DISABLE KEYS */;
INSERT INTO `dropout_forms` VALUES (1,6,3,'2026-05-12 20:25:26.508326','A2',3,17,5,4,5,'KNN',3,3,'no',0.08629786450341954,'hybrid',4,2,9,3),(2,5,7,'2026-05-12 20:27:04.468912','A2',6,8,9,8,7,'KNN',5,8,'no',0,'online',6,2,3.5,2);
/*!40000 ALTER TABLE `dropout_forms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `excursion_participations`
--

DROP TABLE IF EXISTS `excursion_participations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `excursion_participations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `registration_date` date DEFAULT NULL,
  `status` enum('REGISTERED','CONFIRMED','FAILED') DEFAULT NULL,
  `excursion_excursion_id` bigint DEFAULT NULL,
  `member_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb229p4tvsmk1hlnseab4xlf91` (`excursion_excursion_id`),
  KEY `FKnrmyjv3aq0a8np6v9iiydf1vx` (`member_user_id`),
  CONSTRAINT `FKb229p4tvsmk1hlnseab4xlf91` FOREIGN KEY (`excursion_excursion_id`) REFERENCES `excursions` (`excursion_id`),
  CONSTRAINT `FKnrmyjv3aq0a8np6v9iiydf1vx` FOREIGN KEY (`member_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `excursion_participations`
--

LOCK TABLES `excursion_participations` WRITE;
/*!40000 ALTER TABLE `excursion_participations` DISABLE KEYS */;
INSERT INTO `excursion_participations` VALUES (11,'2026-05-10','REGISTERED',1,2),(12,'2026-05-12','CONFIRMED',1,3),(13,'2026-05-15','REGISTERED',2,7),(14,'2026-05-18','CONFIRMED',2,11),(15,'2026-05-20','FAILED',3,6);
/*!40000 ALTER TABLE `excursion_participations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `excursions`
--

DROP TABLE IF EXISTS `excursions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `excursions` (
  `excursion_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `nbr_de_reservation` int DEFAULT NULL,
  `nbr_deplace` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` enum('PLANNED','ONGOING','FINISHED','CANCELLED') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `club_club_id` bigint DEFAULT NULL,
  PRIMARY KEY (`excursion_id`),
  KEY `FKmi3rtwc7pdobhk6dm6thnksu8` (`club_club_id`),
  CONSTRAINT `FKmi3rtwc7pdobhk6dm6thnksu8` FOREIGN KEY (`club_club_id`) REFERENCES `clubs` (`club_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `excursions`
--

LOCK TABLES `excursions` WRITE;
/*!40000 ALTER TABLE `excursions` DISABLE KEYS */;
INSERT INTO `excursions` VALUES (1,'Full immersion English trip to discover British culture and language','2026-06-20','London, UK',30,25,'2026-06-15','PLANNED','London English Immersion',1),(2,'Educational visit to Shakespeare Birthplace and Globe Theatre','2026-06-21','Stratford, UK',20,18,'2026-06-20','ONGOING','Shakespeare Museum Visit',1),(3,'Outdoor English speaking camp for advanced learners','2026-07-07','Oxford, UK',25,0,'2026-07-05','PLANNED','English Language Camp',1),(4,'English debate and public speaking competition event','2026-07-15','Cambridge, UK',15,10,'2026-07-14','FINISHED','Cambridge Debate Tournament',1),(5,'English literature and arts cultural discovery tour','2026-08-01','Edinburgh, UK',20,5,'2026-07-28','CANCELLED','Edinburgh Literary Tour',1);
/*!40000 ALTER TABLE `excursions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membership_requests`
--

DROP TABLE IF EXISTS `membership_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membership_requests` (
  `request_id` bigint NOT NULL AUTO_INCREMENT,
  `decision_date` date DEFAULT NULL,
  `motivation` varchar(255) DEFAULT NULL,
  `request_date` date DEFAULT NULL,
  `status` enum('PENDING','ACCEPTED','REJECTED') DEFAULT NULL,
  `club_club_id` bigint DEFAULT NULL,
  `decided_by_member_user_id` int DEFAULT NULL,
  `member_user_id` int DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `FKamyvo08trcppig70ujyopg4oc` (`club_club_id`),
  KEY `FKrtpo619oga7k0y8acayoe1yl2` (`decided_by_member_user_id`),
  KEY `FK102yrkhbxncdpbh1l45gbk36q` (`member_user_id`),
  CONSTRAINT `FK102yrkhbxncdpbh1l45gbk36q` FOREIGN KEY (`member_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKamyvo08trcppig70ujyopg4oc` FOREIGN KEY (`club_club_id`) REFERENCES `clubs` (`club_id`),
  CONSTRAINT `FKrtpo619oga7k0y8acayoe1yl2` FOREIGN KEY (`decided_by_member_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membership_requests`
--

LOCK TABLES `membership_requests` WRITE;
/*!40000 ALTER TABLE `membership_requests` DISABLE KEYS */;
INSERT INTO `membership_requests` VALUES (1,'2026-05-02','I want to improve my English conversation skills','2026-05-01','ACCEPTED',1,1,2),(2,'2026-05-05','English club will help me prepare for my IELTS exam','2026-05-04','ACCEPTED',1,1,3),(3,NULL,'I am passionate about English literature and culture','2026-05-10','PENDING',1,NULL,7),(4,'2026-05-12','I want to practice English with native speakers','2026-05-11','REJECTED',1,1,11),(5,NULL,'Joining this club will boost my English writing skills','2026-05-15','PENDING',1,NULL,6);
/*!40000 ALTER TABLE `membership_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  `line_total` decimal(38,2) DEFAULT NULL,
  `quantity` int NOT NULL,
  `unit_sale_price` decimal(38,2) DEFAULT NULL,
  `book_book_id` bigint DEFAULT NULL,
  `order_order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `FKpta3c0lr9iwrme6pvagrjpmx9` (`book_book_id`),
  KEY `FKdf4sdmwfnxquyj8hcfrlu9a24` (`order_order_id`),
  CONSTRAINT `FKdf4sdmwfnxquyj8hcfrlu9a24` FOREIGN KEY (`order_order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `FKpta3c0lr9iwrme6pvagrjpmx9` FOREIGN KEY (`book_book_id`) REFERENCES `book` (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (21,59.98,2,29.99,1,6),(22,35.50,1,35.50,2,6),(23,75.00,3,25.00,3,7),(24,18.00,1,18.00,4,7),(25,240.00,2,120.00,5,8);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `currency` enum('TND','EUR','USD') DEFAULT NULL,
  `discount_applied` bit(1) NOT NULL,
  `order_date` date DEFAULT NULL,
  `paid` bit(1) NOT NULL,
  `payment_date` date DEFAULT NULL,
  `status` enum('CREATED','PAID','CANCELLED') DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `user_user_id` int DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FK38709695otpk064vi3y92u08s` (`user_user_id`),
  CONSTRAINT `FK38709695otpk064vi3y92u08s` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (6,'TND',_binary '\0','2026-05-01',_binary '','2026-05-02','PAID',95.48,2),(7,'TND',_binary '','2026-05-05',_binary '','2026-05-06','PAID',93.00,3),(8,'TND',_binary '\0','2026-05-10',_binary '\0',NULL,'CREATED',240.00,6),(9,'TND',_binary '','2026-05-12',_binary '\0',NULL,'CANCELLED',162.00,7),(10,'TND',_binary '\0','2026-05-15',_binary '\0',NULL,'CREATED',29.99,11);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participation_clubs`
--

DROP TABLE IF EXISTS `participation_clubs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participation_clubs` (
  `participation_id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  `role` enum('PRESIDENT','SECRETARY','TREASURER','MEMBER') DEFAULT NULL,
  `status` enum('ACTIVE','SUSPENDED','LEFT') DEFAULT NULL,
  `club_club_id` bigint DEFAULT NULL,
  `member_user_id` int DEFAULT NULL,
  PRIMARY KEY (`participation_id`),
  KEY `FK7jhrwvnwr8on73p6c0h6duoxl` (`club_club_id`),
  KEY `FKtlp82q1jv7aon70yspn0j0vki` (`member_user_id`),
  CONSTRAINT `FK7jhrwvnwr8on73p6c0h6duoxl` FOREIGN KEY (`club_club_id`) REFERENCES `clubs` (`club_id`),
  CONSTRAINT `FKtlp82q1jv7aon70yspn0j0vki` FOREIGN KEY (`member_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participation_clubs`
--

LOCK TABLES `participation_clubs` WRITE;
/*!40000 ALTER TABLE `participation_clubs` DISABLE KEYS */;
INSERT INTO `participation_clubs` VALUES (1,NULL,'2026-05-02','MEMBER','ACTIVE',1,2),(2,NULL,'2026-05-05','SECRETARY','ACTIVE',1,3),(3,NULL,'2026-04-10','PRESIDENT','ACTIVE',1,7),(4,'2026-05-20','2026-03-01','MEMBER','LEFT',1,6),(5,NULL,'2026-05-15','TREASURER','SUSPENDED',1,11);
/*!40000 ALTER TABLE `participation_clubs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `method` enum('CASH','WALLET') DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `status` enum('PENDING','PAID','FAILED','REFUNDED') DEFAULT NULL,
  `member_id` int DEFAULT NULL,
  `training_training_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkqv8pkak4wp6i2t0dd32twj7p` (`member_id`),
  KEY `FK2wgyuvuqx2jatmtje2h5m7jq7` (`training_training_id`),
  CONSTRAINT `FK2wgyuvuqx2jatmtje2h5m7jq7` FOREIGN KEY (`training_training_id`) REFERENCES `trainings` (`training_id`),
  CONSTRAINT `FKkqv8pkak4wp6i2t0dd32twj7p` FOREIGN KEY (`member_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,49.99,'WALLET','2026-01-15 09:30:00.000000','PAID',2,1),(2,89.99,'CASH','2026-02-03 14:15:00.000000','PAID',3,2),(3,69.99,'WALLET','2026-03-10 11:00:00.000000','PENDING',5,3),(4,129.99,'CASH','2026-04-22 16:45:00.000000','FAILED',6,4),(5,59.99,'WALLET','2026-05-18 10:20:00.000000','REFUNDED',7,5);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental`
--

DROP TABLE IF EXISTS `rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental` (
  `rental_id` bigint NOT NULL AUTO_INCREMENT,
  `currency` enum('TND','EUR','USD') DEFAULT NULL,
  `daily_rental_price` decimal(38,2) DEFAULT NULL,
  `discount_applied` bit(1) NOT NULL,
  `due_date` date DEFAULT NULL,
  `paid` bit(1) NOT NULL,
  `payment_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` enum('ACTIVE','RETURNED','LATE','CANCELLED') DEFAULT NULL,
  `total_rental_price` decimal(38,2) DEFAULT NULL,
  `book_book_id` bigint DEFAULT NULL,
  `user_user_id` int DEFAULT NULL,
  PRIMARY KEY (`rental_id`),
  KEY `FKliemaad64kpgm3nihvt9mxb6r` (`book_book_id`),
  KEY `FKhnb3rd7l1c0qg0spt7yhofahv` (`user_user_id`),
  CONSTRAINT `FKhnb3rd7l1c0qg0spt7yhofahv` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKliemaad64kpgm3nihvt9mxb6r` FOREIGN KEY (`book_book_id`) REFERENCES `book` (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental`
--

LOCK TABLES `rental` WRITE;
/*!40000 ALTER TABLE `rental` DISABLE KEYS */;
INSERT INTO `rental` VALUES (1,'TND',2.90,_binary '\0','2026-07-02',_binary '','2026-06-06',NULL,'2026-06-03','ACTIVE',84.10,2,1),(2,'EUR',4.00,_binary '\0','2026-09-24',_binary '\0',NULL,NULL,'2026-04-30','ACTIVE',588.00,4,1);
/*!40000 ALTER TABLE `rental` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `stock_id` bigint NOT NULL AUTO_INCREMENT,
  `last_update` date DEFAULT NULL,
  `quantity` int NOT NULL,
  `reserved_qty` int NOT NULL,
  `book_book_id` bigint DEFAULT NULL,
  PRIMARY KEY (`stock_id`),
  UNIQUE KEY `UK_elqfintlxe9l3vmy6tqihd5rl` (`book_book_id`),
  CONSTRAINT `FKr87sgmr2xigd7qc4ngul0lv1w` FOREIGN KEY (`book_book_id`) REFERENCES `book` (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (2,'2026-06-06',3,0,1),(4,'2026-06-06',2,0,2),(5,'2026-06-06',3,0,4),(9,'2026-06-06',10,0,3),(10,'2026-06-06',6,0,5);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES (1,'Describe a memorable trip you have taken or would like to take. Include:\n\nWhere you went/want to go\nWho you went/would go with\nActivities you did/would do\nWhy the trip is special','15','A Memorable Trip'),(2,'Learn essential English words and phrases for everyday use.','20','English Vocabulary Building'),(3,'Master correct English pronunciation and accent reduction.','25','English Pronunciation Practice'),(4,'Improve your ability to understand and analyze English texts.','30','Reading Comprehension'),(5,'Develop clear and effective writing skills in English.','35','Writing Skills in English'),(6,'Enhance your listening skills and spoken English fluency.','20','Listening and Speaking');
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_tentative`
--

DROP TABLE IF EXISTS `test_tentative`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_tentative` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paragraph` text,
  `score` int NOT NULL,
  `status` enum('PASSED','FAILED','PENDING','CORRECTED') DEFAULT NULL,
  `tutor_feedback` text,
  `user_id` bigint DEFAULT NULL,
  `subject_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKccwel4at278sfag5twpevdxjj` (`subject_id`),
  CONSTRAINT `FKccwel4at278sfag5twpevdxjj` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_tentative`
--

LOCK TABLES `test_tentative` WRITE;
/*!40000 ALTER TABLE `test_tentative` DISABLE KEYS */;
INSERT INTO `test_tentative` VALUES (1,'Last summer, I visited Paris with my family. It was an amazing experience full of culture and food.',85,'PASSED','Good structure and vocabulary. Work on verb tenses.',2,1),(2,'My favourite holiday was when I went to London. I saw Big Ben and the Thames River.',72,'PASSED','Clear ideas but needs more descriptive language.',3,1),(3,'I travelled to Rome last year. The food was delicious and the history was fascinating.',90,'CORRECTED','Excellent writing! Very good use of adjectives.',5,1),(4,'We went to the beach in summer. The weather was hot and the sea was beautiful and calm.',45,'FAILED','Needs improvement in grammar and sentence structure.',6,1),(5,'My best trip was to New York City. I visited Central Park and the Statue of Liberty.',0,'PENDING','Not yet corrected.',7,1);
/*!40000 ALTER TABLE `test_tentative` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_participations`
--

DROP TABLE IF EXISTS `training_participations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_participations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed` bit(1) DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  `reward_amount` decimal(38,2) DEFAULT NULL,
  `reward_transferred` bit(1) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `status` enum('REGISTERED','CONFIRMED','FAILED') DEFAULT NULL,
  `member_id` int DEFAULT NULL,
  `training_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl5p5g0t4dbr6dmdcqrpegn3if` (`member_id`),
  KEY `FKlqov4r2t6caxi61eoqr3sau66` (`training_id`),
  CONSTRAINT `FKl5p5g0t4dbr6dmdcqrpegn3if` FOREIGN KEY (`member_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKlqov4r2t6caxi61eoqr3sau66` FOREIGN KEY (`training_id`) REFERENCES `trainings` (`training_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_participations`
--

LOCK TABLES `training_participations` WRITE;
/*!40000 ALTER TABLE `training_participations` DISABLE KEYS */;
INSERT INTO `training_participations` VALUES (1,_binary '','2026-01-15',50.00,_binary '\0',88,'CONFIRMED',2,1),(2,_binary '','2026-02-03',30.00,_binary '\0',75,'CONFIRMED',3,2),(3,_binary '\0','2026-03-10',0.00,_binary '\0',60,'REGISTERED',5,3),(4,_binary '\0','2026-04-22',0.00,_binary '\0',0,'REGISTERED',6,4),(5,_binary '\0','2026-05-18',0.00,_binary '\0',0,'FAILED',7,5);
/*!40000 ALTER TABLE `training_participations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainings`
--

DROP TABLE IF EXISTS `trainings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainings` (
  `training_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `nbr_de_reservation` int DEFAULT NULL,
  `nbr_deplace` int DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `reward_enabled` bit(1) DEFAULT NULL,
  `reward_max` decimal(38,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` enum('PLANNED','ONGOING','FINISHED','CANCELLED') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `trainer` varchar(255) DEFAULT NULL,
  `club_club_id` bigint DEFAULT NULL,
  PRIMARY KEY (`training_id`),
  KEY `FKcoktf6qfxhhasf54k7b7a45pe` (`club_club_id`),
  CONSTRAINT `FKcoktf6qfxhhasf54k7b7a45pe` FOREIGN KEY (`club_club_id`) REFERENCES `clubs` (`club_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainings`
--

LOCK TABLES `trainings` WRITE;
/*!40000 ALTER TABLE `trainings` DISABLE KEYS */;
INSERT INTO `trainings` VALUES (1,'Master the basics of English: alphabet, greetings, numbers and simple sentences.','2026-03-01',30,0,49.99,_binary '\0',100.00,'2026-02-01','ONGOING','English for Absolute Beginners','4',7),(2,'Learn professional English for emails, meetings, presentations and negotiations.','2026-04-15',25,0,89.99,_binary '\0',100.00,'2026-02-15','ONGOING','Business English Communication','9',8),(3,'Deep dive into English grammar rules: tenses, conditionals, passive voice and more.','2026-05-01',20,0,69.99,_binary '\0',100.00,'2026-03-01','PLANNED','English Grammar Masterclass','4',9),(4,'Intensive preparation for the IELTS exam covering reading, writing, listening and speaking.','2026-04-10',15,0,129.99,_binary '\0',100.00,'2026-03-10','PLANNED','IELTS Preparation Course','9',7),(5,'Practice real-life English conversations: shopping, travel, health and social situations.','2026-06-01',20,0,59.99,_binary '\0',100.00,'2026-04-01','FINISHED','Conversational English for Daily Life','4',11);
/*!40000 ALTER TABLE `trainings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_user_id` int DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfahi57rejrndocy80o51sof0v` (`user_user_id`),
  KEY `FKtfwlfspv2h4wcgc9rjd1658a6` (`wallet_id`),
  CONSTRAINT `FKfahi57rejrndocy80o51sof0v` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKtfwlfspv2h4wcgc9rjd1658a6` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,750.00,'2026-06-06 13:43:02.636002','CREDIT',2,2),(2,400.00,'2026-06-06 13:43:07.040248','CREDIT',3,3),(3,400.00,'2026-06-06 13:43:08.550077','CREDIT',3,3),(4,400.00,'2026-06-06 13:43:13.854160','CREDIT',5,4),(5,200.00,'2026-06-06 13:43:21.749177','CREDIT',6,5);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `class_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `cv` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','STUDENT','TUTOR','EMPLOYE','COMPANY') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'2026-05-11 18:29:08.601543',NULL,'islem@esprit.tn','Islem','2026-06-08 01:31:11.448329','Raissi',NULL,'ADMIN','2026-06-08 01:31:11.520868'),(2,NULL,'2026-05-11 18:29:42.428143',NULL,'bouthayna@esprit.tn','bouthayna','2026-06-07 23:51:50.462965','hammami',NULL,'STUDENT','2026-06-07 23:51:50.464601'),(3,NULL,'2026-05-11 18:30:10.979554',NULL,'oussama@esprit.tn','Oussama','2026-05-12 20:24:38.283869','Hammami',NULL,'STUDENT','2026-05-12 20:24:38.284376'),(4,NULL,'2026-05-11 18:35:38.495559',NULL,'marwa@esprit.tn','Marwa','2026-06-08 01:50:12.727192','Chiabi',NULL,'TUTOR','2026-06-08 01:50:12.728177'),(5,NULL,'2026-05-11 20:06:38.105032',NULL,'amal@esprit.tn','amal',NULL,'elaffi',NULL,'STUDENT','2026-05-11 20:06:38.105032'),(6,NULL,'2026-05-11 20:07:10.814307',NULL,'yasmine@esprit.tn','Yasmin','2026-06-06 14:08:32.669020','Ben Jemaa',NULL,'STUDENT','2026-06-06 14:08:33.691388'),(7,NULL,'2026-05-11 20:07:34.533962',NULL,'hiba@esprit.tn','hiba',NULL,'jlassi',NULL,'STUDENT','2026-05-11 20:07:34.533962'),(8,NULL,'2026-05-11 20:08:25.700239',NULL,'shayma@esprit.tn','shayma','2026-06-08 01:49:22.595337','tlili',NULL,'EMPLOYE','2026-06-08 01:49:22.596314'),(9,NULL,'2026-05-11 20:09:02.128997',NULL,'zayneb@esprit.tn','zayneb','2026-06-06 00:24:29.431985','hammami',NULL,'TUTOR','2026-06-06 00:24:29.433129'),(10,NULL,'2026-05-12 20:33:40.867015',NULL,'saif@esprit.tn','saif','2026-06-08 01:42:46.796125','ben youssef',NULL,'COMPANY','2026-06-08 01:42:47.152312'),(11,NULL,'2026-06-06 00:53:30.508414',NULL,'sami@esprit.tn','Sami',NULL,'Ben Mohamed',NULL,'STUDENT','2026-06-06 00:53:30.508414'),(12,NULL,'2026-06-06 01:13:34.013202',NULL,'hbouthayna18@gmail.com','Bouthayna','2026-06-06 13:57:39.048126','Hammami',NULL,'COMPANY','2026-06-06 13:57:39.049668');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `balance` decimal(38,2) DEFAULT NULL,
  `user_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_24ivpqwa97c1x5904ab7sq875` (`user_user_id`),
  CONSTRAINT `FK9ej3krn622x8clop92d51jrmg` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
INSERT INTO `wallet` VALUES (1,0.00,7),(2,750.00,2),(3,800.00,3),(4,400.00,5),(5,200.00,6),(6,0.00,11);
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-08  3:20:23
