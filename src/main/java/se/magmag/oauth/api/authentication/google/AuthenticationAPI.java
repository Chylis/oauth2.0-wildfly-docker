package se.magmag.oauth.api.authentication.google;

import se.magmag.oauth.api.authentication.google.model.CallbackData;
import se.magmag.oauth.endpoint.google.model.AccessTokenResponse;
import se.magmag.oauth.persistence.entity.User;
import se.magmag.oauth.service.GoogleService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
    public Response login(@Context HttpServletRequest request) {
        final String consentScreenURL = googleService.requestAuthentication(request.getSession());
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
    public Response oauthCallback(@Context HttpServletRequest request, @BeanParam final CallbackData data) {
        LOG.info(String.format("Received callback data: %s", data.toString()));

        final String successfulRedirectURL = "http://magpie.ydns.eu:8002/oauth/api/test/greet";
        final String failedRedirectURL = "https://slashdot.org";
        String redirectURL = failedRedirectURL;

        try {
            googleService.handleAuthCodeReceived(request.getSession(), data);
            redirectURL = successfulRedirectURL;
        } catch (RuntimeException re) {
            LOG.warn("Google callback failed", re);
        }

        return Response.temporaryRedirect(URI.create(redirectURL)).build();
    }
}