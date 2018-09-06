package com.zss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.util.StringUtils;

import com.zss.constants.EurekaConstants;

import java.io.File;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
	
	public static void main(String[] args) {

		/*String envPath = System.getenv(EurekaConstants.ENV_CONF_PATH.toUpperCase());*/
		String envPath = "E:\\github-zss\\config\\eureka-server\\";
		if (StringUtils.isEmpty(envPath)) {
			throw new IllegalStateException(EurekaConstants.ENV_CONF_PATH.toUpperCase() + " is blank");
		}
		if (!envPath.endsWith(File.separator))
			envPath = envPath + File.separator;	
		String configPath = envPath + "config" + File.separator;
		String logPath = envPath + "log" + File.separator;
		System.setProperty("spring.config.location", configPath);
		System.setProperty("log.base", logPath);
		System.setProperty("log.path", configPath);

		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
