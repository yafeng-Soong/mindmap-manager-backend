/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : paper_manager_test

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 15/12/2020 10:54:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
USE mysql;
DELETE FROM user WHERE Host = 'localhost' and User = 'root';
-- UPDATE user SET Host = '%' WHERE User = 'root';
flush privileges;
CREATE DATABASE `paper_manager_test`;
USE `paper_manager_test`;
-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '论文名称',
  `uploader_id` int(11) NOT NULL COMMENT '上传论文的用户id',
  `author` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章作者',
  `keyword` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '论文关键字',
  `abstracts` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '论文摘要',
  `summary` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '暂无概括' COMMENT '自己对论文的概括',
  `publish_year` int(4) NOT NULL COMMENT '发表年份',
  `file_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '文件存储路径',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `publisher_email`(`uploader_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '论文记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (4, '针对SSH匿名流量的网站指纹攻击方法', 1, '顾晓丹，杨明，罗军舟，蒋平', '匿名通信；流量分析；网站指纹攻击；隐马尔科夫模型', '目前在Internet上广泛部署的SSH单代理匿名通信系统利用其动态端口转发功能,在用户和代理之间构建加密隧道,通过对数据进行加密封装和转发,隐藏用户所访问站点的真实地址.为了实现对匿名Web访问的监管,现有工作基于流量分析技术提出了多种针对网站主页的指纹攻击方法,但在如何对目标网站建模、如何选择区分度高的流量特征以提高攻击准确率等问题上仍需进一步的研究.针对这些问题,深入分析SSH匿名流量的特征,提出一种新型的网站指纹攻击方法.该方法基于上下行流量的不同特性,分别抽取不同的区分度高的特征形成上下行指纹,并采取相应的匹配算法进行指纹比对.在此基础上,根据用户访问关联Web页面的行为模式,对所监管的目标网站建立隐马尔科夫模型,将目前只针对网站主页的识别扩展到了多级页面.通过使用公开数据集和在Internet环境中部署实验进行验证,该攻击方法获得了96.8％的准确率,可以有效地识别被监管者所访问的网站.', '暂无概括', 2015, '/files/(1606380967624)针对SSH匿名流量的网站指纹攻击方法_顾晓丹.pdf', '2020-11-20 16:43:07', '2020-11-26 16:56:07');
INSERT INTO `paper` VALUES (5, 'A novel active website fingerprinting attack against Tor anonymous system', 1, 'Gaofeng He; Ming Yang; Xiaodan Gu; Junzhou Luo; Yuanyuan Ma', 'active website fingerprinting; traffic analysis;\r\npattern recognition; anonymous communication; Tor; privacy', 'Tor is a popular anonymizing network and the existing work shows that it can preserve users\' privacy from website fingerprinting attacks well. However, based on our extensive analysis, we find it is the overlap of web objects in returned web pages that make the traffic features obfuscated, thus degrading the attack detection rate. In this paper, we propose a novel active website fingerprinting attack under Tor\'s local adversary model. The main idea resides in the fact that the attacker can delay HTTP requests originated from users for a certain period to isolate responding traffic segments containing different web objects. We deployed our attack in PlanetLab and the experiment lasted for one month. The SVM multi-classification algorithm was then applied on the collected datasets with the introduced features to identify the visited website among 100 top ranked websites in Alexa. Compared to the stat-of-the-art work, the classification result is improved from 48.5% to 65% by delaying at most 10 requests. We also analyzed the timing characteristics of Tor traffic to prove the stealth of our attack. The research results show that anonymity in Tor is not as strong as expected and should be enhanced in the future.', '暂无概括', 2014, '/files/(1606472972361)A novel active website fingerprinting attack against Tor anonymous system.pdf', '2020-11-20 16:49:22', '2020-11-27 18:29:32');
INSERT INTO `paper` VALUES (6, 'An active de-anonymizing attack against tor web traffic', 1, 'Ming Yang; Xiaodan Gu; Zhen Ling; Changxin Yin; Junzhou Luo', 'traffic analysis; active website fingerprinting; anonymous communication; Tor', 'Tor is pervasively used to conceal target websites that users are visiting. A de-anonymization technique against Tor, referred to as website fingerprinting attack, aims to infer the websites accessed by Tor clients by passively analyzing the patterns of encrypted traffic at the Tor client side. However, HTTP pipeline and Tor circuit multiplexing techniques can affect the accuracy of the attack by mixing the traffic that carries web objects in a single TCP connection. In this paper, we propose a novel active website fingerprinting attack by identifying and delaying the HTTP requests at the first hop Tor node. Then, we can separate the traffic that carries distinct web objects to derive a more distinguishable traffic pattern. To fulfill this goal, two algorithms based on statistical analysis and objective function optimization are proposed to construct a general packet delay scheme. We evaluate our active attack against Tor in empirical experiments and obtain the highest accuracy of 98.64%, compared with 85.95% of passive attack. We also perform experiments in the open-world scenario. When the parameter k of k-NN classifier is set to 5, then we can obtain a true positive rate of 90.96% with a false positive rate of 3.9%.', '暂无概括', 2017, '/files/(1606473615157)An active de-anonymizing attack against tor web traffic.pdf', '2020-11-20 16:51:23', '2020-11-27 18:40:15');
INSERT INTO `paper` VALUES (25, 'Tor匿名通信流量在线识别方法', 1, '何高峰, 杨 明, 罗军舟, 张 璐', '匿名通信;Tor;流量识别;TLS 指纹;报文长度分布', '匿名通信技术的滥用给网络监管带来了新的挑战.有效识别出匿名通信流量,是阻止该类技术滥用的前提,具有重要的研究意义和应用价值.现有研究工作侧重于匿名通信关系的确认,无法用于匿名通信流量的识别和阻塞.针对这个问题,围绕广泛使用的Tor匿名通信系统,深入分析运行机制,归纳总结其流量特征.在此基础上,分别提出基于TLS指纹和基于报文长度分布的Tor匿名通信流量识别方法.对两种识别方法的优缺点和适用性进行了详细分析和讨论,并通过CAIDA数据集和在线部署对识别方法进行了验证.实验结果表明,基于TLS指纹和基于报文长度分布的识别方法均能有效识别出Tor匿名通信流量. ', '运用了报文长度来做特征，可以作为流量分析的入门文章', 2013, '/files/(1606474110809)Tor匿名通信流量在线识别方法_何高峰.pdf', '2020-11-27 18:48:30', '2020-11-27 18:48:30');

