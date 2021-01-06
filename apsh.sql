-- MySQL dump 10.13  Distrib 8.0.21, for Linux (x86_64)
--
-- Host: localhost    Database: apsh
-- ------------------------------------------------------
-- Server version	8.0.21

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
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('1','peihongchen','classpath:/db/changelog/db.changelog-master.yaml','2020-11-11 06:46:38',1,'EXECUTED','8:9c611803af8a94b91872afda8b87a6ba','createTable tableName=shifts; createTable tableName=human_resources; createTable tableName=equipment_resources; createTable tableName=orders','',NULL,'3.8.9',NULL,NULL,'5077198173'),('2','bomouhe','classpath:/db/changelog/db.changelog-master.yaml','2020-11-11 06:46:38',2,'EXECUTED','8:bf5d457957d3d1588833e071b29c1b08','createTable tableName=order_production; createTable tableName=suborder_production','',NULL,'3.8.9',NULL,NULL,'5077198173'),('3','bomouhe','classpath:/db/changelog/db.changelog-master.yaml','2020-11-11 06:46:38',3,'EXECUTED','8:edde5a8ed870c5e11fb4acc0ccde3751','addColumn tableName=suborder_production','',NULL,'3.8.9',NULL,NULL,'5077198173'),('4','peihongchen','classpath:/db/changelog/db.changelog-master.yaml','2020-11-11 06:46:38',4,'EXECUTED','8:c6f8cdcbefeaf5e60fc5294497c60e70','insert tableName=shifts; insert tableName=shifts; insert tableName=shifts','Add shift data to db',NULL,'3.8.9',NULL,NULL,'5077198173');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,_binary '\0',NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_resources`
--

DROP TABLE IF EXISTS `equipment_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_resources` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `count` int NOT NULL,
  `weekly_schedule` varchar(20) NOT NULL,
  `daily_schedule` int NOT NULL,
  `is_deleted` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `er_daily_schedule_shifts` (`daily_schedule`),
  CONSTRAINT `er_daily_schedule_shifts` FOREIGN KEY (`daily_schedule`) REFERENCES `shifts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_resources`
--

LOCK TABLES `equipment_resources` WRITE;
/*!40000 ALTER TABLE `equipment_resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `human_resources`
--

DROP TABLE IF EXISTS `human_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `human_resources` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) NOT NULL,
  `group_size` int NOT NULL,
  `weekly_schedule` varchar(20) NOT NULL,
  `daily_schedule` int NOT NULL,
  `is_deleted` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_name` (`group_name`),
  KEY `hr_daily_schedule_shifts` (`daily_schedule`),
  CONSTRAINT `hr_daily_schedule_shifts` FOREIGN KEY (`daily_schedule`) REFERENCES `shifts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `human_resources`
--

LOCK TABLES `human_resources` WRITE;
/*!40000 ALTER TABLE `human_resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `human_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_production`
--

DROP TABLE IF EXISTS `order_production`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_production` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_production`
--

