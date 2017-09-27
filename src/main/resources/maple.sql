/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost
 Source Database       : maple

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : utf-8

 Date: 09/27/2017 21:13:38 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `maple_account`
-- ----------------------------
DROP TABLE IF EXISTS `maple_account`;
CREATE TABLE `maple_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) DEFAULT NULL,
  `platform` int(4) DEFAULT NULL COMMENT '平台代码：1-支付宝，4-银行',
  `account` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `driver_id_index` (`driver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_car`
-- ----------------------------
DROP TABLE IF EXISTS `maple_car`;
CREATE TABLE `maple_car` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '车辆表id',
  `car_status` int(11) DEFAULT NULL COMMENT '车辆状态:1-正常运营，0-已赎回 2-库存中',
  `name` varchar(50) NOT NULL COMMENT '车型',
  `plate_number` varchar(20) DEFAULT NULL COMMENT '车牌号码',
  `engine_number` varchar(50) DEFAULT NULL COMMENT '发动机号',
  `vin` varchar(100) DEFAULT NULL COMMENT '车辆识别号',
  `pick_date` date DEFAULT NULL COMMENT '提车日期',
  `car_licence_front_image` varchar(500) DEFAULT NULL COMMENT '行驶证正面图片',
  `car_licence_back_image` varchar(500) DEFAULT NULL COMMENT '行驶证反面图片',
  `c_insurance_image` varchar(500) DEFAULT NULL COMMENT '商业险图片',
  `m_insurance_image` varchar(500) DEFAULT NULL COMMENT '交强险图片',
  `gps_number` varchar(50) DEFAULT NULL COMMENT 'gps串号',
  `gps_phone` varchar(30) DEFAULT NULL COMMENT 'gps手机号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11440 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_cart`
-- ----------------------------
DROP TABLE IF EXISTS `maple_cart`;
CREATE TABLE `maple_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '销售id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `checked` int(11) DEFAULT NULL COMMENT '是否选择,1=已勾选,0=未勾选',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_category`
-- ----------------------------
DROP TABLE IF EXISTS `maple_category`;
CREATE TABLE `maple_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别Id',
  `parent_id` int(11) DEFAULT NULL COMMENT '父类别id当id=0时说明是根节点,一级类别',
  `name` varchar(50) DEFAULT NULL COMMENT '类别名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '类别状态1-正常,2-已废弃',
  `sort_order` int(4) DEFAULT NULL COMMENT '排序编号,同类展示顺序,数值相等则自然排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100032 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_category`
