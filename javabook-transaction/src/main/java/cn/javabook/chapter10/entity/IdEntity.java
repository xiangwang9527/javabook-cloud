package cn.javabook.chapter10.entity;

import cn.javabook.cloud.core.parent.BaseEntity;
import org.springframework.data.annotation.Id;

/**
 * 仅产生编码的实体基类
 * 
 */
public abstract class IdEntity<T> extends BaseEntity {
	private static final long serialVersionUID = 2092006747042948217L;
	protected String id;
	protected Long guid;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getGuid() {
		return guid;
	}

	public void setGuid(Long guid) {
		this.guid = guid;
	}
}
