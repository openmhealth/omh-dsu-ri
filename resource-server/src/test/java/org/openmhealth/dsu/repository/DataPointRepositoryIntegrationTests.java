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

import com.google.common.collect.Lists;
import com.mongodb.util.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.openmhealth.dsu.factory.DataPointFactory.*;


/**
 * A suite of integration tests for a data point repository.
 *
 * @author Emerson Farrugia
 */
public abstract class DataPointRepositoryIntegrationTests {

    public static final String UNRECOGNIZED_ID = "foo";

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
    public void existsShouldReturnFalseOnUnrecognizedId() {

        assertThat(repository.exists(UNRECOGNIZED_ID), equalTo(false));
    }

    @Test
    public void existsShouldReturnTrueOnMatchingId() {

        assertThat(repository.exists(testDataPoint.getId()), equalTo(true));
    }

    @Test
    public void findOneShouldReturnNotPresentOnUnrecognizedId() {

        Optional<DataPoint> result = repository.findOne(UNRECOGNIZED_ID);

        assertThat(result.isPresent(), equalTo(false));
    }

    @Test
    public void findOneShouldReturnDataPointMatchingId() {

        Optional<DataPoint> result = repository.findOne(testDataPoint.getId());

        assertThat(result.isPresent(), equalTo(true));
        assertThatDataPointsAreEqual(result.get(), testDataPoint);
    }

    public void assertThatDataPointsAreEqual(DataPoint actual, DataPoint expected) {

        assertThat(actual.getUserId(), equalTo(expected.getUserId()));
        assertThat(actual.getHeader(), equalTo(expected.getHeader()));

        // although this uses Mongo libraries to check deep equality, it should work with other data stores
        assertThat(JSON.serialize(actual.getBody()), equalTo(JSON.serialize(expected.getBody())));
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

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setUserId(UNRECOGNIZED_ID).build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaNamespace() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaNamespace(UNRECOGNIZED_ID).build();

        List<DataPoint> dataPoints = newArrayList(repository.findBySearchCriteria(searchCriteria, null, null));

        assertThat(dataPoints, empty());
    }

    @Test
    public void findBySearchCriteriaShouldOnlyReturnDataPointsMatchingSchemaName() {

        DataPointSearchCriteria searchCriteria = newSearchCriteriaBuilder().setSchemaName(UNRECOGNIZED_ID).build();

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

    @Test
    public void deleteShouldNotThrowExceptionOnUnrecognizedId() {

        repository.delete(UNRECOGNIZED_ID);
    }

    @Test
    public void deleteShouldDeleteDataPointMatchingId() {

        repository.delete(testDataPoint.getId());
        assertThat(repository.exists(testDataPoint.getId()), equalTo(false));
    }

    @Test
    public void deleteByIdAndUserIdShouldReturn0OnUnrecognizedId() {

        assertThat(repository.deleteByIdAndUserId(UNRECOGNIZED_ID, TEST_USER_ID), equalTo(0l));
    }

    @Test
    public void deleteByIdAndUserIdShouldReturn0OnUnrecognizedUserId() {

        assertThat(repository.deleteByIdAndUserId(testDataPoint.getId(), UNRECOGNIZED_ID), equalTo(0l));
    }

    @Test
    public void deleteByIdAndUserIdShouldReturn1OnMatchingIdAndUserId() {

        assertThat(repository.deleteByIdAndUserId(testDataPoint.getId(), TEST_USER_ID), equalTo(1l));
        assertThat(repository.exists(testDataPoint.getId()), equalTo(false));
    }

    @Test(expected = DuplicateKeyException.class)
    public void insertShouldThrowExceptionOnDuplicateDataPoint() {

        DataPoint newTestDataPoint = newDataPointBuilder().setId(testDataPoint.getId()).build();
        testDataPoints.add(newTestDataPoint);

        repository.insert(Collections.singleton(newTestDataPoint));
    }

    @Test
    public void insertShouldNotCompensateAfterFailingToSaveDuplicateDataPoint() {

        DataPoint newTestDataPoint1 = newDataPointBuilder().build();
        DataPoint newTestDataPoint2 = newDataPointBuilder().setId(testDataPoint.getId()).build();

        List<DataPoint> newTestDataPoints = Lists.newArrayList(newTestDataPoint1, newTestDataPoint2);
        testDataPoints.addAll(newTestDataPoints);

        try {
            repository.insert(newTestDataPoints);
        }
        catch (DuplicateKeyException e) {
            assertThat(repository.exists(newTestDataPoint1.getId()), equalTo(true));
            return;
        }

        fail();
    }

    @Test
    public void insertShouldSaveUniqueDataPoint() {

        DataPoint newTestDataPoint = newDataPointBuilder().build();
        testDataPoints.add(newTestDataPoint);

        repository.insert(Collections.singleton(newTestDataPoint));

        assertThat(repository.exists(newTestDataPoint.getId()), equalTo(true));
    }
}
