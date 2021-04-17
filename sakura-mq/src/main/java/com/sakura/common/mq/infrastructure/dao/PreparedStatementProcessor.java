package com.sakura.common.mq.infrastructure.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @auther YangFan
 * @Date 2021/1/19 14:37
 */

@FunctionalInterface
public interface PreparedStatementProcessor {

    void process(PreparedStatement ps) throws SQLException;
}
