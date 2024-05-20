package cn.javabook.chapter09.rbac.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.jdbc.core.RowMapper;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 权限entity
 *
 */
public class SysPermission implements Serializable, RowMapper<SysPermission> {
    private static final long serialVersionUID = 4121559180789799491L;

    private int id;
    private int parentid;
    private String parentids;
    private String name;
    private int level;
    private String path;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getParentids() {
        return parentids;
    }

    public void setParentids(String parentids) {
        this.parentids = parentids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public SysPermission mapRow(ResultSet result, int i) throws SQLException {
        SysPermission permission = new SysPermission();

        permission.setId(result.getInt("id"));
        permission.setParentid(result.getInt("parentid"));
        permission.setParentids(result.getString("parentids"));
        permission.setName(result.getString("name"));
        permission.setLevel(result.getInt("level"));
        permission.setPath(result.getString("path"));
        permission.setCreatetime(result.getTimestamp("createtime"));
        permission.setUpdatetime(result.getTimestamp("updatetime"));

        return permission;
    }
}
