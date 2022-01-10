package com.salat.briene;

import com.salat.briene.repositories.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "com.salat.briene.repositories",
        includeFilters = @ComponentScan.Filter(classes = {
                ArticleRepository.class,
                PasswordResetRepository.class,
                RoleRepository.class,
                UserRepository.class
        }, type = FilterType.ASSIGNABLE_TYPE))
@EnableElasticsearchRepositories(
        basePackages = "com.salat.briene.repositories",
        includeFilters = @ComponentScan.Filter(classes = ArticleSearchRepository.class, type = FilterType.ASSIGNABLE_TYPE))
public class BrieneApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrieneApplication.class, args);
    }
}
