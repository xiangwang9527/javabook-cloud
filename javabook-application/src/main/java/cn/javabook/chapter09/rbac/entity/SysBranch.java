package cn.javabook.chapter09.rbac.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.jdbc.core.RowMapper;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 分支机构entity
 *
 */
public class SysBranch implements Serializable, RowMapper<SysBranch> {
    private static final long serialVersionUID = -1214743110268373599L;

    private int id;
    private int parentid;
    private String parentids;
    private String name;
    private int type; // 机构类型，0：集团；1：公司；2：分支机构；3：部门；4：小组
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    public SysBranch mapRow(ResultSet result, int i) throws SQLException {
        SysBranch user = new SysBranch();

        user.setId(result.getInt("id"));
        user.setParentid(result.getInt("parentid"));
        user.setParentids(result.getString("parentids"));
        user.setName(result.getString("name"));
        user.setType(result.getInt("type"));
        user.setCreatetime(result.getTimestamp("createtime"));
        user.setUpdatetime(result.getTimestamp("updatetime"));

        return user;
    }
}
