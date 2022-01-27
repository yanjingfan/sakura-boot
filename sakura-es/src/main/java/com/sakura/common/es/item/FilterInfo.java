package com.sakura.common.es.item;

import lombok.Data;

import java.util.List;

/**
 * @auther YangFan
 * @Date 2020/6/12 11:50
 * 接收请求参数
 */

@Data
public class FilterInfo {

    /**
     * 索引名
     */
    private String indexName;

    /**
     * 查询字段(如sql中的select field1,filed2 form xxx)
     * 参数形式：field1,filed2
     */
    private List<String> selectFields;

    /**
     * 搜索字段（如sql中的select * from xxx where key1 like '%value1%'）
     * 参数拼接样式：key1:value1,key2:value2
     */
    private List<QueryField> queryFields;

    /**
     * 范围查询的字段
     */
    private String rangeFiled;

    /**
     * 排序字段
     */
    private String sortFiled;

    /**
     * 排序方式、默认降序
     */
    private String sortMethod = "desc";

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 分页数
     */
//    @DecimalMin(value="0",message="分页数必须大于等于0")
    private Integer pageNum = 0;

    /**
     * 日志分页大小
     */
//    @DecimalMin(value="0",message="分页大小必须大于等于0")
    private Integer pageSize = 10;

    /**
     * 是否使用深分页（游标查询）
     */
    private Boolean scroll = false;

    /**
     * 游标id
     */
    private String scrollId;

}
