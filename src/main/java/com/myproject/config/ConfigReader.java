package com.myproject.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream(
                "src/test/resources/config.properties"
            );
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static String getBaseUrl()    { return properties.getProperty("base.url"); }
    public static String getBrowser()    { return properties.getProperty("browser", "chrome"); }
    public static boolean isHeadless()   { return Boolean.parseBoolean(properties.getProperty("headless", "false")); }
    public static int getExplicitWait()  { return Integer.parseInt(properties.getProperty("explicit.wait", "10")); }
    public static String getUsername()   { return properties.getProperty("app.username"); }
    public static String getPassword()   { return properties.getProperty("app.password"); }
}

