package me.icharm.orange.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2019/1/22 17:51
 */
@Slf4j
@Service
public class RedisLockService {

    @Autowired
    StringRedisTemplate redis;

    private static final DefaultRedisScript<Boolean> UNLOCK_SCRIPT;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_SCRIPT = new DefaultRedisScript<>(sb.toString(), Boolean.class);
    }

    /**
     * 加锁, 执行如下命令：
     *  SET key randomValue NX PX 30000
     *
     * @return Boolean
     */
    public Boolean lock(String key, String randomValue, Integer expire) {
        byte[] bytesKey = redis.getStringSerializer().serialize(key);
        byte[] bytesRandomValue = redis.getStringSerializer().serialize(randomValue);
        byte[] bytesNX = redis.getStringSerializer().serialize("NX");
        byte[] bytesPX = redis.getStringSerializer().serialize("PX");
        byte[] bytesExprie = redis.getStringSerializer().serialize(expire.toString());
        String result = redis.execute(connection ->
                        (String) connection.execute("SET", bytesKey, bytesRandomValue, bytesNX, bytesPX, bytesExprie),
                true);
        if ("OK".equals(result))
            return true;
        else
            return false;
    }

    /**
     * 解锁, 执行UNLOCK lua脚本
     *
     * @return Boolean
     */
    public Boolean unlock(String key, String randomValue) {
        return redis.execute(UNLOCK_SCRIPT, Collections.singletonList(key), randomValue);
    }

    /**
     * 使用UUID生成随机码
     *
     * @return String
     */
    public String random() {
        return UUID.randomUUID().toString();
    }

}
