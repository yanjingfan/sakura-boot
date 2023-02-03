package com.sakura.common.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sakura.common.db.condition.ConditionOnMissingTenantProperty;
import com.sakura.common.db.database.enums.DatabaseTypeEnum;
import com.sakura.common.db.database.plugin.kingbase.KingbaseMybatisplusPlugin;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @auther YangFan
 * @Date 2020/6/23 12:02
 */

@EnableTransactionManagement
@Configuration
@MapperScan("com.sakura.cloud.**.mapper*")
public class MybatisPlusConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private HttpServletRequest request;

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Conditional(ConditionOnMissingTenantProperty.class)
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 默认使用mysql数据库
        String databaseProductName = DatabaseTypeEnum.MYSQL.getDatabaseName().toLowerCase();
        try {
            databaseProductName = dataSource.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 获取数据库类型
        DatabaseTypeEnum databaseType = DatabaseTypeEnum.getByDatabaseUrl(databaseProductName);
        switch (databaseType) {
            case KINGBASE: {
                // KINGBASE 适配 Mybatis-Plus 时，由于 Mybatis-Plus 无法识别 KINGBASE 数据库类型， 故在相关配置中需将其配置成 postgresql，如：在使用分页插件时，配置方言类型为 postgresql
                interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
                // 添加适配人大金仓数据库转换拦截器
                interceptor.addInnerInterceptor(new KingbaseMybatisplusPlugin());

                break;
            }
            case DM_DBMS: {
                interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.DM));
                // TODO 添加适配达梦数据库转换拦截器
//            interceptor.addInnerInterceptor(new DmMybatisplusPlugin());
                break;
            }
            default: {
                interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
                break;
            }
        }

        return interceptor;
    }

    /**
     * 多租户分页
     * @return
     */
    @ConditionalOnProperty(prefix = "tenant", name = "column")
    @Bean
    public MybatisPlusInterceptor tenantPaginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        TenantLineInnerInterceptor tenantInnerInterceptor = new TenantLineInnerInterceptor();
//        tenantInnerInterceptor.setTenantLineHandler(new TenantLineHandler() {
//            @Override
//            public Expression getTenantId() {
//                //return new LongValue(Long.parseLong(environment.getProperty("tenant.id")));
//                // 可将租户id和租户字段放置在请求头中
//                try {
//                    String tenantId = request.getHeader("tenantId");
//                    if (tenantId == null || "".equals(tenantId.trim())) {
//                        return new LongValue(-1);
//                    }
//                    return new LongValue(Long.parseLong(tenantId));
//                } catch (Exception e) {
//                    return new LongValue(-1);
//                }
//            }
//
//            @Override
//            public String getTenantIdColumn() {
//                return environment.getProperty("tenant.column");
//                // 可将租户id和租户字段放置在请求头中
//                //return request.getHeader("tenantColumn");
//            }
//
//            @Override
//            public boolean ignoreTable(String tableName) {
//                //忽略uid-generator模块相关表
//                if ("worker_node".equalsIgnoreCase(tableName)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        interceptor.addInnerInterceptor(tenantInnerInterceptor);
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
