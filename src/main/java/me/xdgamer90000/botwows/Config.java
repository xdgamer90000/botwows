package me.xdgamer90000.botwows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Properties configData = new Properties();

    public static void load() throws IOException {
        File config = new File("botwows.properties");
        configData.load(new FileInputStream(config));
    }

    public static String getToken(){
        return configData.get("token").toString();
    }

    public static String getAllowedRole(){
        return configData.get("allowed.role").toString();
    }
}
