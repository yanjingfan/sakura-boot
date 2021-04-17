package com.sakura.common.mq.infrastructure.dao;


import com.sakura.common.mq.infrastructure.po.EventRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @auther YangFan
 * @Date 2021/1/19 14:33
 */
public interface EventRecordDao {

    void insertSelective(EventRecord record);

    void updateStatusSelective(EventRecord record);

    List<EventRecord> queryPendingCompensationRecords(LocalDateTime minScheduleTime, LocalDateTime maxScheduleTime, int limit);
}
