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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A subscription.
 *
 * @author Kenan Kujovic
 */
public class Subscription {

    @Id
    private String id;
    private String userId;
    private String callbackUrl;


    /**
     * @param callbackUrl the url where data changes should be posted
     */
    @JsonCreator
    public Subscription(@JsonProperty("callbackUrl") String callbackUrl) {

        checkNotNull(callbackUrl);

        this.id = UUID.randomUUID().toString();
        this.callbackUrl = callbackUrl;
    }

    /**
     * @deprecated should only be used by frameworks for persistence or serialisation
     */
    @Deprecated
    Subscription() {
    }

    /**
     * @return the identifier of the subscription
     */
    public String getId() {
        return id;
    }

    @JsonIgnore
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
