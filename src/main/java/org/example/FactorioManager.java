package org.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Formatter;

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

    public static FactorioManager getInstance(){
        return instance;
    }

    String ip = "";

    void socketStart(){
        try {
            WebSocketHandler client = new WebSocketHandler(new URI("wss://factorio.zone/ws"));
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    static void forceSocketAsociation(String socketID){
        System.out.println("Forcing socket asociation: " + socketID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://factorio.zone/api/user/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "userToken=" + Main.FactorioSecret +
                                "&visitSecret=" + socketID +
                                "&reconnected=false" +
                                "&script=https://factorio.zone/cache/main.3310dda146457be60a23.js"
                ))
                .build();
    }





    public boolean startServer() {
        if(launchId != -1){
            System.out.println("Server already started");
            return false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://factorio.zone/api/instance/start"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(new Formatter().format("visitSecret=%s&region=%s&version=%s&save=%s", token, region, version, save).toString()))
                .build();


        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            if(!response.body().contains("launchId")){
                System.out.println("Server not started");
                Main.bot.getTextChannelById(1181234882609942570L).sendMessage("Error al iniciar servidor, ya esta iniciado").queue();
                Main.bot.getTextChannelById(1181234882609942570L).sendMessage("Porfavor entrar a https://factorio.zone y apagar el server de forma manual").queue();
                return false;
            }
            launchId = Integer.parseInt(response.body().split(",\"launchId\":")[1].replace("}", ""));

            if(launchId == -1){
                System.out.println("Server not started");
                return false;
            }else{
                System.out.println("Server started");
                socketStart();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

            if(response.body().contains("instance stopped")){
                System.out.println("Server stopped");
                ip = "";
                return;
            }else{
                System.out.println("Server not stopped");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
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

    public String getStatus() {
        if (ip != "") {
            return "Online: " + ip;
        } else {
            return "Offline: puedes iniciar el servidor con /start";
        }
    }
}
