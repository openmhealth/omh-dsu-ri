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

package org.openmhealth.dsu.controller;

import org.openmhealth.dsu.domain.EndUserUserDetails;
import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.openmhealth.dsu.configuration.OAuth2Properties.CLIENT_ROLE;
import static org.openmhealth.dsu.configuration.OAuth2Properties.SUBSCRIPTION_SCOPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by kkujovic on 4/12/15.
 */
@ApiController
public class SubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Writes a subscription.
     *
     * @param subscription the subscription to create
     */
    @PreAuthorize("#oauth2.clientHasRole('" + CLIENT_ROLE + "') and #oauth2.hasScope('" + SUBSCRIPTION_SCOPE + "')")
    @RequestMapping(value = "/subscriptions", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription, Authentication auth) {

        String endUserId = getEndUserId(auth);

        //does subscription already exists
        Iterable<Subscription> subscriptions = subscriptionService.findByUserIdAndCallbackUrl(endUserId, subscription.getCallbackUrl());
        if (subscriptions.iterator().hasNext()) {
            return new ResponseEntity<>(CONFLICT);
        }

        // set the owner of the subscription to be the user associated with the access token
        subscription.setUserId(endUserId);

        subscriptionService.save(subscription);

        return new ResponseEntity<>(subscription, CREATED);
    }

    /**
     * Returns list of subscriptions for user.
     *
     */
    @PreAuthorize("#oauth2.clientHasRole('" + CLIENT_ROLE + "') and #oauth2.hasScope('" + SUBSCRIPTION_SCOPE + "')")
    @RequestMapping(value = "/subscriptions", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Subscription>> getSubscriptions(Authentication auth) {
        String endUserId = getEndUserId(auth);

        Iterable<Subscription> subscriptions = subscriptionService.findByUserId(endUserId);
        return new ResponseEntity<>(subscriptions, OK);

    }

    /**
     * Deletes a subscription.
     *
     * @param id of the subscription to delete
     */
    @PreAuthorize("#oauth2.clientHasRole('" + CLIENT_ROLE + "') and #oauth2.hasScope('" + SUBSCRIPTION_SCOPE + "')")
    @RequestMapping(value = "/subscriptions/{id}", method = RequestMethod.DELETE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSubscription(@PathVariable String id, Authentication auth) {
        String endUserId = getEndUserId(auth);

        //delete by is AND user id (onle delete subscription for the associated user)
        Long deleteCount = subscriptionService.deleteByIdAndUserId(id, endUserId);
        return new ResponseEntity<>(deleteCount == 0 ? NOT_FOUND : OK);
    }

    private String getEndUserId(Authentication authentication) {

        return ((EndUserUserDetails) authentication.getPrincipal()).getUsername();
    }
}
