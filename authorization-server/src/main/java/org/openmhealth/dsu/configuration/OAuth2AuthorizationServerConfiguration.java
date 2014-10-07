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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;


/**
 * A configuration that enables and configures the OAuth 2.0 authorization server that ship with Spring Security OAuth.
 *
 * @author Emerson Farrugia
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    public static final String CLIENT_ROLE = "ROLE_CLIENT";

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

        // FIXME figure out what this is for
        oauthServer.realm("dsu/client");
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // FIXME test and enable the different grant types
        // FIXME this is temporary, these ClientDetails should be persisted
        clients.inMemory().withClient("foo")
                .resourceIds("dataPoint")
                .authorizedGrantTypes("password")
                        // .authorizedGrantTypes("password", "authorization_code", "refresh_token",
                        // "implicit")
                .authorities(CLIENT_ROLE)
                .scopes("read", "write");
        //                .secret("secret")
        //                .redirectUris(tonrRedirectUri)
    }

    // FIXME replace the volatile store with a persistent store
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .userApprovalHandler(userApprovalHandler)
                .authenticationManager(authenticationManager);
    }
}
