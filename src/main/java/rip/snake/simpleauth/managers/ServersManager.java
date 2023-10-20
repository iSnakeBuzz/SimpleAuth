package rip.snake.simpleauth.managers;

import com.google.common.collect.Sets;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.dejvokep.boostedyaml.route.Route;
import rip.snake.simpleauth.SimpleAuth;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ServersManager {

    private final SimpleAuth simpleAuth;

    private final Set<String> authServers;
    private final Set<String> lobbyServers;

    private final AtomicInteger authServerIndex = new AtomicInteger(0);
    private final AtomicInteger lobbyServerIndex = new AtomicInteger(0);

    public ServersManager(SimpleAuth simpleAuth) {
        this.simpleAuth = simpleAuth;

        this.authServers = Sets.newConcurrentHashSet();
        this.lobbyServers = Sets.newConcurrentHashSet();
    }

    public void loadServers() {
        this.simpleAuth.getLogger().info("Loading servers...");

        this.authServers.addAll(this.simpleAuth.getConfig().getStringList(Route.from("servers", "auth_servers")));
        this.lobbyServers.addAll(this.simpleAuth.getConfig().getStringList(Route.from("servers", "lobby_servers")));

        // Filtering the servers that don't exist
        this.authServers.removeIf(server -> this.simpleAuth.getProxyServer().getServer(server).isEmpty());
        this.lobbyServers.removeIf(server -> this.simpleAuth.getProxyServer().getServer(server).isEmpty());

        this.simpleAuth.getLogger().info("Loaded " + this.authServers.size() + " auth servers.");
        this.simpleAuth.getLogger().info("Loaded " + this.lobbyServers.size() + " lobby servers.");
    }

    public Optional<RegisteredServer> getAuthServer() {
        if (this.authServers.isEmpty()) return Optional.empty();
        if (this.authServerIndex.get() >= this.authServers.size()) this.authServerIndex.set(0);

        String server = (String) this.authServers.toArray()[this.authServerIndex.getAndIncrement()];
        return this.simpleAuth.getProxyServer().getServer(server);
    }

    public Optional<RegisteredServer> getLobbyServer() {
        if (this.lobbyServers.isEmpty()) return Optional.empty();
        if (this.lobbyServerIndex.get() >= this.lobbyServers.size()) this.lobbyServerIndex.set(0);

        String server = (String) this.lobbyServers.toArray()[this.lobbyServerIndex.getAndIncrement()];
        return this.simpleAuth.getProxyServer().getServer(server);
    }

    public void sendLobby(Player player) {
        this.getLobbyServer().ifPresent(server -> player.createConnectionRequest(server).fireAndForget());
    }
}
