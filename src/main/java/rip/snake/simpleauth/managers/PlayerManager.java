package rip.snake.simpleauth.managers;

import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.Nullable;
import rip.snake.simpleauth.player.AuthPlayer;
import rip.snake.simpleauth.player.TPlayer;

import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static final Map<UUID, AuthPlayer> AUTH_PLAYERS = Maps.newConcurrentMap();
    private static final Map<String, TPlayer> TMP_PLAYERS = Maps.newConcurrentMap();

    public static void PUT_PLAYER(UUID uniqueId, AuthPlayer authPlayer) {
        AUTH_PLAYERS.put(uniqueId, authPlayer);
    }

    public static void REMOVE_PLAYER(UUID uniqueId, String username) {
        AUTH_PLAYERS.remove(uniqueId);
        TMP_PLAYERS.remove(username);
    }

    @Nullable
    public static AuthPlayer GET_PLAYER(UUID uniqueId) {
        return AUTH_PLAYERS.getOrDefault(uniqueId, null);
    }

    public static TPlayer GET_TMP_PLAYER(String username) {
        return TMP_PLAYERS.computeIfAbsent(username.toLowerCase(), s -> new TPlayer(username, false, false, false, 0, null));
    }

}
