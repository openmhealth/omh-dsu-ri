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

package org.openmhealth.dsu.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.openmhealth.dsu.converter.OffsetDateTimeToStringConverter;
import org.openmhealth.dsu.converter.StringToOffsetDateTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.JsonPropertyPreservingFieldNamingStrategy;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import javax.annotation.PreDestroy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * A configuration for Spring Data MongoDB. It sets up converters to support persistence of Java 8 {@link
 * java.time.OffsetDateTime}. The remaining boilerplate is mostly a copy of {@link MongoAutoConfiguration}.
 *
 * @author Emerson Farrugia
 */
@Configuration
@ConditionalOnClass(MongoClient.class)
@EnableConfigurationProperties(MongoProperties.class)
public class MongoPersistenceConfiguration extends AbstractMongoConfiguration {

    @Autowired
    private MongoProperties properties;

    @Autowired(required = false)
    private MongoClientOptions clientOptions;

    @Autowired
    private Environment environment;

    private MongoClient client;

    @PreDestroy
    public void close() {
        if (this.client != null) {
            this.client.close();
        }
    }

    @Bean
    public MongoClient mongo() throws UnknownHostException {
        this.client = this.properties.createMongoClient(this.clientOptions, this.environment);
        return this.client;
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }

    @Override
    public CustomConversions customConversions() {

        List<Converter<?, ?>> converters = new ArrayList<>();

        converters.add(new OffsetDateTimeToStringConverter());
        converters.add(new StringToOffsetDateTimeConverter());

        return new CustomConversions(converters);
    }

    /**
     * @return a naming strategy that makes persisted data match serialized data by converting camel case fields to
     * snake case and respecting {@link JsonProperty} overrides
     */
    protected FieldNamingStrategy fieldNamingStrategy() {

        return new JsonPropertyPreservingFieldNamingStrategy(new SnakeCaseFieldNamingStrategy());
    }
}
