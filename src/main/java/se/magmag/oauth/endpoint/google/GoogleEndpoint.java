package se.magmag.oauth.endpoint.google;

import se.magmag.oauth.endpoint.DefaultEndpoint;
import se.magmag.oauth.endpoint.EndpointParsedResponse;
import se.magmag.oauth.endpoint.EndpointResponse;

import se.magmag.oauth.endpoint.google.model.AccessTokenResponse;
import se.magmag.oauth.endpoint.parser.EndpointResponseParser;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import static se.magmag.oauth.endpoint.google.model.GrantType.AUTHORIZATION_CODE;
import static se.magmag.oauth.endpoint.google.model.Scope.PROFILE;

/**
 * Created by magnus.eriksson on 2016-06-22.
 */
@Slf4j
public class GoogleEndpoint extends DefaultEndpoint {

    @Inject
    private EndpointResponseParser responseParser;

    //TODO: Inject from credentials file
    //@Inject @ApplicationProperty("google.client.id")
    private String clientId = "1008490208113-916k79s8kr07tv7u7vea7vr37fcouona.apps.googleusercontent.com";

    //TODO: Inject from credentials file
    //@Inject @ApplicationProperty("google.client.secret")
    private String clientSecret = "F7ByWO2xCO6RzCSCsdah8423";

    //TODO: base url should be dynamic
    private final static String REDIRECT_URI = "http://magpie.ydns.eu:8080/oauth/api/google/oauthCallback";

    //private final static String DISCOVER_URI = "https://accounts.google.com/.well-known/openid-configuration";
    //private static final String DISCOVER_KEY_TOKEN_ENDPOINT = "token_endpoint";


    /**
     * Called when the user wants to authenticate.
     *
     * Will result in the user consent screen to be presented, which will in turn result in a callback
     * (containing a authorization code) to the specified redirectURL, once the user has approved/declined.
     *
     * @return The URL to the consent screen where the user may approve/decline
     */
    public String requestAuthentication(String state) {
        //TODO: Fetch URI via Discover service
        final Invocation invocation = getTarget("https://www.facebook.com/dialog/oauth?")
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("scope", PROFILE.toString())
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("state", state)
                .request()
                .buildGet();
        final EndpointResponse endpointResponse = invoke(invocation);
        LOG.info(String.format("%s %s", endpointResponse.getStatusCode(), endpointResponse.getLocation().toString()));
        return endpointResponse.getLocation().toString();
    }

    /**
     * Called in order to exchange the auth code for an access token, id_token GoogleJWTClaims (OpenId Connect) and optional refresh token

     * @param authCode The auth code to exchange
     * @return The AccessToken response
     */
    public EndpointParsedResponse<AccessTokenResponse> requestAuthCode(String authCode) {
        Form form = new Form()
                .param("code", authCode)
                .param("client_id", clientId)
                .param("client_secret", clientSecret)
                .param("redirect_uri", REDIRECT_URI)
                .param("grant_type", AUTHORIZATION_CODE.toString());

        //TODO: Fetch URI via Discover service
        final Invocation invocation = getTarget("https://www.googleapis.com/oauth2/v4/token")
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .buildPost(Entity.form(form));

        final EndpointResponse endpointResponse = invoke(invocation);
        return responseParser.parseResponse(endpointResponse, AccessTokenResponse.class);
    }
}
