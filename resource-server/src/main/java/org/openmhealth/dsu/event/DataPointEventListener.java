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
import org.openmhealth.dsu.domain.DataPointAcquisitionProvenance;
import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;


/**
 * Created by kkujovic on 4/12/15.
 */
@Component
public class DataPointEventListener implements ApplicationListener<DataPointEvent> {

    private static final Logger log = LoggerFactory.getLogger(DataPointEventListener.class);


    @Autowired
    private SubscriptionService subscriptionService;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void onApplicationEvent(DataPointEvent event) {

        String userId = event.getUserId();

        Iterable<Subscription> subscriptions = subscriptionService.findByUserId(userId);
        Notification notification = new Notification();
        notification.setDataPointId(event.getDataPointId());
        notification.setEventType(event.getEventType());

        notification.setEventDateTime(OffsetDateTime.now().toString());
        subscriptions.forEach(sub -> {
                    try {
                        ResponseEntity<?> responseEntity = restTemplate.postForEntity(sub.getCallbackUrl(), notification, Notification.class);
                        if (responseEntity.getStatusCode() != OK) {
                            log.error("Notification error, {} status not expected", responseEntity.getStatusCode());
                        }
                    } catch (RestClientException e) {
                        log.error("Exception:", e);
                    }
                }
        );

    }
}