-- ----------------------------
BEGIN;
INSERT INTO `maple_category` VALUES ('100001', '0', '可售车型', '1', null, '2017-03-25 16:46:00', '2017-03-25 16:46:00'), ('100002', '0', '数码3C', '1', null, '2017-03-25 16:46:21', '2017-03-25 16:46:21'), ('100003', '0', '服装箱包', '1', null, '2017-03-25 16:49:53', '2017-03-25 16:49:53'), ('100004', '0', '食品生鲜', '1', null, '2017-03-25 16:50:19', '2017-03-25 16:50:19'), ('100005', '0', '酒水饮料', '1', null, '2017-03-25 16:50:29', '2017-03-25 16:50:29'), ('100006', '100001', '冰箱', '1', null, '2017-03-25 16:52:15', '2017-03-25 16:52:15'), ('100007', '100001', '电视', '1', null, '2017-03-25 16:52:26', '2017-03-25 16:52:26'), ('100008', '100001', '洗衣机', '1', null, '2017-03-25 16:52:39', '2017-03-25 16:52:39'), ('100009', '100001', '空调', '1', null, '2017-03-25 16:52:45', '2017-03-25 16:52:45'), ('100010', '100001', '电热水器', '1', null, '2017-03-25 16:52:54', '2017-03-25 16:52:54'), ('100011', '100002', '电脑', '1', null, '2017-03-25 16:53:18', '2017-03-25 16:53:18'), ('100012', '100002', '手机', '1', null, '2017-03-25 16:53:27', '2017-03-25 16:53:27'), ('100013', '100002', '平板电脑', '1', null, '2017-03-25 16:53:35', '2017-03-25 16:53:35'), ('100014', '100002', '数码相机', '1', null, '2017-03-25 16:53:56', '2017-03-25 16:53:56'), ('100015', '100002', '3C配件', '1', null, '2017-03-25 16:54:07', '2017-03-25 16:54:07'), ('100016', '100003', '女装', '1', null, '2017-03-25 16:54:44', '2017-03-25 16:54:44'), ('100017', '100003', '帽子', '1', null, '2017-03-25 16:54:51', '2017-03-25 16:54:51'), ('100018', '100003', '旅行箱', '1', null, '2017-03-25 16:55:02', '2017-03-25 16:55:02'), ('100019', '100003', '手提包', '1', null, '2017-03-25 16:55:09', '2017-03-25 16:55:09'), ('100020', '100003', '保暖内衣', '1', null, '2017-03-25 16:55:18', '2017-03-25 16:55:18'), ('100021', '100004', '零食', '1', null, '2017-03-25 16:55:30', '2017-03-25 16:55:30'), ('100022', '100004', '生鲜', '1', null, '2017-03-25 16:55:37', '2017-03-25 16:55:37'), ('100023', '100004', '半成品菜', '1', null, '2017-03-25 16:55:47', '2017-03-25 16:55:47'), ('100024', '100004', '速冻食品', '1', null, '2017-03-25 16:55:56', '2017-03-25 16:55:56'), ('100025', '100004', '进口食品', '1', null, '2017-03-25 16:56:06', '2017-03-25 16:56:06'), ('100026', '100005', '白酒', '1', null, '2017-03-25 16:56:22', '2017-03-25 16:56:22'), ('100027', '100005', '红酒', '1', null, '2017-03-25 16:56:30', '2017-03-25 16:56:30'), ('100028', '100005', '饮料', '1', null, '2017-03-25 16:56:37', '2017-03-25 16:56:37'), ('100029', '100005', '调制鸡尾酒', '1', null, '2017-03-25 16:56:45', '2017-03-25 16:56:45'), ('100030', '100005', '进口洋酒', '1', null, '2017-03-25 16:57:05', '2017-03-25 16:57:05'), ('100031', '100028', '软饮料', '1', null, '2017-05-24 23:05:06', '2017-05-24 23:05:09');
COMMIT;

