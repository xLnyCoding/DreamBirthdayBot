package de.dreamland.birthdaybot.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleThread implements Runnable {

    public static volatile boolean run;
    private Scanner scanner;

    private final HashMap<String, ConsoleCommand> commands;

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    public ConsoleThread(Scanner scanner, HashMap<String, ConsoleCommand> commands) {
        run = true;
        this.scanner = scanner;
        this.commands = commands;
    }

    @Override
    public void run() {
        while (run) {
            if(scanner.hasNext()) {
                String[] args = scanner.nextLine().split(" ");
                String command = args[0];
                if(commands.keySet().contains(command)) {
                    logger.info("Running command {}", command);
                    commands.get(command).run(command, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    logger.warn("Command {} not found", command);
                }
            }
        }
    }
}
