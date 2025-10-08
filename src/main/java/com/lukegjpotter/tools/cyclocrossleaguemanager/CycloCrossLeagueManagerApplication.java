package com.lukegjpotter.tools.cyclocrossleaguemanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "CycloCross League Manager API", version = "0.0.1",
        description = "A suite of tools to help run a CycloCross League. The key features are that it parses the " +
                "Results and updates the Standings and also Provides Gridding based on the Standings."))
public class CycloCrossLeagueManagerApplication {

    private static final Logger logger = LoggerFactory.getLogger(CycloCrossLeagueManagerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CycloCrossLeagueManagerApplication.class, args);
        logger.info("Application Started");
    }

    @Bean
    public CommandLineRunner checkEnvironmentVariables(ApplicationContext applicationContext) {
        return args -> {
            //logger.info("Checking Environment Variables");
        };
    }
}
