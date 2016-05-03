/*
Navicat MySQL Data Transfer

Source Server         : 腾讯云
Source Server Version : 50549
Source Host           : 115.159.223.254:3306
Source Database       : crawel

Target Server Type    : MYSQL
Target Server Version : 50549
File Encoding         : 65001

Date: 2016-05-03 17:28:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data
-- ----------------------------
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 NOT NULL,
  `seed` varchar(255) NOT NULL,
  `level` int(255) NOT NULL,
  `time` datetime NOT NULL,
  `signature` varchar(255) NOT NULL COMMENT 'url签名',
  PRIMARY KEY (`id`),
  KEY `dataSig` (`signature`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=16690 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for seeds
-- ----------------------------
DROP TABLE IF EXISTS `seeds`;
CREATE TABLE `seeds` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '0',
  `enable` int(11) DEFAULT '0' COMMENT '0是开启，1是已完成，-1是禁用',
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
