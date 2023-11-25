package rip.snake.simpleauth.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.AllArgsConstructor;
import rip.snake.simpleauth.SimpleAuth;
import rip.snake.simpleauth.managers.PlayerManager;

@AllArgsConstructor
public class MessagesListener {

    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("simple-auth:captcha");
    private final SimpleAuth plugin;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin.getProxyServer().getChannelRegistrar().register(IDENTIFIER);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        // Ensure the identifier is what you expect before trying to handle the data
        if (event.getIdentifier() != IDENTIFIER) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

        String username = in.readUTF().toLowerCase();
        String captcha = in.readUTF();

        // Set the captcha created by the server
        PlayerManager.GET_TMP_PLAYER(username).setServerCaptcha(captcha);
    }

}
