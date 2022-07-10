package com.djulb;

import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.redis.RTaxiStatusRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer{

    private final RTaxiStatusRepository taxiStatusRepository;
    private final ElasticSearchRepository elasticSearchRepository;

    public AppConfig(RTaxiStatusRepository taxiStatusRepository, ElasticSearchRepository elasticSearchRepository) {
        this.taxiStatusRepository = taxiStatusRepository;
        this.elasticSearchRepository = elasticSearchRepository;

        deleteDataAtStartup();
    }

    private void deleteDataAtStartup() {
        taxiStatusRepository.deleteAll();
        elasticSearchRepository.deleteAll();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }
}
