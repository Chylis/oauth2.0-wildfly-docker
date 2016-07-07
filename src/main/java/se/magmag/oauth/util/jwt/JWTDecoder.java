package se.magmag.oauth.util.jwt;

import com.google.common.base.Charsets;
import se.magmag.oauth.endpoint.parser.gson.GsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;

/**
 * Created by magnus.eriksson on 08/07/16.
 */
@Slf4j
public class JWTDecoder {

    @Inject
    private GsonParser gsonParser;

    private static final int INDEX_HEADER = 0;
    private static final int INDEX_CLAIMS = 1;
    private static final int INDEX_SIGNATURE = 2;

    public <T> T decodeClaims(String encodedJWT, Class<T> clazz) {
        LOG.info("Attempting to decode jwt: " + encodedJWT);

        String[] jwtParts = splitJWT(encodedJWT);
        final String claimsString = new String(Base64.decodeBase64(jwtParts[INDEX_CLAIMS]), Charsets.UTF_8);
        LOG.info("Decoded jwt claims: " +  claimsString);
        return gsonParser.getGson().fromJson(claimsString, clazz);
    }

    private String[] splitJWT(String encodedJWT) {
        return encodedJWT.split("\\.");
    }
}
