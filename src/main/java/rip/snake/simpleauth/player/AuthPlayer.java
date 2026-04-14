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

    /**
     * Whether this player joined via Geyser/Floodgate (Bedrock Edition).
     * Bedrock players are authenticated by Xbox Live and bypass SimpleAuth.
     * Defaults to false for all Java players and legacy database entries.
     */
    private boolean isBedrock;

    private long registeredAt;

    public UUID getUniqueId() {
        return UUID.fromString(uniqueId);
    }

    public String getRawUniqueId() {
        return uniqueId;
    }

}
