package com.sakura.common.db.database.plugin.dm;


import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 达梦数据库转换拦截器。
 * 当项目内使用MybatisPlus整合dameng(达梦数据库)时, 由于dameng的sql语法和MySQL不太一样，写了个插件拦截sql并进行修改。
 */
public class DMMybatisplusPlugin extends JsqlParserSupport implements InnerInterceptor {

    public static final String POINT_STR = ".";
    private static final String AS_STR = " AS ";

    /**
     * 查询时处理逻辑
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
//        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
//        // 修改sql后执行
//        String sql = mpBs.sql().replaceAll(DMJsqlParserUtil.BACKQUOTE_STR, StringUtils.EMPTY);
//        mpBs.sql(parserMulti(sql, null));
    }

    /**
     * 增删改查时处理逻辑
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();

        // 修改sql后执行
        if (sct == SqlCommandType.SELECT || sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            String sql = mpBs.sql().replaceAll(DMJsqlParserUtil.BACKQUOTE_STR, StringUtils.EMPTY);
            mpBs.sql(parserMulti(sql, null));
        }
    }

    /**
     * 处理查询sql
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem instanceof Table) {
                plainSelect.setFromItem(fromItem);
            }

            reformatPlainSelect(plainSelect);
        }
    }

    /**
     * 处理新增sql
     */
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        List<Column> columns = insert.getColumns();
        columns.stream().forEach(c -> {
            c.setColumnName(DMJsqlParserUtil.getNewColumnName(c.getColumnName()));
        });
    }

    /**
     * 处理删除sql
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        // 处理 where 条件
        DMJsqlParserUtil.translateParser(delete.getWhere());
    }

    /**
     * 处理修改sql
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        List<Column> columns = update.getColumns();
        columns.stream().forEach(c -> {
            c.setColumnName(DMJsqlParserUtil.getNewColumnName(c.getColumnName()));
        });

        // 处理 where 条件
        DMJsqlParserUtil.translateParser(update.getWhere());
    }

    /**
     * 处理查询字段
     */
    private List<SelectItem> disposeSelectColumn(List<SelectItem> selectItems) {
        return selectItems.stream().map(this::resetSelectItem).collect(Collectors.toList());
    }

    private SelectItem resetSelectItem(SelectItem selectItem) {
        //如果不符合直接返回
        if (!(selectItem instanceof SelectExpressionItem)) {
            return selectItem;
        }

        SelectExpressionItem item = (SelectExpressionItem) selectItem;

        // 如果是列
        if (item.getExpression() instanceof Column) {
            Column columnExp = (Column) item.getExpression();
            return new SelectExpressionItem(reFormatSelectColumn(columnExp, item.getAlias()));
        }

        //如果是函数
        if (item.getExpression() instanceof Function) {
            Function function = (Function) item.getExpression();
            return new SelectExpressionItem(reFormatFunction(function));
        }

        return item;
    }

    /**
     * 重新格式化 查询语句
     *
     * @param plainSelect 查询语句
     * @return 格式化的查询语句
     */
    public void reformatPlainSelect(PlainSelect plainSelect) {
        //处理要查询的字段
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        plainSelect.setSelectItems(disposeSelectColumn(selectItems));

        // 处理 where 条件
        DMJsqlParserUtil.translateParser(plainSelect.getWhere());
    }

    /**
     * 重新格式化列
     *
     * @param columnExp 列
     * @param alias     列别名
     * @return 格式化的列
     */
    private Column reFormatSelectColumn(Column columnExp, Alias alias) {
        if (columnExp == null) {
            return columnExp;
        }

        String tableName = null;
        if (columnExp.getTable() != null) {
            tableName = columnExp.getTable().getName();
        }

        //字段名
        String columnName = columnExp.getColumnName();
        StringBuilder finalName = new StringBuilder();

        if (StringUtils.isNotBlank(tableName)) {
            finalName.append(tableName).append(POINT_STR);
        }

        //拼接字段名
        finalName.append(DMJsqlParserUtil.getNewColumnName(columnName));

        //拼接别名: AS xxx
        if (alias != null) {
            finalName.append(AS_STR).append(DMJsqlParserUtil.getNewColumnName(alias.getName()));
        }

        //重新格式化列名 封装返回
        return new Column(finalName.toString());
    }

    /**
     * 重新格式化查询函数
     *
     * @param function 函数
     * @return 格式化的函数
     */
    private Function reFormatFunction(Function function) {
        if (function.getParameters() == null) {
            return function;
        }
        List<Expression> expressions = function.getParameters().getExpressions();
        if (CollectionUtils.isEmpty(expressions)) {
            return function;
        }

        // 函数名
        String functionName = function.getName();

        //对于是列的参数进行格式化
        expressions = expressions.stream().map(exp -> {
            if (exp instanceof Column) {
                return reFormatSelectColumn((Column) exp, null);
            } else {
                if ("DATE_FORMAT".equalsIgnoreCase(functionName)) {
                    // 替换方法名
//                    function.setName("TO_DATE");
                    // 修改时间格式表达式
                    if (exp instanceof StringValue) {
//                        translateFormat((StringValue) exp);
                    }
                } else if ("STR_TO_DATE".equalsIgnoreCase(functionName)) {
                    if (exp instanceof StringValue) {
//                        translateFormat((StringValue) exp);
                    }
                } else if ("CURDATE".equalsIgnoreCase(functionName)) {
                    // 测试
//                    function.setName("to_char(now(), 'YYYY-MM-DD')");
                } else if ("CURTIME".equalsIgnoreCase(functionName)) {
                    // 测试
//                    function.setName("to_char(now(), 'HH24:MI:SS')");
                }
            }
            return exp;
        }).collect(Collectors.toList());

        //重新设置回去
        function.getParameters().setExpressions(expressions);

        return function;
    }

    private static void translateFormat(StringValue exp) {
        exp.setValue(exp.getValue()
                .replaceAll("%Y", "YYYY")
                .replaceAll("%y", "YYYY")
                .replaceAll("%m", "MM")
                .replaceAll("%M", "MM")
                .replaceAll("%c", "MM")
                .replaceAll("%e", "DD")
                .replaceAll("%d", "DD")
                .replaceAll("%H", "HH24")
                .replaceAll("%h", "HH24")
                .replaceAll("%I", "HH24")
                .replaceAll("%k", "HH24")
                .replaceAll("%l", "HH24")
                .replaceAll("%i", "MI")
                .replaceAll("%S", "SS")
                .replaceAll("%s", "SS")
        );
    }

    /**
     * 重新格式化子查询
     *
     * @param subSelect 子查询
     * @return 格式化的函数
     */
    private SubSelect reFormatSubSelect(SubSelect subSelect) {

        if (subSelect.getSelectBody() instanceof PlainSelect) {
            reformatPlainSelect((PlainSelect) subSelect.getSelectBody());
        }

        return subSelect;
    }


    /**
     * 处理 where 条件（只能处理简单的where，若where后有多个条件会有问题）
     *
     * @param expression
     * @return
     */
    public Expression disposeSelectWhere(Expression expression) {

        if (!(expression instanceof BinaryExpression)) {
            return expression;
        }

        BinaryExpression binaryExpression = (BinaryExpression) expression;

        //如果左边还是多条件的
        if (binaryExpression.getLeftExpression() instanceof BinaryExpression) {
            disposeSelectWhere(binaryExpression.getLeftExpression());
        }

        //如果右边还是多条件的
        if (binaryExpression.getRightExpression() instanceof BinaryExpression) {
            disposeSelectWhere(binaryExpression.getRightExpression());
        }

        //如果左边表达式是列信息 格式化
        if (binaryExpression.getLeftExpression() instanceof Column) {
            Column newColumn = reFormatSelectColumn((Column) binaryExpression.getLeftExpression(), null);
            binaryExpression.setLeftExpression(newColumn);
        }

        //如果左边表达式是 子查询 processPlainSelect
        if (binaryExpression.getLeftExpression() instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) binaryExpression.getLeftExpression();
            if (subSelect.getSelectBody() instanceof PlainSelect) {
                reformatPlainSelect((PlainSelect) subSelect.getSelectBody());
            }
        }

        //如果右边是列信息 格式化
        if (binaryExpression.getRightExpression() instanceof Column) {
            Column newColumn = reFormatSelectColumn((Column) binaryExpression.getLeftExpression(), null);
            binaryExpression.setRightExpression(newColumn);
        }

        //如果右边表达式是 子查询 processPlainSelect
        if (binaryExpression.getRightExpression() instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) binaryExpression.getRightExpression();
            reFormatSubSelect(subSelect);
        }

        return binaryExpression;
    }
}

