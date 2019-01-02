/*
Navicat MySQL Data Transfer

Source Server         : Localhost
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : car_service

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-10-02 14:34:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `origin` varchar(255) DEFAULT NULL,
  `content` varchar(2048) DEFAULT NULL,
  `liked` int(10) DEFAULT NULL COMMENT '点赞数',
  `picture` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('1', '微整形手术 宝马7系中期改款或今年底亮相', null, '网易汽车', '网易汽车1月4日报道 近日据海外媒体报道，宝马7系中期改款车型将于2019年3月投产，这意味着该车最快将于2018年年底亮相。新车将主要针对车头、车位及动力配置方面有所升级。\r\n它的车头车尾进行了比较严密的伪装，意味着新车将在进气格栅、大灯组等位置进行调整，此外车尾灯的灯腔设计也会有所变化。\r\n据悉，新款7系还将拥有新的外观颜色、内饰配色和皮质材料可供消费者选择，将于2019年3月份投产。\r\n动力方面，新车将有所升级其中搭载4.0升双涡轮增压V8发动机的750i/Li车型，其最大功率将提升至472马力；搭载3.0升双涡轮增压直列六缸发动机的740i/Li车型，其最大功率将提升至340马力。\r\n插电式混合动力车型，将由745e/Le车型取代现有的740e/Le车型，期2.0T发动机将保持不变，但是电动机及电池组将会进行升级，最大综合输出功率可达396马力，纯点巡航里程也将有所提升。\r\n', '520', 'http://120.79.183.78/images/maintain_article05.png', '2018-04-19 17:12:38', null, null);
INSERT INTO `article` VALUES ('2', '细节方面有所提升 奔驰全新一代G级官图', null, '汽车最前第一线', '近日，奔驰全新一代G级外观官图曝光，新车延续了奔驰G级自诞生以来的外观样式，只是在细节方面进行了提升。据之前消息，新车将于1月15日开幕的底特律车展正式亮相，并在2018年年中进入中国市场。\r\n外观方面，全新一代G级延续了经典的外观造型，车头大灯组与时俱进的换装了LED光源，搭配全新的灯腔设计，时髦了许多。前中网换装三横幅镂空式造型，前保险样式也有小幅度修改。\r\n内饰方面，内饰方面，新车整体线条设计棱角分明，符合硬派SUV的形象，低配版车型仍然采用传统的指针式仪表盘，中控台配备有12.3英寸大屏幕。高配版车型配备两块12.3英寸全液晶屏幕，仪表盘可选择三种界面设计，全新的三辐式方向盘配备有奔驰双拇指控制系统。大屏幕下方是两个造型较为怪异的空调出风口，中央保留了标志性的三把差速锁设计，并采用用镀铬材质加亮，老款车型的直排换挡杆变成怀挡设计。\r\n', '125', 'http://120.79.183.78/images/maintain_article04.jpg', '2018-04-19 17:33:28', null, null);
INSERT INTO `article` VALUES ('3', '把凯美瑞爆改成皮卡车 当作毕业作品', null, '改装车', '丰田凯美瑞虽然在国内拥有很高的销量，但是买家大部分都是成熟稳重的奶爸们，在路上见到99%的凯美瑞都是原装车。不过呢，别小看剩下的那1%的玩家，在他们的眼中凯美瑞也是改装的好底子，换套避震和轮毂就有不错的效果，起码让自己从车里走出来的时候不像中年大叔。\r\n由于凯美瑞一直以来比较中庸的表现，大部分车迷不会把它和激情挂上钩。而随着第八代车型的出现，这个现象仿佛就出现了改变。车身更加丰富的线条，让人觉得这辆车至少年轻了10岁。往日的大叔车变成了年轻人的目标。\r\n可惜的是，全新的凯美瑞虽然高调露面了，在马路上迟迟没有遇到一辆实车。这也让喜欢它的车迷们非常着急啊。既然都是凯美瑞，那么可不可以把第七代车型爆改成八代的前脸呢？今天我们的文章中就来告诉大家，完全OK！\r\n', '25', 'http://120.79.183.78/images/maintain_article06.jpg', '2018-04-19 17:33:30', null, null);

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `article_id` int(11) DEFAULT NULL,
  `site_id` int(11) DEFAULT NULL,
  `type` varchar(255) NOT NULL COMMENT '收藏类型（服务点/文章）',
  `create_time` datetime DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_collect_cust` (`user_id`),
  KEY `fk_collect_site` (`site_id`),
  KEY `fk_collect_article` (`article_id`),
  CONSTRAINT `fk_collect_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `fk_collect_cust` FOREIGN KEY (`user_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `fk_collect_site` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES ('1', '1', '1', null, 'ARTICLE', '2018-05-05 11:56:00', null);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `cust_id` int(11) NOT NULL,
  `score` float(2,1) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `anonymous` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_customer` (`cust_id`),
  KEY `fk_comment_site` (`site_id`),
  CONSTRAINT `fk_comment_customer` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `fk_comment_site` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('1', '1', '1', '3.5', '时效性 重要性 显著性 趣味性 新鲜性', '1', '2018-04-19 19:56:16', '2018-04-19 20:05:54', null);
INSERT INTO `comment` VALUES ('2', '2', '1', '4.5', '服务好，速度很快，很满意！', '1', '2018-04-19 20:01:25', '2018-04-19 20:46:19', null);
INSERT INTO `comment` VALUES ('3', '3', '1', '3.5', '服务一般，还行吧，同行业中也算不错了！', '0', '2018-04-19 20:07:45', null, null);
INSERT INTO `comment` VALUES ('4', '2', '2', '3.5', '跟描述的一样，这价格很值得！', '1', '2018-04-19 20:24:12', null, null);
INSERT INTO `comment` VALUES ('5', '2', '3', '3.5', '维修人员非常耐心，手艺也很好！', '1', '2018-04-19 20:24:52', null, null);
INSERT INTO `comment` VALUES ('6', '2', '5', '4.5', '非常好，价格十分公道！', '1', '2018-04-19 20:25:13', null, null);
INSERT INTO `comment` VALUES ('7', '3', '2', '3.5', '还可以吧。', '1', '2018-04-19 20:26:01', '2018-04-19 20:45:56', null);
INSERT INTO `comment` VALUES ('8', '3', '3', '3.0', '这家店挺一般的，不过还不至于有多遭，将就吧。', '1', '2018-04-19 20:26:46', '2018-04-19 20:45:25', null);
INSERT INTO `comment` VALUES ('9', '3', '4', '3.0', '价格公道，还好。', '1', '2018-04-19 20:27:27', null, null);
INSERT INTO `comment` VALUES ('10', '3', '5', '2.5', '维修人员技术一般般，时间也稍长了，不过习惯好评吧。', '1', '2018-04-19 20:28:06', null, null);
INSERT INTO `comment` VALUES ('11', '1', '3', '4.0', '挺好的，下次再来！', '1', '2018-04-19 20:38:26', '2018-04-19 20:38:41', null);
INSERT INTO `comment` VALUES ('12', '4', '1', '3.5', '维修人员非常耐心，手艺也很好！', '1', '2018-04-20 02:19:30', '2018-04-20 02:19:41', null);
INSERT INTO `comment` VALUES ('13', '5', '4', '2.5', '还可以吧。', '1', '2018-04-20 02:20:07', '2018-04-20 02:20:13', null);
INSERT INTO `comment` VALUES ('14', '6', '2', '4.0', '价格公道，下次还来！', '1', '2018-04-20 02:20:46', null, null);

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `cust_id` int(10) NOT NULL AUTO_INCREMENT,
  `cust_phone` varchar(11) DEFAULT NULL,
  `cust_password` varchar(255) DEFAULT NULL,
  `cust_email` varchar(255) DEFAULT NULL,
  `cust_name` varchar(255) DEFAULT NULL,
  `car_brand` varchar(255) DEFAULT NULL,
  `car_id` varchar(20) DEFAULT NULL,
  `cust_portrait` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  `taken` varchar(255) DEFAULT NULL,
  `verification` varchar(255) DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cust_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('1', '13900000000', 'e10adc3949ba59abbe56e057f20f883e', '1424600791@qq.com', 'Jundger', '奥迪', 'A8', 'http://120.79.183.78/images/head_portrait_test01.png', '2018-04-13 23:48:02', '2018-10-01 20:51:53', '192.168.1.103', null, '4f34bcd4cfffd1991506462ef90ee0f6', null);
INSERT INTO `customer` VALUES ('2', '13800000000', 'e10adc3949ba59abbe56e057f20f883e', '56931564@qq.com', 'Kangkang', '奔驰', 'C201', 'http://120.79.183.78/images/12096727_170936667107_2.jpg', '2018-04-19 20:17:58', '2018-10-01 21:05:26', '192.168.1.106', null, '9573cba65e03a4a9e18a3ced0db2d182', null);
INSERT INTO `customer` VALUES ('3', '15115468489', '670b14728ad9902aecba32e22fa4f6bd', '123456@qq.com', 'Jack', '宝马', 'F59', 'http://120.79.183.78/images/12.jpg', '2018-04-19 20:20:09', '2018-06-27 16:00:56', '223.104.25.95', null, null, null);
INSERT INTO `customer` VALUES ('4', '17895348494', '670b14728ad9902aecba32e22fa4f6bd', 'heny@qq.com', 'Heny', '捷克', 'D20', 'http://120.79.183.78/images/13061442437495.jpg', '2018-04-19 20:21:21', '2018-06-27 16:01:00', '223.104.25.95', null, null, null);
INSERT INTO `customer` VALUES ('5', '18654895583', '670b14728ad9902aecba32e22fa4f6bd', 'marry@foxmail.com', 'Marry', '保时捷', 'A380', 'http://120.79.183.78/images/110112141946716.jpg', '2018-04-19 20:22:29', '2018-06-27 16:01:04', '192.168.1.102', null, null, null);
INSERT INTO `customer` VALUES ('12', '13500000000', 'e10adc3949ba59abbe56e057f20f883e', '1242891935@qq.com', '仔仔', null, null, 'http://120.79.183.78/images/head_portrait_test01.png', '2018-05-05 13:33:46', '2018-06-27 16:01:12', '113.250.237.204', null, '1d311574c5673f0abdfedf927ef88470', null);
INSERT INTO `customer` VALUES ('13', '17862313371', 'e10adc3949ba59abbe56e057f20f883e', '3513171560@qq.com', null, null, null, 'http://120.79.183.78/images/head_portrait_test01.png', '2018-08-06 09:34:43', '2018-09-27 15:34:37', '223.104.189.234', null, 'adc8b839f7d818bf166f26fe3fc1601d', null);

-- ----------------------------
-- Table structure for fault_code
-- ----------------------------
DROP TABLE IF EXISTS `fault_code`;
CREATE TABLE `fault_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `system` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `describe` varchar(255) DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fault_code
-- ----------------------------
INSERT INTO `fault_code` VALUES ('1', 'P107801', '动力总成系统', '尼桑（日产）、英菲尼迪', '排气阀门正时控制位置传感器-电路故障', null);
INSERT INTO `fault_code` VALUES ('2', 'B009A', '车身系统', '所有汽车制造商', '一般由安全带传感器，其电路或接头故障所致', null);
INSERT INTO `fault_code` VALUES ('3', 'U0112', '网络通讯系统', '所有汽车制造商', '与电池能量控制模块B通讯丢失', null);
INSERT INTO `fault_code` VALUES ('4', 'U0556', '所有系统', '所有汽车制造商', '与电池能量控制模块B通讯丢失', null);

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of feedback
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) NOT NULL,
  `cust_id` int(11) NOT NULL,
  `resolver_id` int(11) DEFAULT NULL,
  `resolve_status` varchar(255) DEFAULT NULL COMMENT '状态（已解决/未解决)',
  `resolver_name` varchar(255) DEFAULT NULL,
  `total_fee` varchar(255) DEFAULT NULL,
  `pay_status` varchar(255) DEFAULT NULL,
  `comment_status` varchar(255) DEFAULT NULL,
  `longitude` float(10,6) DEFAULT NULL,
  `latitude` float(10,6) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `describe` varchar(255) DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_cust` (`cust_id`),
  KEY `fk_order_repairman` (`resolver_id`),
  KEY `order_no` (`order_no`),
  CONSTRAINT `fk_order_cust` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`),
  CONSTRAINT `fk_order_repairman` FOREIGN KEY (`resolver_id`) REFERENCES `repairman` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('84', '20181002013715551', '1', '1', 'FINISH', '莫师傅', null, 'NO', 'NO', '106.528267', '29.455921', '2018-10-02 13:37:16', '2018-10-02 13:51:35', '排气阀门正时控制位置传感器-电路故障; 一般由安全带传感器，其电路或接头故障所致;', 'P107801; B009A;');
INSERT INTO `order` VALUES ('85', '20181002015351592', '1', '1', 'FINISH', '莫师傅', null, 'NO', 'NO', '106.528267', '29.455921', '2018-10-02 13:53:52', '2018-10-02 13:54:32', '排气阀门正时控制位置传感器-电路故障; 一般由安全带传感器，其电路或接头故障所致;', 'P107801; B009A;');

-- ----------------------------
-- Table structure for order_code
-- ----------------------------
DROP TABLE IF EXISTS `order_code`;
CREATE TABLE `order_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `code_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_relate_code` (`code_id`),
  KEY `fk_relate_order` (`order_id`),
  CONSTRAINT `fk_relate_code` FOREIGN KEY (`code_id`) REFERENCES `fault_code` (`id`),
  CONSTRAINT `fk_relate_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_code
-- ----------------------------
INSERT INTO `order_code` VALUES ('120', '84', '1');
INSERT INTO `order_code` VALUES ('121', '84', '2');
INSERT INTO `order_code` VALUES ('122', '85', '1');
INSERT INTO `order_code` VALUES ('123', '85', '2');

-- ----------------------------
-- Table structure for order_notify
-- ----------------------------
DROP TABLE IF EXISTS `order_notify`;
CREATE TABLE `order_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `order_no` varchar(255) DEFAULT NULL,
  `repairman_id` int(11) DEFAULT NULL,
  `repaiaman_phone` varchar(255) DEFAULT NULL,
  `flag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `foreign_key_repairman_id` (`repairman_id`),
  KEY `foreign_key_repairman_phone` (`repaiaman_phone`),
  KEY `foreign_key_order_id` (`order_id`),
  KEY `foreign_key_order_no` (`order_no`),
  CONSTRAINT `foreign_key_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`),
  CONSTRAINT `foreign_key_order_no` FOREIGN KEY (`order_no`) REFERENCES `order` (`order_no`),
  CONSTRAINT `foreign_key_repairman_id` FOREIGN KEY (`repairman_id`) REFERENCES `repairman` (`id`),
  CONSTRAINT `foreign_key_repairman_phone` FOREIGN KEY (`repaiaman_phone`) REFERENCES `repairman` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_notify
-- ----------------------------
INSERT INTO `order_notify` VALUES ('70', '84', '20181002013715551', null, '13588888888', 'ACCEPT');
INSERT INTO `order_notify` VALUES ('71', '85', '20181002015351592', null, '13588888888', 'ACCEPT');

-- ----------------------------
-- Table structure for repairman
-- ----------------------------
DROP TABLE IF EXISTS `repairman`;
CREATE TABLE `repairman` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `portrait` varchar(255) DEFAULT NULL,
  `regist_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `verification` varchar(255) DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of repairman
-- ----------------------------
INSERT INTO `repairman` VALUES ('1', '莫师傅', 'e10adc3949ba59abbe56e057f20f883e', '13588888888', 'repair@qq.com', 'http://120.79.183.78/images/12.jpg', '2018-04-18 20:51:00', '2018-10-01 19:31:46', '4f34bcd4cfffd1991506462ef90ee0f6', '192.168.1.103', null, null);
INSERT INTO `repairman` VALUES ('2', '康师傅', '670b14728ad9902aecba32e22fa4f6bd', '18200000000', 'kang@163.com', 'http://120.79.183.78/images/head_portrait_test01.png', '2018-04-18 21:59:59', '2018-09-30 12:57:25', null, null, null, null);
INSERT INTO `repairman` VALUES ('3', '李师傅', 'e10adc3949ba59abbe56e057f20f883e', '15100000000', 'lisan@foxmail.com', 'http://120.79.183.78/images/head_portrait_test01.png', '2018-04-18 22:01:05', '2018-09-30 12:57:12', null, null, null, null);
INSERT INTO `repairman` VALUES ('6', 'Candy', 'e10adc3949ba59abbe56e057f20f883e', '18875198367', '934035362@qq.com', 'http://120.79.183.78/images/110112141946716.jpg', '2018-09-30 12:09:25', '2018-10-01 21:04:23', '9ae0cb7c5f8195844f918d485229750c', '192.168.1.106', null, null);
INSERT INTO `repairman` VALUES ('7', '林师傅', '670b14728ad9902aecba32e22fa4f6bd', '17800000000', 'linkang@foxmail.com', 'http://120.79.183.78/images/head_portrait_test01.png', '2018-09-30 12:56:54', '2018-09-30 12:59:37', '1d311574c5673f0abdfedf927ef88470', '223.104.25.95', '', '');
INSERT INTO `repairman` VALUES ('8', '王师傅', 'e10adc3949ba59abbe56e057f20f883e', '13623100000', 'laowang@qq.com', 'http://120.79.183.78/images/head_portrait_test01.png', '2018-09-30 12:59:29', '2018-09-30 13:01:03', 'adc8b839f7d818bf166f26fe3fc1601d', '113.250.99.184', null, null);

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  `describe` varchar(255) DEFAULT NULL,
  `liked` int(10) DEFAULT NULL COMMENT '点赞数',
  `longitude` float(10,6) DEFAULT NULL,
  `latitude` float(10,6) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `property` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_site_repairman` (`owner_id`),
  CONSTRAINT `fk_site_repairman` FOREIGN KEY (`owner_id`) REFERENCES `repairman` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of site
-- ----------------------------
INSERT INTO `site` VALUES ('1', '重庆车行舰汽车维修店', '6', '重庆市巴南区花溪街道红光大道66号', '精湛的技术，走心的服务，我们一定不会让你失望的！', '1260', '107.531815', '29.455921', 'http://120.79.183.78/images/service_point01.png', '汽车销售', '');
INSERT INTO `site` VALUES ('2', '重庆长安铃木汽车销售服务有限公司', '2', '重庆市巴南区融汇半岛外河坪枢纽站对面', '这个人很懒，什么都没有留下……', '5', '106.520180', '29.461691', 'http://120.79.183.78/images/service_point02.png', '汽车销售', '');
INSERT INTO `site` VALUES ('3', '重庆东风风神同捷专营店', '1', '重庆市巴南区民主新街168号', '这个人很懒，什么都没有留下……', '8', '106.531815', '29.455921', 'http://120.79.183.78/images/service_point03.png', '汽车销售', '');
INSERT INTO `site` VALUES ('4', '重庆泰正汽修厂', '3', '重庆市巴南区花溪街道建设大道60号', '这个人很懒，什么都没有留下……', '35', '106.519699', '29.444662', 'http://120.79.183.78/images/e07c2deeb8970a7a78df3c7193da6647.png', '汽车维修', '');
INSERT INTO `site` VALUES ('5', '安翔汽车专业保养维修店', '7', '重庆市巴南区王家坝路1号', '这个人很懒，什么都没有留下……', '566', '106.541885', '29.449911', 'http://120.79.183.78/images/44f156b255d817b22fd1ec7251dc396b.png', '汽车维修', '');
INSERT INTO `site` VALUES ('6', '豪峰汽车保养维修', '8', '重庆市巴南区巴南大道6号1幢附1号', '这个人很懒，什么都没有留下……', '23', '106.547493', '29.463280', 'http://120.79.183.78/images/bbc386573523b76306d15171ec45eeeb.png', '汽车保养', '');
DROP TRIGGER IF EXISTS `article_create`;
DELIMITER ;;
CREATE TRIGGER `article_create` BEFORE INSERT ON `article` FOR EACH ROW set new.create_time = now()
;
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `collect_create`;
DELIMITER ;;
CREATE TRIGGER `collect_create` BEFORE INSERT ON `collect` FOR EACH ROW set new.create_time = now()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `create`;
DELIMITER ;;
CREATE TRIGGER `create` BEFORE INSERT ON `customer` FOR EACH ROW set new.create_time = now()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update`;
DELIMITER ;;
CREATE TRIGGER `update` BEFORE UPDATE ON `customer` FOR EACH ROW set new.update_time = now()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `feedback_create`;
DELIMITER ;;
CREATE TRIGGER `feedback_create` BEFORE INSERT ON `feedback` FOR EACH ROW set new.create_time = now()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `order_update`;
DELIMITER ;;
CREATE TRIGGER `order_update` BEFORE UPDATE ON `order` FOR EACH ROW set new.update_time = now()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `repairman_create`;
DELIMITER ;;
CREATE TRIGGER `repairman_create` BEFORE INSERT ON `repairman` FOR EACH ROW set new.regist_time = now()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `repairman_update`;
DELIMITER ;;
CREATE TRIGGER `repairman_update` BEFORE UPDATE ON `repairman` FOR EACH ROW set new.update_time = now()
;;
DELIMITER ;
