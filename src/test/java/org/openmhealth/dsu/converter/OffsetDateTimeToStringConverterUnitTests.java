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

package org.openmhealth.dsu.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


/**
 * @author Emerson Farrugia
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class OffsetDateTimeToStringConverterUnitTests {

    private OffsetDateTimeToStringConverter converter = new OffsetDateTimeToStringConverter();

    @Test
    public void convertWithNullTimestampShouldReturnNull() throws Exception {

        assertThat(converter.convert(null), nullValue());
    }

    @Test
    public void convertShouldReturnCorrectString() throws Exception {

        String result = converter.convert(OffsetDateTime.of(2010, 1, 2, 3, 4, 5, 6, ZoneOffset.ofHours(7)));

        assertThat(result, equalTo("2010-01-02T03:04:05.000000006+07:00"));
    }
}