/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.17 : Database - xsap_dev
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xsap_dev` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xsap_dev`;

/*Table structure for table `t_class_record` */

DROP TABLE IF EXISTS `t_class_record`;

CREATE TABLE `t_class_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `card_name` varchar(50) DEFAULT NULL COMMENT '会员卡名',
  `schedule_id` bigint(20) unsigned DEFAULT NULL COMMENT '排课记录id',
  `note` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL COMMENT '教师评语',
  `check_status` tinyint(1) unsigned DEFAULT '0' COMMENT '用户确认上课与否。1，已上课；0，未上课',
  `reserve_check` tinyint(1) unsigned DEFAULT '0' COMMENT '是否已预约，默认0',
  `create_time` datetime DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `version` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_class_member_id` (`member_id`),
  KEY `fk_class_schedule_id` (`schedule_id`),
  CONSTRAINT `fk_class_member_id` FOREIGN KEY (`member_id`) REFERENCES `t_member` (`id`),
  CONSTRAINT `fk_class_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Data for the table `t_class_record` */

insert  into `t_class_record`(`id`,`member_id`,`card_name`,`schedule_id`,`note`,`comment`,`check_status`,`reserve_check`,`create_time`,`last_modify_time`,`version`) values 
(1,1,'12课时1对1',1,'下午上课','优秀',1,1,'2020-10-01 14:36:44',NULL,4),
(2,1,'12课时1对1',2,'晚上上课','及格',0,1,'2020-10-09 16:06:36',NULL,3),
(3,1,'24课时1对1',3,'下午上课','优秀',1,1,'2020-10-01 14:36:44',NULL,4),
(4,2,'12课时1对1',1,'下午上课','优秀',1,1,'2020-10-01 14:36:44',NULL,3),
(5,2,'12课时1对1',2,'下午上课','优秀',1,1,'2020-10-01 14:36:44',NULL,2),
(6,3,'12课时1对1',4,'下午上课','优秀',1,1,'2020-10-01 14:36:44',NULL,3),
(7,1,'24课时1对1',5,'下午上课','优秀',1,1,'2020-10-01 14:36:44',NULL,2),
(11,1,'24课时1对1',26,NULL,NULL,0,1,'2020-11-01 10:15:00',NULL,3),
(15,3,'6人小班',29,NULL,NULL,1,1,'2020-11-02 10:35:00',NULL,2),
(16,2,'12课时1对1',29,NULL,NULL,1,1,'2020-11-02 10:35:00',NULL,2),
(17,3,'6人小班',30,NULL,NULL,1,0,'2020-10-26 10:25:00',NULL,4),
(18,1,'15人大班',30,NULL,NULL,1,1,'2020-10-26 10:25:00',NULL,2),
(19,2,'12课时1对1',30,NULL,NULL,1,1,'2020-10-26 10:25:00',NULL,2);

/*Table structure for table `t_consume_record` */

DROP TABLE IF EXISTS `t_consume_record`;

CREATE TABLE `t_consume_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `operate_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `card_count_change` int(10) unsigned DEFAULT '0' COMMENT '卡次变化',
  `card_day_change` int(10) unsigned DEFAULT '0' COMMENT '有效天数变化',
  `money_cost` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '花费的金额',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作员',
  `note` varchar(255) DEFAULT NULL,
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `card_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员卡id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `fk_consume_card_id` (`card_id`),
  KEY `fk_consume_member_id` (`member_id`),
  CONSTRAINT `fk_consume_card_id` FOREIGN KEY (`card_id`) REFERENCES `t_member_bind_record` (`card_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_consume_member_id` FOREIGN KEY (`member_id`) REFERENCES `t_member_bind_record` (`member_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 COMMENT='消费记录';

/*Data for the table `t_consume_record` */

insert  into `t_consume_record`(`id`,`operate_type`,`card_count_change`,`card_day_change`,`money_cost`,`operator`,`note`,`member_id`,`card_id`,`create_time`,`last_modify_time`,`version`) values 
(29,'上课支出',1,0,120.00,'系统自动处理',NULL,1,1,'2020-10-21 10:13:53',NULL,1),
(30,'上课支出',2,0,55.56,'系统自动处理',NULL,1,4,'2020-10-21 10:13:53',NULL,1),
(31,'上课支出',1,0,27.78,'系统自动处理',NULL,1,4,'2020-10-21 10:13:53',NULL,1),
(41,'上课支出',1,0,120.00,'系统自动处理',NULL,2,1,'2020-10-21 11:47:24',NULL,1),
(42,'上课支出',3,0,360.00,'系统自动处理',NULL,2,1,'2020-10-21 11:47:24',NULL,1),
(43,'扣费',3,1,360.00,'某某某操作','',1,1,'2020-10-21 13:33:23',NULL,1),
(44,'扣费',10,10,1200.00,'某某某操作','测试',1,1,'2020-10-21 13:41:21',NULL,1),
(47,'上课支出',1,0,120.00,'系统自动处理',NULL,3,1,'2020-10-21 16:19:40',NULL,1),
(49,'上课扣费',1,0,7.14,'某某某操作','',59,2,'2020-10-24 00:02:06',NULL,1),
(51,'上课支出',1,0,0.25,'系统自动处理',NULL,62,15,'2020-10-24 00:30:56',NULL,1),
(52,'上课支出',1,0,120.00,'系统自动处理',NULL,2,1,'2020-10-24 00:30:56',NULL,1),
(53,'上课扣费',1,0,7.14,'某某某操作','',3,2,'2020-10-24 10:05:01',NULL,1),
(54,'上课扣费',1,0,120.00,'某某某操作','',2,1,'2020-10-24 10:46:49',NULL,1),
(57,'上课扣费',2,0,14.28,'某某某操作','',3,2,'2020-10-24 11:54:47',NULL,1),
(58,'上课扣费',2,0,6.18,'某某某操作','',1,3,'2020-10-24 12:01:53',NULL,1),
(59,'上课扣费',2,0,240.00,'某某某操作','',2,1,'2020-10-24 12:04:36',NULL,1);

/*Table structure for table `t_course` */

DROP TABLE IF EXISTS `t_course`;

CREATE TABLE `t_course` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `duration` int(10) unsigned DEFAULT '0' COMMENT '课程时长',
  `contains` int(10) unsigned DEFAULT '0' COMMENT '课堂容纳人数',
  `color` varchar(10) DEFAULT NULL COMMENT '卡片颜色',
  `introduce` varchar(255) DEFAULT NULL COMMENT '课程介绍',
  `times_cost` int(10) unsigned DEFAULT '0' COMMENT '每节课程需花费的次数',
  `limit_sex` varchar(6) DEFAULT NULL COMMENT '限制性别',
  `limit_age` int(10) DEFAULT NULL COMMENT '限制年龄',
  `limit_counts` int(10) DEFAULT NULL COMMENT '限制预约次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='课程表';

/*Data for the table `t_course` */

insert  into `t_course`(`id`,`name`,`duration`,`contains`,`color`,`introduce`,`times_cost`,`limit_sex`,`limit_age`,`limit_counts`,`create_time`,`last_modify_time`,`version`) values 
(1,'历史',30,50,'blue','数学课',1,'男',6,2,'2020-09-09 09:47:42',NULL,2),
(2,'ds',30,45,'red','地理课',2,'女',7,1,NULL,NULL,1),
(3,'英语',34,55,'green','英语课',3,'男',8,1,NULL,NULL,1),
(10,'语文',45,33,'pink','语文课',2,'男',23,2,NULL,NULL,1),
(12,'数学',45,25,'pink','数学课',2,'男',23,2,NULL,NULL,1),
(18,'计算机基础',60,35,'#1fc2a4','',3,'全部',6,4,'2020-10-23 12:40:10','2020-10-23 13:00:49',2),
(19,'美术',55,30,'#1977cc','<p>美术基础</p>',2,'男',5,0,'2020-10-24 11:36:03',NULL,1);

/*Table structure for table `t_course_card` */

DROP TABLE IF EXISTS `t_course_card`;

CREATE TABLE `t_course_card` (
  `card_id` bigint(20) unsigned NOT NULL COMMENT '会员卡id',
  `course_id` bigint(20) unsigned NOT NULL COMMENT '课程id',
  PRIMARY KEY (`card_id`,`course_id`),
  KEY `fk_course_course_id` (`course_id`),
  CONSTRAINT `fk_course_card_id` FOREIGN KEY (`card_id`) REFERENCES `t_member_card` (`id`),
  CONSTRAINT `fk_course_course_id` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中间表：课程-会员卡';

/*Data for the table `t_course_card` */

insert  into `t_course_card`(`card_id`,`course_id`) values 
(1,1),
(2,1),
(15,1),
(1,2),
(2,3),
(3,3),
(15,3),
(1,18),
(15,18),
(1,19),
(2,19),
(3,19),
(15,19);

/*Table structure for table `t_employee` */

DROP TABLE IF EXISTS `t_employee`;

CREATE TABLE `t_employee` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用来登录',
  `sex` varchar(6) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `introduce` varchar(255) DEFAULT NULL COMMENT '介绍',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像文件路径',
  `note` varchar(255) DEFAULT NULL,
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作角色名，暂不使用',
  `role_password` varchar(100) DEFAULT NULL COMMENT '操作角色密码',
  `role_type` tinyint(1) unsigned DEFAULT '0' COMMENT '操作角色类型，1，超级管理员；0，普通管理员',
  `role_email` varchar(50) DEFAULT NULL COMMENT '操作角色邮箱',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '逻辑删除，1有效，0无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='员工表';

/*Data for the table `t_employee` */

insert  into `t_employee`(`id`,`name`,`phone`,`sex`,`birthday`,`introduce`,`avatar_url`,`note`,`role_name`,`role_password`,`role_type`,`role_email`,`is_deleted`,`create_time`,`last_modify_time`,`version`) values 
(1,'张老师','123456','女','2020-09-07','骨干教师','a3.jpg','教数学','admin','567',1,'3496351038@qq.com',0,'2020-09-03 10:22:27','2020-10-01 06:59:49',1),
(2,'李老','123123','男','2020-09-09','骨干教师','A5AB569A1ED24D68A3FEE8D94BB82FBA_favicon.jpg','地理','普通管理员','11',0,'3496351038@qq.com',0,'2020-09-02 14:57:11','2020-10-18 02:44:40',50),
(3,'黑衣人','112358','男','2020-09-25','骨干教师','a8.jpg','玄学','test','111',0,'3496351038@qq.com',0,NULL,NULL,1),
(4,'魏老','4','男','2020-09-26','骨干教师','a6.jpg','教体育','user1','123',0,'3496351038@qq.com',0,'2020-09-27 11:32:57',NULL,1),
(5,'赵老','5','男',NULL,'骨干教师','user.jpg','教体育','user2','123',0,'3496351038@qq.com',0,'2020-09-27 11:32:57',NULL,1),
(6,'钱老','6','男',NULL,'骨干教师','user.jpg','教体育','user3','123',0,'3496351038@qq.com',0,'2020-09-27 11:32:57',NULL,1),
(7,'孙老','7','男',NULL,'骨干教师','user.jpg','教体育','user4','123',0,'3496351038@qq.com',0,'2020-09-27 11:32:57',NULL,1),
(8,'周老','8','男',NULL,'骨干教师','user.jpg','教体育','user5','123',0,'3496351038@qq.com',0,'2020-09-27 11:32:57',NULL,1),
(9,'吴老','92','男',NULL,'骨干教师','CD95F776F24B41ABAF159420CFA3B36B_利德.jpg','教体育','普通管理员','123',0,'3496351038@qq.com',0,'2020-09-27 11:32:57','2020-10-17 10:23:35',3);

/*Table structure for table `t_global_reservation_set` */

DROP TABLE IF EXISTS `t_global_reservation_set`;

CREATE TABLE `t_global_reservation_set` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_day` int(10) DEFAULT NULL COMMENT '可提前预约的天数',
  `end_day` int(10) DEFAULT NULL COMMENT '模式1：提前预约截止天数，上课前',
  `end_time` time DEFAULT NULL COMMENT '模式1：提前预约截止时间(24小时内)，上课前',
  `end_hour` int(10) DEFAULT NULL COMMENT '模式2：提前预约截止小时数，离上课前',
  `cancel_day` int(10) DEFAULT NULL COMMENT '模式1：提前预约取消的距离天数',
  `cancel_time` time DEFAULT NULL COMMENT '模式1：提前预约取消的时间限制（24小时内）',
  `cancel_hour` int(10) DEFAULT NULL COMMENT '模式2：提前预约取消的距离小时数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='全局预约设置表';

/*Data for the table `t_global_reservation_set` */

insert  into `t_global_reservation_set`(`id`,`start_day`,`end_day`,`end_time`,`end_hour`,`cancel_day`,`cancel_time`,`cancel_hour`,`create_time`,`last_modify_time`,`version`) values 
(1,-1,-1,'17:30:59',6,-1,'12:00:59',-1,'2020-10-21 17:02:12','2020-10-21 18:06:28',11);

/*Table structure for table `t_member` */

DROP TABLE IF EXISTS `t_member`;

CREATE TABLE `t_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `sex` varchar(6) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像路径',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '用户的逻辑删除，1有效，0无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 COMMENT='会员表';

/*Data for the table `t_member` */

insert  into `t_member`(`id`,`name`,`sex`,`phone`,`birthday`,`note`,`avatar_url`,`is_deleted`,`create_time`,`last_modify_time`,`version`) values 
(1,'lisi','女','1111','1998-12-12','请问请问','a1.jpg',0,'2020-10-01 22:59:16','2020-10-21 11:16:43',7),
(2,'shun','女','1345678','2020-09-26','人2号','a3.jpg',0,'2020-10-02 22:59:24',NULL,1),
(3,'至安徽','女','11111','2020-09-15','','a2.jpg',0,'2020-10-02 22:59:29','2020-10-18 01:04:35',13),
(57,'','男','8998',NULL,'','user.jpg',1,'2020-10-18 09:18:09',NULL,1),
(58,'','女','111112',NULL,'','user.jpg',1,'2020-10-18 09:27:26',NULL,1),
(59,'李老1','男','12122',NULL,'','user.jpg',0,'2020-10-18 09:32:54',NULL,1),
(60,'李子','男','2',NULL,NULL,'user.jpg',0,NULL,NULL,1),
(61,'lisi1','女','32321',NULL,'','user.jpg',0,'2020-10-20 11:29:10','2020-10-21 11:09:23',2),
(62,'税控机','男','12312343','2020-10-01','撒大苏打','user.jpg',0,'2020-10-20 11:31:27',NULL,1);

/*Table structure for table `t_member_bind_record` */

DROP TABLE IF EXISTS `t_member_bind_record`;

CREATE TABLE `t_member_bind_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) unsigned DEFAULT NULL,
  `card_id` bigint(20) unsigned DEFAULT NULL,
  `valid_count` int(10) unsigned DEFAULT '0' COMMENT '可使用次数',
  `valid_day` int(10) unsigned DEFAULT '0' COMMENT '有效期，按天算',
  `received_money` decimal(10,2) DEFAULT '0.00' COMMENT '实收金额',
  `pay_mode` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `note` varchar(255) DEFAULT NULL,
  `active_status` tinyint(1) unsigned DEFAULT '1' COMMENT '激活状态，1激活，0非激活',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`),
  KEY `fk_bind_member_id` (`member_id`),
  KEY `fk_bind_card_id` (`card_id`),
  CONSTRAINT `fk_bind_card_id` FOREIGN KEY (`card_id`) REFERENCES `t_member_card` (`id`),
  CONSTRAINT `fk_bind_member_id` FOREIGN KEY (`member_id`) REFERENCES `t_member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='中间表：会员绑定记录';

/*Data for the table `t_member_bind_record` */

insert  into `t_member_bind_record`(`id`,`member_id`,`card_id`,`valid_count`,`valid_day`,`received_money`,`pay_mode`,`note`,`active_status`,`create_time`,`last_modify_time`,`version`) values 
(1,1,1,41,44,-1679.10,NULL,'绑定1',1,'2020-10-01 21:15:55','2020-10-21 15:56:31',18),
(2,1,3,5,7,403.82,NULL,'绑定2',0,'2020-10-02 21:16:00','2020-10-21 15:57:55',7),
(3,2,1,70,12,-660.00,NULL,'绑定3',1,'2020-09-11 21:16:18','2020-10-23 22:38:37',9),
(4,3,2,52,55,307.58,NULL,'绑定4',1,'2020-09-18 21:16:12','2020-10-21 16:20:03',5),
(5,1,4,11,5,-111.68,NULL,'bind-5',1,'2020-09-03 21:16:25','2020-10-21 16:03:17',12),
(14,2,4,30,30,380.85,'线下支付',NULL,1,'2020-10-13 13:28:40',NULL,1),
(17,59,9,50,30,121.00,'支付宝','',1,'2020-10-18 09:33:32',NULL,1),
(20,3,14,15,15,300.00,NULL,NULL,1,NULL,NULL,1),
(28,62,15,51,30,199.75,'','',1,'2020-10-20 14:06:09',NULL,2),
(30,59,2,50,30,-4.14,'现金','323',1,'2020-10-21 17:19:37',NULL,3);

/*Table structure for table `t_member_card` */

DROP TABLE IF EXISTS `t_member_card`;

CREATE TABLE `t_member_card` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) unsigned DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述信息',
  `note` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `type` varchar(10) DEFAULT NULL COMMENT '会员卡类型',
  `total_count` int(10) unsigned DEFAULT '24' COMMENT '默认可用次数',
  `total_day` int(10) unsigned DEFAULT '7' COMMENT '默认可用天数',
  `status` tinyint(1) unsigned DEFAULT '0' COMMENT '激活状态，0激活，1非激活',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='会员卡表';

