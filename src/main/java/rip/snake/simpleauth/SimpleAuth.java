package rip.snake.simpleauth;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "simple-auth",
        name = "SimpleAuth",
        version = "${VERSION}",
        authors = {"iSnakeBuzz_"},
        url = "https://github.com/iSnakeBuzz/SimpleAuth",
        description = "A simple authentication plugin for Velocity."
)
public class SimpleAuth {

    @Inject
    private Logger logger;

    @Inject
    public SimpleAuth(ProxyServer server, Logger logger, @DataDirectory Path pluginData) {
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
