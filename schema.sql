-- --------------------------------------------------------
-- Host:                         172.16.0.200
-- Server version:               10.5.5-MariaDB-1:10.5.5+maria~eoan - mariadb.org binary distribution
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for paradise_profilerdaemon
CREATE DATABASE IF NOT EXISTS `paradise_profilerdaemon` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `paradise_profilerdaemon`;

-- Dumping structure for table paradise_profilerdaemon.procs
CREATE TABLE IF NOT EXISTS `procs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `procpath` varchar(512) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `procpath` (`procpath`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table paradise_profilerdaemon.samples
CREATE TABLE IF NOT EXISTS `samples` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roundId` int(11) NOT NULL,
  `sampleTime` datetime NOT NULL DEFAULT current_timestamp(),
  `procId` bigint(20) NOT NULL,
  `self` double NOT NULL,
  `total` double NOT NULL,
  `real` double NOT NULL,
  `over` double NOT NULL,
  `calls` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1_procId_procs.id` (`procId`),
  CONSTRAINT `FK1_procId_procs.id` FOREIGN KEY (`procId`) REFERENCES `procs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table paradise_profilerdaemon.sendmaps_procs
CREATE TABLE IF NOT EXISTS `sendmaps_procs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `procpath` varchar(512) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `procpath` (`procpath`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- Data exporting was unselected.

-- Dumping structure for table paradise_profilerdaemon.sendmaps_samples
CREATE TABLE IF NOT EXISTS `sendmaps_samples` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roundId` int(11) NOT NULL,
  `sampleTime` datetime NOT NULL DEFAULT current_timestamp(),
  `procId` bigint(20) NOT NULL,
  `value` double DEFAULT NULL,
  `calls` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK1_procId_sendmaps_procs.id` (`procId`) USING BTREE,
  CONSTRAINT `FK_sendmaps_samples_sendmaps_procs` FOREIGN KEY (`procId`) REFERENCES `sendmaps_procs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
