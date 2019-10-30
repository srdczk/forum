## 论坛项目


### 资料

[Spring 官网](https://spring.io/)

[Thymeleaf](https://www.thymeleaf.org/)

[Mybatis](https://mybatis.org/mybatis-3/)

[Logback](http://logback.qos.ch/)

### 脚本

```sql
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
```