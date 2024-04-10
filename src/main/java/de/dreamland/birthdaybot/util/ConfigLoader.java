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
        DreamBirthdayBot.getInstance().ownerid = (long) ((HashMap<String, Object>) fileReader.getValues().get("bot")).get("ownerid");
        DreamBirthdayBot.getInstance().guildid = (long) ((HashMap<String, Object>) fileReader.getValues().get("bot")).get("guildid");
        DreamBirthdayBot.getInstance().birthdayid = (long) ((HashMap<String, Object>) fileReader.getValues().get("bot")).get("birthdaychannel");

        DreamBirthdayBot.getInstance().mysqluser = ((HashMap<String, Object>) fileReader.getValues().get("database")).get("ip").toString();
        DreamBirthdayBot.getInstance().mysqlport = ((int) ((HashMap<String, Object>) fileReader.getValues().get("database")).get("port"));
        DreamBirthdayBot.getInstance().mysqldatabase = ((HashMap<String, Object>) fileReader.getValues().get("database")).get("database").toString();
        DreamBirthdayBot.getInstance().mysqluser = ((HashMap<String, Object>) fileReader.getValues().get("database")).get("user").toString();
        DreamBirthdayBot.getInstance().mysqlpassword = ((HashMap<String, Object>) fileReader.getValues().get("database")).get("password").toString();
    }

}