package se.magmag.oauth.service;

import lombok.extern.slf4j.Slf4j;
import se.magmag.oauth.api.authentication.google.model.CallbackData;
import se.magmag.oauth.endpoint.google.GoogleEndpoint;
import se.magmag.oauth.endpoint.google.model.AccessTokenResponse;
import se.magmag.oauth.endpoint.google.model.GoogleIdJWTClaims;
import se.magmag.oauth.persistence.entity.User;
import se.magmag.oauth.util.jwt.JWTDecoder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;

import static se.magmag.oauth.api.session.Constants.SESSION_KEY_GOOGLE_AUTH_STATE;

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

    @EJB
    private UserService userService;

    /**
     * Called when the user wants to authenticate.
     *
     * Populates the associated HTTPSession with an auth state token
     *
     * Will result in the user consent screen to be presented, which will in turn result in a callback
     * (containing a authorization code) to the specified redirectURL, once the user has approved/declined.
     *
     * @return The URL to the consent screen where the user may approve/decline
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String requestAuthentication(HttpSession httpSession) {
        final String googleAuthState = generateStateParam();
        httpSession.setAttribute(SESSION_KEY_GOOGLE_AUTH_STATE, googleAuthState);
        return googleEndpoint.requestAuthentication(googleAuthState);
    }

    /**
     * Prevents cross-site request forgery (CSRF) attacks by generating a unique session token that will later be matched
     * with the authentication response returned by the Google OAuth Login service, to verify that the user is making the
     * request and not a malicious attacker.
     */
    private String generateStateParam() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }


    /**
     * Exchanges the auth code for an access token and id token
     * Creates or updates a user
     */
    public void handleAuthCodeReceived(HttpSession session, CallbackData data) throws IllegalArgumentException, IllegalStateException {
        final AccessTokenResponse accessTokenResponse = exchangeAuthCodeForAccessToken(session, data);
        final GoogleIdJWTClaims idToken = parseIdToken(accessTokenResponse.getId_token());
        updateOrCreateUser(session.getId(), idToken);
    }


    /**
     * Validates the input and exchanges
     * @param session
     * @param data
     * @return
     */
    private AccessTokenResponse exchangeAuthCodeForAccessToken(HttpSession session, CallbackData data) throws IllegalArgumentException, IllegalStateException {
        if (data == null || data.getAuthorizationCode() == null || data.getAuthorizationCode().isEmpty()) {
            throw new IllegalArgumentException("Missing auth code");
        }

        final String sessionState = (String) session.getAttribute(SESSION_KEY_GOOGLE_AUTH_STATE);
        if (sessionState == null || !sessionState.equals(data.getState())) {
            throw new IllegalStateException("Invalid state param");
        }

        return googleEndpoint.requestAccessToken(data.getAuthorizationCode()).getData();
    }

    /**
     * Parses the id token JWT
     * @return the claims of the id token JWT
     */
    private GoogleIdJWTClaims parseIdToken(String idToken) throws IllegalArgumentException {
        if (idToken == null) {
            throw new IllegalArgumentException("Missing id token");
        }
        return jwtDecoder.decodeClaims(idToken, GoogleIdJWTClaims.class);
    }


    /**
     * Creates a new user or updates the user associated with the google id
     * Sets the following properties:
     *   - google id
     *   - http session id
     *   - name
     *   - email
     */
    private User updateOrCreateUser(String sessionId, GoogleIdJWTClaims id) {
        User user = userService.getUserByGoogleId(id.getSub());

        if (user == null) {
            user = userService.createUser();
            user.setGoogleId(id.getSub());
        }

        user.setSessionId(sessionId);
        user.setName(id.getName());
        user.setEmail(id.getEmail());

        return user;
    }
}
