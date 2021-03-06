DROP TABLE IF EXISTS user;
CREATE TABLE user
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(50),
  password VARCHAR(50),
  salt VARCHAR(50),
  email VARCHAR(100),
  type INT DEFAULT 0,
  status INT DEFAULT 0,
  activation_code VARCHAR(100),
  avatar VARCHAR(200),
  gmt_create BIGINT,
  KEY index_username (username(20)),
  KEY index_email (email(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS discuss_post;
CREATE TABLE discuss_post
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(100),
    content TEXT,
    type INT DEFAULT 0 COMMENT '普通-0，置顶1',
    status INT DEFAULT 0 COMMENT '0-正常 1-精华 2-拉黑',
    gmt_create BIGINT,
    comment_count INT DEFAULT 0,
    score INT DEFAULT 0 COMMENT '分数-精华帖子排序',
    KEY index_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS login_ticket;
CREATE TABLE login_ticket
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    ticket VARCHAR(45) NOT NULL,
    status INT DEFAULT 0,
    expired BIGINT NOT NULL,
    KEY  index_ticket (ticket(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS comment;
CREATE TABLE comment
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  entity_type INT NOT NULL,
  entity_id INT NOT NULL,
  target_id INT NOT NULL,
  content TEXT,
  status INT DEFAULT 0,
  gmt_create BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS message;

CREATE TABLE message
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  from_id INT NOT NULL,
  to_id INT NOT NULL,
  conversation_id VARCHAR(32) NOT NULL,
  content TEXT,
  status INT DEFAULT 0 COMMENT '0 未读 1 已经读取',
  gmt_create BIGINT,
  KEY  index_conversation_id(conversation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
