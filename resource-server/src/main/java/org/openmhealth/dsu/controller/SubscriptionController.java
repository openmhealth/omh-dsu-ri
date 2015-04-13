package org.openmhealth.dsu.controller;

import org.openmhealth.dsu.configuration.OAuth2Properties;
import org.openmhealth.dsu.domain.EndUserUserDetails;
import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

import static org.openmhealth.dsu.configuration.OAuth2Properties.CLIENT_ROLE;
import static org.openmhealth.dsu.configuration.OAuth2Properties.SUBSCRIPTION_SCOPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
    // only allow clients with write scope to write data points
    @PreAuthorize("#oauth2.clientHasRole('" + CLIENT_ROLE + "') and #oauth2.hasScope('" + SUBSCRIPTION_SCOPE + "')")
    @RequestMapping(value = "/subscriptions", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription, Authentication auth) {

        String endUserId = getEndUserId(auth);

        //does subscription already exists
        Iterable<Subscription> subscriptions = subscriptionService.findByUserIdAndCallbackUrl(endUserId, subscription.getCallbackUrl());
        if (subscriptions.iterator().hasNext()) {
            return new ResponseEntity<>(subscriptions.iterator().next(), OK);
        }

        // set the owner of the subscription to be the user associated with the access token
        subscription.setUserId(endUserId);

        subscriptionService.save(subscription);

        return new ResponseEntity<Subscription>(subscription, CREATED);
    }

    /**
     * Deletes a subscription.
     *
     * @param id of the subscription to delete
     */
    // only allow clients with write scope to write data points
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
