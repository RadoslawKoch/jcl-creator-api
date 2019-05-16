package com.pallas.jclcreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JclCreatorApplication {

    public static void main(String[] args) {
	SpringApplication.run(JclCreatorApplication.class, args);
    }
}
