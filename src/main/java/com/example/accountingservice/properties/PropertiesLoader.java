package com.example.accountingservice.properties;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {

    private final Map<PropertiesType, Properties> propertiesHolder = new HashMap<>();

    public PropertiesLoader() {
        loadProperties();
    }

    public static Properties getProperties(PropertiesType propertiesType) {
        return (Properties) getInstance().propertiesHolder.get(propertiesType).clone();
    }

    public static final PropertiesLoader getInstance() {
        return SingletonPropertiesLoader.INSTANCE;
    }

    private void loadProperties() {
        for (PropertiesType propertiesType : PropertiesType.values()) {
            Properties properties = new Properties();
            try {
                InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(propertiesType.getFileName());
                properties.load(in);
                propertiesHolder.put(propertiesType, properties);
            } catch (Exception t) {
                throw new RuntimeException("Error in loading properties file : " + propertiesType.getFileName());
            }
        }
    }

    private static class SingletonPropertiesLoader {
        private static final PropertiesLoader INSTANCE;

        static {
            PropertiesLoader singletonInstance = null;
            try {
                singletonInstance = new PropertiesLoader();
            } catch (Exception t) {
                throw new ExceptionInInitializerError(t);
            }
            INSTANCE = singletonInstance;
        }
    }
}
