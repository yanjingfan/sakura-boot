package com.sakura.common.mq.infrastructure.dao.impl;

import com.sakura.common.mq.infrastructure.dao.EventDataDao;
import com.sakura.common.mq.infrastructure.dao.ResultSetConverter;
import com.sakura.common.mq.infrastructure.po.EventData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther YangFan
 * @Date 2021/1/19 14:43
 */

@RequiredArgsConstructor
@Repository
public class EventDataDaoImpl implements EventDataDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private static final ResultSetConverter<EventData> CONVERTER = r -> {
        EventData data = new EventData();
        data.setId(r.getLong("id"));
        data.setRecordId(r.getLong("record_id"));
        data.setSourceData(r.getString("source_data"));
        return data;
    };

    private static final ResultSetExtractor<List<EventData>> MULTI = r -> {
        List<EventData> list = new ArrayList<>();
        while (r.next()) {
            list.add(CONVERTER.convert(r));
        }
        return list;
    };

    @Override
    public void insert(EventData eventData) {
        jdbcTemplate.update("INSERT INTO event_data(record_id, source_data) VALUES (?,?)",
                p -> {
                    p.setLong(1, eventData.getRecordId());
                    p.setString(2, eventData.getSourceData());
                });
    }

    @Override
    public List<EventData> queryByEventIds(String eventIds) {
        return jdbcTemplate.query("SELECT * FROM event_data WHERE record_id IN " + eventIds, MULTI);
    }
}
