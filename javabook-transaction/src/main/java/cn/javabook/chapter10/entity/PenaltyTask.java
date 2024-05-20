package cn.javabook.chapter10.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.StringJoiner;

/**
 * 用户违约订单详细信息POJO
 * 
 */
public class PenaltyTask extends RowIntIdEntity<PenaltyTask> {
	private static final long serialVersionUID = 6960790197243312503L;

	private Long taskid;
	private Integer userobject;
	private Long userid;
	private Integer acceptobject;
	private Long accepterid;
	private String username;
	private String nickname;
	private Integer gender;
	private String avatar;
	private String acceptername;
	private String accepternick;
	private String acceptergender;
	private String accepteravatar;
	private String grade;
	private String scale;
	private Integer score;
	private String photo;
	private String province;
	private String city;
	private String citycode;
	private String district;
	private String zipcode;
	private String images;
	private Integer money;
	private Integer supplement;
	private Integer penalty;
	private String startlocationname;
	private String startlocation;
	private String endlocationname;
	private String endlocation;
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
	private Integer commentstatus;
	private Integer iscontain;
	private Integer isenable;
	private Integer isaudit;

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAcceptername() {
		return acceptername;
	}

	public void setAcceptername(String acceptername) {
		this.acceptername = acceptername;
	}

	public String getAccepternick() {
		return accepternick;
	}

	public void setAccepternick(String accepternick) {
		this.accepternick = accepternick;
	}

	public String getAcceptergender() {
		return acceptergender;
	}

	public void setAcceptergender(String acceptergender) {
		this.acceptergender = acceptergender;
	}

	public String getAccepteravatar() {
		return accepteravatar;
	}

