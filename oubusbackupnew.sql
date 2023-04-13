-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: oubus
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bus` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `LicensePlates` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus`
--

LOCK TABLES `bus` WRITE;
/*!40000 ALTER TABLE `bus` DISABLE KEYS */;
INSERT INTO `bus` VALUES (19,'51B-123.43'),(20,'51B-432.33'),(21,'51B-124.43'),(22,'51K-213.65'),(23,'51K-341.34'),(24,'51K-245.31'),(25,'51D-165.32'),(26,'51D-268.21'),(27,'51D-225.34'),(28,'51A-241.23'),(29,'51A-734.43'),(30,'51A-154.78');
/*!40000 ALTER TABLE `bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bustrip`
--

DROP TABLE IF EXISTS `bustrip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bustrip` (
  `ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `RouteID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DepartureTime` datetime NOT NULL,
  `BusID` int NOT NULL,
  `Surcharge` decimal(15,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `RouteID` (`RouteID`),
  KEY `BusID` (`BusID`),
  CONSTRAINT `bustrip_ibfk_1` FOREIGN KEY (`RouteID`) REFERENCES `route` (`ID`),
  CONSTRAINT `bustrip_ibfk_2` FOREIGN KEY (`BusID`) REFERENCES `bus` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bustrip`
--

LOCK TABLES `bustrip` WRITE;
/*!40000 ALTER TABLE `bustrip` DISABLE KEYS */;
INSERT INTO `bustrip` VALUES ('551688bf-6916-461a-a30b-332bdd13e019','4509ecc9-59d1-4c42-b0bb-de9252816090','2023-04-08 05:00:00',19,25.00),('6685d656-877b-4644-a175-0fa64cf4d95b','6daf7025-39f3-4a5f-90c0-4061b0f90f35','2023-04-13 05:00:00',26,10.00),('7f96bdcb-717d-457b-afc3-54456f945278','ae97bacd-5536-4b26-bd67-51b82b08e9c0','2023-04-12 11:00:00',28,0.00),('88dc0f47-126e-4174-af37-ad0781d6c3f7','4509ecc9-59d1-4c42-b0bb-de9252816090','2023-04-11 23:00:00',22,10.00),('d4d6aae4-a6e5-4f0a-91da-ebb33f69e369','6daf7025-39f3-4a5f-90c0-4061b0f90f35','2023-04-29 05:00:00',22,0.00),('dedc8059-1af4-4c76-a32e-aa7d884c38a8','227fdaeb-425a-43dc-a2c8-35736a2edcf9','2023-04-11 16:22:00',21,1.00);
/*!40000 ALTER TABLE `bustrip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Phone` varchar(13) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('21f396fb-bcb0-46d8-bc7f-e898921d7f47','Ngân','123'),('5404ee15-4d96-471d-9b7a-d3f8a90c3d11','Ngân Bảo','12345'),('aaf4cde9-2401-422f-9711-66579c9e5578','Võ Phú Phát','456');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Thành phố Hồ Chí Minh'),(2,'Đà Nẵng'),(3,'Hà Nội'),(4,'Long An'),(5,'Cà Mau');
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `route`
--

DROP TABLE IF EXISTS `route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `route` (
  `ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DepartureID` int NOT NULL,
  `DestinationID` int NOT NULL,
  `Price` decimal(15,2) NOT NULL,
  `TotalTime` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `DepartureID` (`DepartureID`),
  KEY `DestinationID` (`DestinationID`),
  CONSTRAINT `route_ibfk_1` FOREIGN KEY (`DepartureID`) REFERENCES `location` (`ID`),
  CONSTRAINT `route_ibfk_2` FOREIGN KEY (`DestinationID`) REFERENCES `location` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route`
--

LOCK TABLES `route` WRITE;
/*!40000 ALTER TABLE `route` DISABLE KEYS */;
INSERT INTO `route` VALUES ('0a7ebf40-0ee8-48a4-8b14-572068fe01b2',2,3,300.00,1200),('227fdaeb-425a-43dc-a2c8-35736a2edcf9',2,4,450.00,980),('4509ecc9-59d1-4c42-b0bb-de9252816090',1,2,500.00,1050),('4e27844f-4a13-4080-9ea8-5f38473b7c94',3,4,680.00,1900),('619780e1-61ce-4006-affc-82ed12b28956',4,5,350.00,600),('6daf7025-39f3-4a5f-90c0-4061b0f90f35',1,4,150.00,150),('7a58517f-34fb-4743-afe4-49e7b5cfeeda',2,5,750.00,2500),('7f7a9168-776c-425a-9112-806ec6318d9e',3,5,850.00,2800),('ae97bacd-5536-4b26-bd67-51b82b08e9c0',1,5,310.00,540),('e11c63d2-beec-488e-a78d-29c89ed1d52c',1,3,700.00,2100);
/*!40000 ALTER TABLE `route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES (1,'A01'),(2,'A02'),(3,'A03'),(4,'A04'),(5,'A05'),(6,'A06'),(7,'A07'),(10,'A08'),(11,'B01'),(12,'B02'),(13,'B03'),(14,'B04'),(15,'B05'),(16,'B06'),(17,'B07'),(18,'B08'),(20,'C01'),(21,'C02'),(22,'C03'),(23,'C04'),(24,'C05'),(25,'C06'),(26,'C07'),(27,'C08'),(28,'D01'),(30,'D02'),(31,'D03'),(32,'D04'),(33,'D05'),(34,'D06'),(35,'D07'),(36,'D08');
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CustomerID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `BusTripID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `SeatID` int NOT NULL,
  `StaffID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Status` enum('booked','purchased','empty') NOT NULL,
  `TicketPrice` decimal(15,2) NOT NULL,
  `Time` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `CustomerID` (`CustomerID`),
  KEY `BusTripID` (`BusTripID`),
  KEY `SeatID` (`SeatID`),
  KEY `StaffID` (`StaffID`),
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`ID`),
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`BusTripID`) REFERENCES `bustrip` (`ID`),
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`SeatID`) REFERENCES `seat` (`ID`),
  CONSTRAINT `ticket_ibfk_4` FOREIGN KEY (`StaffID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES ('6ef517f0-a4eb-47a4-9f60-b83a8920e822','5404ee15-4d96-471d-9b7a-d3f8a90c3d11','d4d6aae4-a6e5-4f0a-91da-ebb33f69e369',23,'9e6dfb03-d9d2-46c9-aeae-2b0236c2099c','purchased',150.00,'2023-04-12 15:36:46');
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `ID` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `UserRole` enum('staff','admin') DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('0f77b858-3b2a-41d3-9921-d174f8600433','abc','$2a$10$7FknVd8C1A9r1HEiu/IZt.cXDpNHaqG0SxwYHFb70Rtas5paNbLzK','Vo Phu Phat','staff'),('6001e9b2-d5f5-477f-98f1-49fd1125e192','admin','$2a$10$mm.zF8smvQPy3U6BAOv7/etUebULnNGbtRltQS9vooa8OoM3cV4t.','abc','admin'),('9e6dfb03-d9d2-46c9-aeae-2b0236c2099c','phat','$2a$10$RDMxdSeUnVwvy.tZ/8361.SnplENj3znF5z7VJgJK/Av/s112Lygy','Võ Phú Phát','staff'),('b7dbc023-3957-45eb-a10b-d6835543a394','lalla','$2a$10$vwX6GDufVRdbNen/UgqA6..6yRIw/sn7vwZL7mSaUdiYlwqn/TRSi','BinBin','staff'),('c8ecad89-d275-46f4-b852-b416aa018e52','nganaa','$2a$10$7WjzhCRvWOBMUS7vw2t5m.RqEQuqTTDPbFXQTG2okCO17pTfrWww.','abce','staff');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-13 10:26:02
