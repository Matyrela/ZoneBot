package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Main {
    static JDA bot;


    public static void main(String[] args) throws InterruptedException {
        JDABuilder builder = JDABuilder.createDefault("MTE4MTMxODQwNTI3NDQ3MjU1OA.GwGB1Y.31DN6n_5BrN5hh5BapZVMdy5hhsxrym09QIHgs");
        builder.setActivity(Activity.playing("Factorio"));
        bot = builder.build();
        bot.addEventListener(new SlashHandler());

        bot.awaitReady();

        bot.getGuildById(1181234881750106112L).updateCommands().addCommands(
                Commands.slash("start", "Starts the server"),
                Commands.slash("stop", "Stops the server"),
                Commands.slash("status", "Gets the status of the server")
        ).queue();

        FactorioManager fm = new FactorioManager("zY589s3DSQvFgWLIzVMD83", "sa-east-1", "1.1.100", "slot1");

        System.out.println("Bot is ready!");
    }

    public JDA getBot(){
        return bot;
    }
}