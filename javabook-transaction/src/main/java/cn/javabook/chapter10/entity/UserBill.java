package cn.javabook.chapter10.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.StringJoiner;

/**
 * 用户账单Entity
 * 
 */
public class UserBill extends RowIntIdEntity<UserBill> {
	private static final long serialVersionUID = -3352222495384020696L;

	private Long taskid;
	private Long userid;
	private String outerno; // 商户提现订单号
	private Integer billobject; // 账单对象，0：用户；1：商家；2：公司；3：第三方
	private Long objectid;
	private Integer billtype; // 账单类别，0：发单；1：接单；2：充值；3：提现；4：退款；5：任务违约；6：任务赔偿
	private Integer paytype; // 支付方式，0：余额；1：支付宝；2：微信；3：银行卡
	private Integer money;
	private String subject;
	private Date billtime;

	public UserBill() {
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

	public String getOuterno() {
		return outerno;
	}

	public void setOuterno(String outerno) {
		this.outerno = outerno;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getBillobject() {
		return billobject;
	}

	public void setBillobject(Integer billobject) {
		this.billobject = billobject;
	}

	public Long getObjectid() {
		return objectid;
	}

	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	public Integer getBilltype() {
		return billtype;
	}

	public void setBilltype(Integer billtype) {
		this.billtype = billtype;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getBilltime() {
		return billtime;
	}

	public void setBilltime(Date billtime) {
		this.billtime = billtime;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", UserBill.class.getSimpleName() + "[", "]").add("taskid=" + taskid).add("userid=" + userid).add("outerno='" + outerno + "'").add("billobject=" + billobject).add("objectid=" + objectid).add("billtype=" + billtype).add("paytype=" + paytype).add("money=" + money).add("subject='" + subject + "'").add("billtime=" + billtime).toString();
	}

	@Override
	public UserBill mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserBill bill = new UserBill();
		bill.setGuid(rs.getLong("guid"));
		bill.setTaskid(rs.getLong("taskid"));
		bill.setUserid(rs.getLong("userid"));
		bill.setOuterno(rs.getString("outerno"));
		bill.setBillobject(rs.getInt("billobject"));
		bill.setObjectid(rs.getLong("objectid"));
		bill.setBilltype(rs.getInt("billtype"));
		bill.setPaytype(rs.getInt("paytype"));
		bill.setMoney(rs.getInt("money"));
		bill.setSubject(rs.getString("subject"));
		bill.setBilltime(rs.getTimestamp("billtime"));
		return bill;
	}
}
