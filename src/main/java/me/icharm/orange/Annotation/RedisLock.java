package me.icharm.orange.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis 单节点分布式锁Annotation， 修饰方法。
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2019/1/23 19:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLock {

    // Lock Name
    String key();

    // Expire time (ms).
    int expire() default 30000; // 30s
}
