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

package org.openmhealth.dsu.factory;

import com.mongodb.util.JSON;
import org.openmhealth.dsu.domain.DataPointBuilder;
import org.openmhealth.dsu.domain.DataPointSearchCriteriaBuilder;
import org.openmhealth.schema.domain.SchemaVersion;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * A factory that builds objects to support data point tests.
 *
 * @author Emerson Farrugia
 */
@Service
public class DataPointFactory {

    public static final String TEST_USERNAME = "test";
    public static final String TEST_SCHEMA_NAMESPACE = "test";
    public static final String TEST_SCHEMA_NAME = "test";
    public static final SchemaVersion TEST_SCHEMA_VERSION = new SchemaVersion(1, 1);

    public static DataPointBuilder newDataPointBuilder() {

        return new DataPointBuilder()
                .setUserId(TEST_USERNAME)
                .setSchemaNamespace(TEST_SCHEMA_NAMESPACE)
                .setSchemaName(TEST_SCHEMA_NAME)
                .setSchemaVersion(TEST_SCHEMA_VERSION)
                .setData(newKcalBurnedData());
    }

    public static DataPointSearchCriteriaBuilder newSearchCriteriaBuilder() {

        return new DataPointSearchCriteriaBuilder()
                .setUserId(TEST_USERNAME)
                .setSchemaNamespace(TEST_SCHEMA_NAMESPACE)
                .setSchemaName(TEST_SCHEMA_NAME)
                .setSchemaVersion(TEST_SCHEMA_VERSION);
    }

    public static Map<?, ?> newKcalBurnedData() {

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
}
