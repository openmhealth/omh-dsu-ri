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

package org.openmhealth.dsu.domain;

import org.springframework.data.annotation.Id;

import javax.mail.internet.InternetAddress;
import java.time.OffsetDateTime;


/**
 * A user account.
 *
 * @author Emerson Farrugia
 */
public class User {

    private String username;
    private String password;
    private InternetAddress emailAddress;
    private String registrationKey;
    private OffsetDateTime registrationTimestamp;
    private OffsetDateTime activationTimestamp;

    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InternetAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(InternetAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    public OffsetDateTime getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(OffsetDateTime registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    public OffsetDateTime getActivationTimestamp() {
        return activationTimestamp;
    }

    public void setActivationTimestamp(OffsetDateTime activationTimestamp) {
        this.activationTimestamp = activationTimestamp;
    }
}
