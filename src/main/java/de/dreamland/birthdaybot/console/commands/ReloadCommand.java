package de.dreamland.birthdaybot.console.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import de.dreamland.birthdaybot.console.ConsoleCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReloadCommand implements ConsoleCommand {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    private long lastCall = 0;

    @Override
    public void run(String command, String[] args) {
        logger.info("Reloading...");
        DreamBirthdayBot.getInstance().reload();
    }

}
