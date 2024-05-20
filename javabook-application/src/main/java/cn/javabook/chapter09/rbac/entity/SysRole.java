package cn.javabook.chapter09.rbac.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.jdbc.core.RowMapper;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 角色entity
 *
 */
public class SysRole implements Serializable, RowMapper<SysRole> {
    private static final long serialVersionUID = 6980192718775578676L;

    private int id;
    private int parentid;
    private String parentids;
    private String name;
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
    public SysRole mapRow(ResultSet result, int i) throws SQLException {
        SysRole role = new SysRole();

        role.setId(result.getInt("id"));
        role.setParentid(result.getInt("parentid"));
        role.setParentids(result.getString("parentids"));
        role.setName(result.getString("name"));
        role.setCreatetime(result.getTimestamp("createtime"));
        role.setUpdatetime(result.getTimestamp("updatetime"));

        return role;
    }
}
