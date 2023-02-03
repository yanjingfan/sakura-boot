package com.sakura.common.db.database.config.database;

import com.sakura.common.db.database.enums.DatabaseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @description: 获取数据库类型配置类
 * @author: zjh
 * @date: 2022-08-25
 */
@Configuration
public class DataSourceTypeConfig {

    @Autowired
    private DataSource dataSource;

    public static DatabaseTypeEnum databaseType;

    @PostConstruct
    public void init() {
        // 默认使用mysql数据库
        databaseType = DatabaseTypeEnum.MYSQL;
        String databaseProductName = DatabaseTypeEnum.MYSQL.getDatabaseName().toLowerCase();
        try {
            databaseProductName = dataSource.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseType = DatabaseTypeEnum.getByDatabaseUrl(databaseProductName);
    }

    public DatabaseTypeEnum getDataBaseType() {
        return databaseType;
    }
}

