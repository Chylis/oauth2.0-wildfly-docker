package se.magmag.oauth.endpoint.google.model;

/**
 * Created by magnus.eriksson on 07/07/16.
 */

public enum GrantType {

    AUTHORIZATION_CODE ("authorization_code");



    private final String value;

    GrantType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
