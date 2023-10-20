package rip.snake.simpleauth.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import lombok.AllArgsConstructor;
import rip.snake.simpleauth.SimpleAuth;
import rip.snake.simpleauth.managers.PlayerManager;
import rip.snake.simpleauth.player.TPlayer;

@AllArgsConstructor
public class ChatListener {
    private final SimpleAuth simpleAuth;

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        simpleAuth.getLogger().info(event.toString());
        TPlayer tPlayer = PlayerManager.GET_TMP_PLAYER(event.getPlayer().getUsername());

        if (tPlayer.isNeedAuth() && !tPlayer.isLoggedIn()) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
        }
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        simpleAuth.getLogger().info(event.toString());
    }

}