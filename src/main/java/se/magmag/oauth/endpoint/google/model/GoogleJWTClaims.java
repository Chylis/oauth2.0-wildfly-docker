package se.magmag.oauth.endpoint.google.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by magnus.eriksson on 08/07/16.
 */
@Data
public class GoogleJWTClaims {

    /**
     * The Issuer Identifier for the Issuer of the response. Always https://accounts.google.com or accounts.google.com for Google ID tokens.
     */
    @NonNull private final String iss;

    /**
     * Access token hash. Provides validation that the access token is tied to the identity token.
     * If the ID token is issued with an access token in the server flow, this is always included.
     * This can be used as an alternate mechanism to protect against cross-site request forgery attacks.
     */
    private final String at_hash;

    /**
     * True if the user's e-mail address has been verified; otherwise false.
     */
    private final Boolean email_verified;

    /**
     * An identifier for the user, unique among all Google accounts and never reused.
     * A Google account can have multiple emails at different points in time, but the sub value is never changed.
     * Use sub within your application as the unique-identifier key for the user.
     */
    @NonNull private final String sub;

    /**
     * The client_id of the authorized presenter.
     * This claim is only needed when the party requesting the ID token is not the same as the audience of the ID token.
     * This may be the case at Google for hybrid apps where a web application and Android app have a different client_id but share the same project.
     */
    private final String azp;

    /**
     * The user's email address. This may not be unique and is not suitable for use as a primary key.
     * Provided only if your scope included the string "email".
     */
    private final String email;

    /**
     * The URL of the user's profile page. Might be provided when:
     * - The request scope included the string "profile",
     * - The ID token is returned from a token refresh,
     *
     * When profile claims are present, you can use them to update your app's user records.
     */
    private final String profile;

    /**
     * The URL of the user's profile picture. Might be provided when:
     * - The request scope included the string "profile",
     * - The ID token is returned from a token refresh
     *
     * When picture claims are present, you can use them to update your app's user records.
     */
    private final String picture;

    /**
     * The user's full name, in a displayable form. Might be provided when:
     * - The request scope included the string "profile",
     * - The ID token is returned from a token refresh,
     *
     * When name claims are present, you can use them to update your app's user records.
     */
    private final String name;

    /**
     * Identifies the audience that this ID token is intended for. It must be one of the OAuth 2.0 client IDs of your application.
     */
    @NonNull private final String aud;

    /**
     * The time the ID token was issued, represented in Unix time (integer seconds).
     */
    @NonNull private final String iat;

    /**
     * The time the ID token expires, represented in Unix time (integer seconds).
     */
    @NonNull private final String exp;

    /**
     * The hosted Google Apps domain of the user. Provided only if the user belongs to a hosted domain
     */
    private final String hd;
}
