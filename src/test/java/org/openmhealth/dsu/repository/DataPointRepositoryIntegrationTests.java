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

package org.openmhealth.dsu.repository;

import com.mongodb.util.JSON;
import org.junit.After;
import org.junit.Test;
import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointMetadata;
import org.openmhealth.schema.domain.SchemaId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


/**
 * A suite of integration tests for a data point repository.
 *
 * @author Emerson Farrugia
 */
public abstract class DataPointRepositoryIntegrationTests {

    protected static final String TEST_USERNAME = "test";
    protected static final String TEST_DATA_POINT_ID = UUID.randomUUID().toString();
    protected static final SchemaId TEST_SCHEMA_ID = new SchemaId("test", "test", "1.0");

    @Autowired
    protected DataPointRepository dataPointRepository;


    protected DataPoint newDataPoint(Map<?, ?> data) {

        DataPointMetadata metadata = new DataPointMetadata(TEST_DATA_POINT_ID, TEST_SCHEMA_ID);

        return new DataPoint(TEST_USERNAME, metadata, data);
    }

    protected Map<?, ?> newKcalBurnedData() {

        return (Map<?, ?>) JSON.parse("{" +
                "    'kcal_burned': {" +
                "        'value': 160," +
                "        'unit': 'kcal'" +
                "    }," +
                "    'effective_time_frame': {" +
                "        'time_interval': {" +
                "            'start_time': '2013-02-05T06:25:00Z'," +
                "            'end_time': '2013-02-05T07:25:00Z'" +
                "        }" +
                "    }," +
                "    'activity_name': 'walking'" +
                "}");
    }

    @After
    public void deleteFixture() {

        dataPointRepository.delete(TEST_DATA_POINT_ID);
    }

    @Test
    public void findOneShouldReturnSavedDataPoint() {

        DataPoint expected = dataPointRepository.save(newDataPoint(newKcalBurnedData()));

        Optional<DataPoint> actual = dataPointRepository.findOne(TEST_DATA_POINT_ID);

        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getUserId(), equalTo(expected.getUserId()));
        assertThat(actual.get().getMetadata(), equalTo(expected.getMetadata()));

        // although this uses Mongo libraries to check deep equality, it should work with other data stores
        assertThat(JSON.serialize(actual.get().getData()), equalTo(JSON.serialize(expected.getData())));
    }
}
