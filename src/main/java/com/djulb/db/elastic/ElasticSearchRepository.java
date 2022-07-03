package com.djulb.db.elastic;

import com.djulb.way.elements.ObjectStatus;
import com.djulb.way.elements.ObjectType;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticGps, String>, ElasticSearchRepositoryCustom {
    List<SearchHit<ElasticGps>> searchTop3By(Sort sort);
    List<SearchHit<ElasticGps>> searchTop50ByTypeAndStatus(ObjectType type, ObjectStatus status, Sort sort);
}


