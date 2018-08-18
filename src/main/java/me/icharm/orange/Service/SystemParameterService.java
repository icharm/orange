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
    private StringRedisTemplate redis;

    /**
     * 通过Key获取系统参数表中value
     * 此方法先查询redis缓存，不存在 再查询数据库
     *
     * @param key string
     * @return string
     */
    public String getValueByKey(String key) {
        String value = redis.opsForValue().get(key);
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
        redis.opsForValue().set(key, value);
        return value;
    }

    /**
     * 插入一条系统参数
     *
     * @param systemParameter SystemParameter
     */
    public void insert(SystemParameter systemParameter) {
        spr.save(systemParameter);
    }

    /**
     * 删除系统参数
     * 1. delete in redis cache first
     * 2. then delete in database
     *
     * @param key string
     */
    public void deleteByKey(String key) {
        redis.delete(key);
        List<SystemParameter> systemParameters = spr.findSystemParametersByKeyword(key);
        systemParameters.forEach(systemParameter -> {
            spr.delete(systemParameter);
        });
    }
}
