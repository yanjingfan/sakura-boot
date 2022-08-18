package com.sakura.common.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sakura.common.db.condition.ConditionOnMissingTenantProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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

//    @Autowired
//    private HttpServletRequest request;

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Conditional(ConditionOnMissingTenantProperty.class)
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//默认为Mysql
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

    /**
     * 分页
     * @return
     */
//    @Conditional(ConditionOnMissingTenantProperty.class)
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//        // paginationInterceptor.setOverflow(false);
//        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//        // paginationInterceptor.setLimit(500);
//        // 开启 count 的 join 优化,只针对部分 left join
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        return paginationInterceptor;
//    }

    /**
     * 分页
     * 
     * @return
     */
//    @ConditionalOnProperty(prefix = "tenant", name = "column")
//    @Bean
//    public PaginationInterceptor tenantPaginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//        tenantSqlParser.setTenantHandler(new TenantHandler() {
//            @Override
//            public Expression getTenantId(boolean select) {
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
//            public boolean doTableFilter(String tableName) {
//                // 这里可以判断是否过滤表
//                // if ("user".equals(tableName)) {
//                //    return true;
//                // }
//                if ("worker_node".equalsIgnoreCase(tableName)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//        // paginationInterceptor.setOverflow(false);
//        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//        // paginationInterceptor.setLimit(500);
//        // 开启 count 的 join 优化,只针对部分 left join
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        return paginationInterceptor;
//    }

}
