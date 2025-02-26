/*
 Navicat Premium Dump SQL

 Source Server         : 本地连接
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : i18n-demo

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 26/02/2025 02:22:44
*/

create database `i18n-demo`;

use `i18n-demo`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_base
-- ----------------------------
DROP TABLE IF EXISTS `business_base`;
CREATE TABLE `business_base`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '业务唯一标识',
  `org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属租户ID（与国际化表租户体系一致）',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '业务简介',
  `deleted` int NULL DEFAULT 1 COMMENT '1:已删除 0:正常',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business_base
-- ----------------------------
INSERT INTO business_base (id, org_id, name, description, deleted, create_time, update_time) VALUES (1, 'ORG_01', '电商系统', '面向消费者的在线购物平台', 0, '2025-02-26 01:01:56', '2025-02-26 01:01:56');
INSERT INTO business_base (id, org_id, name, description, deleted, create_time, update_time) VALUES (2, 'ORG_02', '财务系统', '企业级财务管理系统', 0, '2025-02-26 01:01:56', '2025-02-26 01:01:56');
INSERT INTO business_base (id, org_id, name, description, deleted, create_time, update_time) VALUES (3, 'ORG_01', '支付系统', '第三方支付接口服务', 0, '2025-02-26 01:01:56', '2025-02-26 01:01:56');

-- ----------------------------
-- Table structure for international
-- ----------------------------
DROP TABLE IF EXISTS `international`;
CREATE TABLE `international`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `biz_id` bigint NULL DEFAULT NULL COMMENT '业务id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '国际化内容',
  `type` int NULL DEFAULT NULL COMMENT '类型',
  `disabled` int NULL DEFAULT NULL COMMENT '1：停用 0：启用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `international_table_biz_id_org_id_index`(`biz_id` ASC, `org_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '国际化信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of international
-- ----------------------------
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (1, 'ORG_00', 1000, 'Welcome message for organization 00 (Type:1)', 1, 0, 'zh-CN', '2025-02-26 00:47:42', '2025-02-26 11:52:36');
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (2, 'ORG_01', 1, 'english content 0001', 1, 1, 'en-US', '2025-02-26 00:47:42', '2025-02-26 16:17:31');
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (3, 'ORG_01', 1, '中文内容1', 1, 0, 'zh-CN', '2025-02-26 00:47:42', '2025-02-26 16:17:31');
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (4, 'ORG_01', 3, 'english content 00011213', 1, 0, 'en-US', '2025-02-26 00:47:42', '2025-02-26 17:53:03');
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (5, 'ORG_04', 1004, '中文内容121', 1, 1, 'zh-CN', '2025-02-26 00:47:42', '2025-02-26 14:52:13');
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (6, 'ORG_49', 1049, '中文内容121323232', 1, 0, 'zh-CN', '2025-02-26 00:47:42', '2025-02-26 14:52:13');
INSERT INTO international (id, org_id, biz_id, content, type, disabled, locale, create_time, update_time) VALUES (7, 'ORG_01', 3, '中文内容12312', 1, 0, 'zh-CN', '2025-02-26 00:47:42', '2025-02-26 17:53:03');

SET FOREIGN_KEY_CHECKS = 1;
