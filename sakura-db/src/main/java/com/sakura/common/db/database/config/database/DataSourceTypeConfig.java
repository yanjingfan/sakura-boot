package com.sakura.common.db.database.config.database;

import com.sakura.common.db.database.enums.DatabaseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @description: 获取数据库类型配置类
 * 项目代码注入此类，可以获取到数据库类型，通过判断那种数据库，去编写对应的sql和代码逻辑
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

