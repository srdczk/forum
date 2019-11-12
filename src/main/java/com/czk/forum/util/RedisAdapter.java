package com.czk.forum.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

@Component
public class RedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisAdapter.class);

    //设置一个连接池, 每次断开在连接消耗过大
    private JedisPool pool = null;

    private Jedis jedis = null;


    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化Jedis 连接池, 池子!
        pool = new JedisPool("localhost", 6379);
    }

    public String get(String key) {
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value) {
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long srem(String key, String value) {
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long incr(String key) {
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long decr(String key) {
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    @Transactional
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        try {
            jedis = pool.getResource();
            String user = String.valueOf(userId);
            String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
            String userKey = RedisUtil.getUserLikeKey(entityUserId);
            if (jedis.sismember(entityKey, user)) {
                jedis.srem(entityKey, user);
                jedis.decr(userKey);
            } else {
                jedis.sadd(entityKey, user);
                jedis.incr(userKey);
            }
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Transactional
    public void follow(int userId, int entityType, int entityId) {

        try {
            jedis = pool.getResource();
            // 某个用户关注的实体:
            // followee:userId:entityType -> zset(entityId, now);
            String followeeKey = RedisUtil.getFolloweeKey(userId, entityType);
            // 某个实体的所有粉丝儿
            // follower:entityType:entityId -> zset(userId, now);
            String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
            jedis.zadd(followeeKey, System.currentTimeMillis(), String.valueOf(entityId));
            jedis.zadd(followerKey, System.currentTimeMillis(), String.valueOf(userId));

        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Transactional
    public void unfollow(int userId, int entityType, int entityId) {
        try {
            jedis = pool.getResource();
            // 某个用户关注的实体:
            // followee:userId:entityType -> zset(entityId, now);
            String followeeKey = RedisUtil.getFolloweeKey(userId, entityType);
            // 某个实体的所有粉丝儿
            // follower:entityType:entityId -> zset(userId, now);
            String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
            jedis.zrem(followeeKey, String.valueOf(entityId));
            jedis.zrem(followerKey, String.valueOf(userId));

        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long zcard(String key) {
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean isMember(String key, String val) {
        try {
            jedis = pool.getResource();
            Double c = jedis.zscore(key, val);
            if (c == null) return false;
            else return true;
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> getPage(String key, int off, int cnt) {
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, off, off + cnt);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public double zscore(String key, String val) {
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, val);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
