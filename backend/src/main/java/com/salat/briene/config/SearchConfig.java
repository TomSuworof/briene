// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.config;

import com.manticoresearch.client.ApiClient;
import com.manticoresearch.client.api.IndexApi;
import com.manticoresearch.client.api.SearchApi;
import com.manticoresearch.client.api.UtilsApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {

    @Value("${spring.search.url}")
    private String url;

    @Bean
    public ApiClient apiClient() {
        ApiClient defaultClient = com.manticoresearch.client.Configuration.getDefaultApiClient();
        defaultClient.setBasePath(url);
        return defaultClient;
    }

    @Bean
    public IndexApi indexApi() {
        return new IndexApi(apiClient());
    }

    @Bean
    public SearchApi searchApi() {
        return new SearchApi(apiClient());
    }

    @Bean
    public UtilsApi utilsApi() {
        return new UtilsApi(apiClient());
    }
}
