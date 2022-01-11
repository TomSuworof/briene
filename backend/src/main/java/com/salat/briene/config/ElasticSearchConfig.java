package com.salat.briene.config;

import com.salat.briene.repositories.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Elasticsearch repositories only
@EnableElasticsearchRepositories(
        basePackages = "com.salat.briene.repositories",
        includeFilters = @ComponentScan.Filter(classes = ArticleSearchRepository.class, type = FilterType.ASSIGNABLE_TYPE))
// All others repositories
@EnableJpaRepositories(
        basePackages = "com.salat.briene.repositories",
        includeFilters = @ComponentScan.Filter(classes = {
                ArticleRepository.class,
                PasswordResetRepository.class,
                RoleRepository.class,
                UserRepository.class
        }, type = FilterType.ASSIGNABLE_TYPE))
@Configuration
public class ElasticSearchConfig extends ElasticsearchConfigurationSupport {

    @Value("${spring.elasticsearch.rest.uris}")
    private String hostAndPort;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
}