-- ----------------------------
--  Table structure for `maple_co_model`
-- ----------------------------
DROP TABLE IF EXISTS `maple_co_model`;
CREATE TABLE `maple_co_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合作模式表id',
  `car_id` int(11) NOT NULL COMMENT '车辆id',
  `model_type` int(11) NOT NULL COMMENT '合作模式:10-全款缴纳管理费,20-正常租赁,30-租购月供,40-租购周供,31-租购月供合作结束',
  `down_amount` decimal(20,2) DEFAULT NULL COMMENT '首付金额',
  `total_amount` decimal(20,2) DEFAULT NULL COMMENT '总计金额',
  `period_num` int(11) DEFAULT NULL COMMENT '月数',
  `period_start_date` date DEFAULT NULL COMMENT '开始日期',
  `period_end_date` date DEFAULT NULL COMMENT '结束日期',
  `final_amount` decimal(20,2) DEFAULT NULL COMMENT '尾款金额',
  `management_fee` decimal(20,2) DEFAULT NULL COMMENT '管理费金额/每半年',
  `deadline` date DEFAULT NULL COMMENT '全款/尾款交费时间',
  `comment` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `car_id_index` (`car_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=686 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_contact_info`
-- ----------------------------
DROP TABLE IF EXISTS `maple_contact_info`;
CREATE TABLE `maple_contact_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) DEFAULT NULL COMMENT '司机id',
  `wechat` varchar(20) DEFAULT NULL COMMENT '微信号',
  `email` varchar(20) DEFAULT NULL COMMENT '邮箱',
  `mail_add` varchar(20) DEFAULT NULL COMMENT '收件地址',
  `living_add` varchar(20) DEFAULT NULL COMMENT '居住地址',
  `em_name_first` varchar(20) DEFAULT NULL COMMENT '紧急联系人1-姓名',
  `em_phone_first` varchar(20) DEFAULT NULL COMMENT '紧急联系人1-手机',
  `em_add_first` varchar(200) DEFAULT NULL COMMENT '紧急联系人1-详细地址',
  `em_name_second` varchar(20) DEFAULT NULL COMMENT '紧急联系人2-姓名',
  `em_phone_second` varchar(20) DEFAULT NULL COMMENT '紧急联系人2-手机',
  `em_add_second` varchar(200) DEFAULT NULL COMMENT '紧急联系人2-详细地址',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_driver`
-- ----------------------------
DROP TABLE IF EXISTS `maple_driver`;
CREATE TABLE `maple_driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '司机表id',
  `car_id` int(11) DEFAULT NULL COMMENT '车辆id',
  `user_id` int(11) NOT NULL COMMENT '销售id',
  `co_model_id` int(11) DEFAULT NULL COMMENT '合作模式表id',
  `name` varchar(50) NOT NULL COMMENT '司机姓名',
  `id_number` varchar(50) NOT NULL COMMENT '身份证号码',
  `driver_car_image` varchar(500) DEFAULT NULL COMMENT '司机汽车图片',
  `driver_licence_front_image` varchar(500) DEFAULT NULL COMMENT '驾照正本图片',
  `driver_licence_back_image` varchar(500) DEFAULT NULL COMMENT '驾照副本图片',
  `personal_phone` varchar(20) NOT NULL COMMENT '私人手机号',
  `work_phone` varchar(20) DEFAULT NULL COMMENT '工作手机号',
  `driver_licence_file_number` varchar(50) DEFAULT NULL COMMENT '驾照档案号',
  `operation_status` int(4) NOT NULL COMMENT '运营状态:0-意向客户,1-预备上线,2-正常运营,3-解除合作,4-无意向,5-已删除',
  `periods_status` int(4) DEFAULT NULL COMMENT '分期状态:1-正常,2-有逾期,3-逾期三次及以上',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_number` (`id_number`),
  KEY `car_id_index` (`car_id`) USING BTREE,
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61444 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_driver`
-- ----------------------------
BEGIN;
INSERT INTO `maple_driver` VALUES ('61210', '11215', '10', '461', '李林', '522101198111190014', null, null, null, '18086801618', null, null, '2', '1', '2017-09-20 17:00:11', '2017-09-20 17:00:11');
COMMIT;

