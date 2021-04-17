package com.sakura.common.mq.infrastructure.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @auther YangFan
 * @Date 2021/1/19 14:38
 */
@FunctionalInterface
public interface ResultSetConverter<T> {

    T convert(ResultSet resultSet) throws SQLException;
}
