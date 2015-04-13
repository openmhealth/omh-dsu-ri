package org.openmhealth.dsu.event;

import org.openmhealth.dsu.domain.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by kkujovic on 4/12/15.
 */
@Component
public class DataPointEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(String userId, String dataPointId, DataPointEvent.DataPointEventType eventType) {
        DataPointEvent event = new DataPointEvent(this, userId, dataPointId, eventType);
        applicationEventPublisher.publishEvent(event);
    }
}
