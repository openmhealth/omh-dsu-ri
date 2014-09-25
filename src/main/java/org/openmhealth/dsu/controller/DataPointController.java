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

package org.openmhealth.dsu.controller;

import com.google.common.collect.Range;
import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.openmhealth.dsu.service.DataPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;


/**
 * A controller that handles the calls that read and write data points.
 *
 * @author Emerson Farrugia
 */
@ApiController
public class DataPointController {

    private static final Logger log = LoggerFactory.getLogger(DataPointController.class);

    // FIXME this is just for testing until Spring Security is wired up
    private static final String TEST_USER_ID = "test";

    /*
     * These filtering parameters are temporary. They will likely change when a more generic filtering approach is
     * implemented.
     */
    public static final String CREATED_ON_OR_AFTER_PARAMETER = "createdOnOrAfter";
    public static final String CREATED_BEFORE_PARAMETER = "createdBefore";
    public static final String SCHEMA_NAMESPACE_PARAMETER = "schemaNamespace";
    public static final String SCHEMA_NAME_PARAMETER = "schemaName";
    public static final String SCHEMA_VERSION_PARAMETER = "schemaVersion";

    public static final String RESULT_OFFSET_PARAMETER = "offset";
    public static final String RESULT_LIMIT_PARAMETER = "limit";
    public static final String DEFAULT_RESULT_LIMIT = "100";

    @Autowired
    private DataPointService dataPointService;

    /**
     * Reads data points.
     *
     * @param schemaNamespace the namespace of the schema the data points conform to
     * @param schemaName the name of the schema the data points conform to
     * @param schemaVersion the version of the schema the data points conform to
     * @param createdOnOrAfter the earliest creation timestamp of the data points to return, inclusive
     * @param createdBefore the latest creation timestamp of the data points to return, exclusive
     * @param offset the number of data points to skip
     * @param limit the number of data points to return
     * @return a list of matching data points
     */
    // FIXME add authorization
    // TODO confirm if HEAD handling needs anything additional
    @RequestMapping(value = "/data", method = {HEAD, GET}, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Iterable<DataPoint>> readDataPoints(
            @RequestParam(value = SCHEMA_NAMESPACE_PARAMETER) final String schemaNamespace,
            @RequestParam(value = SCHEMA_NAME_PARAMETER) final String schemaName,
            @RequestParam(value = SCHEMA_VERSION_PARAMETER, required = false) final String schemaVersion,
            // TODO replace with Optional<> in Spring MVC 4.1
            @RequestParam(value = CREATED_ON_OR_AFTER_PARAMETER, required = false)
            final OffsetDateTime createdOnOrAfter,
            @RequestParam(value = CREATED_BEFORE_PARAMETER, required = false) final OffsetDateTime createdBefore,
            @RequestParam(value = RESULT_OFFSET_PARAMETER, defaultValue = "0") final Integer offset,
            @RequestParam(value = RESULT_LIMIT_PARAMETER, defaultValue = DEFAULT_RESULT_LIMIT) final Integer limit) {

        // TODO add validation or explicitly comment that this is handled using exception translators

        DataPointSearchCriteria searchCriteria =
                new DataPointSearchCriteria(TEST_USER_ID, schemaNamespace, schemaName, schemaVersion);

        if (createdOnOrAfter != null && createdBefore != null) {
            searchCriteria.setCreationTimestampRange(Range.closedOpen(createdOnOrAfter, createdBefore));
        }
        else if (createdOnOrAfter != null) {
            searchCriteria.setCreationTimestampRange(Range.atLeast(createdOnOrAfter));
        }
        else if (createdBefore != null) {
            searchCriteria.setCreationTimestampRange(Range.lessThan(createdBefore));
        }

        Iterable<DataPoint> dataPoints = dataPointService.findBySearchCriteria(searchCriteria, offset, limit);

        HttpHeaders headers = new HttpHeaders();

        // FIXME add pagination headers
        // headers.set("Next");
        // headers.set("Previous");

        return new ResponseEntity<>(dataPoints, headers, OK);
    }

    /**
     * Reads a data point.
     *
     * @param id the identifier of the data point to read
     * @return a matching data point, if found
     */
    // FIXME add @PostAuthorize
    // TODO force data point identifiers to be globally unique, or rethink this URI
    // TODO confirm if HEAD handling needs anything additional
    @RequestMapping(value = "/data/{id}", method = {HEAD, GET}, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<DataPoint> readDataPoint(@PathVariable String id) {

        Optional<DataPoint> dataPoint = dataPointService.findOne(id);

        if (!dataPoint.isPresent()) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(dataPoint.get(), OK);
    }

    /**
     * Writes data points.
     *
     * @param dataPoints the list of data points to write
     */
    // FIXME add @PreAuthorize
    @RequestMapping(value = "/data", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> writeDataPoints(@RequestBody List<DataPoint> dataPoints) {

        // FIXME add validation

        // FIXME set userId
        for (DataPoint dataPoint : dataPoints) {
            dataPoint.setUserId(TEST_USER_ID);
        }

        dataPointService.save(dataPoints);

        return new ResponseEntity<>(CREATED);
    }

    /**
     * Deletes a data point.
     *
     * @param id the identifier of the data point to delete
     */
    // FIXME authorize
    // TODO can this just be id or are identifiers relative?
    @RequestMapping(value = "/data/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDataPoint(@PathVariable String id) {

        dataPointService.delete(id);

        return new ResponseEntity<>(OK);
    }
}
