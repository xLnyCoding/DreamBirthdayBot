package de.dreamland.birthdaybot.console.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import de.dreamland.birthdaybot.console.ConsoleCommand;

public class ScanOverrideCommand implements ConsoleCommand {
    @Override
    public void run(String command, String[] args) {
        DreamBirthdayBot.getInstance().birthdayScanOverride = true;
    }
}
