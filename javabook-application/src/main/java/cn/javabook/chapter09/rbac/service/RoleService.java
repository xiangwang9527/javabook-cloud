package cn.javabook.chapter09.rbac.service;

import cn.javabook.chapter09.rbac.dao.MySQLDao;
import cn.javabook.chapter09.rbac.entity.SysRole;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 角色Service
 *
 */
@Service
public class RoleService {
    @Resource
    private MySQLDao roleDao;

    // 用户-组-角色
    public List<SysRole> queryUGRByUserId(int uid) {
        String sql = "SELECT r.id, r.parentid, r.parentids, r.name, r.createtime, r.updatetime " +
                "FROM sys_user AS u, sys_user_group AS ug, sys_group AS g, sys_group_role AS gr, sys_role AS r " +
                "WHERE u.id = ? AND u.id = ug.uid AND ug.gid = g.id AND g.id = gr.gid AND gr.rid = r.id";
        return (List<SysRole>) roleDao.find(sql, new SysRole(), uid);
    }

    // 用户-角色
    public List<SysRole> queryURByUserId(int uid) {
        String sql = "SELECT r.id, r.parentid, r.parentids, r.name, r.createtime, r.updatetime " +
                "FROM sys_user AS u, sys_user_role AS ur,  sys_role AS r " +
                "WHERE u.id = ? AND u.id = ur.uid AND ur.rid = r.id";
        return (List<SysRole>) roleDao.find(sql, new SysRole(), uid);
    }

    // 组-角色
    public List<SysRole> queryGRByGroupId(int gid) {
        String sql = "SELECT r.id, r.parentid, r.parentids, r.name, r.createtime, r.updatetime " +
                "FROM sys_group AS g, sys_group_role AS gr, sys_role AS r " +
                "WHERE g.id = ? AND g.id = gr.gid AND gr.rid = r.id";
        return (List<SysRole>) roleDao.find(sql, new SysRole(), gid);
    }

    // 查询角色的子角色
    public List<SysRole> queryChildrenById(int id) {
        String sql = "SELECT id, parentid, parentids, name, createtime, updatetime " +
                "FROM sys_role WHERE parentids LIKE '%" + id + ",%'";
        return (List<SysRole>) roleDao.find(sql, new SysRole());
    }

    // 查询角色的父角色
    public List<SysRole> queryParentsById(final String parentids) {
        String sql = "SELECT id, parentid, parentids, name, createtime, updatetime " +
                "FROM sys_role WHERE id IN(" + parentids.substring(0, parentids.length() - 1) + ")";
        return (List<SysRole>) roleDao.find(sql, new SysRole());
    }
}
