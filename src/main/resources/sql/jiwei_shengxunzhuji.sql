/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : jiwei_shengxunzhuji

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2021-05-17 11:18:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for burn_file
-- ----------------------------
DROP TABLE IF EXISTS `burn_file`;
CREATE TABLE `burn_file` (
  `id` varchar(64) NOT NULL,
  `burn_setting_id` int(10) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL COMMENT '下载文件的url',
  `download_status` int(11) DEFAULT NULL COMMENT '状态  -1=任务异常 0=任务开始 1=任务完成',
  `download_error_info` varchar(255) DEFAULT NULL COMMENT 'download_error_info',
  `call_back_time` datetime DEFAULT NULL COMMENT '发起回调的请求时间',
  `call_back_status` int(11) DEFAULT NULL COMMENT '回调通知状态 -1=回调失败 1=回调成功',
  `call_back_error_info` varchar(255) DEFAULT NULL COMMENT '发起回调失败的原因',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='刻录文件';

-- ----------------------------
-- Table structure for burn_setting
-- ----------------------------
DROP TABLE IF EXISTS `burn_setting`;
CREATE TABLE `burn_setting` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `task_num` int(11) DEFAULT NULL COMMENT '刻录任务计划总数',
  `encryption_type` int(11) DEFAULT NULL COMMENT '加密方式  0=不加密 1=密码加密 2=其他加密',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `oda_save_path` varchar(255) DEFAULT NULL COMMENT '刻录文件存放路径',
  `done_task_num` int(11) DEFAULT NULL COMMENT '子任务完成数',
  `error_task_num` int(11) DEFAULT NULL COMMENT '子任务异常数',
  `call_back_url` varchar(255) DEFAULT NULL COMMENT '回到通知地址',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='刻录父任务\r\n';

-- ----------------------------
-- Table structure for burn_video
-- ----------------------------
DROP TABLE IF EXISTS `burn_video`;
CREATE TABLE `burn_video` (
  `id` varchar(64) NOT NULL,
  `burn_setting_id` int(10) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL COMMENT '审讯机ip',
  `user_name` varchar(255) DEFAULT NULL COMMENT '审讯机登录用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '审讯机登录密码',
  `channelNo` int(11) DEFAULT NULL COMMENT '下载视频的审讯机通道号',
  `end_time` varchar(255) DEFAULT NULL COMMENT '下载视频的结束时间',
  `start_time` varchar(255) DEFAULT NULL COMMENT '下载视频的开始时间',
  `download_status` int(11) DEFAULT NULL COMMENT '状态  -1=任务异常 0=任务开始 1=任务完成',
  `download_error_info` varchar(255) DEFAULT NULL COMMENT 'download_error_info',
  `call_back_time` datetime DEFAULT NULL COMMENT '发起回调的请求时间',
  `call_back_status` int(11) DEFAULT NULL COMMENT '回调通知状态 -1=回调失败 1=回调成功',
  `call_back_error_info` varchar(255) DEFAULT NULL COMMENT '发起回调失败的原因',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='刻录视频';
