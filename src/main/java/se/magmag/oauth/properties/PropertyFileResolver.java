package se.magmag.oauth.properties;

/**
 * Created by magnus.eriksson on 04/07/16.
 */

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

@Singleton
@Slf4j
public class PropertyFileResolver {

    private Map<String, String> properties = new HashMap<String, String>();

    @PostConstruct
    private void init() {

        //"application.properties" matches the property name as defined in the system-properties element in WildFly
        String propertyFile = System.getProperty("application.properties");

        File file = new File(propertyFile);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            LOG.error("Unable to load properties file" + e);
        }

        HashMap <String, String> propertyMap = new HashMap<String, String>();
        for(Map.Entry<Object, Object> x : properties.entrySet()) {
            propertyMap.put((String)x.getKey(), (String)x.getValue());
        }

        this.properties.putAll(propertyMap);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}
