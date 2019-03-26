CREATE TABLE `t_id_gen` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `last_issued` bigint(19) NOT NULL DEFAULT '0',
  `project_name` varchar(64) NOT NULL COMMENT '项目',
  `model_name` varchar(64) NOT NULL COMMENT '需要生成id的实体',
  `steps` int(10) NOT NULL DEFAULT '10' COMMENT '步长',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_bu_project_model` (`project_name`,`model_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

