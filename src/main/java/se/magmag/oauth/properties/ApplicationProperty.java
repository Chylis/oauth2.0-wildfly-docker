package se.magmag.oauth.properties;

/**
 * Created by magnus.eriksson on 04/07/16.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
public @interface ApplicationProperty {

    // no default meaning a value is mandatory
    @Nonbinding
    String name();
}

