package se.magmag.oauth.api.session.authentication;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;

import static se.magmag.oauth.api.session.Constants.SESSION_KEY_AUTHENTICATED;

/**
 * Created by magnus.eriksson on 12/07/16.
 */

@Slf4j
@Interceptor
@Authenticated
public class SessionAuthenticator {

    @Inject
    private HttpServletRequest request;

    @AroundInvoke
    public Object assertSessionIsAuthenticated(InvocationContext context) throws Exception {
        Boolean isAuthenticated = (Boolean) request.getSession().getAttribute(SESSION_KEY_AUTHENTICATED);
        if (isAuthenticated == null || !isAuthenticated) {
            LOG.info("Stopped attempt to access secured endpoint from an unauthenticated session");
            throw new NotAuthorizedException("Unauthenticated session");
        }

        return context.proceed();
    }
}
