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

package org.openmhealth.schema.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * A suite of unit tests for schema version objects.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SchemaVersionUnitTests {

    @Test(expected = NullPointerException.class)
    public void stringConstructorShouldThrowExceptionOnUndefinedVersion() {

        new SchemaVersion(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringConstructorShouldThrowExceptionOnNegativeMajorVersion() {

        new SchemaVersion("-2.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringConstructorShouldThrowExceptionOnMalformedMajorVersion() {

        new SchemaVersion("a.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringConstructorShouldThrowExceptionOnNegativeMinorVersion() {

        new SchemaVersion("2.-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringConstructorShouldThrowExceptionOnMalformedMinorVersion() {

        new SchemaVersion("2.a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringConstructorShouldThrowExceptionOnMalformedQualifier() {

        new SchemaVersion("2.1.%");
    }

    @Test
    public void stringConstructorShouldSupportZeroMajorVersion() {

        SchemaVersion schemaVersion = new SchemaVersion("0.1");

        assertThat(schemaVersion, notNullValue());
        assertThat(schemaVersion.getMajor(), equalTo(0));
        assertThat(schemaVersion.getMinor(), equalTo(1));
        assertThat(schemaVersion.getQualifier().isPresent(), equalTo(false));
    }

    @Test
    public void stringConstructorShouldSupportZeroMinorVersion() {

        SchemaVersion schemaVersion = new SchemaVersion("3.0");

        assertThat(schemaVersion, notNullValue());
        assertThat(schemaVersion.getMajor(), equalTo(3));
        assertThat(schemaVersion.getMinor(), equalTo(0));
        assertThat(schemaVersion.getQualifier().isPresent(), equalTo(false));
    }

    @Test
    public void stringConstructorShouldSupportQualifier() {

        SchemaVersion schemaVersion = new SchemaVersion("2.1.RELEASE");

        assertThat(schemaVersion, notNullValue());
        assertThat(schemaVersion.getMajor(), equalTo(2));
        assertThat(schemaVersion.getMinor(), equalTo(1));
        assertThat(schemaVersion.getQualifier().isPresent(), equalTo(true));
        assertThat(schemaVersion.getQualifier().get(), equalTo("RELEASE"));
    }

    // TODO add more tests
}