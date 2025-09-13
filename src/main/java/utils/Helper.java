package utils;



import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {


    private static final Logger LOGGER = LogManager.getLogger(Helper.class);

    private Helper() {
    }

    public static void sleepInSeconds(int sleepInSeconds) {
        try {
            LOGGER.debug("wait for seconds : " + sleepInSeconds);
            Thread.sleep(sleepInSeconds * 1000L);
        } catch (Exception e) {
            //
        }
    }

    public static void sleepMs(int sleepInMiliSeconds) {
        try {
            LOGGER.debug("wait for seconds : " + sleepInMiliSeconds);
            Thread.sleep(sleepInMiliSeconds);
        } catch (Exception e) {
        }
    }

}