-- ----------------------------
-- Table structure for paper_tag
-- ----------------------------
DROP TABLE IF EXISTS `paper_tag`;
CREATE TABLE `paper_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `paper_id` int(11) NOT NULL COMMENT '文章id',
  `tag_id` int(11) NOT NULL COMMENT '文章所属标签id',
  `creator_id` int(11) NOT NULL COMMENT '文章标签创建者id',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '文章-标签创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '文章-标签更新时间（可能没用，文章只有打上标签和删除标签操作）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_tag
-- ----------------------------
INSERT INTO `paper_tag` VALUES (4, 4, 11, 1, '2020-11-20 16:45:25', '2020-11-20 16:45:25');
INSERT INTO `paper_tag` VALUES (5, 5, 13, 1, '2020-11-20 16:51:40', '2020-11-20 16:51:40');
INSERT INTO `paper_tag` VALUES (6, 6, 13, 1, '2020-11-20 16:51:47', '2020-11-20 16:51:47');
INSERT INTO `paper_tag` VALUES (17, 25, 5, 1, '2020-11-27 18:48:30', '2020-11-27 18:48:30');
INSERT INTO `paper_tag` VALUES (18, 25, 10, 1, '2020-11-27 18:48:30', '2020-11-27 18:48:30');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标签名',
  `father_id` int(11) NOT NULL DEFAULT 0 COMMENT '父节点id',
  `creator_id` int(11) NOT NULL COMMENT '标签创建者id',
  `theme_id` int(11) NOT NULL COMMENT '所属脑图id',
  `inner_order` tinyint(4) NOT NULL COMMENT '节点在所属父节点的内部顺序',
  `state` tinyint(4) NOT NULL DEFAULT 0 COMMENT '节点状态，0-正常，1-被删除，2-被锁定（创建者不希望节点被改动）',
  `position` tinyint(1) NOT NULL DEFAULT 0 COMMENT '节点是否在脑图左边',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '标签创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '标签更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, '暗网与匿名通信', 0, 1, 1, 0, 0, 0, '2020-11-16 23:45:34', '2020-11-16 23:45:34');
INSERT INTO `tag` VALUES (2, '匿名通信系统', 1, 1, 1, 0, 0, 0, '2020-11-16 23:49:42', '2020-11-30 22:36:35');
INSERT INTO `tag` VALUES (3, '安全攻防', 1, 1, 1, 2, 0, 0, '2020-11-16 23:52:33', '2020-12-02 18:46:32');
INSERT INTO `tag` VALUES (4, 'Survey', 1, 1, 1, 1, 0, 1, '2020-11-16 23:53:53', '2020-12-02 18:46:38');
INSERT INTO `tag` VALUES (5, 'Tor', 2, 1, 1, 0, 0, 0, '2020-11-16 23:54:26', '2020-11-16 23:54:26');
INSERT INTO `tag` VALUES (6, 'I2P', 2, 1, 1, 1, 0, 0, '2020-11-16 23:54:38', '2020-11-30 21:48:51');
INSERT INTO `tag` VALUES (7, 'Freenet', 2, 1, 1, 2, 0, 0, '2020-11-16 23:55:10', '2020-12-02 18:42:34');
INSERT INTO `tag` VALUES (8, 'Zeronet', 2, 1, 1, 3, 0, 0, '2020-11-16 23:55:36', '2020-12-02 18:42:34');
INSERT INTO `tag` VALUES (9, '节点攻击', 3, 1, 1, 0, 0, 0, '2020-11-16 23:57:20', '2020-11-16 23:57:20');
INSERT INTO `tag` VALUES (10, '流量分析', 3, 1, 1, 1, 0, 0, '2020-11-16 23:57:35', '2020-12-02 18:43:36');
INSERT INTO `tag` VALUES (11, '被动单端攻击', 10, 1, 1, 0, 0, 0, '2020-11-16 23:58:32', '2020-11-16 23:58:32');
INSERT INTO `tag` VALUES (12, '被动端到端攻击', 10, 1, 1, 1, 0, 0, '2020-11-16 23:58:54', '2020-11-30 21:49:19');
INSERT INTO `tag` VALUES (13, '主动单端攻击', 10, 1, 1, 2, 0, 0, '2020-11-16 23:59:09', '2020-11-30 21:49:21');
INSERT INTO `tag` VALUES (14, '主动端到端攻击', 10, 1, 1, 3, 0, 0, '2020-11-16 23:59:26', '2020-11-30 21:49:27');
INSERT INTO `tag` VALUES (26, '测试', 4, 1, 1, 0, 1, 0, '2020-12-02 18:45:32', '2020-12-02 18:45:44');
INSERT INTO `tag` VALUES (28, '流水印', 14, 1, 1, 0, 1, 0, '2020-12-02 20:46:13', '2020-12-02 20:46:29');
INSERT INTO `tag` VALUES (35, '网络隐蔽信道', 0, 1, 7, 0, 0, 0, '2020-12-03 21:52:01', '2020-12-03 21:52:01');
INSERT INTO `tag` VALUES (36, 'Survey', 35, 1, 7, 0, 0, 1, '2020-12-03 21:52:10', '2020-12-03 21:53:02');
INSERT INTO `tag` VALUES (37, '分类', 35, 1, 7, 1, 0, 0, '2020-12-03 21:53:04', '2020-12-03 21:54:03');
INSERT INTO `tag` VALUES (38, '网络隐蔽信道对抗', 35, 1, 7, 2, 0, 0, '2020-12-03 21:54:05', '2020-12-03 21:54:45');
INSERT INTO `tag` VALUES (39, '时间型网络隐蔽信道', 37, 1, 7, 0, 0, 0, '2020-12-03 21:54:46', '2020-12-04 21:14:56');
INSERT INTO `tag` VALUES (40, '存储型网络隐蔽信道', 37, 1, 7, 1, 0, 0, '2020-12-03 21:55:02', '2020-12-03 21:55:15');
INSERT INTO `tag` VALUES (41, '消除技术', 38, 1, 7, 0, 0, 0, '2020-12-03 21:55:46', '2020-12-03 21:56:07');
INSERT INTO `tag` VALUES (42, '限制技术', 38, 1, 7, 1, 0, 0, '2020-12-03 21:56:09', '2020-12-03 21:56:21');
INSERT INTO `tag` VALUES (43, '检测技术', 38, 1, 7, 2, 0, 0, '2020-12-03 21:56:22', '2020-12-03 21:56:27');
INSERT INTO `tag` VALUES (47, '中心主题', 0, 1, 9, 0, 0, 0, '2020-12-09 22:54:37', '2020-12-09 22:54:37');
INSERT INTO `tag` VALUES (48, '分支主题 1', 47, 1, 9, 4, 3, 0, '2020-12-09 22:54:37', '2020-12-13 21:33:15');
INSERT INTO `tag` VALUES (49, '分支主题 3', 47, 1, 9, 1, 0, 0, '2020-12-09 22:54:37', '2020-12-09 22:54:37');
INSERT INTO `tag` VALUES (51, '分支主题 4', 47, 1, 9, 3, 3, 0, '2020-12-09 22:54:37', '2020-12-13 21:09:13');
INSERT INTO `tag` VALUES (52, '论文相关', 0, 1, 10, 0, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (53, 'Tor相关', 52, 1, 10, 0, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (54, '流水印相关', 52, 1, 10, 1, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (55, '隐蔽信道相关', 52, 1, 10, 2, 0, 1, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (56, '思路借鉴', 52, 1, 10, 3, 0, 1, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (57, 'Tor：The Second-Generation Onion Router. UNENIX Security, 2004.', 53, 1, 10, 0, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (58, 'How to Find Hidden Users A Survey of Attacks on Anonymity Networks. CST, 2015.', 53, 1, 10, 1, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (59, 'Performance and Security Improvements for Tor A Survey.CSUR, 2016.', 53, 1, 10, 2, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (60, 'How Do Tor Users Interact With Onion Services. UNENIX Security, 2018.', 53, 1, 10, 3, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (61, '包载荷', 54, 1, 10, 0, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (62, '流速率', 54, 1, 10, 1, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (63, '包时间 IPD', 54, 1, 10, 2, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (64, '包时间 时间间隔', 54, 1, 10, 3, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (65, '包时间 间隔质心', 54, 1, 10, 4, 0, 0, '2020-12-09 22:59:24', '2020-12-09 22:59:24');
INSERT INTO `tag` VALUES (66, 'Tor机制', 54, 1, 10, 5, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (67, '综述及分类', 55, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (68, '存储型', 55, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (69, '时间型', 55, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (70, '特殊新型', 55, 1, 10, 3, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (71, '检测及防御', 55, 1, 10, 4, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (72, '性能：隐蔽性、鲁棒性、传输效率', 55, 1, 10, 5, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (73, 'Dropping on the Edge Flexibility and Traffic Confirmation in Onion Routing Protocols.PET,2018', 56, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (74, 'I DPID It My Way!A Covert Timing Channel in Software-Defined Networks.ifip Networking, 2018', 56, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (75, 'Sleepy watermark tracing: An active network-based  intrusion response framework. Information Security, 2001.', 61, 1, 10, 0, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (76, 'On flow marking attacks in wireless anonymous communication networks. DCS, 2005.', 62, 1, 10, 0, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (77, 'DSSS based flow marking technique for invisible traceback. SP, 2007.', 62, 1, 10, 1, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (78, 'Robust correlation of enerypted attack traffic through stepping by manipulation of interpacket delays. CCS, 2003.', 63, 1, 10, 0, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (79, 'Adaptive timing-based active watermarking for attack attribution through stepping stones. DCS, 2005.', 63, 1, 10, 1, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (80, 'Tracking anonymous peer-to-peer Volp calls on the Internet. CCS, 2005.', 63, 1, 10, 2, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (81, 'RAINBOW: A robust and invisible non-blind watermark for network flows. NDSS,2009.', 63, 1, 10, 3, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (82, 'Tracing traffie through intermediate hosts that repacketize flows. INFOCOM, 2007.', 64, 1, 10, 0, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (83, 'BotMosaie: Collaborative network watermark for the detection of IRC-based botnets. Journal of Systems and Software, 2013.', 64, 1, 10, 1, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (84, 'SWIRL: A scalable watermark to deteet correlated network flows.NDSS, 2011.', 64, 1, 10, 2, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (85, 'An efficient sequential watermark deteetion model for tracing network attack flows. CSCWD, 2012.', 64, 1, 10, 3, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (86, 'A novel sequential watermark detection model for efficient traceback of secret network attack flows. Journal of Network and Computer Applications, 2013.', 64, 1, 10, 4, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (87, 'DROPWAT An Invisible Network Flow Watermark.itfs, 2017.', 64, 1, 10, 5, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (88, 'INFLOW Inverse Network Flow Watermarking for Detecting Hidden Servers.INFOCOM, 2018.', 64, 1, 10, 6, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (89, 'Network flow watermarking attack on low-latency anonymous communication systems .SP,2007.', 65, 1, 10, 0, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (90, 'An interval centroid based flow watermarking technique for anonymous communication traceback. Journal of Software, 2011.', 65, 1, 10, 1, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (91, 'An interval centroid based spread spectrum watermarking scheme for multi-flow traceback. Journal of Network and Computer Applications, 2012.', 65, 1, 10, 2, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (92, 'An interval centroid based spread spectrum watermark for tracing multiple network flows.SMC,2009.', 65, 1, 10, 3, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (93, 'A double interval centroid-based watermark for network flow traceback.CSCWD, 2010.', 65, 1, 10, 4, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (94, 'Blind packets flow watermark based on centroid of cross inter-packet delays grouping. Journal of Huazhong University of Science and Technology Natural Science Edition, 2015.', 65, 1, 10, 5, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (95, 'Network flow watermarking method based on centroid matching of interval group. PIC, 2015.', 65, 1, 10, 6, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (96, 'Equalized interval centroid based watermarking scheme for stepping stone traceback.DSC, 2016.', 65, 1, 10, 7, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (97, 'The DUSTER Attack Tor Onion Service Attribution Based on Flow Watermarking with Track Hiding.RAID, 2019.', 66, 1, 10, 0, 0, 0, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (98, '定义', 67, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (99, '囚徒模型', 67, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (100, '分类', 67, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (101, 'IP checksum covert channels and selected hash collision. 2001.', 68, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (102, 'Active warden for TCP sequence number base covert channel. In Proc. ICPC, 2015.', 68, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (103, 'Covert messaging through TCP timestamps. In Proc. PET, 2002.', 68, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (104, 'Covert channels in TCP and IP headers. Presentation at DEFCON, 2002.（https://media.defcon.org/DEF%20CON%2010/DEF%20CON%2010%20presentations/DEF%20CON%2010%20-%20hintz-covert.pdf）', 68, 1, 10, 3, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (105, 'IP traceback solutions based on time to live covert channel. In Proc. ICON,2004.', 68, 1, 10, 4, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (106, 'A combinatorial approach to network covert communications with applications in web leaks. In Proc. DSN, 2011.', 68, 1, 10, 5, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (107, 'Covert channels in the TCP/IP protocol suite. First Monday, 2(5), 1997.', 68, 1, 10, 6, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (108, ' CLACK: A network covert channel based on partial acknowledgment encoding. ICC, 2009.', 68, 1, 10, 7, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (109, 'Network covert channels: Design, analysis, detection, and elimination. PhD thesis, Purdue University, 2006.', 69, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (110, 'IP covert timing channels: Design and detection. CCS, 2004.', 69, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (111, 'Model based covert timing channels: Automated modeling and evasion. RAID, 2008.', 69, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (112, 'PHY covert channels: Can you see the idles? NSDI, 2014.', 69, 1, 10, 3, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (113, 'TCP covert timing channels: Design and detection.  DSN, 2008.', 69, 1, 10, 4, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (114, 'Keyboards and covert channels. USENIX Security, 2006.', 69, 1, 10, 5, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (115, 'A Covert Timing Channel Based on Fountain Codes. TrustCom, 2012', 69, 1, 10, 6, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (116, '区块链', 70, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (117, 'ipv6', 70, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (118, '移动网络', 70, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (119, 'Random delays to limit timing covert channel.EISIC, 2016.', 71, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (120, 'IP covert timing channels: Design and detection. CCS, 2004.', 71, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (121, 'Active warden for TCP sequence number base covert channel. ICPC, 2015.', 71, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (122, 'Detecting covert timing channels:An entropy-based approach. CCS, 2007.', 71, 1, 10, 3, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (123, 'On the secrecy of timing based active watermarking trace-back techniques. SP,2006.', 71, 1, 10, 4, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (124, 'NetWarden Mitigating Network Covert Channels while Preserving Performance. UNENIX Security, 2020.', 71, 1, 10, 5, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (125, 'Network Protocol Covert Channels .Countermeasures Techiniques. GCCCE, 2017.', 71, 1, 10, 6, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (126, 'A Study of Network Covert Channel Detection Based on Deep Learning. IMCEC. 2018.', 71, 1, 10, 7, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (127, 'Improving performance of network covert timing channel through Huffman coding. Mathematical & Computer Modelling, 2012.', 72, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (128, 'CoCo: Coding-based covert timing channels for network flows. IH, 2011.', 72, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (129, 'A covert timing channel based on fountain codes.TrustCom, 2012.', 72, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (130, 'A hidden channel method based on TCP timestamp option [Ph.D. Thesis]. Nanjing: Nanjing University of Science and Technology, 2015 (in Chinese with English abstract).', 72, 1, 10, 3, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (131, 'Robust and undetectable steganographic timing channels for i.i.d. traffic.IH, 2010. ', 72, 1, 10, 4, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (132, 'A note on the cofinement problem. lampson, 1973.', 98, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (133, 'The prisoners’ problem and the subliminal channel. 1984.', 99, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (134, '20 years of covert channel modeling and analysis. 1999.', 100, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (135, 'A Pattern-based survey and categorization of network covert channel techniques. ACS, 2015.', 100, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (136, 'A Survey of Key Technologies for Constructing Network Covert Channel. SCN, 2020', 100, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (137, 'Research on a new network covert channel model in blockchain environment.Communications, 2019.', 116, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (138, 'Covert channels in ipv6. PET, 2005.', 117, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (139, 'Research on network concealed channels in ipv6. Journal of Southeast University, 2007.', 117, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (140, 'Ipv6 security: attacks and countermeasures in a nutshell. WOOT,2014.', 117, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (141, 'An end-to-end covert channel via packet dropout for mobile networks. DSN, 2018', 118, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (142, '', 118, 1, 10, 1, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (143, 'A Two-Way VoLTE Covert Channel With Feedback Adaptive to Mobile Network Environment. Access, 2019', 118, 1, 10, 2, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (144, 'A Covert Channel Over VoLTE via Adjusting Silence Periods. Access, 2018', 118, 1, 10, 3, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (145, '隐蔽性、传输效率', 127, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (146, '隐蔽性、鲁棒性、传输效率', 128, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (147, '隐蔽性', 129, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (148, '鲁棒性、传输效率', 130, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (149, '鲁棒性', 131, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (150, 'A Hybrid Covert Channel with Feedback over Mobile Networks. SocialSec, 2019', 142, 1, 10, 0, 0, 1, '2020-12-09 22:59:25', '2020-12-09 22:59:25');
INSERT INTO `tag` VALUES (152, 'Warning\n警告\nAttention\nWarnung\n경고', 0, 1, 12, 0, 0, 0, '2020-12-10 17:00:34', '2020-12-10 17:00:34');
INSERT INTO `tag` VALUES (153, 'This file can not be opened normally, please do not modify and save, otherwise the contents will be permanently lost！', 152, 1, 12, 0, 0, 1, '2020-12-10 17:00:34', '2020-12-10 17:00:34');
INSERT INTO `tag` VALUES (154, '该文件无法正常打开，请勿修改并保存，否则文件内容将会永久性丢失！', 152, 1, 12, 1, 0, 1, '2020-12-10 17:00:34', '2020-12-10 17:00:34');
INSERT INTO `tag` VALUES (155, '該文件無法正常打開，請勿修改並保存，否則文件內容將會永久性丟失！', 152, 1, 12, 2, 0, 1, '2020-12-10 17:00:34', '2020-12-10 17:00:34');
INSERT INTO `tag` VALUES (156, 'この文書は正常に開かないので、修正して保存しないようにしてください。そうでないと、書類の内容が永久に失われます。！', 152, 1, 12, 3, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (157, 'Datei kann nicht richtig geöffnet werden. Bitte ändern Sie diese Datei nicht und speichern Sie sie, sonst wird die Datei endgültig gelöscht werden.', 152, 1, 12, 4, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (158, 'Ce fichier ne peut pas ouvert normalement, veuillez le rédiger et sauvegarder, sinon le fichier sera perdu en permanence. ', 152, 1, 12, 5, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (159, '파일을 정상적으로 열 수 없으며, 수정 및 저장하지 마십시오. 그렇지 않으면 파일의 내용이 영구적으로 손실됩니다!', 152, 1, 12, 6, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (160, 'You can try using XMind 8 Update 3 or later version to open', 153, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (161, '你可以尝试使用 XMind 8 Update 3 或更新版本打开', 154, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (162, '你可以嘗試使用 XMind 8 Update 3 或更新版本打開', 155, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (163, 'XMind 8 Update 3 や更新版を使って開くこともできます', 156, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (164, 'Bitte versuchen Sie, diese Datei mit XMind 8 Update 3 oder später zu öffnen.', 157, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (165, 'Vous pouvez essayer d\'ouvrir avec XMind 8 Update 3 ou avec une version plus récente.', 158, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (166, 'XMind 8 Update 3 또는 이후 버전을 사용하여', 159, 1, 12, 0, 0, 1, '2020-12-10 17:00:35', '2020-12-10 17:00:35');
INSERT INTO `tag` VALUES (182, '暗网与匿名通信', 152, 1, 12, 0, 3, 0, '2020-11-16 23:45:34', '2020-12-13 17:00:36');
INSERT INTO `tag` VALUES (196, '中心主题', 152, 1, 12, 0, 3, 0, '2020-12-09 22:54:37', '2020-12-13 16:23:46');
INSERT INTO `tag` VALUES (202, '中心主题', 152, 1, 12, 0, 3, 0, '2020-12-09 22:54:37', '2020-12-13 18:45:43');
INSERT INTO `tag` VALUES (208, '暗网与匿名通信', 152, 1, 12, 0, 3, 0, '2020-11-16 23:45:34', '2020-12-13 21:05:54');
INSERT INTO `tag` VALUES (224, '分支主题', 47, 1, 9, 5, 0, 0, '2020-12-14 19:45:23', '2020-12-14 19:45:32');

-- ----------------------------
-- Table structure for theme
-- ----------------------------
DROP TABLE IF EXISTS `theme`;
CREATE TABLE `theme`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '脑图名称',
  `creator_id` int(11) NOT NULL COMMENT '脑图创建者id',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '暂无详细描述' COMMENT '脑图描述',
  `state` tinyint(3) NOT NULL DEFAULT 0 COMMENT '脑图状态，0-正常，1-被删除',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '脑图创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '脑图更新时间（内部节点更新也算）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of theme
-- ----------------------------
INSERT INTO `theme` VALUES (1, '暗网与匿名通信', 1, '暂无详细描述', 0, '2020-11-16 19:16:03', '2020-12-13 16:59:30');
INSERT INTO `theme` VALUES (2, '物联网', 1, '暂无详细描述', 0, '2020-11-16 19:16:45', '2020-11-16 19:16:45');
INSERT INTO `theme` VALUES (3, 'WiFi定位', 1, '暂无详细描述', 0, '2020-11-16 19:17:19', '2020-11-16 19:17:19');
INSERT INTO `theme` VALUES (7, '网络隐蔽信道', 1, '网络隐蔽信道关键技术', 0, '2020-12-03 21:52:01', '2020-12-04 21:14:56');
INSERT INTO `theme` VALUES (9, '导入xmind测试', 1, '暂无详细描述', 0, '2020-12-09 22:54:37', '2020-12-14 19:45:32');
INSERT INTO `theme` VALUES (10, '导入测试', 1, '曹长巍学长脑图导入测试', 0, '2020-12-09 22:59:24', '2020-12-11 16:43:16');
INSERT INTO `theme` VALUES (12, '旧版本导入测试', 1, '一个版本不对应的导入', 0, '2020-12-10 17:00:34', '2020-12-13 21:05:54');

-- ----------------------------
-- Table structure for theme_member
-- ----------------------------
DROP TABLE IF EXISTS `theme_member`;
CREATE TABLE `theme_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NOT NULL COMMENT '用户主键id',
  `theme_id` int(11) NOT NULL COMMENT '脑图主键id',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of theme_member
