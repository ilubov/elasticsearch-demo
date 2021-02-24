package com.ilubov.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = {"swagger.enabled"}, havingValue = "true")
public class SwaggerConfig {
    @Value("${swagger.enabled:false}")
    private Boolean enabled;
    @Value("${swagger.title:ilubov}")
    private String title;
    @Value("${swagger.description:Damon Framework}")
    private String description;
    @Value("${swagger.version:5}")
    private String version;

    public SwaggerConfig() {}

    @Bean
    public Docket createRestApi() {
        return (new Docket(DocumentationType.SWAGGER_2))
                .enable(this.enabled)
                .apiInfo(this.apiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder())
                .title(this.title)
                .description(this.description)
                .version(this.version)
                .contact(new Contact("ilubov", "ilubov.com", "o@ilubov.cn"))
                .build();
    }
}
