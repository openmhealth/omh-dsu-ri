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

import org.openmhealth.dsu.domain.User;
import org.openmhealth.dsu.domain.UserRegistrationData;
import org.openmhealth.dsu.domain.UserRegistrationException;
import org.openmhealth.dsu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.OffsetDateTime;


/**
 * @author Emerson Farrugia
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(readOnly = true)
    public boolean doesUserExist(String username) {

        return userRepository.findOne(username).isPresent();
    }

    @Override
    @Transactional
    public void registerUser(UserRegistrationData registrationData) {

        User user = new User();
        user.setUsername(registrationData.getUsername());

        try {
            user.setEmailAddress(new InternetAddress(registrationData.getEmailAddress()));
        }
        catch (AddressException e) {
            throw new UserRegistrationException(registrationData, e);
        }

        user.setPasswordHash(passwordEncoder.encode(registrationData.getPassword()));
        user.setRegistrationTimestamp(OffsetDateTime.now());

        userRepository.save(user);
    }
}