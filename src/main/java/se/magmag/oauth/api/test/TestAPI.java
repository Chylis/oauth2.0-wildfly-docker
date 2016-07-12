package se.magmag.oauth.api.test;

import se.magmag.oauth.api.session.authentication.Authenticated;
import se.magmag.oauth.persistence.entity.User;
import se.magmag.oauth.service.UserService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Created by magnus.eriksson on 12/07/16.
 */
@Path("test")
public class TestAPI {

    @EJB
    private UserService userService;

    @GET
    @Path("greet")
    @Authenticated
    public String greet(@Context HttpServletRequest request) {
        User user = userService.getUserBySessionId(request.getSession().getId());
        return String.format("Hello there %s!", user.getName());
    }
}
