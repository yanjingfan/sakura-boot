package com.sakura.common.es;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.sakura.common.es.exception.ESException;
import com.sakura.common.es.item.FilterInfo;
import com.sakura.common.es.item.QueryField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @auther YangFan
 * @Date 2020/5/28 15:49
 */
@Service
public class ESTemplate {

    @Value("${spring.elasticsearch.rest.uris}")
    private String ESurl;

    //GET /login_log-2020.06.17/_search?_source=user_name,temp_duration&sort=temp_duration:asc&q=user_name:admin&q=temp_duration:[140 TO *]
    public String getObjectString(FilterInfo filterInfo) {
        String indexName = filterInfo.getIndexName();
        Integer page = filterInfo.getPageNum();
//        Long endTime = filterInfo.getEndTime();
//        Long startTime = filterInfo.getStartTime();
        Integer pageSize = filterInfo.getPageSize();
        List<QueryField> queryFields = filterInfo.getQueryFields();
        List<String> selectFields = filterInfo.getSelectFields();
        String sortFiled = filterInfo.getSortFiled();
        String rangeFiled = filterInfo.getRangeFiled();
        String sortMethod = filterInfo.getSortMethod();
        String scrollId = filterInfo.getScrollId();
        Boolean openScroll = filterInfo.getScroll();

        RestTemplate restTemplate = new RestTemplate();

        StringBuilder sb = new StringBuilder("http://");
        sb.append(ESurl).append("/");

        if (!StringUtils.isEmpty(scrollId) && openScroll) {
            sb.append("/_search/scroll?scroll_id=");
            sb.append(scrollId).append("&scroll=1m");
            try {
                return restTemplate.getForObject(sb.toString(), String.class);
            } catch (Exception e) {
                throw new ESException("ES深分页查询出错!");
            }
        } else {
            sb.append(indexName);
            sb.append("/_search?");
            if (!CollectionUtils.isEmpty(selectFields)) {
                sb.append("_source=");
                StringBuilder sBuilder = new StringBuilder();
                selectFields.forEach(field -> sBuilder.append(field).append(","));
                String substring = sBuilder.substring(0, sBuilder.length() - 1);
                sb.append(substring);
            }
            //搜索
            if (!CollectionUtils.isEmpty(queryFields)) {
                queryFields.forEach(field -> {
                    sb.append("&q=").append(field.getKey()).append(":").append(field.getValue());
                });
            }
            //只接受long型范围查询
//            if (!StringUtils.isEmpty(rangeFiled)) {
//                if (!CollectionUtils.isEmpty(queryFields)) {
//                    sb.append(" and q=")
//                            .append(rangeFiled)
//                            .append(":[");
//                } else {
//                    sb.append("&q=")
//                            .append(rangeFiled)
//                            .append(":[");
//                }
//                if (startTime != null) {
//                    sb.append(startTime);
//                } else {
//                    sb.append("*");
//                }
//                sb.append(" TO ");
//                if (endTime != null) {
//                    sb.append(endTime);
//                } else {
//                    sb.append("*");
//                }
//                sb.append("]");
//            }
            //默认降序排序
            if (!StringUtils.isEmpty(sortFiled)) {
                sb.append("&sort=")
                        .append(sortFiled)
                        .append(":")
                        .append(sortMethod);
            }
            //分页
            if (page != null && page > 0) {
                if (page * pageSize > 10000) {
                    page--;
                    sb.append("&from=").append(10000 - pageSize);
                } else {
                    page--;
                    sb.append("&from=").append(page * pageSize);
                }
            }
            if (pageSize != null) {
                sb.append("&size=").append(pageSize);
            }
            try {
                return restTemplate.getForObject(sb.toString(), String.class);
            } catch (Exception e) {
                throw new ESException("ES通用查询出错!");
            }
        }
    }

