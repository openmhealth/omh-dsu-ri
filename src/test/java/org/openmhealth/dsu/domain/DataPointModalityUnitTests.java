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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.openmhealth.dsu.domain.DataPointModality.SELF_REPORTED;


/**
 * A suite of unit tests for the data point modality enumeration.
 *
 * @author Emerson Farrugia
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class DataPointModalityUnitTests {

    @Test
    public void getJsonValueShouldReturnCorrectValue() throws Exception {
        assertThat(SELF_REPORTED.getJsonValue(), equalTo("self reported"));
    }

    @Test
    public void findByJsonValueShouldReturnNullWithUndefinedValue() throws Exception {
        assertThat(DataPointModality.findByJsonValue(null), nullValue());
    }

    @Test
    public void findByJsonValueShouldReturnNullWithUnknownValue() throws Exception {
        assertThat(DataPointModality.findByJsonValue("foo"), nullValue());
    }

    @Test
    public void findByJsonValueShouldReturnMatchingValue() throws Exception {
        assertThat(DataPointModality.findByJsonValue("self reported"), equalTo(SELF_REPORTED));
    }
}