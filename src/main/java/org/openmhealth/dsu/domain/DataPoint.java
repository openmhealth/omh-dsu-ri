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

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A data point.
 *
 * @author Emerson Farrugia
 */
public class DataPoint {

    private String userId;
    private DataPointMetadata metadata;
    private JsonNode data;


    /**
     * @param userId the identifier of the user this data point belongs to
     * @param metadata the metadata of this data point
     * @param data the data of this data point
     */
    public DataPoint(String userId, DataPointMetadata metadata, JsonNode data) {

        checkNotNull(userId);
        checkNotNull(metadata);
        checkNotNull(data);

        this.userId = userId;
        this.metadata = metadata;
        this.data = data;
    }

    @Id
    public String getId() {
        return getMetadata().getId();
    }

    public String getUserId() {
        return userId;
    }

    @Valid
    public DataPointMetadata getMetadata() {
        return metadata;
    }

    public JsonNode getData() {
        return data;
    }
}
