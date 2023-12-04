package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Main {
    static JDA bot;


    public static void main(String[] args) throws InterruptedException {
        new ConfigManager();
        String discordToken = ConfigManager.properties.getProperty("discordToken");
        System.out.println(discordToken);
        JDABuilder builder = JDABuilder.createDefault(discordToken);
        builder.setActivity(Activity.playing("Factorio"));
        bot = builder.build();
        bot.addEventListener(new SlashHandler());

        bot.awaitReady();

        bot.updateCommands().addCommands(
                Commands.slash("start", "Starts the server"),
                Commands.slash("stop", "Stops the server"),
                Commands.slash("status", "Gets the status of the server"),
                Commands.slash("backup", "Backs up the server")
        ).queue();
        FactorioManager fm = new FactorioManager(discordToken, "sa-east-1", "1.1.100", "slot1");
        System.out.println("Bot is ready!");
    }

    public JDA getBot(){
        return bot;
    }
}