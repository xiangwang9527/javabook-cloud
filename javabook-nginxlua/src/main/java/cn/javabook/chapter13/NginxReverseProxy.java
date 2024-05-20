package cn.javabook.chapter13;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Nginx反向代理
 *
 */
@RestController
public class NginxReverseProxy {
    @GetMapping("/")
    public String index() {
        Map map = new ConcurrentHashMap();
        return "Hello Nginx";
    }
}
