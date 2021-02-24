package com.ilubov.repository;

import com.ilubov.vo.EsTestVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsTestRepository extends ElasticsearchRepository<EsTestVo, Long> {

    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    List<EsTestVo> findByName(String name);

    List<EsTestVo> findByDesc(String desc);

    Page<EsTestVo> findByDesc(String desc, Pageable pageable);

    List<EsTestVo> findByNameOrDesc(String name, String desc);
}

