package com.starlingbank.roundup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

/**
 * This class is used to load json resource files
 */
@Slf4j
public class ResourceFileLoaderUtils {
    private ResourceFileLoaderUtils()
    {
        throw new IllegalStateException("Utility classes");
    }
    public static <T>Object loadResourceFile(String filePath, Class<T> aClazz) {
        String classfilePath  = filePath.contains("classpath")?filePath:"classpath:".concat(filePath);
        Object obj = null;
        try {
            File file =  ResourceUtils.getFile(classfilePath);
            String jsonString = new String(Files.readAllBytes(file.toPath()));
            ObjectMapper objectMapper = new ObjectMapper();
            obj = objectMapper.readValue(jsonString, aClazz);
        } catch (FileNotFoundException e) {
           log.error("Could not find the file :{}",e);
        } catch (IOException e) {
            log.error("Input/output exception :{}",e);
        }
        return obj;
    }
}
