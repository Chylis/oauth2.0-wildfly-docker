package se.magmag.oauth.endpoint.google.model;

/**
 * Created by magnus.eriksson on 07/07/16.
 */
public enum Scope {
    PROFILE ("profile"),
    EMAIL ("email");

    private String value;

    Scope(String value) {
        this.value = value;
    }

    public String toString() {
        return new StringBuilder("openid").append(" ").append(value).toString();
    }

}
