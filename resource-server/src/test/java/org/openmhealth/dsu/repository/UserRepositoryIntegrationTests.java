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

package org.openmhealth.dsu.repository;

import org.junit.After;
import org.junit.Test;
import org.openmhealth.dsu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


/**
 * A suite of integration tests for a user repository.
 *
 * @author Emerson Farrugia
 */
public abstract class UserRepositoryIntegrationTests {

    protected static final String TEST_USERNAME = "test";
    protected static final String TEST_PASSWORD_HASH = "passwordHash";
    protected static final String TEST_EMAIL_ADDRESS_AS_STRING = "test@test.test";
    protected static final OffsetDateTime TEST_REGISTRATION_TIMESTAMP = OffsetDateTime.now().minusDays(1);

    @Autowired
    protected UserRepository userRepository;

    protected User newUser() {

        User user = new User();

        user.setUsername(TEST_USERNAME);
        user.setPasswordHash(TEST_PASSWORD_HASH);
        user.setRegistrationTimestamp(TEST_REGISTRATION_TIMESTAMP);
        try {
            user.setEmailAddress(new InternetAddress(TEST_EMAIL_ADDRESS_AS_STRING));
        }
        catch (AddressException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @After
    public void deleteFixture() {

        userRepository.delete(TEST_USERNAME);
    }

    @Test
    public void findOneShouldReturnSavedUser() {

        User expected = userRepository.save(newUser());

        Optional<User> actual = userRepository.findOne(TEST_USERNAME);

        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get(), equalTo(expected));
    }
}
