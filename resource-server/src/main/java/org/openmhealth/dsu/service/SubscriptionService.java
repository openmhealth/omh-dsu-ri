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

package org.openmhealth.dsu.service;

import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.openmhealth.dsu.domain.Subscription;

import javax.annotation.Nullable;
import java.util.Optional;


/**
 * A service that manages subscriptions.
 *
 * @author Kenan Kujovic
 */
public interface SubscriptionService {

    Optional<Subscription> findOne(String id);

    Iterable<Subscription> findByUserId(String userId);;

    Subscription save(Subscription subscription);

    Iterable<Subscription> findByUserIdAndCallbackUrl(String userId, String callbackUrl);

    void delete(String id);

    Long deleteByIdAndUserId(String id, String endUserId);
}
