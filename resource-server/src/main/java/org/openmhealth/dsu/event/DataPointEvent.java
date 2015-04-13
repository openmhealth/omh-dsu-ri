package org.openmhealth.dsu.event;

import org.openmhealth.dsu.domain.DataPoint;
import org.springframework.context.ApplicationEvent;

/**
 * Created by kkujovic on 4/12/15.
 */
public class DataPointEvent extends ApplicationEvent {

    private final DataPointEventType eventType;
    private final String dataPointId;
    private final String userId;

    public enum DataPointEventType {
        CREATE, DELETE
    }

    public DataPointEvent(Object source, String userId,  String dataPointId, DataPointEventType eventType) {
        super(source);
        this.dataPointId = dataPointId;
        this.userId = userId;
        this.eventType = eventType;
    }

    public DataPointEventType getEventType() {
        return eventType;
    }

    public String getDataPointId() {
        return dataPointId;
    }

    public String getUserId() {
        return userId;
    }
}
