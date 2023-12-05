package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigManager {
    public static Properties properties;

    public ConfigManager() {
        properties = new Properties();
        loadConfig();
    }

    public void crearConfig() {
        try {
            OutputStream file = new FileOutputStream("config.properties");
            properties.setProperty("discordToken", " ");
            properties.setProperty("factorioToken", " ");
            properties.store(file, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            InputStream file = new FileInputStream("config.properties");
            properties.load(file);
        } catch (Exception e) {
            e.printStackTrace();
            crearConfig();
        }

    }
}