	public void setAccepteravatar(String accepteravatar) {
		this.accepteravatar = accepteravatar;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public Integer getSupplement() {
		return supplement;
	}

	public void setSupplement(Integer supplement) {
		this.supplement = supplement;
	}

	public Integer getPenalty() {
		return penalty;
	}

	public void setPenalty(Integer penalty) {
		this.penalty = penalty;
	}

	public String getStartlocationname() {
		return startlocationname;
	}

	public void setStartlocationname(String startlocationname) {
		this.startlocationname = startlocationname;
	}

	public String getStartlocation() {
		return startlocation;
	}

	public void setStartlocation(String startlocation) {
		this.startlocation = startlocation;
	}

	public String getEndlocationname() {
		return endlocationname;
	}

	public void setEndlocationname(String endlocationname) {
		this.endlocationname = endlocationname;
	}

	public String getEndlocation() {
		return endlocation;
	}

	public void setEndlocation(String endlocation) {
		this.endlocation = endlocation;
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

	public Integer getCommentstatus() {
		return commentstatus;
	}

	public void setCommentstatus(Integer commentstatus) {
		this.commentstatus = commentstatus;
	}

	public Integer getIscontain() {
		return iscontain;
	}

	public void setIscontain(Integer iscontain) {
		this.iscontain = iscontain;
	}

	public Integer getIsenable() {
		return isenable;
	}

	public void setIsenable(Integer isenable) {
		this.isenable = isenable;
	}

	public Integer getIsaudit() {
		return isaudit;
	}

	public void setIsaudit(Integer isaudit) {
		this.isaudit = isaudit;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", PenaltyTask.class.getSimpleName() + "[", "]").add("taskid=" + taskid).add("userobject=" + userobject).add("userid=" + userid).add("acceptobject=" + acceptobject).add("accepterid=" + accepterid).add("username='" + username + "'").add("nickname='" + nickname + "'").add("gender=" + gender).add("avatar='" + avatar + "'").add("acceptername='" + acceptername + "'").add("accepternick='" + accepternick + "'").add("acceptergender='" + acceptergender + "'").add("accepteravatar='" + accepteravatar + "'").add("grade='" + grade + "'").add("scale='" + scale + "'").add("score=" + score).add("photo='" + photo + "'").add("province='" + province + "'").add("city='" + city + "'").add("citycode='" + citycode + "'").add("district='" + district + "'").add("zipcode='" + zipcode + "'").add("images='" + images + "'").add("money=" + money).add("supplement=" + supplement).add("penalty=" + penalty).add("startlocationname='" + startlocationname + "'").add("startlocation='" + startlocation + "'").add("endlocationname='" + endlocationname + "'").add("endlocation='" + endlocation + "'").add("descript='" + descript + "'").add("closetime=" + closetime).add("publishtime=" + publishtime).add("accepttime=" + accepttime).add("finishtime=" + finishtime).add("modifiedtime=" + modifiedtime).add("canceltime=" + canceltime).add("confirmtime=" + confirmtime).add("type=" + type).add("status=" + status).add("paystatus=" + paystatus).add("commentstatus=" + commentstatus).add("iscontain=" + iscontain).add("isenable=" + isenable).add("isaudit=" + isaudit).toString();
	}

	@Override
	public PenaltyTask mapRow(ResultSet rs, int rowNum) throws SQLException {
		PenaltyTask penaltyTask = new PenaltyTask();
		penaltyTask.setGuid(rs.getLong("guid"));
		penaltyTask.setTaskid(rs.getLong("guid"));
		penaltyTask.setUserobject(rs.getInt("userobject"));
		penaltyTask.setUserid(rs.getLong("userid"));
		penaltyTask.setAcceptobject(rs.getInt("acceptobject"));
		penaltyTask.setAccepterid(rs.getLong("accepterid"));
		penaltyTask.setUsername(rs.getString("username"));
		penaltyTask.setNickname(rs.getString("nickname"));
		penaltyTask.setGender(rs.getInt("gender"));
		penaltyTask.setAvatar(rs.getString("avatar"));
		penaltyTask.setAcceptername(rs.getString("acceptername"));
		penaltyTask.setAccepternick(rs.getString("accepternick"));
		penaltyTask.setAcceptergender(rs.getString("acceptergender"));
		penaltyTask.setAccepteravatar(rs.getString("accepteravatar"));
		penaltyTask.setGrade(rs.getString("grade"));
		penaltyTask.setScale(rs.getString("scale"));
		penaltyTask.setScore(rs.getInt("score"));
		penaltyTask.setPhoto(rs.getString("photo"));
		penaltyTask.setProvince(rs.getString("province"));
		penaltyTask.setCity(rs.getString("city"));
		penaltyTask.setCitycode(rs.getString("citycode"));
		penaltyTask.setDistrict(rs.getString("district"));
		penaltyTask.setZipcode(rs.getString("zipcode"));
		penaltyTask.setImages(rs.getString("images"));
		penaltyTask.setMoney(rs.getInt("money"));
		penaltyTask.setSupplement(rs.getInt("supplement"));
		penaltyTask.setPenalty(rs.getInt("penalty"));
		penaltyTask.setStartlocationname(rs.getString("startlocationname"));
		penaltyTask.setStartlocation(rs.getString("startlocation"));
		penaltyTask.setEndlocationname(rs.getString("endlocationname"));
		penaltyTask.setEndlocation(rs.getString("endlocation"));
		penaltyTask.setDescript(rs.getString("descript"));
		penaltyTask.setClosetime(rs.getInt("closetime"));
		penaltyTask.setPublishtime(rs.getTimestamp("publishtime"));
		penaltyTask.setAccepttime(rs.getTimestamp("accepttime"));
		penaltyTask.setFinishtime(rs.getTimestamp("finishtime"));
		penaltyTask.setModifiedtime(rs.getTimestamp("modifiedtime"));
		penaltyTask.setCanceltime(rs.getTimestamp("canceltime"));
		penaltyTask.setConfirmtime(rs.getTimestamp("confirmtime"));
		penaltyTask.setType(rs.getInt("type"));
		penaltyTask.setStatus(rs.getInt("status"));
		penaltyTask.setPaystatus(rs.getInt("paystatus"));
		penaltyTask.setCommentstatus(rs.getInt("commentstatus"));
		penaltyTask.setIscontain(rs.getInt("iscontain"));
		penaltyTask.setIsenable(rs.getInt("isenable"));
		penaltyTask.setIsaudit(rs.getInt("isaudit"));
		return penaltyTask;
	}
}
