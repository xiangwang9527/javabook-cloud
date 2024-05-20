package cn.javabook.chapter10.dao;

import cn.javabook.cloud.core.parent.BaseDao;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.Duration;

/**
 * Redis数据操作
 *
 */
@Component
public class RedisDao extends BaseDao {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Object get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void set(String key, String object) {
        stringRedisTemplate.opsForValue().set(key, object);
    }

    public void set(String key, String object, long timeouts) {
        stringRedisTemplate.opsForValue().set(key, object, Duration.ofMillis(timeouts));
    }

    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }
}
