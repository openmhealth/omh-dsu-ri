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

import java.time.OffsetDateTime;
import java.util.Optional;


/**
 * A search criteria bean used to represent a search for data points.
 *
 * @author Emerson Farrugia
 */
public class DataPointSearchCriteria {

    private String userId;
    private String schemaName;
    private String schemaVersion;
    private OffsetDateTime startTimestamp;
    private OffsetDateTime endTimestamp;

    public DataPointSearchCriteria(String userId, String schemaName, String schemaVersion) {
        this.userId = userId;
        this.schemaName = schemaName;
        this.schemaVersion = schemaVersion;
    }

    public String getUserId() {
        return userId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    // TODO consider Range<OffsetDateTime> instead
    // FIXME this might go away based on metadata discussions
    public Optional<OffsetDateTime> getStartTimestamp() {
        return Optional.of(startTimestamp);
    }

    public void setStartTimestamp(OffsetDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Optional<OffsetDateTime> getEndTimestamp() {
        return Optional.of(endTimestamp);
    }

    public void setEndTimestamp(OffsetDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
