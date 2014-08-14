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

import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.openmhealth.dsu.service.DataPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;


/**
 * A controller that handles the calls that read and write data points.
 *
 * @author Emerson Farrugia
 */
// FIXME review this whole thing
@ApiController
public class DataPointController {

    public static final String DEFAULT_RESULT_LIMIT = "100";

    // FIXME this is just for testing until Spring Security is wired up
    private static final String TEST_USER_ID = "test";

    /**
     * FIXME A parameter that limits the returned data points to those with an effective timestamp on or after the given
     * timestamp. FIXME Is it just the start timestamp on an interval?
     */
    public static final String START_TIMESTAMP_PARAMETER = "t_start";


    public static final String END_TIMESTAMP_PARAMETER = "t_end";
    public static final String OFFSET_PARAMETER = "num_to_skip";
    public static final String LIMIT_PARAMETER = "num_to_return";

    private static final Logger log = LoggerFactory.getLogger(DataPointController.class);

    @Autowired
    private DataPointService dataPointService;

    /**
     * Reads data points.
     *
     * @param schemaName the name of the schema the data points conform to
     * @param schemaVersion the version of the schema the data points conform to
     * @param startTimestamp the earliest data points to return // FIXME what timestamp is this referring to?
     * effective?
     * @param endTimestamp the latest data points to return // FIXME
     * @param offset the number of data points to skip
     * @param limit the number of data points to return
     * @return a list of matching data points
     */
//    @PreAuthorize("#oauth2.clientHasRole('ROLE_CLIENT')") // FIXME
    @RequestMapping(value = {
            "/{schemaName}/{schemaVersion}/data",
            "/{schemaName}:{schemaVersion}/data" // TODO confirm if we need this
    },
            method = {HEAD, GET}, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Iterable<DataPoint>> readData(
            @PathVariable final String schemaName,
            @PathVariable final String schemaVersion,
            // TODO replace with Optional<> in Spring MVC 4.1
            @RequestParam(value = START_TIMESTAMP_PARAMETER, required = false) final OffsetDateTime startTimestamp,
            @RequestParam(value = END_TIMESTAMP_PARAMETER, required = false) final OffsetDateTime endTimestamp,
//            @RequestParam(value = PARAM_COLUMN_LIST, required = false) final List<String> columnList,
            @RequestParam(value = OFFSET_PARAMETER, defaultValue = "0") final Integer offset,
            @RequestParam(value = LIMIT_PARAMETER, defaultValue = DEFAULT_RESULT_LIMIT) final Integer limit) {


        DataPointSearchCriteria searchCriteria = new DataPointSearchCriteria(TEST_USER_ID, schemaName, schemaVersion);

        // FIXME what timestamp is this supposed to refer to?
        if (startTimestamp != null) {
            searchCriteria.setStartTimestamp(startTimestamp);
        }
        if (endTimestamp != null) {
            searchCriteria.setEndTimestamp(endTimestamp);
        }

        Iterable<DataPoint> dataPoints = dataPointService.findBySearchCriteria(searchCriteria, offset, limit);

        HttpHeaders headers = new HttpHeaders();

        // FIXME
        // headers.set("Next");
        // headers.set("Previous");

        return new ResponseEntity<>(dataPoints, headers, HttpStatus.OK);
    }

    /**
     * Writes data points.
     */
    // FIXME authorize
    @RequestMapping(value = {
            "/{schemaName}/{schemaVersion}/data",
            "/{schemaName}:{schemaVersion}/data"
    },
            method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> writeData(
            @RequestBody List<DataPoint> dataPoints) {

        // FIXME add validation here or in service

        dataPointService.save(dataPoints);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
