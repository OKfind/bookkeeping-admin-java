-- 用户表
CREATE TABLE `sys_user` (
    `id`          INT          NOT NULL AUTO_INCREMENT  COMMENT '用户id',
    `username`    VARCHAR(50)  NOT NULL                  COMMENT '用户名',
    `password`    VARCHAR(64)  NOT NULL                  COMMENT 'MD5加盐后的密码',
    `salt`        VARCHAR(10)  NOT NULL                  COMMENT 'MD5加密的盐值',
    `nickname`    VARCHAR(50)  DEFAULT NULL              COMMENT '昵称',
    `email`       VARCHAR(100) DEFAULT NULL              COMMENT '邮箱',
    `phone`       VARCHAR(20)  DEFAULT NULL              COMMENT '联系方式',
    `user_pic`    VARCHAR(255) DEFAULT NULL              COMMENT '用户头像',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
