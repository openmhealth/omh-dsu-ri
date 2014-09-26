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

    private DataPointModality modality;
    private OffsetDateTime acquisitionTimestamp;

    public DataPointAcquisitionProvenance(DataPointModality modality, OffsetDateTime acquisitionTimestamp) {

        checkNotNull(modality);
        checkNotNull(acquisitionTimestamp);

        this.modality = modality;
        this.acquisitionTimestamp = acquisitionTimestamp;
    }

    public DataPointModality getModality() {
        return modality;
    }

    public OffsetDateTime getAcquisitionTimestamp() {
        return acquisitionTimestamp;
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        DataPointAcquisitionProvenance that = (DataPointAcquisitionProvenance) object;

        if (acquisitionTimestamp != null ? !acquisitionTimestamp.equals(that.acquisitionTimestamp)
                : that.acquisitionTimestamp != null) {
            return false;
        }
        if (modality != that.modality) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = modality != null ? modality.hashCode() : 0;
        result = 31 * result + (acquisitionTimestamp != null ? acquisitionTimestamp.hashCode() : 0);
        return result;
    }
}
