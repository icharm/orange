package me.icharm.orange.Service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Repository.Common.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Provide User info by token
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/23 15:05
 */
@Slf4j
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
     * @return Optional
     */
    public Optional<User> findByToken(String token) {
        String jsonUser = redisService.get(token);
        User user = JSON.parseObject(jsonUser, User.class);
        return Optional.ofNullable(user);
    }

    /**
     * Refresh user into redis.
     *
     * @param token String
     * @param user User
     */
    public void refresh(String token, User user) {
        String jsonUser = JSON.toJSONString(user);
        redisService.set(token, jsonUser, 21600L);
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

    /**
     * Update user info of redis and authentication.
     *
     * @param user User
     */
    public void reAuth(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();
        // updata redis data.
        refresh(token, user);
        // updata authentication.
        authentication = new UsernamePasswordAuthenticationToken(user, token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}