/*Data for the table `t_member_card` */

insert  into `t_member_card`(`id`,`name`,`price`,`description`,`note`,`type`,`total_count`,`total_day`,`status`,`create_time`,`last_modify_time`,`version`) values 
(1,'12课时1对1',240.00,'网课','卡1','无限次',2,2,0,NULL,'2020-10-20 16:59:55',2),
(2,'6人小班',100.00,'30课时','卡2','100次',14,4,0,NULL,NULL,1),
(3,'15人大班',99.00,'32课时','卡3','99次',32,4,0,NULL,NULL,1),
(4,'24课时1对1',333.33,'18课时','note-4','无限次',12,12,0,NULL,NULL,1),
(5,'36课时1对1',333.33,'18课时','note-4','无限次',14,14,0,NULL,NULL,1),
(6,'48课时1对12',333.33,'18课时','note-6','无限次',14,14,0,NULL,NULL,1),
(7,'64课时1对4',333.33,'18课时','note-7','无限次',12,14,0,NULL,NULL,1),
(8,'36课时1对8',333.33,'18课时','note-8','无限次',12,14,1,NULL,NULL,1),
(9,'12人',333.00,'18课时','note-9','无限次',14,14,1,NULL,'2020-10-20 17:00:18',2),
(12,'体验卡',12.50,'免费使用10次','值得拥有','有限次数100',30,15,1,'2020-10-19 10:17:34','2020-10-20 09:55:32',2),
(14,'学而思',NULL,'','','',43,43,1,'2020-10-19 14:38:54',NULL,1),
(15,'金卡',11.00,'2333','233都是','次卡(无期限)',44,42,0,'2020-10-20 11:42:20',NULL,1);

