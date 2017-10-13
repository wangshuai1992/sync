-- ----------------------------
-- cloud-config
-- ----------------------------
DROP TABLE IF EXISTS `mapper_config`;
CREATE TABLE `mapper_config` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `gmt_created` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  `app_name` varchar(64) DEFAULT '',
  `tab_name` varchar(64) DEFAULT '',
  `mapper_class` varchar(128) DEFAULT '',
  `do_class` varchar(128) DEFAULT '',
  `query_method` varchar(64) DEFAULT '',
  `insert_method` varchar(64) DEFAULT '',
  `update_method` varchar(64) DEFAULT '',
  `delete_method` varchar(64) DEFAULT '',
  `is_deleted` tinyint(1) DEFAULT 0,
  `remark` varchar(128) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MybatisMapper配置表';



-- ----------------------------
-- cloud-sync
-- ----------------------------
CREATE TABLE `cloud_sync` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `gmt_created` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `is_deleted` tinyint(2) NOT NULL COMMENT '是否删除0否1是',
  `sync_operate_id` bigint(20) NOT NULL COMMENT '同步业务编码',
  `sync_operate_app` varchar(50) NOT NULL COMMENT '同步业务应用',
  `sync_table_name` varchar(50) NOT NULL COMMENT '同步数据表名称',
  `sync_operate_name` varchar(255) NOT NULL COMMENT '同步业务名称',
  `sync_operate_type` varchar(10) NOT NULL COMMENT '同步业务类型',
  `sync_flag` bigint(2) NOT NULL COMMENT '同步标识 0 未同步 1已同步',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8 COMMENT='数据同步表'


CREATE TABLE `cloud_sync_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `gmt_created` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `is_deleted` tinyint(2) NOT NULL COMMENT '是否删除 0否1是',
  `from_id` bigint(20) NOT NULL COMMENT '关联方id',
  `to_id` bigint(20) NOT NULL COMMENT '被关联方id',
  `relate_site_code` varchar(50) NOT NULL COMMENT '关联站点code',
  `relate_resource` varchar(50) NOT NULL COMMENT '关联表名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='同步系统关联关系'