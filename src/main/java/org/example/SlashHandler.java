package org.example;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import okhttp3.internal.ws.RealWebSocket.Message;

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
                event.reply("Iniciando backup, nombre del archivo: " + FactorioManager.getInstance().backupServer()).queue();
                break;
        }
    }
}
