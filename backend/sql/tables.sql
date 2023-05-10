-- Table favorite
CREATE TABLE `favorite`
(
    `id`          bigint unsigned                                       NOT NULL AUTO_INCREMENT,
    `place_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `uid`         bigint unsigned                                       NOT NULL,
    `create_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uni_uid_placeId` (`uid`, `place_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 27
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- Tale feedback
CREATE TABLE `feedback`
(
    `id`          bigint unsigned                                       NOT NULL AUTO_INCREMENT,
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin        NOT NULL,
    `client_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `create_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- Tale review
CREATE TABLE `review`
(
    `id`          bigint unsigned                                       NOT NULL AUTO_INCREMENT,
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin        NOT NULL,
    `place_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `uid`         bigint unsigned                                       NOT NULL,
    `create_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- Tale user
CREATE TABLE `user`
(
    `uid`         bigint unsigned                                        NOT NULL AUTO_INCREMENT,
    `email`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `nickname`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin           DEFAULT NULL,
    `state`       varchar(32) COLLATE utf8mb4_bin                        NOT NULL,
    `token`       varchar(255) COLLATE utf8mb4_bin                                DEFAULT NULL,
    `create_time` datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`uid`),
    UNIQUE KEY `uni_email` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;