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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmhealth.dsu.configuration.Application;
import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.service.DataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.openmhealth.dsu.factory.DataPointFactory.newDataPointBuilder;
import static org.openmhealth.dsu.factory.DataPointFactory.newKcalBurnedData;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * A suite of integration tests for the data point controller.
 *
 * @author Emerson Farrugia
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {
        Application.class,
        DataPointControllerIntegrationTests.TestConfiguration.class}
)
public class DataPointControllerIntegrationTests {

    @Configuration
    static class TestConfiguration {

        @Bean
        @Primary
        public DataPointService dataPointService() {

            DataPointService mockService = Mockito.mock(DataPointService.class);

            when(mockService.findOne("foo")).thenReturn(Optional.empty());

            return mockService;
        }
    }


    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private DataPointService mockDataPointService;

    private MockMvc mockMvc;

    @Before
    public void initialiseClientMock() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    @Test
    public void readDataShouldReturn404OnMissingDataPoint() throws Exception {

        mockMvc.perform(get("/v2/data/foo")
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void readDataShouldReturnDataPoint() throws Exception {

        DataPoint dataPoint = newDataPointBuilder().setData(newKcalBurnedData()).build();

        when(mockDataPointService.findOne(dataPoint.getId())).thenReturn(Optional.of(dataPoint));

        mockMvc.perform(get("/v2/data/" + dataPoint.getId())
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        // TODO add assertions
    }

    // TODO implement more tests
}