/*Table structure for table `t_member_log` */

DROP TABLE IF EXISTS `t_member_log`;

CREATE TABLE `t_member_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `involve_money` decimal(10,2) DEFAULT '0.00' COMMENT '影响的金额',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作员名称',
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `card_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员卡id',
  `create_time` datetime DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `version` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_log_card_id` (`card_id`),
  KEY `fk_log_member_id` (`member_id`),
  CONSTRAINT `fk_log_card_id` FOREIGN KEY (`card_id`) REFERENCES `t_member_bind_record` (`card_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_log_member_id` FOREIGN KEY (`member_id`) REFERENCES `t_member_bind_record` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='操作记录';

/*Data for the table `t_member_log` */

insert  into `t_member_log`(`id`,`type`,`involve_money`,`operator`,`member_id`,`card_id`,`create_time`,`last_modify_time`,`version`) values 
(17,'绑卡操作',121.00,'系统处理',59,9,'2020-10-18 09:33:32',NULL,1),
(18,'system',0.00,NULL,1,4,NULL,NULL,1),
(30,'充值',0.00,'某某某操作',1,1,'2020-10-21 13:31:58',NULL,1),
(31,'消费',360.00,'某某某操作',1,1,'2020-10-21 13:33:23',NULL,1),
(32,'消费',1200.00,'某某某操作',1,1,'2020-10-21 13:41:21',NULL,1),
(34,'充值',10.00,'某某某操作',1,3,'2020-10-21 14:07:04',NULL,1),
(35,'绑卡操作',3.00,'系统处理',59,2,'2020-10-21 17:19:37',NULL,1),
(37,'上课扣费',7.14,'某某某操作',59,2,'2020-10-24 00:02:06',NULL,1),
(38,'上课扣费',7.14,'某某某操作',3,2,'2020-10-24 10:05:01',NULL,1),
(39,'上课扣费',120.00,'某某某操作',2,1,'2020-10-24 10:46:50',NULL,1),
(41,'上课扣费',14.28,'某某某操作',3,2,'2020-10-24 11:54:48',NULL,1),
(43,'上课扣费',6.18,'某某某操作',1,3,'2020-10-24 12:01:53',NULL,1),
(44,'上课扣费',240.00,'某某某操作',2,1,'2020-10-24 12:04:36',NULL,1);

/*Table structure for table `t_recharge_record` */

DROP TABLE IF EXISTS `t_recharge_record`;

CREATE TABLE `t_recharge_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `add_count` int(10) unsigned DEFAULT '0' COMMENT '充值可用次数',
  `add_day` int(10) unsigned DEFAULT '0' COMMENT '延长有效天数',
  `received_money` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '实收金额',
  `pay_mode` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作员',
  `note` varchar(255) DEFAULT NULL,
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `card_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员卡id',
  `create_time` datetime DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `version` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_charge_card_id` (`card_id`),
  KEY `fk_charge_member_id` (`member_id`),
  CONSTRAINT `fk_charge_card_id` FOREIGN KEY (`card_id`) REFERENCES `t_member_bind_record` (`card_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_charge_member_id` FOREIGN KEY (`member_id`) REFERENCES `t_member_bind_record` (`member_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='充值记录';

/*Data for the table `t_recharge_record` */

insert  into `t_recharge_record`(`id`,`add_count`,`add_day`,`received_money`,`pay_mode`,`operator`,`note`,`member_id`,`card_id`,`create_time`,`last_modify_time`,`version`) values 
(13,0,0,0.00,NULL,NULL,NULL,3,14,NULL,NULL,1),
(16,0,0,0.00,'','某某某操作','',1,1,'2020-10-21 13:31:58',NULL,1),
(18,2,2,10.00,'微信','某某某操作','swq',1,3,'2020-10-21 14:07:04',NULL,1);

/*Table structure for table `t_reservation_record` */

DROP TABLE IF EXISTS `t_reservation_record`;

CREATE TABLE `t_reservation_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status` tinyint(1) unsigned DEFAULT '0' COMMENT '预约状态，1有效，0无效',
  `reserve_nums` int(10) unsigned DEFAULT '0' COMMENT '单次操作预约人数',
  `cancel_times` int(10) unsigned DEFAULT '0' COMMENT '取消次数统计',
  `comment` varchar(255) DEFAULT NULL COMMENT '教师评语',
  `note` varchar(255) DEFAULT NULL,
  `class_note` varchar(255) DEFAULT NULL COMMENT '上课备注',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作员',
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `card_name` varchar(50) DEFAULT NULL COMMENT '会员指定的会员卡来预约',
  `schedule_id` bigint(20) unsigned DEFAULT NULL COMMENT '排课记录id',
  `create_time` datetime DEFAULT NULL,
  `last_modify_time` datetime DEFAULT NULL,
  `version` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_reserve_member_id` (`member_id`),
  KEY `fk_reserve_schedule_id` (`schedule_id`),
  CONSTRAINT `fk_reserve_member_id` FOREIGN KEY (`member_id`) REFERENCES `t_member` (`id`),
  CONSTRAINT `fk_reserve_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='预约记录';

/*Data for the table `t_reservation_record` */

insert  into `t_reservation_record`(`id`,`status`,`reserve_nums`,`cancel_times`,`comment`,`note`,`class_note`,`operator`,`member_id`,`card_name`,`schedule_id`,`create_time`,`last_modify_time`,`version`) values 
(1,1,1,2,NULL,NULL,NULL,'海豚',1,'12课时1对1',1,'2020-09-22 03:27:17','2020-10-09 15:05:08',6),
(2,1,1,0,NULL,NULL,NULL,'企鹅',1,'12课时1对1',2,'2020-09-22 03:27:29','2020-10-07 15:05:14',1),
(3,1,0,0,NULL,NULL,NULL,'企鹅',2,'12课时1对1',3,'2020-09-22 03:27:33','2020-10-06 15:05:18',1),
(4,0,0,0,NULL,NULL,NULL,'海豚',3,'12课时1对1',1,'2020-10-03 22:59:52','2020-10-07 15:05:22',1),
(16,0,1,0,NULL,'',NULL,NULL,1,'24课时1对1',26,'2020-10-23 17:08:31','2020-10-23 17:10:30',2),
(20,1,2,0,NULL,'',NULL,NULL,3,'6人小班',29,'2020-10-24 10:04:45',NULL,1),
(21,1,2,0,NULL,'',NULL,NULL,2,'12课时1对1',29,'2020-10-24 10:41:56',NULL,1),
(22,0,1,2,NULL,'',NULL,NULL,3,'6人小班',30,'2020-10-24 11:53:11','2020-10-24 12:13:10',3),
(23,1,1,0,NULL,'',NULL,NULL,1,'15人大班',30,'2020-10-24 11:53:39',NULL,1),
(24,1,2,0,NULL,'',NULL,NULL,2,'12课时1对1',30,'2020-10-24 11:54:06',NULL,1);

/*Table structure for table `t_schedule_record` */

DROP TABLE IF EXISTS `t_schedule_record`;

CREATE TABLE `t_schedule_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `course_id` bigint(20) unsigned DEFAULT NULL COMMENT '课程号',
  `teacher_id` bigint(20) unsigned DEFAULT NULL COMMENT '教师号',
  `order_nums` int(10) unsigned DEFAULT '0' COMMENT '此项排课的预约人数',
  `start_date` date DEFAULT NULL COMMENT '上课日期',
  `class_time` time DEFAULT NULL COMMENT '上课时间',
  `limit_sex` varchar(6) DEFAULT NULL COMMENT '限制性别',
  `limit_age` int(10) DEFAULT NULL COMMENT '限制年龄',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(10) unsigned DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `only_course_teach` (`course_id`,`teacher_id`,`class_time`,`start_date`),
  KEY `fk_sche_teacher_id` (`teacher_id`),
  CONSTRAINT `fk_sche_course_id` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`),
  CONSTRAINT `fk_sche_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `t_employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='中间表：排课计划表';

/*Data for the table `t_schedule_record` */

insert  into `t_schedule_record`(`id`,`course_id`,`teacher_id`,`order_nums`,`start_date`,`class_time`,`limit_sex`,`limit_age`,`create_time`,`last_modify_time`,`version`) values 
(1,1,1,33,'2020-09-08','09:00:45','女',12,'2020-09-17 00:52:44',NULL,4),
(2,3,1,28,'2020-09-04','09:00:33','男',14,'2020-10-02 00:52:50',NULL,1),
(3,2,2,45,'2020-09-08','09:00:44','女',12,'2020-10-03 00:52:54',NULL,1),
(4,1,2,21,'2020-09-09','14:00:55','男',15,'2020-10-04 00:53:00',NULL,1),
(5,1,2,7,'2020-09-09','15:30:00','男',13,'2020-10-01 00:53:04',NULL,1),
(12,1,1,33,'2020-09-10','09:00:45','女',12,'2020-09-17 00:52:44',NULL,4),
(21,1,1,0,'2020-09-08','10:05:00',NULL,NULL,'2020-10-22 22:22:11',NULL,1),
(22,3,1,28,'2020-09-11','09:00:33','男',14,'2020-10-23 09:18:08',NULL,1),
(23,2,2,0,'2020-10-02','10:30:00',NULL,NULL,'2020-10-23 11:09:03',NULL,1),
(25,3,1,0,'2020-10-04','09:30:00',NULL,NULL,'2020-10-23 11:18:29',NULL,1),
(26,10,1,1,'2020-11-01','09:30:00',NULL,NULL,'2020-10-23 16:51:42',NULL,4),
(28,2,2,45,'2020-11-02','09:00:44','女',12,'2020-10-23 20:30:45',NULL,1),
(29,1,1,4,'2020-11-02','10:05:00',NULL,NULL,'2020-10-23 20:30:45',NULL,3),
(30,19,3,2,'2020-10-26','09:30:00',NULL,NULL,'2020-10-24 11:52:22',NULL,6);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
