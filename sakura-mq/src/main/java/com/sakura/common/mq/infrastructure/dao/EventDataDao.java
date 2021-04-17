package com.sakura.common.mq.infrastructure.dao;


import com.sakura.common.mq.infrastructure.po.EventData;

import java.util.List;

/**
 * @auther YangFan
 * @Date 2021/1/19 14:33
 */
public interface EventDataDao {

    void insert(EventData eventData);

    List<EventData> queryByEventIds(String eventIds);
}
