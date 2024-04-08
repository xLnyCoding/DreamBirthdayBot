package de.dreamland.birthdaybot.util;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class ConfigLoader {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    YAMLFileReader fileReader = new YAMLFileReader("./config.yml");

    public ConfigLoader() {

    }

    public void loadConfig() {

        if(DreamBirthdayBot.getInstance().debug) return;

        DreamBirthdayBot.getInstance().token = ((HashMap<String, Object>) fileReader.getValues().get("bot")).get("token").toString();
        DreamBirthdayBot.getInstance().ownerid = Integer.toUnsignedLong((int) ((HashMap<String, Object>) fileReader.getValues().get("bot")).get("ownerid"));
    }

}