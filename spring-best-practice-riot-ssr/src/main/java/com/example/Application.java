package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// ScriptTemplateViewResolver つまり、JavaScript製のテンプレートエンジン（Handlebars, EJS, etc.）使うぐらいならやっぱりThymeleafがいい！
//	@Bean
//	public ViewResolver viewResolver() {
//		ScriptTemplateViewResolver viewResolver = new ScriptTemplateViewResolver();
//		viewResolver.setPrefix("/static/");
//		viewResolver.setSuffix(".html");
//		return viewResolver;
//	}
//
//	@Bean
//	public ScriptTemplateConfigurer scriptTemplateConfig() {
//		ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
//		configurer.setEngineName("nashorn");
//		configurer.setScripts(
//				"/static/render.js"
//		);
//		configurer.setRenderFunction("render");
//		configurer.setSharedEngine(false);
//		return configurer;
//	}
}
