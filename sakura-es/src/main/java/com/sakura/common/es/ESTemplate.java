package com.sakura.common.es;

import com.sakura.common.es.exception.ESException;
import com.sakura.common.es.item.FilterInfo;
import com.sakura.common.es.item.QueryField;
import org.springframework.beans.factory.annotation.Value;
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
        Long endTime = filterInfo.getEndTime();
        Long startTime = filterInfo.getStartTime();
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
            if (!StringUtils.isEmpty(rangeFiled)) {
                if (!CollectionUtils.isEmpty(queryFields)) {
                    sb.append(" and q=")
                            .append(rangeFiled)
                            .append(":[");
                } else {
                    sb.append("&q=")
                            .append(rangeFiled)
                            .append(":[");
                }
                if (startTime != null) {
                    sb.append(startTime);
                } else {
                    sb.append("*");
                }
                sb.append(" TO ");
                if (endTime != null) {
                    sb.append(endTime);
                } else {
                    sb.append("*");
                }
                sb.append("]");
            }
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

}
