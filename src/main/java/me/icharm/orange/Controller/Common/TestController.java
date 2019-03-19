package me.icharm.orange.Controller.Common;

import me.icharm.orange.Service.RedisLockService;
import me.icharm.orange.Util.RedisLockSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2019/1/22 17:19
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    RedisLockService redisLockService;

    @RequestMapping("/redislock")
    public String redisLock() {
//        RedisLockSimple lock = new RedisLockSimple("TEST_LOCK_KEY", 30000);
        String key = "TEST_LOCK_KEY";
        String random = redisLockService.random();
        Integer expire = 90000; // 90s
        try{
            boolean result = redisLockService.lock(key, random, expire);
            System.out.println("Lock result: " + result);
//            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean result = redisLockService.unlock(key, random);
            System.out.println("Unlock result: " + result);
        }
        return "What happened??";
    }
}
