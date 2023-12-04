package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashHandler extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "start":
                event.reply("Iniciando servidor...").queue();
                FactorioManager.getInstance().startServer();
                break;

            case "stop":
                event.reply("Deteniendo servidor...").queue();
                FactorioManager.getInstance().stopServer();
                break;
            case "backup":
                event.reply("Iniciando backup...").queue();
                FactorioManager.getInstance().backupServer();
                break;
        }
    }
}
