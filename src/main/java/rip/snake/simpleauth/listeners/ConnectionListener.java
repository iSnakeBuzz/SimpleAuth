package rip.snake.simpleauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import lombok.AllArgsConstructor;
import rip.snake.simpleauth.SimpleAuth;
import rip.snake.simpleauth.managers.PlayerManager;
import rip.snake.simpleauth.player.AuthPlayer;
import rip.snake.simpleauth.player.TPlayer;
import rip.snake.simpleauth.utils.MojangAPI;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class ConnectionListener {
    private final SimpleAuth simpleAuth;

    @Subscribe(order = PostOrder.NORMAL)
    public void onPreLogin(PreLoginEvent event) {
        simpleAuth.getLogger().info(event.getResult().getReasonComponent().toString());

        String username = event.getUsername();
        Optional<AuthPlayer> authPlayer = simpleAuth.getMongoManager().fetchUsername(username);
        TPlayer tPlayer = PlayerManager.GET_TMP_PLAYER(username);

        // If the player is present in the database verify that the player is premium.
        if (authPlayer.isPresent()) {
            // Mark the player as registered.
            tPlayer.setRegistered(true);

            // If the player is premium, we can just return here.
            if (authPlayer.get().isPremium()) {
                tPlayer.setLoggedIn(true);
                return;
            }

            // If the player is not premium, we can set the player to need auth.
            tPlayer.setNeedAuth(true);
            event.setResult(PreLoginEvent.PreLoginComponentResult.forceOfflineMode());
            return;
        }

        // Fetch the UUID from Mojang. Rate limits apply.
        Optional<UUID> uuid = MojangAPI.fetchUsername(username);

        // Verify that the UUID is present.
        if (uuid.isPresent()) {
            // Get the AuthPlayer from the database in case the player has logged in before with a different username with the same UUID.
            simpleAuth.getMongoManager().fetchUniqueId(uuid.get()).ifPresent(aPlayer -> {
                // Mark the player as registered.
                tPlayer.setRegistered(true);

                // If the player is premium, we can just return here.
                if (aPlayer.isPremium()) {
                    tPlayer.setLoggedIn(true);
                    return;
                }

                // If the player is not premium, we can set the player to need auth and return.
                tPlayer.setNeedAuth(true);
                event.setResult(PreLoginEvent.PreLoginComponentResult.forceOfflineMode());
            });
            return;
        }

        // If the UUID is not present, the player is not premium, needs auth.
        tPlayer.setNeedAuth(true);
        event.setResult(PreLoginEvent.PreLoginComponentResult.forceOfflineMode());
    }

    @Subscribe
    public void onQuit(DisconnectEvent event) {
        PlayerManager.REMOVE_PLAYER(event.getPlayer().getUniqueId(), event.getPlayer().getUsername());
    }

}
