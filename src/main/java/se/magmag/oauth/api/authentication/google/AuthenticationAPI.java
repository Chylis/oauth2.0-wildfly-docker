package se.magmag.oauth.api.authentication.google;

import se.magmag.oauth.api.authentication.google.model.CallbackData;
import se.magmag.oauth.service.GoogleService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by magnus.eriksson on 22/06/16.
 */

@Slf4j
@Path("google")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationAPI {

    @Inject
    private GoogleService googleService;

    /**
     * Called by the client to initiate a authentication flow.
     * Will send a authentication request to google and retrieve the consent screen URL
     * @return The URL to the consent screen
     */
    @GET
    @Path("login")
    public Response login() {
        final String consentScreenURL = googleService.requestAuthentication();
        LOG.info("Login called. Consent url: " + consentScreenURL);

        if (consentScreenURL != null) {
            return Response.temporaryRedirect(URI.create(consentScreenURL)).build();
        }

        return Response.serverError().build();
    }


    /**
     * Called by Google (i.e. the redirect URI) once the user accepts/declines the permissions requested in the user consent screen
     * Returns a HTTP redirect to the client's web client (which should intercept the URL and dismiss the webview)
     */
    @GET
    @Path("oauthCallback")
    public Response oauthCallback(@BeanParam final CallbackData data) {
        final String successfulRedirectURL = "https://slashdot.org";
        final String failedRedirectURL = "https://techcrunch.com/";
        String redirectURL;

        LOG.info(String.format("Received callback data: %s", data.toString()));

        if (data.getAuthorizationCode() != null && data.getAuthorizationCode().length() > 0) {
            googleService.handleAuthorizationCodeReceived(data.getAuthorizationCode(), data.getState());
            redirectURL = successfulRedirectURL;
        } else {
            redirectURL = failedRedirectURL;
        }

        return Response.temporaryRedirect(URI.create(redirectURL)).build();
    }
}