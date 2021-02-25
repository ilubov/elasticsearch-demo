package com.ilubov.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ilubov.vo.ElasticSearchPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public final class ElasticSearchUtil {

    private static ElasticSearchUtil instance = null;

    public ElasticSearchUtil() {
        instance = this;
    }

    @Value("${es.name-server}")
    private String server;

    @Value("${es.es-sql}")
    private String esSql;

    @Value("${es.type}")
    private String type;

    public static <T> ElasticSearchPageVo<T> query(String sql, Class<T> clazz) {
        String url = instance.server + instance.esSql;
        ElasticSearchPageVo<T> elasticSearchPageVo = new ElasticSearchPageVo<>();
        List<T> list = Lists.newArrayList();
        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(sql).execute().body();
        JSONObject json = JSONObject.parseObject(result);
        JSONObject hits = json.getJSONObject("hits");
        if (hits != null && hits.size() != 0) {
            JSONArray hitsArray = hits.getJSONArray("hits");
            JSONObject obj = hits.getJSONObject("total");
            int total = (int) obj.get("value");
            if (hitsArray != null && hitsArray.size() != 0) {
                for (int i = 0; i < hitsArray.size(); i++) {
                    JSONObject jsonObject = hitsArray.getJSONObject(i);
                    JSONObject source = jsonObject.getJSONObject("_source");
                    list.add(JSON.parseObject(JSON.toJSONString(source), clazz));
                }
            }
            elasticSearchPageVo.setList(list);
            elasticSearchPageVo.setCount(total);
        }
        return elasticSearchPageVo;
    }

    public static <T> void insert(T obj, String index) {
        String url = instance.server + index + instance.type;
        String str = JSON.toJSONString(obj);
        HttpRequest.post(url).body(str).execute();
    }
}
