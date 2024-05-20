package cn.javabook.chapter10.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * MySQL数据操作
 *
 */
@Component
@DS("master")
public class MySQLDao extends BaseDao {
    private static final long serialVersionUID = 8200769150543370743L;

    @Resource
    private JdbcTemplate jdbcTemplate;

    // 查询对象
    public Object findOne(String sql, RowMapper<?> rowMapper) {
        try {
            Object object = jdbcTemplate.queryForObject(sql, rowMapper);
            if (null != object) {
                return object;
            }
        } catch (EmptyResultDataAccessException e) {
            // 解决jdbcTemplate.queryForObject()结果为null时抛异常的问题
            return null;
        } catch (DataAccessException e) {
            logger.error("查询对象异常：{}", e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
        }
        return null;
    }

    // 查询列表
    public List<?> find(String sql, RowMapper<?> rowMapper) {
        try {
            List<?> list = jdbcTemplate.query(sql, rowMapper);
            if (0 != list.size()) {
                return list;
            }
        } catch (DataAccessException e) {
            logger.error("查询列表异常：{}", e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
        }
        return null;
    }

    // 创建
    public Long create(String sql) {
        try {
            if (1 <= jdbcTemplate.update(sql)) {
                return 0L;
            }
        } catch (DuplicateKeyException e) {
            logger.error("数据重复：{}", e.getMessage());
            return -2L;
        } catch (DataAccessException e) {
            logger.error("创建异常：{}", e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_INSERT_FAILED;
        }
        return -1L;
    }

    // 更新或删除
    public boolean update(String sql) {
        try {
            return 0 <= jdbcTemplate.update(sql);
        } catch (DataAccessException e) {
            logger.error("更新或删除异常：{}", e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_UPDATE_FAILED;
        }
    }
}
