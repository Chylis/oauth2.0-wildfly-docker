package se.magmag.oauth.endpoint.google.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by magnus.eriksson on 07/07/16.
 */
@Data
public class AccessTokenResponse {

    @NonNull private final String access_token;
    @NonNull private final String id_token; //OpenId Connect GoogleIdJWTClaims that contains identity information about the user that is digitally signed by Google.
    @NonNull private final Long expires_in;
    @NonNull private final String token_type; //Identifies the type of token returned. At this time, this field always has the value Bearer.
    private final String refresh_token;
}
