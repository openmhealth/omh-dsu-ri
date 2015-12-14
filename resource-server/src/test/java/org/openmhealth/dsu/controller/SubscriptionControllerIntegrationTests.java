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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmhealth.dsu.configuration.Application;
import org.openmhealth.dsu.configuration.TestConfiguration;
import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.domain.SubscriptionBuilder;
import org.openmhealth.dsu.service.DataPointService;
import org.openmhealth.dsu.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for subscription controller.
 *
 * Created by kkujovic on 4/13/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {
        Application.class,
        SubscriptionControllerIntegrationTests.Configuration.class}
)
public class SubscriptionControllerIntegrationTests {

    private static final String CONTROLLER_URI = "/v1.0.M1/subscriptions";
    public static final String UNRECOGNIZED_DATA_POINT_ID = "foo";


    @TestConfiguration
    static class Configuration {

        @Bean
        @Primary
        public SubscriptionService dataPointService() {
            SubscriptionService mockService = Mockito.mock(SubscriptionService.class);
            when(mockService.findOne(UNRECOGNIZED_DATA_POINT_ID)).thenReturn(Optional.empty());
            return mockService;
        }
    }

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void initialiseClientMock() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    @Test
    public void postShouldReturnUnauthorizedWithoutAccessToken() throws Exception {

        SubscriptionBuilder builder = new SubscriptionBuilder();
        Subscription subscription = builder.setCallbackUrl("http://localhost:8080").build();
        mockMvc.perform(
                post(CONTROLLER_URI).
                        content(objectMapper.writeValueAsString(subscription))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
