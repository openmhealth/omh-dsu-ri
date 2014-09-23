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

import org.springframework.data.annotation.PersistenceConstructor;

import javax.validation.Valid;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A data point.
 *
 * @author Emerson Farrugia
 */
public class DataPoint {

    private String userId;
    private DataPointMetadata metadata;
    private Map<?, ?> data;


    /**
     * @param userId the identifier of the user this data point belongs to
     * @param metadata the metadata of this data point
     * @param data the data of this data point
     */
    public DataPoint(String userId, DataPointMetadata metadata, Map<?, ?> data) {

        checkNotNull(userId);
        checkNotNull(metadata);
        checkNotNull(data);

        this.userId = userId;
        this.metadata = metadata;
        this.data = data;
    }

    @PersistenceConstructor
    DataPoint() {
    }

    public String getUserId() {
        return userId;
    }

    @Valid
    public DataPointMetadata getMetadata() {
        return metadata;
    }

    public Map<?, ?> getData() {
        return data;
    }
}
