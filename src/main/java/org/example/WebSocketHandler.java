package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketHandler extends WebSocketClient {

    public WebSocketHandler(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Conexión abierta");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
        if(message.contains("secret")){
            if(message.contains("\"type\":\"visit\"")){
                FactorioManager.forceSocketAsociation(message.split("\"secret\":\"")[1].split("\"")[0]);
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Conexión cerrada");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
