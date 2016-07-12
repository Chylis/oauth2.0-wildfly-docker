package se.magmag.oauth.persistence;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by magnus.eriksson on 12/07/16.
 */

public final class DbFactory {

    private static final String PERSISTENCE_UNIT = "db";

    private DbFactory() {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, FIELD, PARAMETER})
    public @interface Database {}

    @Produces
    @DbFactory.Database @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;
}
