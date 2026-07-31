package com.justdoit712.springboot.demo.springdoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * OpenAPI 3 配置类
 * 用于全局定义文档的基本信息（如标题、描述、版本、作者等）
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:52
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot JDK25 - OpenAPI 3 演示文档")
                        .description("这是一个基于 SpringDoc 的标准 OpenAPI 3 接口文档，自带可交互的 Swagger UI。")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("justdoit712")
                                .url("https://github.com/justdoit712")
                                .email("justdoit712@example.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
