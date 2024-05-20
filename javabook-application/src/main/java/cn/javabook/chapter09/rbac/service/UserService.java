package cn.javabook.chapter09.rbac.service;

import cn.javabook.chapter09.rbac.dao.MySQLDao;
import cn.javabook.chapter09.rbac.entity.SysUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 用户Service
 *
 */
@Service
public class UserService {
    @Resource
    private MySQLDao mySQLDao;

    public SysUser queryById(int id) {
        String sql = "SELECT id, username, password, createtime, updatetime FROM sys_user WHERE id = ?";
        return (SysUser) mySQLDao.findOne(sql, new SysUser(), id);
    }

    public SysUser queryByUsername(String username) {
        String sql = "SELECT id, username, password, createtime, updatetime FROM sys_user WHERE username = ?";
        return (SysUser) mySQLDao.findOne(sql, new SysUser(), username);
    }

    public List<SysUser> queryAll() {
        String sql = "SELECT id, username, password, createtime, updatetime FROM sys_user";
        return mySQLDao.find(sql, new SysUser());
    }
}
