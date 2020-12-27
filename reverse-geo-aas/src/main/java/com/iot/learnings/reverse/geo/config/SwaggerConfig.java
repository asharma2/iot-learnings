package com.iot.learnings.reverse.geo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apiV1() {
		return new Docket(DocumentationType.SWAGGER_2) //
				.groupName("REVERSEGEO") //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("com.iot.learnings.reverse.geo.web")) //
				.paths(PathSelectors.regex("/v1*")) //
				.build() //
				.apiInfo(new ApiInfoBuilder() //
						.version("1.0") //
						.title("REVERSE-GEO-SERVICE") //
						.description("Service to fetch the reverse geo location") //
						.license("©2020") //
						.licenseUrl("https://rategain.com") //
						.termsOfServiceUrl("") //
						.version("1.0") //
						.contact(new Contact("", "", "atul.sharma@rategain.com")) //
						.build());
	}

}
