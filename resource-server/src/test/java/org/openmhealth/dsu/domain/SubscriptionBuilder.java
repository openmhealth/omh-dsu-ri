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

/**
 * Helper for creating subscription objects.
 * 
 * Created by kkujovic on 4/13/15.
 */
public class SubscriptionBuilder {

    private String userId;
    private String callbackUrl;

    public SubscriptionBuilder() {

    }

    public SubscriptionBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public SubscriptionBuilder setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    public Subscription build() {
        Subscription subscription = new Subscription(callbackUrl);
        subscription.setUserId(userId);
        return subscription;
    }

}
