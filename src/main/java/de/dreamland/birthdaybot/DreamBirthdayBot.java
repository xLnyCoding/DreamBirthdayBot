package de.dreamland.birthdaybot;

import de.dreamland.birthdaybot.commands.AddBirthdayCommand;
import de.dreamland.birthdaybot.commands.CommandListener;
import de.dreamland.birthdaybot.commands.SetCustomGifCommand;
import de.dreamland.birthdaybot.commands.UnsetCustomGifCommand;
import de.dreamland.birthdaybot.console.ConsoleManager;
import de.dreamland.birthdaybot.console.commands.*;
import de.dreamland.birthdaybot.jobs.BirthdayScanJob;
import de.dreamland.birthdaybot.util.ConfigLoader;
import de.dreamland.birthdaybot.util.DatabaseConnector;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;

public class DreamBirthdayBot {

    /* SINGLETON DECLARATION START */
    private static DreamBirthdayBot dreamBirthdayBot;

    private DreamBirthdayBot() {

    }

    public static void main(String[] args) {
        dreamBirthdayBot = new DreamBirthdayBot();
        getInstance().start(args);
    }

    public static DreamBirthdayBot getInstance() {
        return dreamBirthdayBot;
    }
    /* SINGLETON DECLARATION END */

    /**
     * Utility method used to access resource stream
     *
     * @param location location of the file in the resources folder
     * @return the InputStream of the desired file
     */
    public InputStream getResource(String location) {
        return getClass().getClassLoader().getResourceAsStream(location);
    }

    private final Logger logger = LogManager.getLogger(getClass().getSimpleName()); //Logger for this class only

    public boolean debug = false; // Debug mode. Read documentation for further Information
    public JDA botApi; // Access to the Discord gateway and api
    public boolean reload = false; // Used to determine whether the system should shut down
    public String token = ""; // Load empty string for token var
    public long ownerid = 0; // ID of the bot owner for commands such as eval and shutdown / restart
    public long guildid = 0;
    public long birthdayid = 0;

    public volatile boolean birthdayScanOverride = false;

    public volatile List<String> gifs;

    public String mysqlip = "";
    public int mysqlport = 3306;
    public String mysqldatabase = "";
    public String mysqluser = "";
    public String mysqlpassword = "";

    public ConfigLoader configLoader;
    public volatile DatabaseConnector databaseConnector;
    public ConsoleManager consoleManager;
    public BirthdayScanJob birthdayScanJob;

    public AddBirthdayCommand addBirthdayCommand;
    public SetCustomGifCommand setCustomGifCommand;
    public UnsetCustomGifCommand unsetCustomGifCommand;

    /**
     * Method to boot the bot, private because the application can only be started once, use the reload method to reload
     * @param args command line arguments
     */
    private void start(String[] args) {
        logger.info("Attempting to start bot...");
        configLoader = new ConfigLoader();
        configLoader.loadConfig(); // Load the configuration file

        consoleManager = new ConsoleManager(System.in);
        consoleManager.registerCommand("ping", new PingCommand());
        consoleManager.registerCommand("reload", new ReloadCommand());
        consoleManager.registerCommand("stop", new ShutdownCommand());
        consoleManager.registerCommand("scanb", new ScanOverrideCommand());
        consoleManager.registerCommand("status", new StatusCommand());
        consoleManager.start();


        // Get the bot token from command line args if available
        if(args.length != 0) {
            logger.info("Loading command line token....");
            token = args[0];
        }

        databaseConnector = new DatabaseConnector(mysqlip, mysqlport, mysqldatabase, mysqluser, mysqlpassword, true, true, true);
        databaseConnector.connect();

        botLogin(); // log in the bot

        logger.info("Loading commands...");
        addBirthdayCommand = new AddBirthdayCommand();
        setCustomGifCommand = new SetCustomGifCommand();
        unsetCustomGifCommand = new UnsetCustomGifCommand();
        logger.info("Commands loaded!");

        logger.info("Loading listeners...");
        botApi.addEventListener(new CommandListener());
        logger.info("Listeners loaded!");

        logger.info("Loading jobs...");
        birthdayScanJob = new BirthdayScanJob();
        logger.info("Jobs loaded!");

        logger.info("Starting jobs...");
        birthdayScanJob.start();
        logger.info("Jobs started!");

        logger.info("Bot startup successful, have fun!");
    }

    /**
     * Stop method, accessible from anywhere in the application to stop the bot
     */
    public void stop() {
        // Bot reload and shutdown logic
        databaseConnector.disconnect();

        logger.info("Reload target reached!");
        if(reload) return; // Stop method execution if reload target reached
        // Bot shutdown logic

        logger.info("Unloading commands...");
        List<Command> commands = DreamBirthdayBot.getInstance().botApi.getGuildById(DreamBirthdayBot.getInstance().guildid).retrieveCommands().complete();
        for(Command command : commands) {
            switch (command.getName()) {
                case "set-birthday":
                    DreamBirthdayBot.getInstance().botApi.getGuildById(DreamBirthdayBot.getInstance().guildid).deleteCommandById(command.getId()).queue();
                    break;
                default:
                    break;
            }
        }
        logger.info("Commands unloaded!");

        logger.info("Shutdown target reached!");
        System.exit(0); // Exit the VM if shutdown target reached
    }

    /**
     * Method to initiate system reload
     */
    public void reload() {
        reload = true;
        stop();
        purgeVars();
        start(new String[5]);
        reload = false;
    }

    public void botLogin() {
        logger.info("Attempting bot login...");
        try {
            botApi = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES)
                    .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                    .disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING)
                    .disableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableCache(CacheFlag.VOICE_STATE)
                    .disableCache(CacheFlag.ONLINE_STATUS)
                    .setMemberCachePolicy(MemberCachePolicy.DEFAULT)
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        botApi.getPresence().setStatus(OnlineStatus.ONLINE);
        logger.info("Bot logged in!");
    }

    /**
     * Purge all the variables back to defaults for a system reload.
     * Only runtime need to be purged, the config vars will be overwritten on config read
     */
    public void purgeVars() {
        botApi = null;
        configLoader = null;
    }
}
