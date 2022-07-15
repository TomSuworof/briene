package com.salat.briene.config;

import com.salat.briene.repositories.*;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.URI;

// Elasticsearch repositories only
@EnableElasticsearchRepositories(
        basePackages = "com.salat.briene.repositories",
        includeFilters = @ComponentScan.Filter(classes = ArticleSearchRepository.class, type = FilterType.ASSIGNABLE_TYPE))
// All others repositories
@EnableJpaRepositories(
        basePackages = "com.salat.briene.repositories",
        includeFilters = @ComponentScan.Filter(classes = {
                ArticleRepository.class,
                CommentRepository.class,
                PasswordResetRepository.class,
                RoleRepository.class,
                TagRepository.class,
                UserRepository.class,
        }, type = FilterType.ASSIGNABLE_TYPE))
@Configuration
public class ElasticSearchConfig extends ElasticsearchConfigurationSupport {

    @Value("${spring.elasticsearch.rest.uris}")
    private String url;

    @Bean
    public RestHighLevelClient client() {
        URI connUri = URI.create(url);
        String[] auth = connUri.getUserInfo().split(":");

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                        .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                                .setDefaultCredentialsProvider(provider)
                                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
}
