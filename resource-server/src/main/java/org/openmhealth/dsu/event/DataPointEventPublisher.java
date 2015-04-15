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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Data point event publisher. It's called every time there is data point change.
 *
 * Created by kkujovic on 4/12/15.
 */
@Component
public class DataPointEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Generates data point event and publishes it.
     *
     * @param userId
     * @param dataPointId
     * @param eventType
     */
    public void publishEvent(String userId, String dataPointId, DataPointEvent.DataPointEventType eventType) {
        String eventDateTime = OffsetDateTime.now().toString();
        DataPointEvent event = new DataPointEvent(this, userId, dataPointId, eventType, eventDateTime);
        applicationEventPublisher.publishEvent(event);
    }
}
