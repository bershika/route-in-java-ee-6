-- MySQL dump 10.13  Distrib 5.5.8, for linux2.6 (i686)
--
-- Host: localhost    Database: RouteDB
-- ------------------------------------------------------
-- Server version	5.5.8

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
-- Table structure for table `HUB`
--

DROP TABLE IF EXISTS `HUB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HUB` (
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `a1` float NOT NULL,
  `a2` float NOT NULL,
  `b1` float NOT NULL,
  `b2` float NOT NULL,
  `manualMode` tinyint(1) NOT NULL,
  PRIMARY KEY (`city`,`state`),
  KEY `locationKey` (`city`,`state`),
  CONSTRAINT `hub_locationConstraint` FOREIGN KEY (`city`, `state`) REFERENCES `LOCATION` (`city`, `state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HUB`
--

LOCK TABLES `HUB` WRITE;
/*!40000 ALTER TABLE `HUB` DISABLE KEYS */;
/*!40000 ALTER TABLE `HUB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LOCATION`
--

DROP TABLE IF EXISTS `LOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LOCATION` (
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `lan` float NOT NULL,
  `lng` float NOT NULL,
  PRIMARY KEY (`city`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LOCATION`
--

LOCK TABLES `LOCATION` WRITE;
/*!40000 ALTER TABLE `LOCATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `LOCATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEMBER`
--

DROP TABLE IF EXISTS `MEMBER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEMBER` (
  `email` varchar(255) NOT NULL,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEMBER`
--

LOCK TABLES `MEMBER` WRITE;
/*!40000 ALTER TABLE `MEMBER` DISABLE KEYS */;
INSERT INTO `MEMBER` VALUES ('john.smith@mailinator.com','John Smith');
/*!40000 ALTER TABLE `MEMBER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEMBER_HUBSERVICE`
--

DROP TABLE IF EXISTS `MEMBER_HUBSERVICE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEMBER_HUBSERVICE` (
  `city` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `rate` int(11) NOT NULL,
  `state` varchar(255) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`city`,`email`,`rate`,`state`),
  KEY `hubKey` (`city`,`state`),
  KEY `memeberKey` (`email`),
  CONSTRAINT `hubservice_memberConstraint` FOREIGN KEY (`email`) REFERENCES `MEMBER` (`email`),
  CONSTRAINT `hubservice_hubConstraint` FOREIGN KEY (`city`, `state`) REFERENCES `HUB` (`city`, `state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEMBER_HUBSERVICE`
--

LOCK TABLES `MEMBER_HUBSERVICE` WRITE;
/*!40000 ALTER TABLE `MEMBER_HUBSERVICE` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEMBER_HUBSERVICE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POINT`
--

DROP TABLE IF EXISTS `POINT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `POINT` (
  `destName` varchar(255) NOT NULL,
  `destState` varchar(255) NOT NULL,
  `hubName` varchar(255) NOT NULL,
  `hubState` varchar(255) NOT NULL,
  `createdDate` datetime DEFAULT NULL,
  `fake` tinyint(1) NOT NULL,
  `rate` float NOT NULL,
  PRIMARY KEY (`destName`,`destState`,`hubName`,`hubState`, `rate`),
  CONSTRAINT `point_routeConstraint` FOREIGN KEY (`hubName`, `hubState`, `destName`, `destState`) REFERENCES `ROUTE` (`hubName`, `hubState`, `destName`, `destState`),
   CONSTRAINT `point_hubConstraint` FOREIGN KEY (`hubName`, `hubState`) REFERENCES `HUB` (`city`, `state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POINT`
--

LOCK TABLES `POINT` WRITE;
/*!40000 ALTER TABLE `POINT` DISABLE KEYS */;
/*!40000 ALTER TABLE `POINT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROUTE`
--

DROP TABLE IF EXISTS `ROUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROUTE` (
  `destName` varchar(255) NOT NULL,
  `destState` varchar(255) NOT NULL,
  `hubName` varchar(255) NOT NULL,
  `hubState` varchar(255) NOT NULL,
  `distance` int(11) NOT NULL,
  `encPoints` text DEFAULT NULL,
  PRIMARY KEY (`destName`,`destState`,`hubName`,`hubState`),
  KEY `hubKey` (`hubName`,`hubState`),
  KEY `destKey` (`destName`,`destState`),
  CONSTRAINT `route_destConstraint` FOREIGN KEY (`destName`, `destState`) REFERENCES `LOCATION` (`city`, `state`),
  CONSTRAINT `route_hubConstraint` FOREIGN KEY (`hubName`, `hubState`) REFERENCES `HUB` (`city`, `state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROUTE`
--

LOCK TABLES `ROUTE` WRITE;
/*!40000 ALTER TABLE `ROUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `ROUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SURCHARGE`
--

DROP TABLE IF EXISTS `SURCHARGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SURCHARGE` (
  `email` varchar(255) NOT NULL,
  `value` float NOT NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `memberConstrain` FOREIGN KEY (`email`) REFERENCES `MEMBER` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SURCHARGE`
--

LOCK TABLES `SURCHARGE` WRITE;
/*!40000 ALTER TABLE `SURCHARGE` DISABLE KEYS */;
/*!40000 ALTER TABLE `SURCHARGE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

DROP TABLE IF EXISTS `SYNONYM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SYNONYM` (
  `city` varchar(255) NOT NULL,
  `synonym` varchar(255) NOT NULL,
  PRIMARY KEY (`synonym`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
