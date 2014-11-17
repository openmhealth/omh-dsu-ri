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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;


/**
 * The modality of a data point. The modality represents whether the data point was sensed by a device or
 * application, or whether it was reported by the user.
 *
 * @author Emerson Farrugia
 */
public enum DataPointModality {

    SENSED,
    SELF_REPORTED,
    PROXY_REPORTED;

    private static Map<String, DataPointModality> constantsByJsonValue = new HashMap<>();

    static {
        for (DataPointModality constant : values()) {
            constantsByJsonValue.put(constant.getJsonValue(), constant);
        }
    }

    @JsonValue
    public String getJsonValue() {
        return name().toLowerCase().replaceAll("_", "-");
    }

    @JsonCreator
    @Nullable
    public static DataPointModality findByJsonValue(String jsonValue) {
        return constantsByJsonValue.get(jsonValue);
    }
}
