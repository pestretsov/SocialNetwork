package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

/**
 * Created by artemypestretsov on 8/15/16.
 */
public class MongoUtils {
    public static Properties getPropertiesFromFile(String propertiesFile) {

        try (InputStream inputStream = new FileInputStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            return properties;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
