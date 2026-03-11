package com.dk.logistics.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("分布式权限管理系统 API")
                        .description("基于 Spring Boot + Spring Security + MyBatis-Plus 的 RBAC 权限管理系统接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("dk")
                                .email("example@example.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目说明")
                        .url("https://github.com/ldk3589/logistics-example"));
    }
}