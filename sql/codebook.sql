/*
 Navicat Premium Data Transfer

 Source Server         : 39.100.153.170
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 39.100.153.170:3306
 Source Schema         : codebook

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 28/09/2019 19:11:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for KeepLink
-- ----------------------------
DROP TABLE IF EXISTS `KeepLink`;
CREATE TABLE `KeepLink` (
  `id` bigint(18) NOT NULL,
  `kname` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `kurl` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `userid` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for NodebookContent
-- ----------------------------
DROP TABLE IF EXISTS `NodebookContent`;
CREATE TABLE `NodebookContent` (
  `nid` bigint(18) NOT NULL,
  `nodecontent` mediumtext COLLATE utf8_bin NOT NULL COMMENT '笔记内容',
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for NodebookItem
-- ----------------------------
DROP TABLE IF EXISTS `NodebookItem`;
CREATE TABLE `NodebookItem` (
  `id` bigint(18) NOT NULL,
  `nodeBookName` varchar(500) COLLATE utf8_bin NOT NULL COMMENT '笔记名称',
  `share` int(1) NOT NULL COMMENT '是否共享',
  `sharemsg` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '分享被关闭原因',
  `pId` bigint(18) NOT NULL COMMENT '父节点id',
  `updateTime` datetime DEFAULT NULL,
  `parent` varchar(5) COLLATE utf8_bin NOT NULL COMMENT '看当前节点是否是父节点',
  `open` varchar(5) COLLATE utf8_bin NOT NULL COMMENT '打开或者折叠',
  `userid` bigint(18) NOT NULL,
  `nodetype` int(3) NOT NULL DEFAULT '0' COMMENT '节点类型编号',
  PRIMARY KEY (`id`),
  KEY `index_parent` (`parent`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for people
-- ----------------------------
DROP TABLE IF EXISTS `people`;
CREATE TABLE `people` (
  `id` bigint(18) NOT NULL,
  `uname` varchar(60) COLLATE utf8_bin DEFAULT NULL,
  `passcode` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `myemail` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `myhome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mygitee` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mygithub` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `emailvalidation` int(1) DEFAULT '0' COMMENT '邮箱验证(1=验证,0=未验证)',
  `openshare` int(1) DEFAULT '0' COMMENT '是否共享笔记(0=否,1=是)',
  `allowdelete` int(1) DEFAULT '0' COMMENT '允许删除操作(0=不允许,1=允许)',
  `loginReminder` int(1) DEFAULT '0' COMMENT '登陆提醒(0=不提醒,1=提醒)',
  `upass` int(1) DEFAULT '0' COMMENT '通行证 (0=正常,1=异常)(默认为0)',
  `upassmessage` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '通行证消息(主要在通行证异常时记录消息)',
  `uavatar` mediumtext COLLATE utf8_bin COMMENT '头像地址',
  `ubackground` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '背景图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
