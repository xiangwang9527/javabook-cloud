package cn.javabook.chapter10.entity;

import cn.javabook.cloud.core.utils.DateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 支付订单Entity
 * 
 */
public class PaymentOrder extends RowIntIdEntity<PaymentOrder> {
	private static final long serialVersionUID = 1380093393237087272L;

	protected Long userid; // 用户编码
	protected Long taskid; // 订单编码
	protected String transno; // 支付交易流水号
	protected String outerno; // 支付交易订单号
	protected String title; // 支付标题
	protected String paytype; // 支付类别 0：余额；1：支付宝；2：微信；3：银行卡（接单时默认为0）
	protected String payclass; // 支付类型 0：消费；1：充值；2：提现
	private String paychannel; // 支付渠道：0：APP；1：WEB；2：SCAN；3：FACE；4：PACKAGE；5：POINT
	protected Double amount; // 支付金额
	private Integer period; // 订单有效期(单位分钟)，默认0
	private String reason; // 订单失败原因
	protected String status; // 支付状态 CREATED；WAITING_PAYMENT；SUCCESS；TRADE_SUCCESS；FAILED；CANCELED；TIMEOUTED
	protected Date createtime; // 下单时间
	protected Date updatetime; // 更新时间
	protected String remark; // 订单备注

	public PaymentOrder() {
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public String getTransno() {
		return transno;
	}

	public void setTransno(String transno) {
		this.transno = transno;
	}

	public String getOuterno() {
		return outerno;
	}

	public void setOuterno(String outerno) {
		this.outerno = outerno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getPayclass() {
		return payclass;
	}

	public void setPayclass(String payclass) {
		this.payclass = payclass;
	}

	public String getPaychannel() {
		return paychannel;
	}

	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public PaymentOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		PaymentOrder payment = new PaymentOrder();
		payment.setGuid(rs.getLong("guid"));
		payment.setUserid(rs.getLong("userid"));
		payment.setTaskid(rs.getLong("taskid"));
		payment.setTransno(rs.getString("transno"));
		payment.setOuterno(rs.getString("outerno"));
		payment.setTitle(rs.getString("title"));
		payment.setPaytype(rs.getString("paytype"));
		payment.setPayclass(rs.getString("payclass"));
		payment.setPaychannel(rs.getString("paychannel"));
		payment.setAmount(rs.getDouble("amount"));
		payment.setPeriod(rs.getInt("period"));
		payment.setReason(rs.getString("reason"));
		payment.setStatus(rs.getString("status"));
		payment.setCreatetime(rs.getTimestamp("createtime"));
		payment.setUpdatetime(rs.getTimestamp("updatetime"));
		payment.setRemark(rs.getString("remark"));
		return payment;
	}

	@Override
	public String toString() {
		return String.format(
				"{\"id\":\"%s\", \"guid\":%d, \"userid\":%d, \"taskid\":%d, \"transno\":\"%s\", \"outerno\":\"%s\", \"title\":\"%s\", \"paytype\":\"%s\", "
						+ "\"payclass\":\"%s\", \"paychannel\":\"%s\", \"amount\":%f, \"period\":%d, \"reason\":\"%s\", \"status\":\"%s\", \"createtime\":\"%s\", "
						+ "\"updatetime\":\"%s\", \"remark\":\"%s\"}",
				id, guid, userid, taskid, transno, outerno, title, paytype, payclass, paychannel, amount, period, reason, status,
				DateUtil.parse(createtime), DateUtil.parse(updatetime), remark);
	}
}
