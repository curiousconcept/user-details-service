CREATE TABLE IF NOT EXISTS `encrypted_user_details` (
  `encrypted_user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_details` MEDIUMBLOB NOT NULL,
  `random_crypt_bytes` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`encrypted_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;