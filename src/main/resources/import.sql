-- You can use this file to load seed data into the database using SQL statements
insert into MEMBER (name, email) values ('Anna', 'bershika@gmail.com')
insert into SURCHARGE (email, value) values ('bershika@gmail.com', 25)
insert into LOCATION(city,state) values ('Seattle', 'WA')
insert into LOCATION(city,state, lan, lng) values ('Kent', 'WA', 0, 0)
insert into HUB(city,state) values ('Seattle', 'WA')
insert into LOCATION(city,state, lan, lng) values ('Dallas', 'TX', 0, 0)
insert into HUB(city,state, a1, a2, b1, b2, manualMode) values ('Dallas', 'TX', 0, 0, 0, 0, false)
insert into MEMBER_HUBSERVICE(city,state, rate, email) values ('Dallas', 'TX', 1234, 'bershika@gmail.com')
insert into ROUTE(hubName,hubState, destName, destState, distance) values ('Seattle', 'WA', 'Kent', 'WA', 13)
insert into POINT(hubName,hubState, destName, destState, rate, fake) values ('Seattle', 'WA', 'Kent', 'WA', 23, false)

select * from SYNONYM;

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
