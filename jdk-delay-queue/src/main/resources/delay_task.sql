/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 12/11/2022 16:04:50
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for delay_task
-- ----------------------------
DROP TABLE IF EXISTS `delay_task`;
CREATE TABLE `delay_task`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `task_id`     varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务id',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `invoke_time` datetime NOT NULL COMMENT '任务指定的执行时间',
    `is_invoked`  tinyint  NOT NULL DEFAULT 0 COMMENT '是否已经执行',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
