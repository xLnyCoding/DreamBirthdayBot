package de.dreamland.birthdaybot.console.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import de.dreamland.birthdaybot.console.ConsoleCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShutdownCommand implements ConsoleCommand {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    private long lastCall = 0;

    @Override
    public void run(String command, String[] args) {
        if(lastCall == 0 || lastCall+5000<System.currentTimeMillis()) {
            logger.info("To shut down, type the command again within 5 seconds");
            lastCall = System.currentTimeMillis();
        } else {
            logger.info("Shutting down...");
            DreamBirthdayBot.getInstance().stop();
        }
    }

}
