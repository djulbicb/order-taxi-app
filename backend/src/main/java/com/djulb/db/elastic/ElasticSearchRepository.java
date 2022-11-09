package com.djulb.db.elastic;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.ObjectType;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticSearchRepository extends ElasticsearchRepository<EGps, String>, ElasticSearchRepositoryCustom {
    List<SearchHit<EGps>> searchTop3By(Sort sort);
    List<SearchHit<EGps>> searchTop50ByTypeAndStatus(ObjectType type, ObjectStatus status, Sort sort);
}


