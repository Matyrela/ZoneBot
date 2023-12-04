package org.example;

import net.dv8tion.jda.internal.requests.WebSocketClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
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

    // visitSecret=8f9tU1QQ5nWjS4qtDI5dnM&region=us-west-1&version=1.1.100&save=slot1

    public static FactorioManager getInstance() {
        return instance;

        // connect to websocket wss://factorio.zone/ws

    }

    public void startServer() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String requestBodyString = "visitSecret=" + token + "&region=" + region + "&version=" + version + "&save="
                + save;
        System.out.println(requestBodyString);
        RequestBody body = RequestBody.create(requestBodyString, mediaType);
        Request request = new Request.Builder()
                .url("https://factorio.zone/api/instance/start")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            System.out.println(response.body().string());
            String launchIdValue = response.body().toString().split("\"launchId\":")[1].split(",")[0];
            this.launchId = Integer.parseInt(launchIdValue);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void stopServer() {
        if (launchId == -1) {
            System.out.println("Server not started");
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://factorio.zone/api/instance/stop"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers
                        .ofString(new Formatter().format("visitSecret=%s&launchId=%d", token, launchId).toString()))
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

    public String backupServer() {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String requestBodyString = "save=" + save + "&visitSecret=" + token;
        RequestBody body = RequestBody.create(requestBodyString, mediaType);
        Request request = new Request.Builder()
                .url("https://factorio.zone/api/save/download")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String fileName = "backup/"
                    + new Timestamp(System.currentTimeMillis()).toString().replaceAll("-", "").replaceAll(":", "")
                    + ".zip";
            byte[] bytes = response.body().bytes();
            Path filePath = Path.of(fileName);
            Files.write(filePath, bytes);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
