package org.openmhealth.dsu.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

/**
 * Created by kkujovic on 4/12/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {
    private String dataPointId;
    private DataPointEvent.DataPointEventType eventType;
    private String eventDateTime;

    public String getDataPointId() {
        return dataPointId;
    }

    public void setDataPointId(String dataPointId) {
        this.dataPointId = dataPointId;
    }

    public DataPointEvent.DataPointEventType getEventType() {
        return eventType;
    }

    public void setEventType(DataPointEvent.DataPointEventType eventType) {
        this.eventType = eventType;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }
}
