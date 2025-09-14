package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {
    
    private static final Logger LOGGER = LogManager.getLogger(FileManager.class);
    private static final String DEFAULT_FILE_PATH = "src/test/resources/productInfo.txt";


    public static void writeToFile(String content ) {
        try {
            Path path = Paths.get("src/test/resources/productInfo.txt");

            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            
            Files.write(path, content.getBytes(), 
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND);
                
            LOGGER.info("Dosyaya yazıldı: " + path.toString());
        } catch (Exception e) {
            LOGGER.error("Dosya yazma hatası: " + e.getMessage());
            throw new RuntimeException("Dosyaya yazılamadı: " + e.getMessage());
        }
    }

    public static String readFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return "";
            }
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            LOGGER.error("Dosya okuma hatası: " + e.getMessage());
            throw new RuntimeException("Dosya okunamadı: " + e.getMessage());
        }
    }

    public static void clearFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                LOGGER.info("Dosya temizlendi: " + filePath);
            }
        } catch (Exception e) {
            LOGGER.error("Dosya temizleme hatası: " + e.getMessage());
            throw new RuntimeException("Dosya temizlenemedi: " + e.getMessage());
        }
    }
}
