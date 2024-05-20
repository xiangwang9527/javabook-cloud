package cn.javabook.chapter09.rbac.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.jdbc.core.RowMapper;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用户entity
 *
 */
public class SysUser implements Serializable, RowMapper<SysUser> {
    private static final long serialVersionUID = -1214743110268373599L;

    private int id;
    private int bid;
    private String username;
    private String password;
    private int scope; // 0：全部，1：部门及以下，2：仅个人
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

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
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
    public SysUser mapRow(ResultSet result, int i) throws SQLException {
        SysUser user = new SysUser();

        user.setId(result.getInt("id"));
        user.setUsername(result.getString("username"));
        user.setPassword(result.getString("password"));
        user.setCreatetime(result.getTimestamp("createtime"));
        user.setUpdatetime(result.getTimestamp("updatetime"));

        return user;
    }
}
