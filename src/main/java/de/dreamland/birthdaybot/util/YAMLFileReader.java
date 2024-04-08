package de.dreamland.birthdaybot.util;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

public class YAMLFileReader {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    private HashMap<String, Object> values = new HashMap<>();

    public YAMLFileReader(String path) {
        try {
            File file = new File(path);

            if(DreamBirthdayBot.getInstance().debug) return;

            if(!file.exists() && !DreamBirthdayBot.getInstance().debug) {
                logger.info("File not found! Creating new config file");
                try (InputStream in = DreamBirthdayBot.getInstance().getResource("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    logger.warn("Could not create file: " + e.getMessage());
                    e.printStackTrace();
                }
                logger.info("File created");
            }

            Yaml yaml = new Yaml();
            values = yaml.load(new FileInputStream(file));

            logger.info(values.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, Object> getValues() {
        return values;
    }
}
