package rip.snake.simpleauth.player;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TPlayer {

    private final String username;

    private boolean needAuth;
    private boolean loggedIn;
    private boolean registered;

    private RegisteredServer server;

    public void needsAuth() {
        this.needAuth = true;
    }

    public void loggedIn() {
        this.loggedIn = true;
    }

    public void registered() {
        this.registered = true;
    }

    public void setServer(RegisteredServer server) {
        this.server = server;
    }

}
