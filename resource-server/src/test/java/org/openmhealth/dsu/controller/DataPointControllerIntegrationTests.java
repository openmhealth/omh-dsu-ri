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
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmhealth.dsu.configuration.Application;
import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointHeader;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openmhealth.dsu.factory.DataPointFactory.newDataPointBuilder;
import static org.openmhealth.dsu.factory.DataPointFactory.newKcalBurnedBody;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    private static final String CONTROLLER_URI = "/v2/data";


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

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void initialiseClientMock() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    @Test
    public void readDataShouldReturn404OnMissingDataPoint() throws Exception {

        mockMvc.perform(
                get(CONTROLLER_URI + "/foo")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readDataShouldReturnDataPoint() throws Exception {

        DataPoint dataPoint = newDataPointBuilder().setBody(newKcalBurnedBody()).build();
        DataPointHeader header = dataPoint.getHeader();

        when(mockDataPointService.findOne(dataPoint.getId())).thenReturn(Optional.of(dataPoint));

        mockMvc.perform(
                get(CONTROLLER_URI + "/" + dataPoint.getId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.header.id").value(header.getId()))
                .andExpect(jsonPath("$.header.creation_timestamp").value(header.getCreationTimestamp().toString()))
                .andExpect(jsonPath("$.header.schema_id.namespace").value(header.getSchemaId().getNamespace()))
                .andExpect(jsonPath("$.header.schema_id.name").value(header.getSchemaId().getName()))
                .andExpect(jsonPath("$.header.schema_id.version.major")
                        .value(header.getSchemaId().getVersion().getMajor()))
                .andExpect(jsonPath("$.header.schema_id.version.minor")
                        .value(header.getSchemaId().getVersion().getMinor()));
        // TODO add data assertions
    }

    @SuppressWarnings("unchecked")
    @Test
    public void writeDataShouldWriteDataPoint() throws Exception {

        DataPoint dataPoint = newDataPointBuilder().setBody(newKcalBurnedBody()).build();
        List<DataPoint> dataPoints = Lists.newArrayList(dataPoint);

        mockMvc.perform(
                post(CONTROLLER_URI)
                        .content(objectMapper.writeValueAsString(dataPoints))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(mockDataPointService).save(anyCollection());
        // TODO compare internals
        // verify(mockDataPointService).save((Collection<DataPoint>) Matchers.argThat(contains(dataPoint)));
    }

    // TODO implement more tests
    // client name is incorrect
    // client password is incorrect
    // client has incorrect role
    // client has incorrect scope
    // token doesn't exist
    // data point belongs to a a different user
    // namespace?
    // username is incorrect
    // user password is incorrect
    // data point doesn't exist
}
