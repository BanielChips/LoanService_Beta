-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: ip_access_project
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `devices`
--
DROP DATABASE IF EXISTS ip_access_project_db;
CREATE DATABASE ip_access_project_db;
USE ip_access_project_db;
DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `devices` (
  `deviceID` int NOT NULL AUTO_INCREMENT,
  `deviceType` ENUM('LAPTOP', 'TABLET', 'PHONE', 'HOTSPOT') NOT NULL,
  `deviceStatus` ENUM('ACTIVE', 'INACTIVE', 'LOANED', 'DAMAGED') NOT NULL,
  `availability` tinyint(1) NOT NULL DEFAULT '1',
  `renterID` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`deviceID`),
  KEY `fk_renter` (`renterID`),
  CONSTRAINT `fk_renter` FOREIGN KEY (`renterID`) REFERENCES `users` (`userID`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devices`
--

LOCK TABLES `devices` WRITE;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
INSERT INTO `devices` VALUES (46,'Laptop A','ACTIVE', 1,NULL,'2025-03-16 18:09:26'),(47,'Laptop B','ACTIVE',0,NULL,'2025-03-16 18:09:26'),(48,'Projector A','ACTIVE',1,NULL,'2025-03-16 18:09:26'),(49,'Tablet A','ACTIVE',1,NULL,'2025-03-16 18:09:26'),(50,'Smartphone B','ACTIVE',0,NULL,'2025-03-16 18:09:26'),(51,'Laptop','ACTIVE',1,5,'2025-03-17 01:26:31'),(52,'Laptop','ACTIVE',1,5,'2025-03-17 01:28:53'),(53,'Laptop','ACTIVE',1,5,'2025-03-17 01:29:50');
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loaninformation`
--

DROP TABLE IF EXISTS `loaninformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loaninformation` (
  `loanID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `deviceId` int NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `loanStatus` enum('ACTIVE', 'OVERDUE', 'RESERVED', 'REVIEW') not null,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`loanID`),
  KEY `fk_loan_user` (`userID`),
  KEY `fk_loan_device` (`deviceId`),
  CONSTRAINT `fk_loan_device` FOREIGN KEY (`deviceId`) REFERENCES `devices` (`deviceID`),
  CONSTRAINT `fk_loan_user` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loaninformation`
--

LOCK TABLES `loaninformation` WRITE;
/*!40000 ALTER TABLE `loaninformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `loaninformation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `zipCode` varchar(10) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
    `password` varchar(30) NOT NULL,
  `role` ENUM('USER','ADMIN') not null,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (4,'John','Doe','12345','john.doe@email.com','password', 'user','6666666666','2025-03-16 18:06:19')/*(5,'Jane','Smith','67890','jane.smith@email.com','pass','USER',555-5678,'2025-03-16 18:06:19'),(7,'Bob','Williams','11223','bob.williams@email.com','rah','USER',555-2468,'2025-03-16 18:06:19'),(9,'bobby','person','11223','bobbyp@email.com','rahhh','USER','1112223333','2025-03-16 23:41:06'),(11,'donald','duck','11223','donaldD@email.com','aerre','USER','1112223333','2025-03-17 00:25:00'),(13,'daffy','duck',NULL,'daffyD@email.com','bor','USER','2223334444','2025-03-17 00:36:14'),(15,'will','weaton',NULL,'will@email.com','gyah','USER','2223334444','2025-03-17 00:42:37'),(16,'alan','david','22334','alanD@email.com','brooo','1112224444','2025-03-17 00:44:30'),(17,'miller','lee','33445','miller@email.com','3334446666','2025-03-17 00:49:54'),(18,'cat','fish','11223','catfish@email.com','3334445555','2025-03-17 02:06:38');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE alerts (
                        alertID int AUTO_INCREMENT PRIMARY KEY,
                        alertName VARCHAR(255) NOT NULL,
                        alertBody TEXT NOT NULL,
                        timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        alertType ENUM(
                            'NEW_OVERDUE_DEVICE',
                            'ANNOUNCEMENT',
                            'DEVICE_REPAIR_STATUS_UPDATE',
                            'NEW_DEVICE_REQUEST',
                            'DEVICE_RETURNED',
                            'LOAN_APPROVAL_REQUIRED',
                            'SYSTEM_MAINTENANCE',
                            'URGENT_ACTION_REQUIRED',
                            'NOTIFICATION'
                            ) NOT NULL,
                        alertPriority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
                        isRead BOOLEAN NOT NULL DEFAULT FALSE,
                        expiryDate DATETIME NULL
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `alerts` WRITE;
/*!40000 ALTER TABLE `alerts` DISABLE KEYS */;
/*!40000 ALTER TABLE `alerts` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- Dump completed on 2025-03-16 22:35:49
