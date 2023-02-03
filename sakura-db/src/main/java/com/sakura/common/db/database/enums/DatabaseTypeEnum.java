package com.sakura.common.db.database.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 数据源枚举类
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DatabaseTypeEnum {

    MYSQL("MySQL", "mysql", "mysql"),
    ORACLE("Oracle", "oracle", "oracle"),
    POSTGRE_SQL("PostgreSQL", "postgre", "postgre"),
    /**
     * 人大金仓
     */
    KINGBASE("KINGBASE", "kingbasees", "kingbase"),
    /**
     * 达梦sh
     */
    DM_DBMS("DM DBMS", "dm", "dm");

    private String databaseName;
    private String databaseId;
    /**
     * 数据库连接url子字符串
     */
    private String databaseUrlSubStr;

    public static DatabaseTypeEnum getByDatabaseName(String databaseName) {
        return Arrays.stream(DatabaseTypeEnum.values()).filter(d -> d.getDatabaseName().equals(databaseName)).findFirst().orElse(null);
    }

    public static DatabaseTypeEnum getByDatabaseUrl(String databaseUrl) {
        return Arrays.stream(DatabaseTypeEnum.values()).filter(d -> databaseUrl.contains(d.getDatabaseUrlSubStr())).findFirst().orElse(null);
    }

    public static List<DatabaseTypeEnum> getAllDatabaseIdEnums() {
        return Arrays.stream(DatabaseTypeEnum.values()).collect(Collectors.toList());
    }

}
