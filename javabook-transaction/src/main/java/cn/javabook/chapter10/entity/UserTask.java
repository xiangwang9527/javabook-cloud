package cn.javabook.chapter10.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用户订单Entity
 * 
 */
public class UserTask extends RowIntIdEntity<UserTask> {
	private static final long serialVersionUID = -3712858530264316180L;

	private Integer userobject; // 发单用户类型 0：用户；1：商家；2：平台；3：第三方
	private Long userid;
	private Integer acceptobject; // 接单用户类型 0：用户；1：商家；2：平台；3：第三方
	private Long accepterid;
	private String province;
	private String city;
	private String citycode;
	private String district;
	private String zipcode;
	private String images;
	private Integer money;
	private String descript;
	private Integer closetime;// 秒数，希望从发布任务开始到后多少秒完成
	private Date publishtime;
	private Date accepttime;
	private Date finishtime;
	private Date modifiedtime;
	private Date canceltime;
	private Date confirmtime;
	private Integer type;
	private Integer status;
	private Integer paystatus;
	private Integer tradetype; // 交易类别，0：支出；1：收入；2：充值；3：提现；4：退款；5：支付违约金；6：获得赔偿金
	private Integer paytype; // 支付方式，0：余额；1：支付宝；2：微信；3：银行卡

	public UserTask() {
	}

	public Integer getUserobject() {
		return userobject;
	}

	public void setUserobject(Integer userobject) {
		this.userobject = userobject;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getAcceptobject() {
		return acceptobject;
	}

	public void setAcceptobject(Integer acceptobject) {
		this.acceptobject = acceptobject;
	}

	public Long getAccepterid() {
		return accepterid;
	}

	public void setAccepterid(Long accepterid) {
		this.accepterid = accepterid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getClosetime() {
		return closetime;
	}

	public void setClosetime(Integer closetime) {
		this.closetime = closetime;
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

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public Date getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(Date modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	public Date getCanceltime() {
		return canceltime;
	}

	public void setCanceltime(Date canceltime) {
		this.canceltime = canceltime;
	}

	public Date getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(Date confirmtime) {
		this.confirmtime = confirmtime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
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

	@Override
	public String toString() {
		return String.format(
				"{\"guid\":%d, \"userobject\":%d, \"userid\":%d, \"acceptobject\":%d, \"accepterid\":%d, \"province\":\"%s\", \"city\":\"%s\", \"citycode\":\"%s\", \"district\":\"%s\", "
						+ "\"zipcode\":\"%s\", \"images\":\"%s\", \"money\":%d, \"descript\":\"%s\", \"closetime\":%d, \"publishtime\":%d, \"accepttime\":%d, \"finishtime\":%d, "
						+ "\"modifiedtime\":%d, \"canceltime\":%d, \"confirmtime\":%d, \"type\":%d, \"status\":%d, \"paystatus\":%d, \"tradetype\":%d, \"paytype\":%d}",
				guid, userobject, userid, acceptobject, accepterid, province, city, citycode, district, zipcode, images, money, descript, closetime, publishtime,
				accepttime, finishtime, modifiedtime, canceltime, confirmtime, type, status, paystatus, tradetype, paytype);
	}

	@Override
	public UserTask mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserTask task = new UserTask();
		task.setGuid(rs.getLong("guid"));
		task.setUserobject(rs.getInt("userobject"));
		task.setUserid(rs.getLong("userid"));
		task.setAcceptobject(rs.getInt("acceptobject"));
		task.setAccepterid(rs.getLong("accepterid"));
		task.setProvince(rs.getString("province"));
		task.setCity(rs.getString("city"));
		task.setCitycode(rs.getString("citycode"));
		task.setDistrict(rs.getString("district"));
		task.setZipcode(rs.getString("zipcode"));
		task.setImages(rs.getString("images"));
		task.setMoney(rs.getInt("money"));
		task.setDescript(rs.getString("descript"));
		task.setClosetime(rs.getInt("closetime"));
		task.setPublishtime(rs.getTimestamp("publishtime"));
		task.setAccepttime(rs.getTimestamp("accepttime"));
		task.setFinishtime(rs.getTimestamp("finishtime"));
		task.setModifiedtime(rs.getTimestamp("modifiedtime"));
		task.setCanceltime(rs.getTimestamp("canceltime"));
		task.setConfirmtime(rs.getTimestamp("confirmtime"));
		task.setType(rs.getInt("type"));
		task.setStatus(rs.getInt("status"));
		task.setPaystatus(rs.getInt("paystatus"));
		task.setTradetype(rs.getInt("tradetype"));
		task.setPaytype(rs.getInt("paytype"));
		return task;
	}
}
