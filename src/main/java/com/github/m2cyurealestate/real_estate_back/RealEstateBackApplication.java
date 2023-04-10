package com.github.m2cyurealestate.real_estate_back;

import com.github.m2cyurealestate.real_estate_back.services.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "com.github.m2cyurealestate.real_estate_back.api",
        "com.github.m2cyurealestate.real_estate_back.config",
        "com.github.m2cyurealestate.real_estate_back.security",
        "com.github.m2cyurealestate.real_estate_back.services",
        "com.github.m2cyurealestate.real_estate_back.persistence"
})
@ConfigurationPropertiesScan("com.github.m2cyurealestate.real_estate_back.config.properties")
@EnableTransactionManagement
public class RealEstateBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstateBackApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            userService.registerDefaultAdminIfNotFound(
                    "SRE_ADMIN",
                    "m2.sid.pds.realestate@gmail.com",
                    "$2a$10$QaHtfsI6zCp4zsBkve.sOO0/lVoDltq9Yn20Xw5KqvYRPD52Ez8qe"
            );
        };
    }
}