-- ----------------------------

-- ----------------------------
-- Table structure for theme_operation
-- ----------------------------
DROP TABLE IF EXISTS `theme_operation`;
CREATE TABLE `theme_operation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `theme_id` int(11) NOT NULL COMMENT '脑图id',
  `tag_id` int(11) NOT NULL COMMENT '节点id',
  `operator_id` int(11) NOT NULL COMMENT '操作者id',
  `type` tinyint(4) NOT NULL COMMENT '操作类型，1-添加节点，2-删除节点，3-重命名节点，4-移动节点（改变父节点），5-重排序节点（同层移动），6-彻底删除，7-引入节点，8-还原节点',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of theme_operation
-- ----------------------------
INSERT INTO `theme_operation` VALUES (5, 1, 4, 1, 4, '2020-11-30 21:17:34');
INSERT INTO `theme_operation` VALUES (6, 1, 4, 1, 4, '2020-11-30 21:18:22');
INSERT INTO `theme_operation` VALUES (7, 1, 4, 1, 4, '2020-11-30 22:28:55');
INSERT INTO `theme_operation` VALUES (8, 1, 4, 1, 4, '2020-11-30 22:29:11');
INSERT INTO `theme_operation` VALUES (9, 1, 8, 1, 4, '2020-11-30 22:29:49');
INSERT INTO `theme_operation` VALUES (10, 1, 7, 1, 4, '2020-11-30 22:33:22');
INSERT INTO `theme_operation` VALUES (11, 1, 7, 1, 4, '2020-11-30 22:35:36');
INSERT INTO `theme_operation` VALUES (12, 1, 8, 1, 4, '2020-11-30 22:35:43');
INSERT INTO `theme_operation` VALUES (13, 1, 4, 1, 4, '2020-11-30 22:35:55');
INSERT INTO `theme_operation` VALUES (14, 1, 4, 1, 4, '2020-11-30 22:36:08');
INSERT INTO `theme_operation` VALUES (15, 1, 4, 1, 4, '2020-11-30 22:36:13');
INSERT INTO `theme_operation` VALUES (16, 1, 4, 1, 4, '2020-11-30 22:36:33');
INSERT INTO `theme_operation` VALUES (17, 1, 3, 1, 4, '2020-11-30 22:36:35');
INSERT INTO `theme_operation` VALUES (29, 1, 8, 1, 5, '2020-12-01 10:16:48');
INSERT INTO `theme_operation` VALUES (30, 1, 8, 1, 5, '2020-12-01 10:17:07');
INSERT INTO `theme_operation` VALUES (35, 1, 7, 1, 4, '2020-12-02 18:42:13');
INSERT INTO `theme_operation` VALUES (36, 1, 7, 1, 4, '2020-12-02 18:42:34');
INSERT INTO `theme_operation` VALUES (37, 1, 10, 1, 5, '2020-12-02 18:43:12');
INSERT INTO `theme_operation` VALUES (38, 1, 10, 1, 5, '2020-12-02 18:43:36');
INSERT INTO `theme_operation` VALUES (39, 1, 26, 1, 1, '2020-12-02 18:45:32');
INSERT INTO `theme_operation` VALUES (40, 1, 26, 1, 3, '2020-12-02 18:45:41');
INSERT INTO `theme_operation` VALUES (41, 1, 26, 1, 2, '2020-12-02 18:45:44');
INSERT INTO `theme_operation` VALUES (42, 1, 4, 1, 4, '2020-12-02 18:46:32');
INSERT INTO `theme_operation` VALUES (43, 1, 4, 1, 4, '2020-12-02 18:46:38');
INSERT INTO `theme_operation` VALUES (47, 1, 28, 1, 1, '2020-12-02 20:46:13');
INSERT INTO `theme_operation` VALUES (48, 1, 28, 1, 3, '2020-12-02 20:46:27');
INSERT INTO `theme_operation` VALUES (49, 1, 28, 1, 2, '2020-12-02 20:46:29');
INSERT INTO `theme_operation` VALUES (57, 7, 36, 1, 1, '2020-12-03 21:52:56');
INSERT INTO `theme_operation` VALUES (58, 7, 36, 1, 4, '2020-12-03 21:53:02');
INSERT INTO `theme_operation` VALUES (59, 7, 37, 1, 1, '2020-12-03 21:54:03');
INSERT INTO `theme_operation` VALUES (60, 7, 38, 1, 1, '2020-12-03 21:54:45');
INSERT INTO `theme_operation` VALUES (61, 7, 39, 1, 1, '2020-12-03 21:55:01');
INSERT INTO `theme_operation` VALUES (62, 7, 40, 1, 1, '2020-12-03 21:55:15');
INSERT INTO `theme_operation` VALUES (63, 7, 41, 1, 1, '2020-12-03 21:56:08');
INSERT INTO `theme_operation` VALUES (64, 7, 42, 1, 1, '2020-12-03 21:56:21');
INSERT INTO `theme_operation` VALUES (65, 7, 43, 1, 1, '2020-12-03 21:56:27');
INSERT INTO `theme_operation` VALUES (69, 7, 39, 1, 2, '2020-12-04 21:14:56');
INSERT INTO `theme_operation` VALUES (76, 12, 196, 1, 6, '2020-12-13 16:23:46');
INSERT INTO `theme_operation` VALUES (77, 1, 27, 1, 6, '2020-12-13 16:58:53');
INSERT INTO `theme_operation` VALUES (78, 1, 44, 1, 6, '2020-12-13 16:59:30');
INSERT INTO `theme_operation` VALUES (80, 12, 182, 1, 6, '2020-12-13 17:00:36');
INSERT INTO `theme_operation` VALUES (89, 12, 202, 1, 6, '2020-12-13 18:45:43');
INSERT INTO `theme_operation` VALUES (94, 12, 208, 1, 6, '2020-12-13 21:05:54');
INSERT INTO `theme_operation` VALUES (98, 9, 51, 1, 6, '2020-12-13 21:09:13');
INSERT INTO `theme_operation` VALUES (106, 9, 48, 1, 6, '2020-12-13 21:33:15');
INSERT INTO `theme_operation` VALUES (107, 9, 224, 1, 1, '2020-12-14 19:45:32');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户自增主建',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱，用以区分用户',
  `salt` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盐值，加密用',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/imgs/header/default.jpg' COMMENT '头像路径',
  `role` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'user' COMMENT '角色',
  `state` int(2) NULL DEFAULT 0 COMMENT '账户状态，0—未激活，1—激活',
  `signature` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '这家伙很懒，什么也没有写~' COMMENT '签名和简介',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_email`(`email`(10)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456789@qq.com', '1a9532077e17b79578a1a2146b9e800f', '21f3d9dd382fc8574986bd510c86d2e5', '/imgs/header/123456789@qq.com.jpg', 'user', 1, '这家伙很懒，什么也没有写~', '2020-11-14 23:50:10', '2020-11-21 22:09:12');

-- ----------------------------
-- Triggers structure for table paper
-- ----------------------------
DROP TRIGGER IF EXISTS `delete_paper`;
delimiter ;;
CREATE TRIGGER `delete_paper` BEFORE DELETE ON `paper` FOR EACH ROW DELETE FROM paper_tag WHERE paper_id = old.id
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table tag
-- ----------------------------
DROP TRIGGER IF EXISTS `delete_tag`;
delimiter ;;
CREATE TRIGGER `delete_tag` BEFORE DELETE ON `tag` FOR EACH ROW BEGIN
DELETE FROM paper_tag WHERE tag_id = old.id;
DELETE FROM theme_operation WHERE tag_id = old.id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table theme
-- ----------------------------
DROP TRIGGER IF EXISTS `delete_theme`;
delimiter ;;
CREATE TRIGGER `delete_theme` BEFORE DELETE ON `theme` FOR EACH ROW DELETE FROM theme_operation WHERE theme_id = old.id
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table theme_operation
-- ----------------------------
DROP TRIGGER IF EXISTS `insert_trigger`;
delimiter ;;
CREATE TRIGGER `insert_trigger` AFTER INSERT ON `theme_operation` FOR EACH ROW UPDATE theme SET update_time = NOW() WHERE id = new.theme_id
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
