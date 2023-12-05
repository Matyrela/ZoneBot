package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashHandler extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "start":
                if(FactorioManager.getInstance().startServer()){
                    event.reply("Iniciando servidor...").queue();
                }else{
                    event.reply("Error al iniciar servidor").queue();
                }
                break;

            case "stop":
                FactorioManager.getInstance().stopServer();
                event.reply("Solicitud enviada...").queue();
                break;

            case "status":
                event.reply("Obteniendo estado...").queue();
                event.reply(FactorioManager.getInstance().getStatus()).queue();
                break;

            case "backup":
                event.reply("Iniciando backup...").queue();
                FactorioManager.getInstance().backupServer();
                break;
        }
    }
}
