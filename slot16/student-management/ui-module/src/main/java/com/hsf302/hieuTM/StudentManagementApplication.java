package com.hsf302.hieuTM;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.hsf302.hieuTM",
        "service",
        "repository"
})
@EntityScan("pojo")
@EnableJpaRepositories("repository")
public class StudentManagementApplication {
}