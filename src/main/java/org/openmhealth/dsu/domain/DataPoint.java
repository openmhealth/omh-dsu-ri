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

package org.openmhealth.dsu.domain;

import org.springframework.data.annotation.Id;


/**
 * A data point.
 *
 * @author Emerson Farrugia
 */
public class DataPoint {

    private String userId;
    private DataPointMetadata metadata;
    private DataPointPayload payload;


    /**
     * @param userId the identifier of the user this data point belongs to
     * @param metadata the metadata of the data point
     * @param payload the payload of the data point
     */
    public DataPoint(String userId, DataPointMetadata metadata, DataPointPayload payload) {

        this.userId = userId;
        this.metadata = metadata;
        this.payload = payload;
    }

    @Id
    public String getId() {
        return getMetadata().getId();
    }

    public String getUserId() {
        return userId;
    }

    public DataPointMetadata getMetadata() {
        return metadata;
    }

    public DataPointPayload getPayload() {
        return payload;
    }
}
