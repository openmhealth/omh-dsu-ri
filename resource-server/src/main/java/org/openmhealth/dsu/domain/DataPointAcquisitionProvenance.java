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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The acquisition provenance of a data point, representing how and when the data point was acquired.
 *
 * @author Emerson Farrugia
 */
public class DataPointAcquisitionProvenance {

    private String sourceName;
    private OffsetDateTime sourceCreationDateTime;
    private DataPointModality modality;

    /**
     * @deprecated should only be used by frameworks for persistence or serialisation
     */
    @Deprecated
    DataPointAcquisitionProvenance() {
    }

    /**
     * @param sourceName the name of the source of the data point
     */
    public DataPointAcquisitionProvenance(String sourceName) {

        checkNotNull(sourceName);
        checkArgument(!sourceName.isEmpty());

        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    /**
     * @return the timestamp of data point creation at the source
     */
    public Optional<OffsetDateTime> getSourceCreationDateTime() {
        return Optional.ofNullable(sourceCreationDateTime);
    }

    public void setSourceCreationDateTime(OffsetDateTime sourceCreationDateTime) {
        this.sourceCreationDateTime = sourceCreationDateTime;
    }

    /**
     * @return the modality whereby the measure is obtained
     */
    public Optional<DataPointModality> getModality() {
        return Optional.ofNullable(modality);
    }

    public void setModality(DataPointModality modality) {
        this.modality = modality;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        DataPointAcquisitionProvenance that = (DataPointAcquisitionProvenance) object;

        if (modality != that.modality) {
            return false;
        }
        if (sourceCreationDateTime != null ? !sourceCreationDateTime.equals(that.sourceCreationDateTime)
                : that.sourceCreationDateTime != null) {
            return false;
        }
        if (!sourceName.equals(that.sourceName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceName.hashCode();
        result = 31 * result + (sourceCreationDateTime != null ? sourceCreationDateTime.hashCode() : 0);
        result = 31 * result + (modality != null ? modality.hashCode() : 0);
        return result;
    }
}
