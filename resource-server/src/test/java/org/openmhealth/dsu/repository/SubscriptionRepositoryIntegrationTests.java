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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.domain.SubscriptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Subscription repository integration tests.
 *
 *
 * Created by kkujovic on 4/13/15.
 */
public abstract class SubscriptionRepositoryIntegrationTests {

    public static final String UNRECOGNIZED_ID = "foo";
    public static final String TEST_USER_ID = "userid";
    public static final String TEST_URL = "http://localhost:9001/callback";

    @Autowired
    private SubscriptionRepository repository;

    private Subscription testSubscription;

    @Before
    public void initialiseFixture() {

        testSubscription = repository.save(
                new SubscriptionBuilder().
                        setCallbackUrl(TEST_URL).
                        setUserId(TEST_USER_ID).
                        build());
    }

    @After
    public void deleteFixture() {
        repository.delete(testSubscription.getId());
    }

    @Test
    public void existsShouldReturnFalseOnUnrecognizedId() {

        assertThat(repository.exists(UNRECOGNIZED_ID), equalTo(false));
    }

    @Test
    public void existsShouldReturnTrueOnMatchingId() {
        assertThat(repository.exists(testSubscription.getId()), equalTo(true));
    }

    @Test
    public void deleteByIdAndUserIdShouldReturn1OnMatchingIdAndUserId() {

        assertThat(repository.deleteByIdAndUserId(testSubscription.getId(), TEST_USER_ID), equalTo(1l));
        assertThat(repository.exists(testSubscription.getId()), equalTo(false));
    }
}
