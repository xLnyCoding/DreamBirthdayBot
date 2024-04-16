package de.dreamland.birthdaybot.console.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import de.dreamland.birthdaybot.console.ConsoleCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatusCommand implements ConsoleCommand {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only


    @Override
    public void run(String command, String[] args) {

        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long freeMemory = Runtime.getRuntime().maxMemory() - usedMemory;

        logger.info("Bot Status");
        logger.info("CPU Cores: " + Runtime.getRuntime().availableProcessors());
        logger.info("RAM: " + usedMemory/1000000 + "mb/" + Runtime.getRuntime().maxMemory()/1000000 + "mb Free: " + freeMemory/1000000 + "mb");
    }
}