LOCK TABLES `order_production` WRITE;
/*!40000 ALTER TABLE `order_production` DISABLE KEYS */;
INSERT INTO `order_production` VALUES (10,'764098'),(11,'762838'),(12,'417830'),(13,'414837'),(14,'762485'),(15,'764310'),(16,'764104'),(17,'417174');
/*!40000 ALTER TABLE `order_production` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `delivery_date` date NOT NULL,
  `product_count` int NOT NULL,
  `is_deleted` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shifts`
--

DROP TABLE IF EXISTS `shifts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shifts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shifts`
--

LOCK TABLES `shifts` WRITE;
/*!40000 ALTER TABLE `shifts` DISABLE KEYS */;
INSERT INTO `shifts` VALUES (1,'早班','07:00:00','19:00:00'),(2,'晚班','19:00:00','07:00:00'),(3,'全天','00:00:00','23:59:59');
/*!40000 ALTER TABLE `shifts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suborder_production`
--

DROP TABLE IF EXISTS `suborder_production`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suborder_production` (
  `id` int NOT NULL AUTO_INCREMENT,
  `suborder_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `manpower_ids` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_production_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_production_suborder_production` (`order_production_id`),
  CONSTRAINT `order_production_suborder_production` FOREIGN KEY (`order_production_id`) REFERENCES `order_production` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=409 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suborder_production`
--

LOCK TABLES `suborder_production` WRITE;
/*!40000 ALTER TABLE `suborder_production` DISABLE KEYS */;
INSERT INTO `suborder_production` VALUES (37,'764098 3','2018-11-07 05:00:00','2018-11-07 11:00:00','40组-高燕（5）','line351',10),(38,'764098 1','2018-11-06 23:00:00','2018-11-07 05:00:00','40组-高燕（5）','line350',10),(39,'764098 4','2018-11-07 17:00:00','2018-11-07 23:00:00','15组-李娟（5）','line352',10),(40,'764098 0','2018-11-07 11:00:00','2018-11-07 17:00:00','15组-李娟（5）','line350',10),(41,'764098 2','2018-11-07 05:00:00','2018-11-07 11:00:00','26组-杨  丽（5）','line350',10),(42,'764098 5','2018-11-07 23:00:00','2018-11-08 05:00:00','26组-杨  丽（5）','line350',10),(43,'762838 0','2018-11-07 11:00:00','2018-11-07 14:00:00','30组-陈  梅(4)','line430',11),(44,'417830 0','2018-11-06 23:00:00','2018-11-07 05:00:00','10组-陈  云（5）','line060',12),(45,'417830 1','2018-11-07 05:00:00','2018-11-07 11:00:00','10组-陈  云（5）','line061',12),(46,'417830 3','2018-11-07 05:00:00','2018-11-07 11:00:00','27组-徐燕(5)','line060',12),(47,'417830 2','2018-11-06 23:00:00','2018-11-07 05:00:00','26组-杨  丽（5）','line062',12),(48,'417830 4','2018-11-06 23:00:00','2018-11-07 00:00:00','27组-徐燕(5)','line061',12),(49,'414837 1','2018-11-07 05:00:00','2018-11-07 11:00:00','39组-刘  霞（3）,4组-赵勤（3）','line110',13),(50,'414837 2','2018-11-08 05:00:00','2018-11-08 11:00:00','24组-张 娟1（3）,39组-刘  霞（3）','line111',13),(51,'414837 3','2018-11-08 23:00:00','2018-11-09 05:00:00','24组-张 娟1（3）,39组-刘  霞（3）','line110',13),(52,'414837 4','2018-11-09 05:00:00','2018-11-09 07:00:00','24组-张 娟1（3）,39组-刘  霞（3）','line110',13),(53,'414837 0','2018-11-07 23:00:00','2018-11-08 05:00:00','24组-张 娟1（3）,39组-刘  霞（3）','line110',13),(54,'762485 2','2018-11-08 05:00:00','2018-11-08 11:00:00','26组-杨  丽（5）','line351',14),(55,'762485 1','2018-11-08 17:00:00','2018-11-08 23:00:00','15组-李娟（5）','line350',14),(56,'762485 3','2018-11-07 23:00:00','2018-11-08 05:00:00','27组-徐燕(5)','line352',14),(57,'762485 6','2018-11-07 23:00:00','2018-11-08 05:00:00','40组-高燕（5）','line353',14),(58,'762485 7','2018-11-08 05:00:00','2018-11-08 07:00:00','27组-徐燕(5)','line350',14),(59,'762485 0','2018-11-08 11:00:00','2018-11-08 17:00:00','15组-李娟（5）','line351',14),(60,'762485 5','2018-11-09 05:00:00','2018-11-09 11:00:00','26组-杨  丽（5）','line351',14),(61,'762485 4','2018-11-08 23:00:00','2018-11-09 05:00:00','26组-杨  丽（5）','line350',14),(62,'764310 0','2018-11-06 23:00:00','2018-11-07 05:00:00','11组-张  娟（4）','line140',15),(63,'764310 1','2018-11-07 05:00:00','2018-11-07 07:00:00','11组-张  娟（4）','line140',15),(64,'764104 0','2018-11-07 05:00:00','2018-11-07 11:00:00','24组-张 娟1（3）','line141',16),(65,'764104 2','2018-11-07 11:00:00','2018-11-07 15:00:00','23组-吴凤（4）','line143',16),(66,'764104 1','2018-11-07 11:00:00','2018-11-07 17:00:00','1组-彭慧 (5)','line142',16),(67,'417174 1','2018-11-06 23:00:00','2018-11-07 05:00:00','36组-谢霞(4)','line331',17),(68,'417174 3','2018-11-06 23:00:00','2018-11-07 05:00:00','4组-赵勤（3）,7组-黄娣（4）','line333',17),(69,'417174 5','2018-11-07 05:00:00','2018-11-07 11:00:00','13组-刘燕（3）,14组-周  清（4）','line330',17),(70,'417174 0','2018-11-06 23:00:00','2018-11-07 05:00:00','13组-刘燕（3）,14组-周  清（4）','line330',17),(71,'417174 2','2018-11-06 23:00:00','2018-11-07 05:00:00','24组-张 娟1（3）,39组-刘  霞（3）','line332',17),(72,'417174 4','2018-11-06 23:00:00','2018-11-07 05:00:00','8组-张萍（4）','line340',17);
/*!40000 ALTER TABLE `suborder_production` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-05 13:50:02
