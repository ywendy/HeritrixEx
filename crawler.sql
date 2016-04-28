/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 50711
Source Host           : localhost:3310
Source Database       : crawler

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-04-29 04:00:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data
-- ----------------------------
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `seed` varchar(255) NOT NULL,
  `level` int(255) NOT NULL,
  `time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for seeds
-- ----------------------------
DROP TABLE IF EXISTS `seeds`;
CREATE TABLE `seeds` (
  `id` int(11) NOT NULL,
  `enable` int(11) DEFAULT '0',
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
