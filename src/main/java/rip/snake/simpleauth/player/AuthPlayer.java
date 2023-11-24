package rip.snake.simpleauth.player;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class AuthPlayer {

    private final String uniqueId;
    private final String username;

    private final String lastServer;
    private final String lastIP;
    private final long lastLogin;

    private String hashedPassword;
    private boolean isPremium;

    public UUID getUniqueId() {
        return UUID.fromString(uniqueId);
    }

    public String getRawUniqueId() {
        return uniqueId;
    }

}
