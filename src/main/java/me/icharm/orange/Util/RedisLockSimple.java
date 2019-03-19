package me.icharm.orange.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * 单Redis节点分布式锁实现
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2019/1/21 17:09
 */
@Slf4j
public class RedisLockSimple {

    @Autowired
    StringRedisTemplate redis;


    private final byte[] key;

    /**
     * 随机码，每个线程、客户端唯一。
     */
    private final byte[] randomValue = randomByMac();

    private final byte[] expire;

    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 构造
     *
     * @param key 资源名标识
     * @param expire 过期时间（Second）
     */
    public RedisLockSimple(String key, Integer expire) {
        this.key = redis.getStringSerializer().serialize(key);
        this.expire = redis.getStringSerializer().serialize(expire.toString());
    }

    /**
     * 加锁, 执行如下命令：
     *  SET key randomValue NX PX 30000
     *
     * @return Boolean
     */
    public Boolean lock() {
        final byte[][] bytes = new byte[5][randomValue.length];
        bytes[0] = key;
        bytes[1] = randomValue;
        bytes[2] = redis.getStringSerializer().serialize("NX");
        bytes[3] = redis.getStringSerializer().serialize("PX");
        bytes[4] = expire;
        return redis.execute(connection -> (Boolean) connection.execute("SET", bytes), true);
    }

    /**
     * 解锁, 执行UNLOCK lua脚本
     *
     * @return Boolean
     */
    public Boolean unlock() {
        final byte[][] bytes = new byte[2][randomValue.length];
        bytes[0] = key;
        bytes[1] = randomValue;
        return redis.execute(connection -> (Boolean) connection.execute(UNLOCK_LUA, bytes), true);
    }


    /**
     * 根据本机MAC地址+时间戳生产随机码， 如果出现异常则使用UUID生成随机码
     *
     * @return byte[]
     */
    private byte[] randomByMac() {
        byte[] mac;
        try {
            // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
            mac = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();
        } catch (Exception e) {
            log.error("获取MAC地址出错, 改用UUID随机码", e);
            String str = UUID.randomUUID().toString();
            return redis.getStringSerializer().serialize(str);
        }
        // 加上时间戳
        String date = new Timestamp(System.currentTimeMillis()).toString();
        byte[] dateByte = redis.getStringSerializer().serialize(date);
        byte[] rdm = new byte[mac.length+dateByte.length];
        System.arraycopy(mac, 0, rdm, 0, mac.length);
        System.arraycopy(dateByte, 0, rdm, mac.length, dateByte.length);

        return rdm;
    }
}
