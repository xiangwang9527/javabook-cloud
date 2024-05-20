package cn.javabook.chapter10.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 会计原始凭证
 * 
 */
public class AccountingVoucher extends RowIntIdEntity<AccountingVoucher> {
	private static final long serialVersionUID = 6846505111332381916L;

	private String voucherno; // 原始凭证号 （交易记录的唯一凭证号）
	private Long requestno; // 请求号（当前时间的毫秒数）
	private String entrytype; // 会计分录类型（对应枚举EntryTypeEnum.java）
	private String origin; // 来源系统 0：APP；1：WEB；2：SCAN；3：FACE；4：PACKAGE；5：POINT
	private Double payamount; // 付款方支付金额
	private Double changeamount; // 收款方变动金额
	private String payeraccountno; // 付款方账户编号（退款交易：原路退，即交易付款方为payeraccountno，那么退款也用payeraccountno作为收钱一方）
	private String receiveraccountno;// 收款方账户编号（退款交易：原路退，既交易付款方为receiveraccountno，那么退款也用receiveraccountno作为退钱一方）
	private Double income; // 平台收入（退款交易：平台收入实际变动金额）
	private Double cost; // 平台成本（退款交易：平台成本实际变动金额）
	private Double profit; // 平台利润（退款交易：平台利润实际变动金额）
	private Date createtime; // 会计日期
	private String remark; // 备注

	// 分录步骤，1：产生交易；2：清算对账
	private int step; // 非数据库映射字段，只用于传参
	private String messageid; // 非数据库映射字段，只用于传参

	public String getVoucherno() {
		return voucherno;
	}

	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}

	public Long getRequestno() {
		return requestno;
	}

	public void setRequestno(Long requestno) {
		this.requestno = requestno;
	}

	public String getEntrytype() {
		return entrytype;
	}

	public void setEntrytype(String entrytype) {
		this.entrytype = entrytype;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}

	public Double getChangeamount() {
		return changeamount;
	}

	public void setChangeamount(Double changeamount) {
		this.changeamount = changeamount;
	}

	public String getPayeraccountno() {
		return payeraccountno;
	}

	public void setPayeraccountno(String payeraccountno) {
		this.payeraccountno = payeraccountno;
	}

	public String getReceiveraccountno() {
		return receiveraccountno;
	}

	public void setReceiveraccountno(String receiveraccountno) {
		this.receiveraccountno = receiveraccountno;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public AccountingVoucher mapRow(ResultSet rs, int rowNum) throws SQLException {
		AccountingVoucher voucher = new AccountingVoucher();
		voucher.setGuid(rs.getLong("guid"));
		voucher.setVoucherno(rs.getString("voucherno"));
		voucher.setRequestno(rs.getLong("requestno"));
		voucher.setEntrytype(rs.getString("entrytype"));
		voucher.setOrigin(rs.getString("origin"));
		voucher.setPayamount(rs.getDouble("payamount"));
		voucher.setChangeamount(rs.getDouble("changeamount"));
		voucher.setPayeraccountno(rs.getString("payeraccountno"));
		voucher.setReceiveraccountno(rs.getString("receiveraccountno"));
		voucher.setIncome(rs.getDouble("income"));
		voucher.setCost(rs.getDouble("cost"));
		voucher.setProfit(rs.getDouble("profit"));
		voucher.setCreatetime(rs.getTimestamp("createtime"));
		voucher.setRemark(rs.getString("remark"));
		return voucher;
	}
}
