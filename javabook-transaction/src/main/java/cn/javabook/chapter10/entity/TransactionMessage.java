package cn.javabook.chapter10.entity;

import cn.javabook.cloud.core.utils.DateUtil;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 事务消息实体
 * 
 */
public class TransactionMessage extends RowStringIdEntity<TransactionMessage> implements Serializable {
	private static final long serialVersionUID = 3810437609261260137L;

	private String type; // 消息类型
	private String flag; // 消息标识，用于业务消息区分
	private String queue; // 消息队列名称
	private String content; // 内容
	private String status; // 消息状态
	private String createtime; // 发送时间，MONGO使用
	private Date sendtime; // 发送时间，MYSQL使用
	private Integer repeats; // 重复次数
	private String isdead; // 是否已死亡：存活中、已死亡
	private String params; // 回调参数字符串

	public TransactionMessage() {
	}

	public TransactionMessage(String id, Long guid, String type, String flag, String queue, String content, String status, String createtime, Date sendtime, Integer repeats, String isdead, String params) {
		this.id = id;
		this.guid = guid;
		this.type = type;
		this.flag = flag;
		this.queue = queue;
		this.content = content;
		this.status = status;
		this.createtime = createtime;
		this.sendtime = sendtime;
		this.repeats = repeats;
		this.isdead = isdead;
		this.params = params;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Date getSendtime() {
		return sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	public Integer getRepeats() {
		return repeats;
	}

	public void setRepeats(Integer repeats) {
		this.repeats = repeats;
	}

	public String getIsdead() {
		return isdead;
	}

	public void setIsdead(String isdead) {
		this.isdead = isdead;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void addRepeats() {
		++repeats;
	}

	@Override
	public TransactionMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
		TransactionMessage message = new TransactionMessage();
		message.setGuid(rs.getLong("guid"));
		message.setType(rs.getString("type"));
		message.setFlag(rs.getString("flag"));
		message.setQueue(rs.getString("queue"));
		message.setContent(rs.getString("content"));
		message.setStatus(rs.getString("status"));
		message.setCreatetime(rs.getString("createtime"));
		message.setSendtime(rs.getTimestamp("sendtime"));
		message.setRepeats(rs.getInt("repeats"));
		message.setIsdead(rs.getString("isdead"));
		return message;
	}

	@Override
	public String toString() {
		return String.format(
				"{\"guid\":%d, \"type\":\"%s\", \"flag\":\"%s\", \"queue\":\"%s\", \"content\":\"%s\", \"status\":\"%s\", " +
				"\"createtime\":\"%s\", \"sendtime\":\"%s\", \"repeats\":%d, \"isdead\":\"%s\"}",
				guid, type, flag, queue, content, status, createtime, DateUtil.parse(sendtime), repeats, isdead);
	}

}
