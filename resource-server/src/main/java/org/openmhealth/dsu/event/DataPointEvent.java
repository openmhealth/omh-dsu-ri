/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.dsu.event;

import org.openmhealth.dsu.domain.DataPoint;
import org.springframework.context.ApplicationEvent;

/**
 * Event used in subscription API. It's created for every data point change (create and delete)
 * Created by kkujovic on 4/12/15.
 */
public class DataPointEvent extends ApplicationEvent {

    private final DataPointEventType eventType;
    private final String dataPointId;
    private final String userId;
    private final String eventDateTime;

    public enum DataPointEventType {
        CREATE, DELETE
    }

    public DataPointEvent(Object source, String userId, String dataPointId, DataPointEventType eventType, String eventDateTime) {
        super(source);
        this.dataPointId = dataPointId;
        this.userId = userId;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
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

    public String getEventDateTime() {
        return eventDateTime;
    }
}
