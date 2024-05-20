package cn.javabook.chapter13;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nginx负载均衡服务
 *
 */
@RestController
public class NginxLoadBalance {
    @GetMapping("/test")
    public String test(final String username) {
        return "hello, " + username + " from server01";
    }
}
