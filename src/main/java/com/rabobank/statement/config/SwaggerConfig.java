package com.rabobank.statement.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * Configuration class which load api configuration details
 * @author vijai
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${company.name}")
	String companyName;

	@Value("${company.website}")
	String companyWebsite;

	@Value("${company.email}")
	String companyEmail;

	@Value("${company.license.url}")
	String companylicenseUrl;

	@Value("${api.version}")
	String apiVersion;

	@Value("${api.description}")
	String apiDescription;

	@Value("${api.title}")
	String apiTitle;

	@Value("${swagger.config.api.basepackage}")
	String basePackage;

	@Value("${swagger.config.api.path}")
	String swaggerPath;

	@Bean
	public Docket productApi() {
		ArrayList<ResponseMessage> li = new ArrayList<>();
		li.add(new ResponseMessageBuilder().code(400).message("Process Error").responseModel(new ModelRef("Error"))
				.build());
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(basePackage))
				.paths(regex(swaggerPath)).build().apiInfo(metaData()).globalResponseMessage(RequestMethod.POST, li);
	}

	private ApiInfo metaData() {
		Contact contact = new Contact(companyName, companyWebsite, companyEmail);
		ApiInfoBuilder builder = new ApiInfoBuilder();
		return builder.description(apiDescription).version(apiVersion).licenseUrl(companylicenseUrl).title(apiTitle)
				.contact(contact).build();
	}
}