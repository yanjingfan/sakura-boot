
package com.sakura.common.loki.item;

import lombok.Data;

import java.util.List;

@Data
public class FilterInfo {

 /**
     * 是否开启LokiQL查询 默认是false 开启时除了语句和时间查询可以生效
     */
    private Boolean lokiQLFaucet = false;

     /**
     * loki语言LokiQL
     */
    private String lokiQL;

    /**
     * 索引名称JobName
     */
    private String jobName;

    /**
     * 查询的字段(配置在promtail里面的关键字字段)
     */
    private List<String> selectFields;

    /**
     * 搜索字段（全文模糊搜索字段）
     */
    private List<QueryField> queryFields;

    /**
     * 开始时间 时间格式 yyyy-MM-dd HH:mm:ss
     */
    private String start;

    /**
     * 结束时间 时间格式  yyyy-MM-dd HH:mm:ss
     */
    private String end;

    /**
     * 排序字段
     */
    private String sortFiled;

    /**
     * 排序方式、默认降序 ，排序目前只有时间字段 timestamp,time,date排序
     */
    private String sortMethod = "desc";

    /**
     *限制日志条数 默认1000条
     */
    private Integer limit=1000;

    /**
     *偏移量 默认2
     */
    private Integer step=2;
    
    /**
     * loki日志数据里包含了该字段数据 直接打印 不再继续处理数据
     */
    private String wordFields;

}
