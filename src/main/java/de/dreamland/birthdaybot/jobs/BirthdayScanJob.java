package de.dreamland.birthdaybot.jobs;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import de.dreamland.birthdaybot.util.RandomGifPicker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BirthdayScanJob {

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    public void start() {
        new Thread(() -> {
            int lastDay = 0;
            while (true) {

                Instant instant = Instant.now();
                ZonedDateTime date = instant.atZone(ZoneId.of("UTC"));

                if(DreamBirthdayBot.getInstance().birthdayScanOverride || date.getHour() == 10 && date.getMinute() == 0 && (lastDay < date.getDayOfMonth() || (date.getDayOfMonth() == 1 && lastDay >= 28))) {
                    lastDay = date.getDayOfMonth();
                    logger.info("Day: " + date.getDayOfMonth() + " Month: " + date.getMonthValue());
                    logger.info("Scanning birthdays...");
                    DreamBirthdayBot.getInstance().birthdayScanOverride = false;
                    ResultSet rs = DreamBirthdayBot.getInstance().databaseConnector.getResult("SELECT uid, year FROM birthdays WHERE day="+date.getDayOfMonth()+" AND month="+date.getMonthValue()+";");
                    try {
                        while(rs.next()) {
                            logger.info("Checking uid " + rs.getLong("uid"));
                            Member member = DreamBirthdayBot.getInstance().botApi.getGuildById(DreamBirthdayBot.getInstance().guildid).getMemberById(rs.getLong("uid"));
                            if(member == null) {
                                DreamBirthdayBot.getInstance().databaseConnector.update("DELETE FROM birthdays WHERE uid="+rs.getLong("uid")+";");
                            } else {
                                Guild guild = DreamBirthdayBot.getInstance().botApi.getGuildById(DreamBirthdayBot.getInstance().guildid);
                                TextChannel channel = guild.getTextChannelById(DreamBirthdayBot.getInstance().birthdayid);

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("It's " + member.getEffectiveName() + "'s Birthday");
                                if(rs.getInt("year") != 0) {
                                    embedBuilder.setDescription(member.getEffectiveName() + " is turning " + (date.getYear()-rs.getInt("year")) + " years old! \nHappy Birthday!");
                                } else {
                                    embedBuilder.setDescription("Happy Birthday!");
                                }

                                logger.info(member.getIdLong());

                                embedBuilder.setImage(RandomGifPicker.getRandomGif(member.getIdLong()));

                                channel.sendMessage(member.getAsMention()).addEmbeds(embedBuilder.build()).queue();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "BirthdayScanJob").start();
    }

}
