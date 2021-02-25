package com.ilubov.vo;

import lombok.Data;

import java.util.List;

@Data
public class ElasticSearchPageVo<T> {

    private List<T> list;

    private Integer count;
}
