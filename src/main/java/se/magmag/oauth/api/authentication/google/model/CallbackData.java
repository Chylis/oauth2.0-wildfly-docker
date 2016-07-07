package se.magmag.oauth.api.authentication.google.model;

import lombok.Data;
import javax.ws.rs.QueryParam;

/**
 * Created by magnus.eriksson on 07/07/16.
 */

@Data
public class CallbackData {

    @QueryParam("code")
    private String authorizationCode;

    @QueryParam("error")
    private String errorCode;

    @QueryParam("error_description")
    private String errorDescription;

    @QueryParam("state")
    private String state;

    public String toString() {
        return String.format("code: '%s', state: '%s', errorCode: '%s', errorDesc: '%s'",
                authorizationCode, state, errorCode, errorDescription);
    }

}
