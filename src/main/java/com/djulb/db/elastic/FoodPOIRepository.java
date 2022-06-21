package com.djulb.db.elastic;

import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FoodPOIRepository extends ElasticsearchRepository<ElasticGps, String>, FoodPOIRepositoryCustom {
    List<SearchHit<ElasticGps>> searchTop3By(Sort sort);
    List<SearchHit<ElasticGps>> searchTop50ByTypeAndStatus(ElasticGps.Type type, ElasticGps.Status status, Sort sort);
}


