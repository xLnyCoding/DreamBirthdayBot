package de.dreamland.birthdaybot.commands;

import de.dreamland.birthdaybot.DreamBirthdayBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SetCustomGifCommand {

    public SlashCommandData data;

    public SetCustomGifCommand() {
        data = Commands.slash("set-custom-gif", "Sets a custom birthday gif for a specific user");
        data.setGuildOnly(true);
        data.addOptions(new OptionData(OptionType.USER, "user", "Select a user", true));
        data.addOptions(new OptionData(OptionType.STRING, "gif", "Set a gif for the specific user", true));
        data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));

        DreamBirthdayBot.getInstance().botApi.getGuildById(DreamBirthdayBot.getInstance().guildid).upsertCommand(data).queue();
    }

}
