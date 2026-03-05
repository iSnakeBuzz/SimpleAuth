package rip.snake.simpleauth.commands;

import com.velocitypowered.api.command.SimpleCommand;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import rip.snake.simpleauth.SimpleAuth;
import rip.snake.simpleauth.managers.PlayerManager;
import rip.snake.simpleauth.utils.PasswordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class ResetPassCommand implements SimpleCommand {

    private final SimpleAuth simpleAuth;

    // Check if the player has permission to use the command
    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("simpleauth.command.resetpass");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            String currentArg = invocation.arguments().length > 0 ? invocation.arguments()[0].toLowerCase() : "";
            return new ArrayList<>(simpleAuth.getMongoManager().fetchAllUsernames(currentArg));
        });
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();

        if (args.length != 1) {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(
                    simpleAuth.getMessages().getString("messages.resetpass-usage", "<red>Usage: <yellow>/resetpass <username>")
            ));
            return;
        }

        String username = args[0].toLowerCase();

        CompletableFuture.supplyAsync(() -> simpleAuth.getMongoManager().fetchUsername(username))
                .thenAccept(authPlayerOpt -> {
                    authPlayerOpt.ifPresentOrElse(authPlayer -> {
                        try {
                            String hashedPassword = PasswordUtils.hashPassword("12345");
                            authPlayer.setHashedPassword(hashedPassword);
                            simpleAuth.getMongoManager().createPlayerOrUpdate(authPlayer);
                            PlayerManager.REMOVE_TMP_PLAYER(username);
                        } catch (Exception e) {
                            simpleAuth.getLogger().error("Failed to reset password for player: {}", username, e);
                            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(
                                    simpleAuth.getMessages().getString("messages.resetpass-error", "<red>An error occurred while resetting the password.")
                            ));
                            return;
                        }

                        String successMessage = simpleAuth.getMessages().getString("messages.resetpass-success", "<green>Successfully reset the password for <username>!");
                        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(
                                successMessage,
                                Placeholder.unparsed("username", username)
                        ));

                        simpleAuth.getProxyServer().getPlayer(authPlayer.getUniqueId()).ifPresent(targetPlayer -> {
                            targetPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                                    simpleAuth.getMessages().getString("messages.resetpass-notify", "<green>Your password has been reset by an administrator.")
                            ));
                        });

                    }, () -> {
                        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(
                                simpleAuth.getMessages().getString("messages.resetpass-not-registered", "<red>This user is not registered!")
                        ));
                    });
                })
                .exceptionally(ex -> {
                    simpleAuth.getLogger().error("Failed to process resetpass command for: {}", username, ex);
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(
                            simpleAuth.getMessages().getString("messages.resetpass-error", "<red>An error occurred while resetting the password.")
                    ));
                    return null;
                });
    }
}
