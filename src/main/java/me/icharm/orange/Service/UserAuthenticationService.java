package me.icharm.orange.Service;

//import org.springframework.stereotype.Service;

/**
 * Provide User info by token
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/23 15:05
 */

import com.alibaba.fastjson.JSON;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Repository.Common.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserAuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisService redisService;

    /**
     * Generate token by UUID.
     * Then Sava into redis and set six hour effective
     *
     * @param user
     * @return
     */
    public String login(User user) {
        // Generate Token by UUID
        String token = UUID.randomUUID().toString();
        // Store in redis
        String jsonUser = JSON.toJSONString(user);
        redisService.set(token, jsonUser, 21600L); // 6 hour
        return token;
    }

    /**
     * Finds a User by its dao-key.
     *
     * @param token User dao key
     * @return
     */
    public Optional<User> findByToken(String token) {
        String jsonUser = redisService.get(token);
        User user = JSON.parseObject(jsonUser, User.class);
        return Optional.ofNullable(user);
    }

    /**
     * Logs out the given input {@code token}.
     *
     * @param token for User to logout
     */
    public void logout(String token) {
        // Delete from redis
        redisService.delete(token);
    }
}