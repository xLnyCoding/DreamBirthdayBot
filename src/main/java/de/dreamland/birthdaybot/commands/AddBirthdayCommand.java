package de.dreamland.birthdaybot.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class AddBirthdayCommand {

    public SlashCommandData data;

    public AddBirthdayCommand() {
        data = Commands.slash("set-birthday", "Sets your birthday");
        data.setGuildOnly(true);
        data.addOptions(new OptionData(OptionType.INTEGER, "day", "The day of your birthday", true)
                .setRequiredRange(0, 31));
        data.addOptions(new OptionData(OptionType.INTEGER, "month", "The month of your birthday", true)
                .setRequiredRange(0, 12));
        data.addOptions(new OptionData(OptionType.INTEGER, "year", "The year of your birthday", false)
                .setRequiredRange(1900, 2023));
        data.setDefaultPermissions(DefaultMemberPermissions.ENABLED);

        DreamBirthdayBot.getInstance().botApi.getGuildById(DreamBirthdayBot.getInstance().guildid).upsertCommand(data).queue();
    }

}
