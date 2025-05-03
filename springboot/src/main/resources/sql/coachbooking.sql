CREATE TABLE IF NOT EXISTS `coachbooking` (
  `booking_id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `coach_id` int NOT NULL COMMENT '教练ID',
  `course_id` int DEFAULT NULL COMMENT '课程ID',
  `appointment_time` datetime NOT NULL COMMENT '预约时间',
  `is_private` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为私教服务：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`booking_id`),
  KEY `idx_coach_time` (`coach_id`, `appointment_time`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教练预约表'; 