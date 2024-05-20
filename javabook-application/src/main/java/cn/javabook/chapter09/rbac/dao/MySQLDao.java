package cn.javabook.chapter09.rbac.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * MySQLDao
 * 
 */
@Component
public class MySQLDao<T> {
	@Resource
	private JdbcTemplate jdbcTemplate;

	// 创建数据
	public int create(final String sql, final @Nullable Object... args) throws Exception {
		try {
			if (1 <= jdbcTemplate.update(sql, args)) {
				return 0;
			}

			return -1;
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			throw new DuplicateKeyException("data duplicate exception");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("create data exception");
		}
	}

	// 查询数量
	public Integer count(final String sql, final Object[] args) {
		try {
			return jdbcTemplate.queryForObject(sql, args, Integer.class);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询单条数据
	public Object findOne(final String sql, final RowMapper<?> rowMapper, final @Nullable Object... args) {
		try {
			List<?> list = jdbcTemplate.query(sql, rowMapper, args);
			if (null != list && list.size() > 0) {
				return list.get(0);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获得列表
	public List<?> find(final String sql, final RowMapper<?> rowMapper, final @Nullable Object... args) {
		try {
			List<?> list = jdbcTemplate.query(sql, rowMapper, args);
			if (null != list && 0 != list.size()) {
				return list;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 更新或删除数据
	public boolean update(final String sql, final @Nullable Object... args) throws Exception {
		try {
			return 0 <= jdbcTemplate.update(sql, args);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("update or remove object exception");
		}
	}
}
