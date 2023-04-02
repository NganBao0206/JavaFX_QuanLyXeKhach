-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: oubus
-- ------------------------------------------------------
-- Server version	8.0.32

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
INSERT INTO `bustrip` VALUES ('05206786-8e36-403c-b5f0-ee5ffbcbbc99','DN-HN','2023-04-01 05:00:00',20,10.00),('292af818-dae2-4bb3-981e-13b236b8fd9b','DN-HN','2023-04-01 23:00:00',19,50.00),('abb71b30-e27d-4e63-ada2-94b5f6fca4d8','DN-HN','2023-04-01 05:00:00',19,0.00);
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
  `Phone` varchar(13) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Phone` (`Phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Thành phố Hồ Chí Minh'),(2,'Đã Nẵng'),(3,'Hà Nội');
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
INSERT INTO `route` VALUES ('DN-HCM',2,1,500.00,1440),('DN-HN',2,3,300.00,960),('HCM-DN',1,2,500.00,1440),('HCM-HN',1,3,700.00,2100),('HN-DN',3,2,300.00,960),('HN-HCM',3,1,700.00,2100);
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
INSERT INTO `seat` VALUES (1,'A01'),(2,'A02'),(3,'A03'),(4,'A04'),(5,'A05'),(6,'A06'),(7,'A08'),(8,'A09'),(9,'A10'),(10,'B01'),(11,'B02'),(12,'B03'),(13,'B04'),(14,'B05'),(15,'B06'),(16,'B07'),(17,'B08'),(18,'B09'),(19,'B10'),(20,'C01'),(21,'C02'),(22,'C03'),(23,'C04'),(24,'C05'),(25,'C06'),(26,'C07'),(27,'C08'),(28,'C09'),(29,'C10'),(30,'D01'),(31,'D02'),(32,'D03'),(33,'D04'),(34,'D05'),(35,'D06'),(36,'D07'),(37,'D08'),(38,'D09'),(39,'D10');
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
  `Status` enum('booked','purchased','empty') DEFAULT NULL,
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
INSERT INTO `user` VALUES ('6001e9b2-d5f5-477f-98f1-49fd1125e192','admin','$2a$10$vFiIebzaGuTXYb5vVblMk..v8hO39/NqUrXyfL4285rmcA4zANLfq','abc','admin');
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

-- Dump completed on 2023-04-02 12:33:26
