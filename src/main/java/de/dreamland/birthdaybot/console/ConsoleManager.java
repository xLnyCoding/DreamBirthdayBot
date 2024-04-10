package de.dreamland.birthdaybot.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ConsoleManager {

    private final Thread thread;

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    private final HashMap<String, ConsoleCommand> commands = new HashMap<>();

    public ConsoleManager(InputStream is) {
        logger.info("Building ConsoleManager...");
        Scanner scanner = new Scanner(is);
        thread = new Thread(new ConsoleThread(scanner, commands), "ConsoleListenerThread");
        logger.info("ConsoleManager ready!");
    }

    public void start() {
        logger.info("Starting ConsoleManager...");
        thread.start();
        logger.info("ConsoleManager started!");
    }

    public void registerCommand(String commandName, ConsoleCommand command) {
        if(!thread.isAlive())
        commands.put(commandName, command);
    }

}
