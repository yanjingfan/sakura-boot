package com.sakura.common.web.datamask;

import java.util.function.Function;

public enum DataMaskEnum {
    /**
     * 名称脱敏
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2"))
    ,
    /**
     * 电话脱敏
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"))
    ,
    /**
     * 邮箱脱敏
     */
    EMAIL(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2"))
    ,
    /**
     * 住址脱敏
     */
    ADDRESS(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****"))
    ;

    /**
     * 成员变量  是一个接口类型
     */
    private Function<String, String> function;

    DataMaskEnum(Function<String, String> function) {
        this.function = function;
    }

    public Function<String, String> function() {
        return this.function;
    }
}
