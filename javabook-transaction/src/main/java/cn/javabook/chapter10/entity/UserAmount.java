package cn.javabook.chapter10.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用户账户Entity
 * 
 */
public class UserAmount extends RowIntIdEntity<UserAmount> {
	private static final long serialVersionUID = -1878228857245583406L;

	private Long guid;
	private Integer balance;
	private Date createtime;
	private Date updatetime;

	public UserAmount() {
	}

	@Override
	public Long getGuid() {
		return guid;
	}

	@Override
	public void setGuid(Long guid) {
		this.guid = guid;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
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
	public UserAmount mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserAmount amount = new UserAmount();
		amount.setGuid(rs.getLong("guid"));
		amount.setBalance(rs.getInt("balance"));
		amount.setCreatetime(rs.getTimestamp("createtime"));
		amount.setUpdatetime(rs.getTimestamp("updatetime"));
		return amount;
	}
}
