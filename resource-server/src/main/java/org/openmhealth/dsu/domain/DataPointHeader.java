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

import org.openmhealth.schema.domain.SchemaId;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The header of a data point.
 *
 * @author Emerson Farrugia
 */
public class DataPointHeader {

    private String id;
    private SchemaId schemaId;
    private OffsetDateTime creationTimestamp;
    private DataPointAcquisitionProvenance acquisitionProvenance;

    /**
     * @param id the identifier of the data point
     * @param schemaId the identifier of the schema the data point conforms to
     */
    public DataPointHeader(String id, SchemaId schemaId) {

        this(id, schemaId, OffsetDateTime.now());
    }

    /**
     * @param id the identifier of the data point
     * @param schemaId the identifier of the schema the data point conforms to
     * @param creationTimestamp the creation time of this data point
     */
    public DataPointHeader(String id, SchemaId schemaId, OffsetDateTime creationTimestamp) {

        checkNotNull(id);
        checkArgument(!id.isEmpty());
        checkNotNull(schemaId);
        checkNotNull(creationTimestamp);

        this.id = id;
        this.schemaId = schemaId;
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * @deprecated should only be used by frameworks for persistence or serialisation
     */
    @Deprecated
    DataPointHeader() {
    }

    public String getId() {
        return id;
    }

    public SchemaId getSchemaId() {
        return schemaId;
    }

    public OffsetDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * @return the acquisition provenance of the data point
     */
    public Optional<DataPointAcquisitionProvenance> getAcquisitionProvenance() {
        return Optional.ofNullable(acquisitionProvenance);
    }

    public void setAcquisitionProvenance(DataPointAcquisitionProvenance acquisitionProvenance) {
        this.acquisitionProvenance = acquisitionProvenance;
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

        DataPointHeader that = (DataPointHeader) object;

        if (acquisitionProvenance != null
                ? !acquisitionProvenance.equals(that.acquisitionProvenance)
                : that.acquisitionProvenance != null) {
            return false;
        }
        if (!creationTimestamp.equals(that.creationTimestamp)) {
            return false;
        }
        if (!id.equals(that.id)) {
            return false;
        }
        if (!schemaId.equals(that.schemaId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
