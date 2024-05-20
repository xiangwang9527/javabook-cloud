package cn.javabook.chapter10.entity;

import cn.javabook.cloud.core.parent.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.jdbc.core.RowMapper;

/**
 * 产生整型编码和结果集映射的实体基类
 * 
 */
public abstract class RowIntIdEntity<T> extends BaseEntity implements RowMapper<T> {
	private static final long serialVersionUID = 2025379600121656006L;
	protected int id;
	protected Long guid;

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getGuid() {
		return guid;
	}

	public void setGuid(Long guid) {
		this.guid = guid;
	}
}