    /**
     * es的post请求
     * {
     *     "query": {
     *         "bool": {
     *             "must": [
     *                 {
     *                     "match_phrase": {
     *                         "age": 10
     *                     }
     *                 },
     *                 {
     *                     "range": {
     *                         "time.keyword": {
     *                             "gte": "2022-01-01 11:20:32",
     *                             "lte": "2022-12-24 11:20:32"
     *                         }
     *                     }
     *                 }
     *             ]
     *         }
     *     },
     *     "sort": [
     *         {
     *             "time.keyword": {
     *                 "order": "asc"
     *             }
     *         }
     *     ]
     * }
     * @param filterInfo
     * @return
     */
    public String postObjectString(FilterInfo filterInfo) {
        String indexName = filterInfo.getIndexName();
        String end = filterInfo.getEnd();
        Integer page = filterInfo.getPageNum();
        String start = filterInfo.getStart();
        Integer pageSize = filterInfo.getPageSize();
        List<QueryField> queryFields = filterInfo.getQueryFields();
        List<String> selectFields = filterInfo.getSelectFields();
        String sortFiled = filterInfo.getSortFiled();
        Boolean sortIsStr = filterInfo.getSortIsStr();
        String rangeFiled = filterInfo.getRangeFiled();
        Boolean rangeIsStr = filterInfo.getRangeIsStr();
        String sortMethod = filterInfo.getSortMethod();
        String scrollId = filterInfo.getScrollId();
        Boolean openScroll = filterInfo.getScroll();

        RestTemplate restTemplate = new RestTemplate();

        StringBuilder sb = new StringBuilder("http://");
        sb.append(ESurl).append("/");

        if (!StringUtils.isEmpty(scrollId) && openScroll) {
            sb.append("/_search/scroll?scroll_id=");
            sb.append(scrollId).append("&scroll=1m");
            try {
                return restTemplate.getForObject(sb.toString(), String.class);
            } catch (Exception e) {
                throw new ESException("ES深分页查询出错!");
            }
        } else {
            sb.append(indexName);
            sb.append("/_search?");
            if (!CollectionUtils.isEmpty(selectFields)) {
                sb.append("_source=");
                StringBuilder sBuilder = new StringBuilder();
                selectFields.forEach(field -> sBuilder.append(field).append(","));
                String substring = sBuilder.substring(0, sBuilder.length() - 1);
                sb.append(substring);
            }
            JSONObject queryObject = new JSONObject();
            // 搜索
            JSONObject boolObject = new JSONObject();
            JSONObject mustObject = new JSONObject();
            JSONArray must = new JSONArray();
            if (!CollectionUtils.isEmpty(queryFields)) {
                queryFields.stream().forEach(item -> {
                    JSONObject matchPhrase = new JSONObject();
                    JSONObject match = new JSONObject();
                    match.put(item.getKey(), item.getValue());
                    matchPhrase.put("match_phrase", match);
                    must.add(matchPhrase);
                });
            }

            // 默认降序排序，目前测试的时候，只有数值类的字段允许排序
            if (!StringUtils.isEmpty(sortFiled)) {
                JSONObject sortFiledObject = new JSONObject();
                JSONObject orderObject = new JSONObject();
                orderObject.put("order", sortMethod);
                //排序字段为字符串时，后缀添加.keyword
                if (sortIsStr) {
                    sortFiled = sortFiled + ".keyword";
                }
                sortFiledObject.put(sortFiled, orderObject);
                queryObject.put("sort", sortFiledObject);
            }

            // 范围查询
            if (!StringUtils.isEmpty(rangeFiled)) {
                JSONObject rangeObject = new JSONObject();
                JSONObject rangeFiledObject = new JSONObject();
                JSONObject range = new JSONObject();
                range.put("gte", start);
                range.put("lte", end);
                //范围查询字段为字符串时，后缀添加.keyword
                if (rangeIsStr) {
                    rangeFiled = rangeFiled + ".keyword";
                }
                rangeFiledObject.put(rangeFiled, range);
                rangeObject.put("range", rangeFiledObject);
                must.add(rangeObject);
            }

            // 分页
            if (page != null && page > 0) {
                if (page * pageSize > 10000) {
                    page--;
                    sb.append("&from=").append(10000 - pageSize);
                } else {
                    page--;
                    sb.append("&from=").append(page * pageSize);
                }
            }

            if (pageSize != null) {
                sb.append("&size=").append(pageSize);
            }
            mustObject.put("must", must);
            boolObject.put("bool", mustObject);
            queryObject.put("query", boolObject);
            return sendPostRequest(sb.toString(), queryObject);
        }
    }

    public String sendPostRequest(String url, JSONObject params) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //这里设置为APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将请求头部和参数合成一个请求
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params, headers);
        //执行POST请求
        ResponseEntity<String> entity = client.postForEntity(url, requestEntity, String.class);
        return entity.getBody();
    }

}
