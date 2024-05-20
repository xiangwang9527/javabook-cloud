package cn.javabook.chapter10.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用户交易记录Entity
 *
 */
public class UserTrade extends RowIntIdEntity<UserTrade> {
	private static final long serialVersionUID = -3352222495384020696L;

	private Long taskid;
	private Long userid;
	private Integer tradeobject; // 交易对象，0：用户；1：商家；2：平台；3：第三方
	private Long objectid;
	private Integer tradetype; // 交易类别，0：支出；1：收入；2：充值；3：提现；4：退款；5：支付违约金；6：获得赔偿金
	private Integer paytype; // 支付方式，0：余额；1：支付宝；2：微信；3：银行卡
	private Integer money;// 依据交易类型不同，金额不同
	private Integer status; // 交易状态，0：未完成；1：已完成
	private String remark;// 备注，保存提现时的商户订单号
	private Date publishtime; // 接单时间
	private Date accepttime; // 接单时间
	private Date canceltime; // 取消时间
	private Date tradetime;

	public UserTrade() {
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getTradeobject() {
		return tradeobject;
	}

	public void setTradeobject(Integer tradeobject) {
		this.tradeobject = tradeobject;
	}

	public Long getObjectid() {
		return objectid;
	}

	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	public Integer getTradetype() {
		return tradetype;
	}

	public void setTradetype(Integer tradetype) {
		this.tradetype = tradetype;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}

	public Date getAccepttime() {
		return accepttime;
	}

	public void setAccepttime(Date accepttime) {
		this.accepttime = accepttime;
	}

	public Date getCanceltime() {
		return canceltime;
	}

	public void setCanceltime(Date canceltime) {
		this.canceltime = canceltime;
	}

	public Date getTradetime() {
		return tradetime;
	}

	public void setTradetime(Date tradetime) {
		this.tradetime = tradetime;
	}

	@Override
	public UserTrade mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserTrade trade = new UserTrade();
		trade.setGuid(rs.getLong("guid"));
		trade.setTaskid(rs.getLong("taskid"));
		trade.setUserid(rs.getLong("userid"));
		trade.setTradeobject(rs.getInt("tradeobject"));
		trade.setObjectid(rs.getLong("objectid"));
		trade.setTradetype(rs.getInt("tradetype"));
		trade.setPaytype(rs.getInt("paytype"));
		trade.setMoney(rs.getInt("money"));
		trade.setStatus(rs.getInt("status"));
		trade.setRemark(rs.getString("remark"));
		trade.setPublishtime(rs.getTimestamp("publishtime"));
		trade.setAccepttime(rs.getTimestamp("accepttime"));
		trade.setCanceltime(rs.getTimestamp("canceltime"));
		trade.setTradetime(rs.getTimestamp("tradetime"));

		return trade;
	}
}
