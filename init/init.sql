CREATE DATABASE  IF NOT EXISTS `db_applaudo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_applaudo`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: db_applaudo
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip_code` varchar(5) DEFAULT NULL,
  `user_sid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1fiah5alda6b4bnxo9cynld4q` (`user_sid`),
  CONSTRAINT `FK1fiah5alda6b4bnxo9cynld4q` FOREIGN KEY (`user_sid`) REFERENCES `users` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'Lejamani','Honduras',_binary '\0','Comayagua',_binary '','Barrio La Cruz','12101','5109fedb-5c1f-4144-9593-4836c8eecbee'),(2,'Testing 34','Honduras',_binary '','La Paz',_binary '','Ave. Testing 34','11303','5109fedb-5c1f-4144-9593-4836c8eecbee'),(3,'Comayagua','Honduras',_binary '\0','Comayagua',_binary '','Barrio Torondon','12101','9bd24c7d-aacc-4477-91fe-a8ad9ed45e44'),(4,'Comayagua','Honduras',_binary '','Comayagua',_binary '','Barrio Cabañas','12101','9bd24c7d-aacc-4477-91fe-a8ad9ed45e44');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item_session`
--

DROP TABLE IF EXISTS `cart_item_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item_session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `user_sid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mg6n2fkr11ilh9sjtwr6o7ck` (`product_id`),
  KEY `FK9v2nqw455a6t8u4mp4gjattvv` (`user_sid`),
  CONSTRAINT `FK1mg6n2fkr11ilh9sjtwr6o7ck` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK9v2nqw455a6t8u4mp4gjattvv` FOREIGN KEY (`user_sid`) REFERENCES `users` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item_session`
--

LOCK TABLES `cart_item_session` WRITE;
/*!40000 ALTER TABLE `cart_item_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_item_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime(6) NOT NULL,
  `status` bit(1) DEFAULT NULL,
  `tracking_num` varchar(255) DEFAULT NULL,
  `address_id` bigint NOT NULL,
  `payment_id` bigint NOT NULL,
  `user_sid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf5464gxwc32ongdvka2rtvw96` (`address_id`),
  KEY `FK8aol9f99s97mtyhij0tvfj41f` (`payment_id`),
  KEY `FKddwqq2wa2ylp6c5hqjbmn4mxm` (`user_sid`),
  CONSTRAINT `FK8aol9f99s97mtyhij0tvfj41f` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
  CONSTRAINT `FKddwqq2wa2ylp6c5hqjbmn4mxm` FOREIGN KEY (`user_sid`) REFERENCES `users` (`sid`),
  CONSTRAINT `FKf5464gxwc32ongdvka2rtvw96` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2023-01-06 03:36:34.601000',_binary '','0c9fc7dc-bb9d-4cb9-ac0c-89a2240d39af',1,1,'5109fedb-5c1f-4144-9593-4836c8eecbee'),(2,'2023-01-06 03:42:23.025000',_binary '\0','d46ca070-58d9-4c22-ba86-a853413597b2',4,6,'9bd24c7d-aacc-4477-91fe-a8ad9ed45e44');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders_detail`
--

DROP TABLE IF EXISTS `orders_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `unit_price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgic892mhh720sx9wmoq4cjtgp` (`order_id`),
  KEY `FKg9ar77rwmbwbablvscd6qt8hh` (`product_id`),
  CONSTRAINT `FKg9ar77rwmbwbablvscd6qt8hh` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKgic892mhh720sx9wmoq4cjtgp` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_detail`
--

LOCK TABLES `orders_detail` WRITE;
/*!40000 ALTER TABLE `orders_detail` DISABLE KEYS */;
INSERT INTO `orders_detail` VALUES (1,893.32,4,1,1),(2,893.32,1,2,1),(3,89.32,1,2,3);
/*!40000 ALTER TABLE `orders_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_type`
--

DROP TABLE IF EXISTS `payment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_type`
--

LOCK TABLES `payment_type` WRITE;
/*!40000 ALTER TABLE `payment_type` DISABLE KEYS */;
INSERT INTO `payment_type` VALUES (1,'CREDIT CARD');
/*!40000 ALTER TABLE `payment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cc_expiration_date` varchar(255) DEFAULT NULL,
  `cc_number` varchar(19) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `type_id` bigint NOT NULL,
  `user_sid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk2www4i4mkkv6fyohe2seoojc` (`type_id`),
  KEY `FKbg7lvu7nudutd3hb9i6abaegx` (`user_sid`),
  CONSTRAINT `FKbg7lvu7nudutd3hb9i6abaegx` FOREIGN KEY (`user_sid`) REFERENCES `users` (`sid`),
  CONSTRAINT `FKk2www4i4mkkv6fyohe2seoojc` FOREIGN KEY (`type_id`) REFERENCES `payment_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,'5/26','5425233430109903',_binary '\0','MASTERCARD',_binary '',1,'5109fedb-5c1f-4144-9593-4836c8eecbee'),(2,'5/25','4917484589897107',_binary '\0','VISA',_binary '',1,'5109fedb-5c1f-4144-9593-4836c8eecbee'),(3,'2/25','2222420000001113',_binary '\0','MASTERCARD',_binary '',1,'5109fedb-5c1f-4144-9593-4836c8eecbee'),(4,'2/25','4007702835532454',_binary '','VISA',_binary '',1,'5109fedb-5c1f-4144-9593-4836c8eecbee'),(5,'3/25','4263982640269299',_binary '\0','VISA',_binary '',1,'9bd24c7d-aacc-4477-91fe-a8ad9ed45e44'),(6,'3/24','6250941006528599',_binary '','CHINA_UNION_PAY',_binary '',1,'9bd24c7d-aacc-4477-91fe-a8ad9ed45e44');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `product_name` varchar(50) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `stock` int NOT NULL,
  `unit_price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Edicion UEFA Champions League','Camisa Real Madrid',_binary '',7,893.32),(2,'Edicion La Liga','Camisa Real Madrid',_binary '\0',12,813.42),(3,'Calcetas niño','Calcetas Red-Point',_binary '',3,89.32),(4,'MousePad Pequeño','Mosuepad Logitech',_binary '\0',0,50.32),(5,'Edicion Joma.','Pulsera',_binary '',6,38.42);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `sid` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('5109fedb-5c1f-4144-9593-4836c8eecbee','chrismartinez@outlook.es','Cristhian Josue','Martinez Lara',_binary '','','chrisjosuel'),('9bd24c7d-aacc-4477-91fe-a8ad9ed45e44','maria.fernanda@gmail.com','Maria Fernanda','Martinez Lara',_binary '','','maria.lara');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-06 12:42:44
