package me.icharm.orange.Service;

import lombok.extern.slf4j.Slf4j;
import me.icharm.orange.Model.Common.SystemParameter;
import org.apache.commons.lang.StringUtils;
import me.icharm.orange.Repository.Common.SystemParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 19:52
 */
@Service
@Slf4j
public class SystemParameterService {

    @Autowired
    private SystemParameterRepository spr;

    @Autowired
    RedisService redisService;

    /**
     * Get system parameter by key.
     * This method query redis service first, if not exist, then query database.
     *
     * @param key string
     * @return string
     */
    public String getValueByKey(String key) {
        String value = redisService.get(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        // query database
        SystemParameter systemParameter = spr.findSystemParameterByKeyword(key);
        if (systemParameter == null) {
            log.info("Database not find parameter by key : {}", key);
            return "";
        }
        value = systemParameter.getValue();
        // update redis cache
        redisService.set(key, value);
        return value;
    }

    /**
     * Insert a system parameter to database
     *
     * @param systemParameter SystemParameter
     */
    public void insert(SystemParameter systemParameter) {
        spr.save(systemParameter);
    }

    /**
     * Delete system parameter.
     * 1. delete in redis cache first
     * 2. then delete in database
     *
     * @param key string
     */
    public void deleteByKey(String key) {
        redisService.delete(key);
        List<SystemParameter> systemParameters = spr.findSystemParametersByKeyword(key);
        systemParameters.forEach(systemParameter ->
            spr.delete(systemParameter)
        );
    }
}
