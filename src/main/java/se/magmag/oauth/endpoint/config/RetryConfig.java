package se.magmag.oauth.endpoint.config;

import lombok.Data;

/**
 * RetryConfig contains the configuration of timeouts and retry behaviour towards sub systems.
 */
@Data
public class RetryConfig {

    private static final int DEFAULT_NO_OF_RETRIES = 5;
    private static final int DEFAULT_WAIT_UNTIL_RETRY_MS = 500;
    private static final int DEFAULT_WAIT_INCREMENT_MS = 500;

    private static final int DEFAULT_READ_TIMEOUT_MS = 2000;
    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 2000;

    private int readTimeout = DEFAULT_NO_OF_RETRIES;
    private int connectionTimeout = DEFAULT_CONNECT_TIMEOUT_MS;;
    private int retries = DEFAULT_NO_OF_RETRIES;
    private int waitUntilRetryMs = DEFAULT_WAIT_INCREMENT_MS;;
    private int waitIncrementMs = DEFAULT_WAIT_UNTIL_RETRY_MS;
}
