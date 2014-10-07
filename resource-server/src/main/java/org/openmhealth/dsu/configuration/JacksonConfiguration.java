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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Optional;


/**
 * A configuration for Jackson.
 *
 * @author Emerson Farrugia
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());

        return objectMapper;
    }

    @Bean
    public SimpleModule jdk18Module() {

        SimpleModule module = new SimpleModule();

        module.addSerializer(new OptionalSerializer());
        module.addDeserializer(Optional.class, new OptionalDeserializer());

        return module;
    }

    /**
     * @author Gili Tzabari
     * @see <a href="https://github.com/FasterXML/jackson-databind/issues/494">related issue</a>
     */
    static class OptionalSerializer extends StdSerializer<Optional<?>> {

        OptionalSerializer() {
            super(Optional.class, true);
        }

        @Override
        public void serialize(Optional<?> value, JsonGenerator generator, SerializerProvider provider)
                throws IOException {

            if (value.isPresent()) {
                generator.writeObject(value.get());
            }
            else {
                generator.writeNull();
            }
        }
    }


    /**
     * @author Gili Tzabari
     * @see <a href="https://github.com/FasterXML/jackson-databind/issues/494">related issue</a>
     */
    static class OptionalDeserializer extends StdDeserializer<Optional<?>>
            implements ContextualDeserializer {

        private static final long serialVersionUID = 1L;
        private Class<?> targetClass;

        OptionalDeserializer() {
            super(Optional.class);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property)
                throws JsonMappingException {

            if (property != null) {
                // See http://jackson-users.ning.com/forum/topics/deserialize-with-generic-type
                JavaType type = property.getType();
                JavaType ofType = type.containedType(0);
                this.targetClass = ofType.getRawClass();
            }

            return this;
        }

        @Override
        public Optional<?> deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {

            return Optional.of(parser.readValueAs(targetClass));
        }

        @Override
        public Optional<?> getNullValue() {
            return Optional.empty();
        }
    }
}
