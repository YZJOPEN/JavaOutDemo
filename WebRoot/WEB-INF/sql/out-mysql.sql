/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : open

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-05-23 09:38:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_clockin
-- ----------------------------
DROP TABLE IF EXISTS `t_clockin`;
CREATE TABLE `t_clockin` (
  `clockInId` varchar(125) NOT NULL COMMENT '签到记录ID',
  `position` varchar(255) NOT NULL COMMENT '签到地址',
  `clockInTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '签到时间',
  `state` int(11) DEFAULT '1' COMMENT '状态',
  `record_id` varchar(125) DEFAULT NULL COMMENT '签到记录对应的外出登记ID',
  PRIMARY KEY (`clockInId`),
  KEY `clockIn_record_Id` (`record_id`),
  CONSTRAINT `clockIn_record_Id` FOREIGN KEY (`record_id`) REFERENCES `t_outrecord` (`recordId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_outrecord
-- ----------------------------
DROP TABLE IF EXISTS `t_outrecord`;
CREATE TABLE `t_outrecord` (
  `recordId` varchar(125) NOT NULL DEFAULT '' COMMENT '外出登记ID',
  `userName` varchar(255) DEFAULT NULL COMMENT '用户名',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `outTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '外出时间',
  `backTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '返回时间',
  `outReason` varchar(255) DEFAULT NULL COMMENT '外出事由',
  `photoUrl` varchar(125) DEFAULT NULL COMMENT '头像url',
  `deptName` varchar(255) DEFAULT NULL COMMENT '部门名称',
  `state` int(11) DEFAULT '1' COMMENT '状态（1：提交，2：撤回）',
  `orgId` varchar(125) DEFAULT NULL COMMENT '人员所在部门ID',
  `openId` varchar(125) DEFAULT NULL COMMENT '云之家人员openId',
  `stage` int(11) DEFAULT '0' COMMENT '区分我的（1）团队（0）',
  PRIMARY KEY (`recordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
