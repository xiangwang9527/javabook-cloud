package cn.javabook.chapter09.rbac.controller;

import cn.javabook.chapter09.rbac.annotations.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 *
 */
@RestController
public class UserController {
    /**
     * 接口访问：
     * 用例一：清漪（uid=7）->组（无）->角色（产品，rid=5），结果：无权限
     * 用例二：姜立（uid=9）->组（无）->角色（客服，rid=4），结果：正常访问
     * 用例三：石昊（uid=3）->组（gid=10001）->角色（客服、产品、运营，rid=4,5,6），结果：正常访问
     *
     */
    @PreAuthorize(role = "客服")
    @RequestMapping(value = "/api/v1.0.0/user/details", method = RequestMethod.GET)
    public String details(String username) {
        return username + " 有查看用户详情的权限";
    }

    /**
     * 接口访问：
     * 用例四：柳神（uid=4）->组（gid=10002）->角色（会计、出纳、库管、配送，rid=7,8,9,10），结果：无权限
     *
     */
    @PreAuthorize(role = "产品")
    @RequestMapping(value = "/api/v1.0.0/system/setting/password", method = RequestMethod.GET)
    public String password(String username) {
        return username + " 有修改密码的权限";
    }
}
