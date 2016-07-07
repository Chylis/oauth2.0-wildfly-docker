package se.magmag.oauth.service;

import lombok.extern.slf4j.Slf4j;
import se.magmag.oauth.endpoint.google.GoogleEndpoint;
import se.magmag.oauth.endpoint.google.model.AccessTokenResponse;
import se.magmag.oauth.endpoint.google.model.GoogleJWTClaims;
import se.magmag.oauth.util.jwt.JWTDecoder;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by magnus.eriksson on 2016-07-07.
 */
@Slf4j
@Stateless
public class GoogleService {

    @Inject
    private GoogleEndpoint googleEndpoint;

    @Inject
    private JWTDecoder jwtDecoder;

    /**
     * Called when the user wants to authenticate.
     *
     * Will result in the user consent screen to be presented, which will in turn result in a callback
     * (containing a authorization code) to the specified redirectURL, once the user has approved/declined.
     *
     * @return The URL to the consent screen where the user may approve/decline
     */
    public String requestAuthentication() {
        //TODO: Generate state param to prevent cross request site forgery
        return googleEndpoint.requestAuthentication("state mag");
    }

    public void handleAuthorizationCodeReceived(final String authorizationCode, final String state) {
        //TODO: Validate state
        final AccessTokenResponse accessTokenResponse = googleEndpoint.requestAuthCode(authorizationCode).getData();

        if (accessTokenResponse.getId_token() != null) {
            GoogleJWTClaims claims = jwtDecoder.decodeClaims(accessTokenResponse.getId_token(), GoogleJWTClaims.class);
            LOG.info("Received claims for: " + claims.getName());

            /*
            TODO: Query user database.

            - If the user already exists; start an application session for that user.

            - If the user does not exist; redirect the user to your new-user sign-up flow.
             You may be able to auto-register the user based on the information you receive from Google, or at the very least
             you may be able to pre-populate many of the fields that you require on your registration form.
             In addition to the information in the ID token, you can get additional user profile information at our user profile endpoints.
             */
        }

        LOG.info("handleAuthorizationCodeReceived complete");
    }
}
