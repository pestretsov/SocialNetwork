package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by artemypestretsov on 8/8/16.
 */
public class RestUtils {
    public static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
