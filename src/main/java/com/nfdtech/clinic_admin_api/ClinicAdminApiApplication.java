package com.nfdtech.clinic_admin_api;

import com.nfdtech.clinic_admin_api.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ClinicAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicAdminApiApplication.class, args);
    }

    @Bean
    public AuditorAwareImpl auditorAware() {
        return new AuditorAwareImpl();
    }

}
