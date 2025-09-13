package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DataManager {


    private static Object document;

    private DataManager() {
    }

    public static String getData(String key) {
        if (document == null) {
            String jsonString;
            try {
                jsonString = FileUtils.readFileToString(new File("src/test/resources/data.json"), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        }
        return JsonPath.read(document, key);
    }



}
