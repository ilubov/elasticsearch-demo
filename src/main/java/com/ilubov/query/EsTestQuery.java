package com.ilubov.query;

import lombok.Data;

@Data
public class EsTestQuery {

    private Integer page;

    private Integer size;

    private String name;

    private String desc;
}
