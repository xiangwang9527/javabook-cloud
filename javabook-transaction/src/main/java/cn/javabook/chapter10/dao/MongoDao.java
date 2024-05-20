package cn.javabook.chapter10.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import cn.javabook.cloud.core.parent.BaseException;
import cn.javabook.cloud.core.parent.BaseDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * MongoDB数据操作
 *
 */
@Component
public class MongoDao extends BaseDao {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 依据编码查询文档
     *
     * @param query
     * @param entityClass
     * @return
     */
    public Object find(Query query, Class<?> entityClass) {
        try {
            return mongoTemplate.findOne(query, entityClass);
        } catch (Exception e) {
            logger.error("Mongo - 依据编码查询文档异常：" + e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
        }
    }

    /**
     * 依据条件查询文档
     *
     * @param query
     * @param entityClass
     * @return
     */
    public List<?> findAll(Query query, Class<?> entityClass) {
        try {
            return mongoTemplate.find(query, entityClass);
        } catch (Exception e) {
            logger.error("Mongo - 依据条件查询文档异常：" + e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_QUERY_FAILED;
        }
    }

    /**
     * 更新文档
     *
     * @param query
     * @param update
     * @param entityClass
     * @return
     */
    public boolean upsert(Query query, Update update, Class<?> entityClass) {
        try {
            UpdateResult result = mongoTemplate.upsert(query, update, entityClass);
            return result.wasAcknowledged();
        } catch (Exception e) {
            logger.error("Mongo - 更新文档异常：" + e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_UPDATE_FAILED;
        }
    }

    /**
     * 删除文档
     *
     * @param query
     * @param collectionName
     */
    public void remove(Query query, String collectionName) {
        try {
            DeleteResult result = mongoTemplate.remove(query, collectionName);
            result.wasAcknowledged();
        } catch (Exception e) {
            logger.error("Mongo - 删除文档异常：" + e.getMessage());
            throw BaseException.EXCEPTION_PUBLIC_DB_DELETE_FAILED;
        }
    }
}
