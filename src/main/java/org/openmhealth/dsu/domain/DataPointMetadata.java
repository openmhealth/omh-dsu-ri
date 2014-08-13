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

import java.util.Optional;


/**
 * The metadata of a data point.
 *
 * @author Emerson Farrugia
 */
public class DataPointMetadata {

    private String id;
    private String schemaId;
    private DataPointAcquisitionProvenance acquisitionProvenance;

    /**
     * @param id the globally unique identifier of the data point
     * @param schemaId the identifier of the schema the data point conforms to
     */
    public DataPointMetadata(String id, String schemaId) {

        this.id = id;
        this.schemaId = schemaId;
    }

    public String getId() {
        return id;
    }

    public String getSchemaId() {
        return schemaId;
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
}
