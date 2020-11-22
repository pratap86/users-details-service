package com.pratap.ws.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	
	private static final String TITLE = "User Details Api Documentation";
	private static final String DESCRIPTION = "User Details Service";
	private static final String CONTEXT_PATH = "/user-details-api.*";
	private static final String CONTROLLERS_PATH = "com.pratap.ws.ui.controllers";
	private static final Set<String> DEFAULT_PRODUCE_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json", "application/xml"));

	@Bean
	public Docket clinicalApi() {
		
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
						.produces(DEFAULT_PRODUCE_AND_CONSUMES)
						.consumes(DEFAULT_PRODUCE_AND_CONSUMES)
						.select()
						.apis(RequestHandlerSelectors.basePackage(CONTROLLERS_PATH))
						.paths(PathSelectors.regex(CONTEXT_PATH))
						.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
						.description(DESCRIPTION)
						.version("2.0")
						.title(TITLE)
						.termsOfServiceUrl("https://www.google.co.in/")
						.contact(new Contact("Pratap Narayan", "https://www.linkedin.com/in/pratap-narayan-85a07725/", "narayanpratap86@gmail.com"))
						.license("Apache Licence")
						.build();
	}
}
