CREATE DATABASE  IF NOT EXISTS `disaster` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `disaster`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: disaster
-- ------------------------------------------------------
-- Server version	5.6.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `lat` float DEFAULT NULL,
  `lon` float DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `help_id` varchar(45) DEFAULT NULL,
  `message` varchar(150) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `active` binary(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'m1',NULL,21,105,1,NULL,NULL,'hanoi','2014-05-09 17:59:53',NULL),(2,'m2',NULL,21,105,1,NULL,NULL,'hue','2014-05-09 17:30:39',NULL),(3,'m3',NULL,21.0056,105.805,1,NULL,NULL,'HaNoi','2014-05-09 17:31:07',NULL),(4,'m4',NULL,21,105,1,NULL,NULL,'hue','2014-05-09 17:32:23',NULL),(5,'m5',NULL,21,105,1,NULL,NULL,'hue','2014-05-09 17:32:29',NULL),(6,'m6',NULL,21,105,1,NULL,NULL,'hue','2014-05-09 17:32:36',NULL),(7,'m7',NULL,21.0056,105.805,1,NULL,NULL,'HaNoi','2014-05-10 13:33:16',NULL),(8,'m8',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'m9',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(10,'hung',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,'A4B3EE9A41534225',NULL,21.0056,105.805,1,NULL,NULL,'HaNoi','2014-05-10 13:01:12',NULL),(12,'7CB595DA0A0C98C8',NULL,0,0,1,NULL,NULL,'HaNoi','2014-05-10 12:46:53',NULL),(13,'44E1246855A56AA8',NULL,21.0055,105.805,1,NULL,NULL,'HaNoi','2014-05-10 12:56:21',NULL),(14,'2488452271AA0200',NULL,21.0056,105.805,1,NULL,NULL,'HaNoi','2014-05-10 14:57:16',NULL),(15,'7105E46594976A91',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,'D38029011E5479EA',NULL,21.0056,105.805,1,NULL,NULL,'HaNoi','2014-05-10 14:57:23',NULL),(17,'CB559D68CA4C9D4A',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(18,'ABCB4760326731DB',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(19,'7E9BDBE264C09179',NULL,21.0056,105.805,1,NULL,NULL,'HaNoi','2014-05-10 15:33:56',NULL),(20,'5913767B04D2DDC4',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,'C6A168086E0AB5C1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(22,'CBAD748BA8CE56ED',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(23,'EBBC82783BE34172',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(24,'9B2A2E5C7D26ED02',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(25,'5779C3C34EEB0D32',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(26,'52C3714B2E948DB8',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(27,'E391779BE522043B',NULL,21.0056,105.805,0,NULL,NULL,'HaNoi','2014-05-10 16:54:37',NULL),(28,'04AAB90D2369737B',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(29,'2E0CDBD582C35E12',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(30,'05267D2A3D155949',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cities` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(45) DEFAULT NULL,
  `IsDanger` int(11) DEFAULT '0',
  `last_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`index`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (4,'hanoi',1,'2014-05-10 16:54:37'),(5,'hue',1,'2014-05-09 18:00:03');
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_disaster`
--

DROP TABLE IF EXISTS `custom_disaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_disaster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dtype` int(11) DEFAULT NULL,
  `lat` float DEFAULT NULL,
  `lon` float DEFAULT NULL,
  `radius` float DEFAULT NULL,
  `time` float DEFAULT NULL,
  `description` longtext,
  `last_time` timestamp NULL DEFAULT NULL,
  `archived` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_disaster`
--

LOCK TABLES `custom_disaster` WRITE;
/*!40000 ALTER TABLE `custom_disaster` DISABLE KEYS */;
INSERT INTO `custom_disaster` VALUES (1,0,21,105,5,1,'chay rat to','2014-05-10 05:19:06',0);
/*!40000 ALTER TABLE `custom_disaster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_friend`
--

DROP TABLE IF EXISTS `user_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_friend` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `fid` int(11) NOT NULL,
  PRIMARY KEY (`index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_friend`
--

LOCK TABLES `user_friend` WRITE;
/*!40000 ALTER TABLE `user_friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work`
--

DROP TABLE IF EXISTS `work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `hours` decimal(5,2) DEFAULT '0.00',
  `date` date DEFAULT NULL,
  `archived` int(1) DEFAULT '0',
  `description` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work`
--

LOCK TABLES `work` WRITE;
/*!40000 ALTER TABLE `work` DISABLE KEYS */;
INSERT INTO `work` VALUES (2,1.00,'2014-12-12',0,'12323');
/*!40000 ALTER TABLE `work` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-11  0:26:33
