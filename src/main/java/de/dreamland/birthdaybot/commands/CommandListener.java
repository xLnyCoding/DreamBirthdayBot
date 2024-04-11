package de.dreamland.birthdaybot.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandListener extends ListenerAdapter {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        event.deferReply(true).queue();
        logger.info("Running command: " + event.getName() + " executed by: " + event.getUser().getName());

        if(event.getName().equalsIgnoreCase("set-birthday")) {
            long uid = event.getUser().getIdLong();
            String name = event.getUser().getName();
            int day = event.getOption("day").getAsInt();
            int month = event.getOption("month").getAsInt();
            int year;
            if(event.getOption("year") != null) {
                year = event.getOption("year").getAsInt();
            } else {
                year = 0;
            }

            ResultSet rs = DreamBirthdayBot.getInstance().databaseConnector.getResult("SELECT uid FROM birthdays WHERE uid='"+uid+"';");
            try {
                if(rs.next()) {
                    DreamBirthdayBot.getInstance().databaseConnector.update("UPDATE birthdays SET name='"+name+"', day="+day+", month="+month+", year="+year+" WHERE uid='"+uid+"';");
                } else {
                    DreamBirthdayBot.getInstance().databaseConnector.update("INSERT INTO birthdays (uid, name, day, month, year) VALUES ("+uid+", '"+name+"', "+day+", "+month+", "+year+");");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            event.getHook().sendMessage("Your birthday was set successfully!").queue();
            return;
        }

        if(event.getName().equalsIgnoreCase("set-custom-gif")) {
            User user = event.getOption("user").getAsUser();
            String link = event.getOption("gif").getAsString();

            ResultSet rs = DreamBirthdayBot.getInstance().databaseConnector.getResult("SELECT uid FROM gifs WHERE uid='"+user.getIdLong()+"';");
            try {
                if (rs.next()) {
                    DreamBirthdayBot.getInstance().databaseConnector.update("UPDATE gifs SET name='" + user.getName() + "', link=" + link + " WHERE uid='" + user.getIdLong() + "';");
                } else {
                    DreamBirthdayBot.getInstance().databaseConnector.update("INSERT INTO gifs (uid, name, link) VALUES ("+ user.getIdLong() + ", '" + user.getName() + "', '" + link + "');");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            event.getHook().sendMessage("Gif set successfully!").queue();
            return;
        }

        if(event.getName().equalsIgnoreCase("unset-custom-gif")) {
            User user = event.getOption("user").getAsUser();

            DreamBirthdayBot.getInstance().databaseConnector.update("DELETE FROM gifs WHERE uid="+user.getIdLong()+";");

            event.getHook().sendMessage("Gif unset successfully!").queue();
            return;
        }

        event.getHook().sendMessage("Command not found!").queue();

    }

}
