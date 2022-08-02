package com.sakura.common.loki;

import com.alibaba.fastjson2.JSONObject;
import com.sakura.common.loki.exception.LokiException;
import com.sakura.common.loki.item.FilterInfo;
import com.sakura.common.loki.item.QueryField;
import com.sakura.common.loki.util.LokiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Service
public class LokiTemplate {

    @Value("${spring.loki.url}")
    private String Lokiurl;

    public List<Object> searchLokiLogByFilters(FilterInfo filterInfo) {
		try {
			// 查询loki日志
			if (filterInfo != null) {
				Boolean lokiQLFaucet=filterInfo.getLokiQLFaucet();
				String wordFields=filterInfo.getWordFields();
				String lokiQL=filterInfo.getLokiQL();
				String jobName=filterInfo.getJobName();
				List<String> selectFields=filterInfo.getSelectFields();
				List<QueryField> queryFields=filterInfo.getQueryFields();
				String start=filterInfo.getStart();
				String end=filterInfo.getEnd();
				String sortFiled=filterInfo.getSortFiled();
				String sortMethod=filterInfo.getSortMethod();
				Integer limit = filterInfo.getLimit();
				Integer step=filterInfo.getStep();
				StringBuilder sb = new StringBuilder("http://");
				sb.append(Lokiurl).append("/loki/api/v1/query_range?");
				// 默认日志大小
				if (limit != null) {
					sb.append("&limit=").append(limit);
				}
				//是否开启了只用Loki的语言来查询数据
				//否则的话就可以使用另一种查询方式
				if(lokiQLFaucet) {
					if(!StringUtils.isEmpty(lokiQL)) {
						sb.append("&query=");
						sb.append(URLEncoder.encode(lokiQL));
					}
				}else {
				
				// 查询job {job="log-access"}
				if (!StringUtils.isEmpty(jobName)) {
					StringBuilder lokiQLString = new StringBuilder();
					sb.append("&query=");
					// label精准查询
					lokiQLString.append("{").append("job").append("=").append("\"").append(jobName).append("\"");
					if (!CollectionUtils.isEmpty(queryFields)) {
						for(QueryField qf:queryFields) {
							lokiQLString.append(",").append(qf.getKey()).append("=").append("\"").append(qf.getValue()).append("\"");
						}
					}
					lokiQLString.append("}");
					sb.append(URLEncoder.encode(lokiQLString.toString()));
					// 搜索关键字模糊查询
					if (!CollectionUtils.isEmpty(selectFields)) {
						StringBuilder keyword = new StringBuilder();
						for(String selectField:selectFields) {
							keyword.append("|=").append("\"").append(selectField).append("\"");
						}
						sb.append(URLEncoder.encode(keyword.toString()));
					}
				}
				}
				
				// 只接受long型范围查询
				if (!StringUtils.isEmpty(start)) {
					Long startTimeSize=LokiUtil.getDateLong(start);
					sb.append("&start=");
					sb.append(startTimeSize*1000000);
				}
				if (!StringUtils.isEmpty(end)) {
					Long endTimeSize=LokiUtil.getDateLong(end);
					sb.append("&end=");
					sb.append(endTimeSize*1000000);
				}
				if (step!=null) {
					sb.append("&step=");
					sb.append(step);
				}
				//得到lokiQL
				String s = sb.toString();
				List<Object> list = new ArrayList<>();
				//请求Loki接口
				JSONObject jo = LokiUtil.get(s);
				//处理请求数据
				list=LokiUtil.dealData(jo, filterInfo);
				return list;
			}
			return null;
		} catch (Exception e) {
			throw new LokiException("获取loki数据失败",e);
		}
	}

}
