package cn.javabook.chapter09.rbac.service;

import cn.javabook.chapter09.rbac.dao.MySQLDao;
import cn.javabook.chapter09.rbac.entity.SysPermission;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 权限Service
 *
 */
@Service
public class PermissionService {
    @Resource
    private MySQLDao mySQLDao;

    // 查询用户-组-角色-权限
    @SuppressWarnings("unchecked")
    public List<SysPermission> queryUGRPByUserId(final int uid) {
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_user AS u, sys_user_group AS ug, sys_group AS g, sys_group_role AS gr, " +
                "sys_role AS r, sys_role_permission AS rp, sys_permission AS p " +
                "WHERE u.id = ? AND u.id = ug.uid AND ug.gid = g.id AND g.id = gr.gid AND gr.rid = r.id " +
                "AND r.id = rp.rid AND rp.pid = p.id AND p.level >= 2";
        return (List<SysPermission>) mySQLDao.find(sql, new SysPermission(), uid);
    }

    // 查询用户-角色-权限
    @SuppressWarnings("unchecked")
    public List<SysPermission> queryURPByUserId(final int uid) {
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_user AS u, sys_user_role AS ur, sys_role AS r, sys_role_permission AS rp, sys_permission AS p " +
                "WHERE u.id = ? AND u.id = ur.uid AND ur.rid = r.id AND r.id = rp.rid AND rp.pid = p.id AND p.level >= 2";
        return (List<SysPermission>) mySQLDao.find(sql, new SysPermission(), uid);
    }

    // 查询用户-权限
    public List<SysPermission> queryUPByUserId(final int uid) {
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_user AS u, sys_user_permission AS up, sys_permission AS p " +
                "WHERE u.id = ? AND u.id = up.uid AND up.pid = p.id AND p.level >= 2";
        return (List<SysPermission>) mySQLDao.find(sql, new SysPermission(), uid);
    }

    // 查询组-角色-权限
    @SuppressWarnings("unchecked")
    public List<SysPermission> queryGRPByGropuId(final int gid) {
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_group AS g, sys_group_role AS gr, sys_role AS r, sys_role_permission AS rp, sys_permission AS p " +
                "WHERE g.id = ? AND g.id = gr.gid AND gr.rid = r.id AND r.id = rp.rid AND rp.pid = p.id AND p.level >= 2";
        return (List<SysPermission>) mySQLDao.find(sql, new SysPermission(), gid);
    }

    // 查询组-权限
    public List<SysPermission> queryByGroupId(final int gid) {
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_group AS u, sys_group_permission AS gp, sys_permission AS p " +
                "WHERE g.id = ? AND g.id = gp.gid AND gp.pid = p.id AND p.level >= 2";
        return (List<SysPermission>) mySQLDao.find(sql, new SysPermission(), gid);
    }

    // 查询角色-权限
    public List<SysPermission> queryByRoleId(final int rid) {
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_role AS r, sys_role_permission AS rp, sys_permission AS p " +
                "WHERE r.id = ? AND r.id = rp.rid AND rp.pid = p.id AND p.level >= 2";
        return mySQLDao.find(sql, new SysPermission(), rid);
    }

    // 查询多个角色-权限
    public List<SysPermission> queryByMultiRoleIds(final String rids) {
        String ids = rids.substring(0, rids.length() - 1);
        String sql = "SELECT p.id, p.parentid, p.parentids, p.name, p.level, p.path, p.createtime, p.updatetime " +
                "FROM sys_role AS r, sys_role_permission AS rp, sys_permission AS p " +
                "WHERE r.id IN(" + ids + ") AND r.id = rp.rid AND rp.pid = p.id AND p.level >= 2";
        return mySQLDao.find(sql, new SysPermission());
    }
}
