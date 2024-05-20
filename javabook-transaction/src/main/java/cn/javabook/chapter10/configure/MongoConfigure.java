package cn.javabook.chapter10.configure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import javax.annotation.Resource;

/**
 * 去除_class字段的配置类
 *
 */
@Configuration
public class MongoConfigure implements InitializingBean {
    @Resource
    private MappingMongoConverter mappingConverter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 去除插入数据库的_class字段
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
