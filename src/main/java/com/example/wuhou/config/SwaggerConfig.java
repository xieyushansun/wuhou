package com.example.wuhou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.wuhou.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("测试")
                .description("测试")
                .termsOfServiceUrl("https://localhost:8080")
                .version("1.1")
                .build();
    }
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
////        super.addResourceHandlers(registry);
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/");
//            registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
            // 解决 SWAGGER 404报错
            registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }

//        @Override
//        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//
//        }



//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .pathMapping("/")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.nvn.controller"))
//                .paths(PathSelectors.any())
//                .build().apiInfo(new ApiInfoBuilder()
//                        .title("SpringBoot整合Swagger")
//                        .description("SpringBoot整合Swagger，详细信息......")
//                        .version("9.0")
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
//                        .build());
//    }
}