-- ----------------------------
--  Table structure for `maple_finish_order`
-- ----------------------------
DROP TABLE IF EXISTS `maple_finish_order`;
CREATE TABLE `maple_finish_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` bigint(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `join_model_name` varchar(20) DEFAULT NULL,
  `check_level_name` varchar(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `finish_flowfee` decimal(20,2) DEFAULT NULL,
  `finish_finish_cnt` int(6) DEFAULT NULL,
  `finish_serve_time` decimal(20,2) DEFAULT NULL,
  `finish_online_time` decimal(20,2) DEFAULT NULL,
  `finish_work_distance` decimal(20,2) DEFAULT NULL,
  `finish_fee_time` decimal(20,2) DEFAULT NULL,
  `finish_serve_distance` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `driver_id_index` (`driver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=773 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_insurance`
-- ----------------------------
DROP TABLE IF EXISTS `maple_insurance`;
CREATE TABLE `maple_insurance` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '保险表id',
  `car_id` int(11) NOT NULL COMMENT '车辆id',
  `insurance_type` int(11) NOT NULL COMMENT '保险品种:1-交强险,2-商业险',
  `company_name` varchar(50) DEFAULT NULL COMMENT '保险公司名',
  `insurance_price` decimal(20,2) DEFAULT NULL COMMENT '保险金额',
  `expire_date` date DEFAULT NULL COMMENT '到期时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `car_id_index` (`car_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_order`
-- ----------------------------
DROP TABLE IF EXISTS `maple_order`;
CREATE TABLE `maple_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL COMMENT '销售id',
  `driver_id` int(11) DEFAULT NULL COMMENT '司机id',
  `payment` decimal(20,2) DEFAULT NULL COMMENT '实际付款金额,单位是元,保留两位小数',
  `payment_type` int(4) DEFAULT NULL COMMENT '支付平台:1-支付宝,2-微信,3-现金,4-银行转账',
  `postage` int(10) DEFAULT NULL COMMENT '运费,单位是元   --可删?--',
  `status` int(10) DEFAULT NULL COMMENT '订单状态:0-已取消-10-未付款，20-已付定金，40-已购买保险，50-交易成功，60-交易关闭',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `estimate_time` datetime DEFAULT NULL COMMENT '预计到货时间',
  `estimate_register_time` datetime DEFAULT NULL COMMENT '预计上牌时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_order`
-- ----------------------------
BEGIN;
INSERT INTO `maple_order` VALUES ('28', '1503129908550', '1', '60043', '1.00', '4', '0', '10', null, null, null, null, null, '2017-08-19 16:05:08', '2017-08-19 16:05:08'), ('29', '1503209304769', '1', '60044', '111.00', '4', '0', '10', null, null, null, null, null, '2017-08-20 14:08:24', '2017-08-20 14:08:24'), ('30', '1503209354244', '1', '60045', '11.00', '4', '0', '10', null, null, null, null, null, '2017-08-20 14:09:14', '2017-08-20 14:09:14'), ('31', '1503324652908', '1', '60046', '100000.00', '4', '0', '10', null, null, null, null, null, '2017-08-21 22:10:52', '2017-08-21 22:10:52'), ('32', '1504087717672', '1', '60047', '11.00', '4', '0', '10', null, null, null, null, null, '2017-08-30 18:08:37', '2017-08-30 18:08:37');
COMMIT;

-- ----------------------------
--  Table structure for `maple_order_item`
-- ----------------------------
DROP TABLE IF EXISTS `maple_order_item`;
CREATE TABLE `maple_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单子表id',
  `user_id` int(11) DEFAULT NULL COMMENT '销售id',
  `car_id` int(11) DEFAULT NULL COMMENT '车辆id',
  `order_no` bigint(20) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片地址',
  `current_unit_price` decimal(20,2) DEFAULT NULL COMMENT '生成订单时的商品单价，单位是元,保留两位小数',
  `quantity` int(10) DEFAULT NULL COMMENT '商品数量',
  `total_price` decimal(20,2) DEFAULT NULL COMMENT '商品总价,单位是元,保留两位小数',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_no_index` (`order_no`) USING BTREE,
  KEY `order_no_user_id_index` (`user_id`,`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_order_item`
-- ----------------------------
BEGIN;
INSERT INTO `maple_order_item` VALUES ('6', '1', '10044', '1503129908550', '1', '福瑞迪1.6L双燃料', 'Untitled-1.jpg', '4299.00', '1', '1.00', '2017-08-19 16:05:08', '2017-08-19 16:05:08'), ('7', '1', '10045', '1503209304769', '30', '福瑞迪1.6L双燃料自动', 'Untitled-1.jpg', '4299.00', '1', '111.00', '2017-08-20 14:08:24', '2017-08-20 14:08:24'), ('8', '1', '10046', '1503209354244', '30', '福瑞迪1.6L双燃料自动', 'Untitled-1.jpg', '4299.00', '1', '11.00', '2017-08-20 14:09:14', '2017-08-20 14:09:14'), ('9', '1', '10047', '1503324652908', '1', '东风-悦达起亚-福瑞迪-手动时尚型CNG-2016-手动', 'Untitled-1.jpg', '4299.00', '1', '100000.00', '2017-08-21 22:10:52', '2017-08-21 22:10:52'), ('10', '1', '10049', '1504087717672', '1', '东风-悦达起亚-福瑞迪-手动时尚型CNG-2016-手动', 'Untitled-1.jpg', '4299.00', '1', '11.00', '2017-08-30 18:08:37', '2017-08-30 18:08:37');
COMMIT;

-- ----------------------------
--  Table structure for `maple_pay_info`
-- ----------------------------
DROP TABLE IF EXISTS `maple_pay_info`;
CREATE TABLE `maple_pay_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) DEFAULT NULL COMMENT '司机id',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `pay_platform` int(10) DEFAULT NULL COMMENT '支付平台:1-支付宝,2-微信,3-线下转账',
  `platform_number` varchar(200) DEFAULT NULL COMMENT '支付流水号',
  `platform_status` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `payment_type` int(4) DEFAULT NULL COMMENT '付款类型:1-定金(保证金),2-首付,3-尾款,4-全款',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_pay_info`
-- ----------------------------
BEGIN;
INSERT INTO `maple_pay_info` VALUES ('1', '60043', '1503129908550', '3', '55555555', '0', '2', '2017-08-19 16:19:07', '2017-08-19 16:19:10');
COMMIT;

-- ----------------------------
--  Table structure for `maple_period_payment`
-- ----------------------------
DROP TABLE IF EXISTS `maple_period_payment`;
CREATE TABLE `maple_period_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分期表id',
  `driver_id` int(11) DEFAULT NULL COMMENT '司机id',
  `car_id` int(11) NOT NULL COMMENT '车辆表id',
  `payment` decimal(20,2) NOT NULL COMMENT '实际付款金额,单位是元,保留两位小数',
  `payment_platform` int(4) DEFAULT NULL COMMENT '支付平台:1-支付宝,2-微信,3-现金,4-银行转账',
  `platform_number` varchar(200) DEFAULT NULL COMMENT '支付流水号',
  `platform_status` int(4) DEFAULT NULL COMMENT '支付状态:1-正常,2-逾期,0-作废',
  `comment` varchar(200) DEFAULT NULL COMMENT '备注',
  `pay_time` datetime DEFAULT NULL COMMENT '付款日期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `car_id_index` (`car_id`) USING BTREE,
  KEY `dirver_id_index` (`driver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_period_plan`
-- ----------------------------
DROP TABLE IF EXISTS `maple_period_plan`;
CREATE TABLE `maple_period_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `co_model_id` int(11) DEFAULT NULL COMMENT '合作模式id',
  `amount` decimal(20,2) DEFAULT NULL COMMENT '每期金额',
  `start_date` datetime DEFAULT NULL COMMENT '每期开始日期',
  `end_date` datetime DEFAULT NULL COMMENT '每期结束日期',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `maple_product`
-- ----------------------------
DROP TABLE IF EXISTS `maple_product`;
CREATE TABLE `maple_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `category_id` int(11) NOT NULL COMMENT '分类id,对应maple_category表的主键',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '商品副标题',
  `main_image` varchar(500) DEFAULT NULL COMMENT '产品主图,url相对地址',
  `sub_images` text COMMENT '图片地址,json格式,扩展用',
  `detail` text COMMENT '商品详情',
  `price` decimal(20,2) NOT NULL COMMENT '价格,单位-元保留两位小数',
  `stock` int(11) NOT NULL COMMENT '库存数量',
  `status` int(6) DEFAULT '1' COMMENT '商品状态.1-在售 2-下架 3-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_product`
-- ----------------------------
BEGIN;
INSERT INTO `maple_product` VALUES ('1', '100001', '东风-悦达起亚-福瑞迪-手动时尚型CNG-2016-手动', '起亚-福瑞迪', 'Untitled-1.jpg', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg,42b1b8bc-27c7-4ee1-80ab-753d216a1d49.jpeg,2f1b3de1-1eb1-4c18-8ca2-518934931bec.jpeg', '<p><img alt=\"1TB2WLZrcIaK.eBjSspjXXXL.XXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/ffcce953-81bd-463c-acd1-d690b263d6df.jpg\" width=\"790\" height=\"920\"><img alt=\"2TB2zhOFbZCO.eBjSZFzXXaRiVXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/58a7bd25-c3e7-4248-9dba-158ef2a90e70.jpg\" width=\"790\" height=\"1052\"><img alt=\"3TB27mCtb7WM.eBjSZFhXXbdWpXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/2edbe9b3-28be-4a8b-a9c3-82e40703f22f.jpg\" width=\"790\" height=\"820\"><br></p>', '4299.00', '98', '1', '2017-04-13 19:07:47', '2017-08-30 18:08:37'), ('2', '100001', '一汽-大众-捷达-自动时尚型-2015-手动', '大众-新捷达', 'W020150527328576778659.jpg', null, null, '999999.00', '10', '1', '2017-05-19 20:54:53', '2017-05-19 20:54:56'), ('3', '100001', '东风-雪铁龙-世嘉-手动时尚型CNG-2016-手动', '雪铁龙-世嘉', 'W020150527328576778659.jpg', null, null, '999999.00', '10', '1', '2017-05-19 20:54:53', '2017-05-19 20:54:56'), ('30', '100001', '东风悦达-起亚-福瑞迪-手动原厂CNG-2016-手动', '起亚-福瑞迪', 'Untitled-1.jpg', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg,42b1b8bc-27c7-4ee1-80ab-753d216a1d49.jpeg,2f1b3de1-1eb1-4c18-8ca2-518934931bec.jpeg', '<p><img alt=\"1TB2WLZrcIaK.eBjSspjXXXL.XXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/ffcce953-81bd-463c-acd1-d690b263d6df.jpg\" width=\"790\" height=\"920\"><img alt=\"2TB2zhOFbZCO.eBjSZFzXXaRiVXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/58a7bd25-c3e7-4248-9dba-158ef2a90e70.jpg\" width=\"790\" height=\"1052\"><img alt=\"3TB27mCtb7WM.eBjSZFhXXbdWpXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/2edbe9b3-28be-4a8b-a9c3-82e40703f22f.jpg\" width=\"790\" height=\"820\"><br></p>', '110000.00', '100', '1', '2017-04-13 19:07:47', '2017-08-20 14:09:14'), ('32', '100001', '东风-风神-A60-手动原厂CNG-2016-手动', '东风-风神A60', null, null, null, '190000.00', '100', '1', '2017-08-23 17:06:03', '2017-08-23 17:06:07');
COMMIT;

-- ----------------------------
--  Table structure for `maple_ticket`
-- ----------------------------
DROP TABLE IF EXISTS `maple_ticket`;
CREATE TABLE `maple_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `car_id` int(11) DEFAULT NULL,
  `ticket_times` int(11) DEFAULT NULL COMMENT '违章次数',
  `score` int(11) DEFAULT NULL COMMENT '违章总分',
  `money` int(11) DEFAULT NULL COMMENT '罚款总额',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `car_id_index` (`car_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_ticket`
-- ----------------------------
BEGIN;
INSERT INTO `maple_ticket` VALUES ('1', '11215', '4', '5', '300', '2017-09-24 14:41:36', '2017-09-24 14:41:38'), ('2', '11216', '5', '12', '500', '2017-09-24 15:14:21', '2017-09-24 15:14:21'), ('3', '11227', '17', '39', '1850', '2017-09-24 15:18:53', '2017-09-24 15:18:53');
COMMIT;

-- ----------------------------
--  Table structure for `maple_user`
-- ----------------------------
DROP TABLE IF EXISTS `maple_user`;
CREATE TABLE `maple_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '用户密码，MD5加密',
  `name` varchar(50) NOT NULL COMMENT '用户姓名',
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `question` varchar(100) DEFAULT NULL COMMENT '找回密码问题',
  `answer` varchar(100) DEFAULT NULL COMMENT '找回密码答案',
  `role` int(4) NOT NULL COMMENT '角色0-管理员,1-销售员,2-财务人员,3-主管人员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `maple_user`
-- ----------------------------
BEGIN;
INSERT INTO `maple_user` VALUES ('1', 'admin', '202CB962AC59075B964B07152D234B70', '大力', null, null, null, null, '1', '2017-05-31 15:52:18', '2017-05-31 15:52:21');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
