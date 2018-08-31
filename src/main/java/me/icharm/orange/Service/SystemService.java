package me.icharm.orange.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/25 10:11
 */
@Service
public class SystemService {

    @Autowired
    Environment environment;


    /**
     * Return root path of spring server
     *
     * @return string
     */
    public String rootPathOfServer() {
        return "http://" + environment.getProperty("server.address");
    }
}
