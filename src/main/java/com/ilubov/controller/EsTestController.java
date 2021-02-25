package com.ilubov.controller;

import com.alibaba.fastjson.JSON;
import com.ilubov.query.EsTestQuery;
import com.ilubov.repository.EsTestRepository;
import com.ilubov.util.ElasticSearchUtil;
import com.ilubov.vo.EsTestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "es测试")
@RestController
@RequestMapping("/es/test")
public class EsTestController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsTestRepository esTestRepository;

    @Value("${es.index.test}")
    private String test;

    @PostMapping("/save")
    @ApiOperation("保存")
    public Object save(@RequestBody EsTestVo entity) {
        entity.setTime(new Date());
        return esTestRepository.save(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Object update(@RequestBody EsTestVo entity) {
        String id = entity.getId().toString();
        Document document = Document.parse(JSON.toJSONString(entity));
        UpdateQuery build = UpdateQuery.builder(id)
                .withDocument(document) .withScriptedUpsert(true) .build();
        return elasticsearchRestTemplate.update(build, IndexCoordinates.of("test"));
    }

    @GetMapping("/page")
    @ApiOperation("分页")
    public Object page(EsTestQuery query) {
        PageRequest pageRequest = PageRequest.of(query.getPage() - 1, query.getSize());
        return esTestRepository.findAll(pageRequest);
    }

    @GetMapping("/find-all")
    @ApiOperation("全部数据")
    public Object all() {
        return esTestRepository.findAll();
    }

    @GetMapping("/find-by-id")
    @ApiOperation("根据id查询")
    public Object queryById(Long id) {
        return esTestRepository.findById(id);
    }

    @GetMapping("/find-by-name")
    @ApiOperation("根据名字查询")
    public Object findByName(String name) {
        return esTestRepository.findByName(name);
    }

    @GetMapping("/find-by-desc")
    @ApiOperation("根据描述分词查询")
    public Object findByDesc(String desc) {
        return esTestRepository.findByDesc(desc);
    }

    @GetMapping("/delete-by-name")
    @ApiOperation("根据id删除")
    public void deleteById(Long id) {
        esTestRepository.deleteById(id);
    }

    @GetMapping("/delete-all")
    @ApiOperation("删除全部")
    public void deleteAll() {
        esTestRepository.deleteAll();
    }

    @GetMapping("/page-by-desc")
    @ApiOperation("根据描述分词分页")
    public Object findByDesc(EsTestQuery query) {
        PageRequest pageRequest = PageRequest.of(query.getPage() - 1, query.getSize());
        return esTestRepository.findByDesc(query.getDesc(), pageRequest);
    }

    @GetMapping("/find-by-name-or-desc")
    @ApiOperation("根据名字或者描述查询")
    public Object findByNameOrDesc(EsTestQuery query) {
        return esTestRepository.findByNameOrDesc(query.getName(), query.getDesc());
    }

    @PostMapping("/save2")
    @ApiOperation("保存2")
    public void save2(@RequestBody EsTestVo entity) {
        entity.setTime(new Date());
        ElasticSearchUtil.insert(entity, test);
    }

    @GetMapping("/page2")
    @ApiOperation("分页2")
    public Object page2(EsTestQuery query) {
        int start = (query.getPage() - 1) * query.getSize(), end = query.getSize();
        String sql = "select * from " + test.replace("/", "") + " limit " + start + "," + end;
        return ElasticSearchUtil.query(sql, EsTestVo.class);
    }

    @GetMapping("/sql")
    @ApiOperation("sql查询")
    public Object sql(String sql) {
        return ElasticSearchUtil.query(sql, EsTestVo.class);
    }
}
