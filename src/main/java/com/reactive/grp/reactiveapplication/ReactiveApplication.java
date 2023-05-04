package com.reactive.grp.reactiveapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class ReactiveApplication {

	public static void main(String[] args) {

	 ApplicationContext applicationContext = SpringApplication.run(ReactiveApplication.class, args);
		System.out.println(List.of(applicationContext.getBeanDefinitionNames()));


	}

}
