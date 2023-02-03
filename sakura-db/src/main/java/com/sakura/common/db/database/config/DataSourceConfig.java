package com.sakura.common.db.database.config;

import com.sakura.common.db.database.enums.DatabaseTypeEnum;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * @description: Mybatis动态数据源映射
 * @author: zjh
 * @date: 2022-08-25
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @ConditionalOnMissingBean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        List<DatabaseTypeEnum> databaseIdEnums = DatabaseTypeEnum.getAllDatabaseIdEnums();
        databaseIdEnums.stream().forEach(d -> {
            properties.setProperty(d.getDatabaseName(), d.getDatabaseId());
        });
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

}

