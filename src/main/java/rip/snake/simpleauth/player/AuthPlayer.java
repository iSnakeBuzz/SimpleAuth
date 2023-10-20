package rip.snake.simpleauth.player;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class AuthPlayer {

    private final String uniqueId;
    private final String username;

    private final String last_server;
    private final String last_ip;
    private final long last_login;

    private String hashedPassword;
    private boolean isPremium;

    public UUID getUniqueId() {
        return UUID.fromString(uniqueId);
    }

    public String getRawUniqueId() {
        return uniqueId;
    }

}
