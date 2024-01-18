package rip.snake.simpleauth.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import rip.snake.simpleauth.SimpleAuth;

import java.util.Optional;

@AllArgsConstructor
public class LobbyCommand implements SimpleCommand {

    private final SimpleAuth simpleAuth;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            return;
        }

        Player player = (Player) invocation.source();
        Optional<RegisteredServer> lobbyServer = simpleAuth.getServersManager().getLobbyServer();
        if (lobbyServer.isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(simpleAuth.getMessages().getString("messages.no-lobby-server")));
            return;
        }

        player.createConnectionRequest(lobbyServer.get()).fireAndForget();
    }

}