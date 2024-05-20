package cn.javabook.chapter09.rbac.service;

import cn.javabook.chapter09.rbac.dao.MySQLDao;
import cn.javabook.chapter09.rbac.entity.SysGroup;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组Service
 *
 */
@Service
public class GroupService {
    @Resource
    private MySQLDao mySQLDao;

    public SysGroup queryById(int id) {
        String sql = "SELECT id, parentid, parentids, name, createtime, updatetime FROM sys_group WHERE id = ?";
        return (SysGroup) mySQLDao.findOne(sql, new SysGroup(), id);
    }

    public SysGroup queryByName(String name) {
        String sql = "SELECT id, parentid, parentids, name, createtime, updatetime FROM sys_group WHERE name = ?";
        return (SysGroup) mySQLDao.findOne(sql, new SysGroup(), name);
    }

    // 用户所属的组
    public List<SysGroup> queryByUserid(int uid) {
        String sql = "SELECT g.id, g.parentid, g.parentids, g.name, g.createtime, g.updatetime " +
                "FROM sys_user AS u, sys_user_group AS ug, sys_group AS g " +
                "WHERE u.id = ? AND u.id = ug.uid AND ug.gid = g.id";
        return (List<SysGroup>) mySQLDao.find(sql, new SysGroup(), uid);
    }

    // 查询组所属的子组
    public List<SysGroup> queryChildrenById(int id) {
        String sql = "SELECT id, parentid, parentids, name, createtime, updatetime FROM sys_group WHERE parentids LIKE '%" + id + ",%'";
        return (List<SysGroup>) mySQLDao.find(sql, new SysGroup(), id);
    }

    // 查询组的父组
    public List<SysGroup> queryParentsById(String parentids) {
        String sql = "SELECT id, parentid, parentids, name, createtime, updatetime FROM sys_group WHERE id IN(" + parentids + ")";
        return (List<SysGroup>) mySQLDao.find(sql, new SysGroup(), parentids);
    }
}
