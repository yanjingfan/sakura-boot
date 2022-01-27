package com.sakura.common.es.item;

/**
 * @auther yangfan
 * @date 2022/1/26
 * @describle
 */
public class QueryField {

    /**
     * 搜索字段
     */
    private String key;

    /**
     * 搜索值
     */
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
