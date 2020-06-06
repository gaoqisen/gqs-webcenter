package com.github.gaoqisen.webcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.github.gaoqisen")
public class WebcenterConsoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebcenterConsoleApplication.class, args);
	}

}
