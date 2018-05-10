/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : open

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-05-09 10:23:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_clockin
-- ----------------------------
DROP TABLE IF EXISTS `t_clockin`;
CREATE TABLE `t_clockin` (
  `clockInId` varchar(125) NOT NULL,
  `position` varchar(255) NOT NULL,
  `clockInTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` int(11) DEFAULT '1',
  `record_id` varchar(125) DEFAULT NULL,
  PRIMARY KEY (`clockInId`),
  KEY `clockIn_record_Id` (`record_id`),
  CONSTRAINT `clockIn_record_Id` FOREIGN KEY (`record_id`) REFERENCES `t_outrecord` (`recordId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_outrecord
-- ----------------------------
DROP TABLE IF EXISTS `t_outrecord`;
CREATE TABLE `t_outrecord` (
  `recordId` varchar(125) NOT NULL DEFAULT '',
  `userName` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `outTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `backTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `outReason` varchar(255) DEFAULT NULL,
  `photoUrl` varchar(125) DEFAULT NULL,
  `deptName` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT '1',
  `orgId` varchar(125) DEFAULT NULL,
  `openId` varchar(125) DEFAULT NULL,
  `stage` int(11) DEFAULT '0',
  PRIMARY KEY (`recordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
