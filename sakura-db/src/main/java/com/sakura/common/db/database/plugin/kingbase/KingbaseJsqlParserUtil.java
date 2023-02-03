package com.sakura.common.db.database.plugin.kingbase;


import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:基于Jsqlparser的sql解析功能，并获取表名和where后面的条件
 */
@Slf4j
public class KingbaseJsqlParserUtil {

    /**
     * 反引号
     */
    public static final String BACKQUOTE_STR = "`";
    /**
     * 单引号
     */
    public static final String SINGLE_QUOTE_STR = "\'";
    /**
     * 双引号
     */
    public static final String DOUBLE_QUOTE_STR = "\"";

    /**
     * 获取SQL中的全部表名
     *
     * @param sql
     * @return
     */
    public static String getTableName(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableNameList = tablesNamesFinder.getTableList(statement);
            if (!CollectionUtils.isEmpty(tableNameList)) {
                StringBuffer allTableNames = new StringBuffer();
                tableNameList.forEach(tableName -> {
                    allTableNames.append(tableName + ",");
                });
                String allTableName = allTableNames.toString().substring(0, allTableNames.toString().length() - 1);
                return allTableName;
            }
        } catch (JSQLParserException e) {
            log.warn("getTableName fail>>>>【{}】", e);
        }
        return null;
    }

    /**
     * 处理字段
     *
     * @param expression
     */
    public static void translateParser(Expression expression) {
        //初始化接受获得的字段信息
        if (expression instanceof BinaryExpression) {
            //获得左边表达式
            Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
            //获得左边表达式为Column对象，则直接获得列名
            if (leftExpression instanceof Column) {
                String newColumnName = getNewColumnName(((Column) leftExpression).getColumnName());
                ((Column) leftExpression).setColumnName(newColumnName);

            } else if (leftExpression instanceof InExpression) {
                parserInExpression(leftExpression);
            } else if (leftExpression instanceof IsNullExpression) {
                parserIsNullExpression(leftExpression);
            } else if (leftExpression instanceof BinaryExpression) {
                //递归调用
                translateParser(leftExpression);
            } else if (expression instanceof Parenthesis) {
                //递归调用
                Expression expression1 = ((Parenthesis) expression).getExpression();
                translateParser(expression1);
            }

            //获得右边表达式，并分解
            Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
            if (rightExpression instanceof BinaryExpression) {
                parserBinaryExpression(rightExpression);
            } else if (rightExpression instanceof InExpression) {
                parserInExpression(rightExpression);
            } else if (rightExpression instanceof IsNullExpression) {
                parserIsNullExpression(rightExpression);
            } else if (rightExpression instanceof Parenthesis) {
                //递归调用
                Expression expression1 = ((Parenthesis) rightExpression).getExpression();
                translateParser(expression1);
            }
        } else if (expression instanceof InExpression) {
            parserInExpression(expression);
        } else if (expression instanceof IsNullExpression) {
            parserIsNullExpression(expression);
        } else if (expression instanceof Parenthesis) {
            //递归调用
            Expression expression1 = ((Parenthesis) expression).getExpression();
            translateParser(expression1);
        }
    }

    /**
     * 解析in关键字左边的条件
     *
     * @param expression
     */
    public static void parserInExpression(Expression expression) {
        Expression leftExpression = ((InExpression) expression).getLeftExpression();
        if (leftExpression instanceof Column) {
            ((Column) leftExpression).setColumnName(getNewColumnName(((Column) leftExpression).getColumnName()));
        }
    }

    /**
     * 解析is null 和 is not null关键字左边的条件
     *
     * @param expression
     */
    public static void parserIsNullExpression(Expression expression) {
        Expression leftExpression = ((IsNullExpression) expression).getLeftExpression();
        if (leftExpression instanceof Column) {
            ((Column) leftExpression).setColumnName(getNewColumnName(((Column) leftExpression).getColumnName()));
        }
    }

    public static void parserBinaryExpression(Expression expression) {
        Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
        if (leftExpression instanceof Column) {
            ((Column) leftExpression).setColumnName(getNewColumnName(((Column) leftExpression).getColumnName()));
        }
    }

    /**
     * 列名前后拼接上双引号
     *
     * @param columnName
     * @return
     */
    public static String getNewColumnName(String columnName) {
        //替换反引号与双引号为空，最后再拼接上去
        columnName = columnName.replaceAll(BACKQUOTE_STR, StringUtils.EMPTY).replaceAll(DOUBLE_QUOTE_STR, StringUtils.EMPTY).replaceAll(SINGLE_QUOTE_STR, StringUtils.EMPTY);

        //字段前后拼接双引号
        return new StringBuilder().append(DOUBLE_QUOTE_STR).append(columnName).append(DOUBLE_QUOTE_STR).toString();
    }

    /**
     * 测试类
     *
     * @param args
     * @throws JSQLParserException
     */
    public static void main(String[] args) {
        String sql = "select * from tt where id='0'";
        String tableName = getTableName(sql);
        System.out.println("tableName:" + tableName);
    }
}

