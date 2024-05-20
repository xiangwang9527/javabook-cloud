package cn.javabook.chapter10.service;

import cn.javabook.chapter10.entity.UserAmount;
import cn.javabook.cloud.core.parent.BaseService;
import cn.javabook.cloud.core.parent.IService;
import cn.javabook.chapter10.entity.User;
import cn.javabook.chapter10.dao.MySQLDao;
import cn.javabook.chapter10.entity.UserBill;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 支付服务类
 *
 */
@Service
public class PaymentService extends BaseService implements IService {
    private static final long serialVersionUID = 8328123103623490017L;

    @Resource
    private MySQLDao mySQLDao;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 查询用户账单
    public List<UserBill> queryUserBills() {
        List<UserBill> list = (List<UserBill>) mySQLDao.find("select * from sys_user_bill", new UserBill());
        return list;
    }

//    public List<Map<String, Object>> queryMySQlData() {
//        return jdbcTemplate.queryForList("select * from sys_config");
//    }

    // 集成mongodb时，配置中的密码需要使用password: '123456'，而不是password: 123456，否则会报错：Exception authenticating MongoCredential{mechanism=SCRAM-SHA-256
    public void saveMongoData() {
        UserAmount userAmount = new UserAmount();
        userAmount.setGuid(1L);
        userAmount.setBalance(1);
        mongoTemplate.save(userAmount, "test");
    }

    public User queryOneMongoData() {
        Query query = new Query();
        Object object = mongoTemplate.findOne(query, User.class, "test");
        return (User) object;
    }

    public String queryMongoData() {
        Query query = new Query();
        List<Map> maps = mongoTemplate.find(query, Map.class, "riskevent");
        List<Map> all = mongoTemplate.findAll(Map.class);
        return maps.toString();
    }

    public void saveRedisData() {
        stringRedisTemplate.opsForValue().set("test", "javabook");
    }

    public String queryRedisData() {
        return stringRedisTemplate.opsForValue().get("test");
    }

    @Override
    public Object getSavedObject(String key) {
        return null;
    }

    @Override
    public void setSavedObject(String key, String value, long expire) {
    }
}
