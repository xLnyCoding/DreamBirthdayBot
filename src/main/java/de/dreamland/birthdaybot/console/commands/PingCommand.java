package de.dreamland.birthdaybot.console.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import de.dreamland.birthdaybot.console.ConsoleCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingCommand implements ConsoleCommand {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    @Override
    public void run(String command, String[] args) {
        long pingApi = DreamBirthdayBot.getInstance().botApi.getRestPing().complete();
        long pingGateway = DreamBirthdayBot.getInstance().botApi.getGatewayPing();
        logger.info("API-Ping: {} ms", pingApi);
        logger.info("Gateway-Ping: {} ms", pingGateway);
    }

}
