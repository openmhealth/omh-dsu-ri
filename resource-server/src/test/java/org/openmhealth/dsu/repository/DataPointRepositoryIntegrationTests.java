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
import org.junit.Before;
import org.junit.Test;
import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.openmhealth.dsu.factory.DataPointFactory.newDataPointBuilder;
import static org.openmhealth.dsu.factory.DataPointFactory.newSearchCriteriaBuilder;


/**
 * A suite of integration tests for a data point repository.
 *
 * @author Emerson Farrugia
 */
public abstract class DataPointRepositoryIntegrationTests {

    @Autowired
    protected DataPointRepository repository;

    private DataPoint testDataPoint;
    private List<DataPoint> testDataPoints;


    @Before
    public void initialiseFixture() {

        testDataPoint = repository.save(newDataPointBuilder().build());

        testDataPoints = new ArrayList<>();
        testDataPoints.add(testDataPoint);
    }

    @After
    public void deleteFixture() {

        for (DataPoint dataPoint : testDataPoints) {
            repository.delete(dataPoint.getId());
        }
    }

    @Test
    public void findOneShouldReturnSavedDataPoint() {

        Optional<DataPoint> actual = repository.findOne(testDataPoint.getId());

        assertThat(actual.isPresent(), equalTo(true));
        assertThatDataPointsAreEqual(actual.get(), testDataPoint);
    }

    public void assertThatDataPointsAreEqual(DataPoint actual, DataPoint expected) {

        assertThat(actual.getUserId(), equalTo(expected.getUserId()));
        assertThat(actual.getMetadata(), equalTo(expected.getMetadata()));

        // although this uses Mongo libraries to check deep equality, it should work with other data stores
        assertThat(JSON.serialize(actual.getData()), equalTo(JSON.serialize(expected.getData())));
    }

    @Test(expected = NullPointerException.class)
    public void findBySearchCriteriaShouldThrowExceptionWithUndefinedCriteria() {

        repository.findBySearchCriteria(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findBySearchCriteriaShouldThrowExceptionWithNegativeOffset() {

        repository.findBySearchCriteria(newSearchCriteriaBuilder().build(), -1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findBySearchCriteriaShouldThrowExceptionWithNegativeLimit() {

        repository.findBySearchCriteria(newSearchCriteriaBuilder().build(), null, -1);
    }

    @Test
    // identical to
    // findBySearchCriteriaShouldReturnDataPointsMatchingSchemaMajorVersion
    // findBySearchCriteriaShouldReturnDataPointsMatchingSchemaMinorVersion
    // findBySearchCriteriaShouldReturnDataPointsMatchingSchemaName
    // findBySearchCriteriaShouldReturnDataPointsMatchingSchemaNamespace
    public void findBySearchCriteriaShouldReturnDataPointsMatchingUserId() {

        List<DataPoint> dataPoints =
                newArrayList(repository.findBySearchCriteria(newSearchCriteriaBuilder().build(), null, null));

        assertThat(dataPoints, hasSize(1));
        assertThatDataPointsAreEqual(dataPoints.get(0), testDataPoint);
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingUserId() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setUserId("foo").build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaNamespace() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaNamespace("foo").build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaName() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaName("foo").build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaMajorVersion() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaVersionMajor(0).build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaMinorVersion() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaVersionMinor(0).build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldReturnDataPointsMatchingSchemaVersionQualifier() {

        DataPoint newTestDataPoint = newDataPointBuilder().setSchemaVersionQualifier("RC1").build();

        newTestDataPoint = repository.save(newTestDataPoint);
        testDataPoints.add(newTestDataPoint);

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaVersionQualifier("RC1").build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, hasSize(1));
        assertThatDataPointsAreEqual(dataPoints.get(0), newTestDataPoint);
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaVersionQualifier() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaVersionQualifier("RC1").build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingMissingSchemaVersionQualifier() {

        DataPoint newTestDataPoint = newDataPointBuilder().setSchemaVersionQualifier("RC1").build();

        newTestDataPoint = repository.save(newTestDataPoint);
        testDataPoints.add(newTestDataPoint);

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, hasSize(1));
        assertThatDataPointsAreEqual(dataPoints.get(0), testDataPoint);
    }
}
