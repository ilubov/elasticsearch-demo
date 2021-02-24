package com.ilubov.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "test")
public class EsTestVo {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Date)
    private Date time;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String desc;
}
