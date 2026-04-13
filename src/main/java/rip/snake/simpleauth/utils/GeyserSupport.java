package rip.snake.simpleauth.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.geysermc.floodgate.api.FloodgateApi;
import rip.snake.simpleauth.SimpleAuth;

/**
 * Optional wrapper for Geyser/Floodgate integration.
 *
 * <p>This class safely checks for the presence of the Floodgate plugin at runtime.
 * All Bedrock-related logic is gated behind {@link #isFloodgatePresent()}, so if
 * Floodgate is not installed on the Velocity proxy, nothing here is ever called
 * and the plugin behaves exactly as before.
 *
 * <p>Floodgate authenticates Bedrock players against Xbox Live before they even
 * reach Velocity. Therefore, forcing them through /login or /register is both
 * unnecessary and harmful to their experience.
 */
public class GeyserSupport {

    private final SimpleAuth plugin;
    private final boolean floodgatePresent;

    public GeyserSupport(SimpleAuth plugin) {
        this.plugin = plugin;
        this.floodgatePresent = detectFloodgate(plugin.getProxyServer());

        if (floodgatePresent) {
            plugin.getLogger().info("[GeyserSupport] Floodgate detected! Bedrock support is active.");
        } else {
            plugin.getLogger().info("[GeyserSupport] Floodgate not found. Bedrock support is disabled.");
        }
    }

    /**
     * Checks if the Floodgate plugin is loaded on this Velocity proxy.
     *
     * @return true if Floodgate is present and the geyser.enabled config option is true.
     */
    public boolean isFloodgatePresent() {
        return floodgatePresent
                && plugin.getConfig().getBoolean("geyser.enabled", true);
    }

    /**
     * Checks whether a connected player is a Bedrock (Geyser) player.
     *
     * <p>This must only be called after confirming {@link #isFloodgatePresent()} is true,
     * as it accesses the Floodgate API directly.
     *
     * @param player the Velocity Player instance
     * @return true if the player is connected via Geyser/Floodgate
     */
    public boolean isBedrockPlayer(Player player) {
        if (!floodgatePresent) return false;
        try {
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Exception e) {
            plugin.getLogger().warn("[GeyserSupport] Failed to query FloodgateApi for {}: {}",
                    player.getUsername(), e.getMessage());
            return false;
        }
    }

    /**
     * Returns whether Bedrock players should automatically bypass authentication.
     * Controlled by the config option geyser.auto-bypass-auth.
     *
     * @return true if Bedrock players should skip /login and /register
     */
    public boolean shouldBypassAuth() {
        return plugin.getConfig().getBoolean("geyser.auto-bypass-auth", true);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Safely detects Floodgate by checking if the plugin is registered in Velocity's
     * plugin manager AND if the Floodgate API class is available on the classpath.
     * Using both checks prevents false positives or NoClassDefFoundErrors.
     */
    private boolean detectFloodgate(ProxyServer server) {
        boolean pluginLoaded = server.getPluginManager()
                .getPlugin("floodgate")
                .isPresent();

        if (!pluginLoaded) return false;

        // Secondary check: ensure the API class itself is accessible at runtime
        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
