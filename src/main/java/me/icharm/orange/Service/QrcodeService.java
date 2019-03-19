package me.icharm.orange.Service;

import me.icharm.orange.Constant.Common.UserStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Qrcode token management on redis.
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/11/30 16:08
 */
@Service
public class QrcodeService {

    @Autowired
    RedisService redisService;

    private static String PREFIX = "QRCODE_";

    // 30s
    private static Long EXPRIE = 30L;

    /**
     * Generate qrcode token which value is status.
     *
     * @return String
     */
    public String token() {
        // Generate Token by UUID
        String token = UUID.randomUUID().toString();
        redisService.set(PREFIX + token, UserStatusEnum.CREATION.code, EXPRIE);
        return token;
    }

    /**
     * Query qrcode status.
     *
     * @param token String
     * @return Integer
     */
    public Integer status(String token) {
        String status = redisService.get(PREFIX + token);
        if (StringUtils.isBlank(status))
            return UserStatusEnum.UNABLE.code;
        return Integer.valueOf(status);
    }

    /**
     * Delete qrcode token.
     *
     * @param token String
     */
    public void delete(String token) {
        redisService.delete(PREFIX + token);
    }

    /**
     * Update qrcode status.
     *
     * @param token String
     * @param status Integer
     */
    public void update(String token, Integer status) {
        redisService.set(PREFIX + token, status, EXPRIE * 10);
    }
}
