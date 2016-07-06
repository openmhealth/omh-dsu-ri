package org.openmhealth.dsu.common;

import org.openmhealth.dsu.domain.EndUserUserDetails;
import org.springframework.security.core.Authentication;

/**
 * Created by wwadge on 06/07/2016.
 */
public class Util {

    public static String getEndUserId(Authentication authentication) {
        // return based on whether we have a client_cred or pass grant
        return authentication.getPrincipal() instanceof String ? (String) authentication.getPrincipal() : ((EndUserUserDetails) authentication.getPrincipal()).getUsername();
    }

}
