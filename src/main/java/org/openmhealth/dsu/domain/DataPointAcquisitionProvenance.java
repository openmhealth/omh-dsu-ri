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

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The acquisition provenance of a data point, representing how and when the data point was acquired.
 *
 * @author Emerson Farrugia
 */
public class DataPointAcquisitionProvenance {

    private DataPointOrigin origin;
    private OffsetDateTime acquisitionTimestamp;

    public DataPointAcquisitionProvenance(DataPointOrigin origin, OffsetDateTime acquisitionTimestamp) {

        checkNotNull(origin, "An origin hasn't been specified.");
        checkNotNull(acquisitionTimestamp, "An acquisition timestamp hasn't been specified.");

        this.origin = origin;
        this.acquisitionTimestamp = acquisitionTimestamp;
    }

    public DataPointOrigin getOrigin() {
        return origin;
    }

    public OffsetDateTime getAcquisitionTimestamp() {
        return acquisitionTimestamp;
    }
}
