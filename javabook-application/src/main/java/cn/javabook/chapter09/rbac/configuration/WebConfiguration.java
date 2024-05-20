package cn.javabook.chapter09.rbac.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 允许跨域访问
 *
 */
@Configuration
@Component
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/")
                .addResourceLocations("classpath:templates/");
        super.addResourceHandlers(registry);
    }

    /**
     * 跨域支持
     *
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许跨域的域名，可以用*表示允许任何域名使用
                .allowedOrigins("*")
                //允许任何请求头
                .allowedHeaders("*")
                // 带上cookie信息
                .allowCredentials(true)
                .allowedMethods("*")
                // maxAge(3600 * 24)表明在3600 * 24秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(24 * 60 * 60);
    }
}
