package org.example;

import net.dv8tion.jda.internal.requests.WebSocketClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.Formatter;
import java.util.concurrent.CompletionStage;

public class FactorioManager {
    int launchId = -1;
    String token;
    String region;
    String version;
    String save;

    static FactorioManager instance = null;

    public FactorioManager(String Token, String Region, String Version, String Save) {
        this.token = Token;
        this.region = Region;
        this.version = Version;
        this.save = Save;
        this.instance = this;
    }

    //visitSecret=8f9tU1QQ5nWjS4qtDI5dnM&region=us-west-1&version=1.1.100&save=slot1

    public static FactorioManager getInstance(){
        return instance;

        //connect to websocket wss://factorio.zone/ws








    }

    public void startServer() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://factorio.zone/api/instance/start"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(new Formatter().format("visitSecret=%s&region=%s&version=%s&save=%s", token, region, version, save).toString()))
                .build();


        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            launchId = Integer.parseInt(response.body().split(",\"launchId\":")[1].replace("}", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer(){
        if(launchId == -1){
            System.out.println("Server not started");
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://factorio.zone/api/instance/stop"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(new Formatter().format("visitSecret=%s&launchId=%d", token, launchId).toString()))
                .build();


        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backupServer() {
        if(launchId == -1){
            System.out.println("Server not started");
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://factorio.zone/api/save/download"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(new Formatter().format("visitSecret=%s&launchId=%d", token).toString()))
                .build();
    }
}
