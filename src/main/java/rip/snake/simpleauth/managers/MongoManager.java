package rip.snake.simpleauth.managers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import dev.dejvokep.boostedyaml.route.Route;
import org.checkerframework.checker.nullness.qual.Nullable;
import rip.snake.simpleauth.SimpleAuth;
import rip.snake.simpleauth.player.AuthPlayer;

import java.util.Optional;
import java.util.UUID;


public class MongoManager {

    private final SimpleAuth simpleAuth;

    private @Nullable MongoClient mongoClient;
    private @Nullable MongoDatabase simpleAuthDatabase;

    private @Nullable MongoCollection<AuthPlayer> authPlayerCollection;

    public MongoManager(SimpleAuth simpleAuth) {
        this.simpleAuth = simpleAuth;
    }

    public void connect() {
        String uri = simpleAuth.getConfig().getString(Route.from("mongo", "uri"), "mongodb://localhost:27017");
        String database = simpleAuth.getConfig().getString(Route.from("mongo", "database"), "simple-auth");

        try {
            this.mongoClient = MongoClients.create(uri);
            this.simpleAuthDatabase = mongoClient.getDatabase(database);
            this.authPlayerCollection = simpleAuthDatabase.getCollection("auth-players", AuthPlayer.class);
        } catch (Exception e) {
            simpleAuth.getLogger().error("Failed to connect to MongoDB!", e);
        }
    }

    public void createIndexes() {
        if (authPlayerCollection == null) return;
        IndexOptions unique = new IndexOptions().unique(true);

        authPlayerCollection.createIndex(Indexes.text("uniqueId"), unique);
        authPlayerCollection.createIndex(Indexes.text("username"));
    }

    public void disconnect() {
        if (mongoClient == null) return;
        mongoClient.close();
    }

    public void createPlayer(AuthPlayer authPlayer) {
        if (authPlayerCollection == null) return;
        PlayerManager.PUT_PLAYER(authPlayer.getUniqueId(), authPlayer);
        authPlayerCollection.insertOne(authPlayer);
    }

    public Optional<AuthPlayer> fetchUsername(String username) {
        if (authPlayerCollection == null) return Optional.empty();

        AuthPlayer authPlayer = authPlayerCollection.find(Filters.eq("username", username)).first();

        if (authPlayer != null) {
            PlayerManager.PUT_PLAYER(authPlayer.getUniqueId(), authPlayer);
        }

        return Optional.ofNullable(authPlayer);
    }

    public Optional<AuthPlayer> fetchUniqueId(UUID uniqueId) {
        if (authPlayerCollection == null) return Optional.empty();

        AuthPlayer authPlayer = authPlayerCollection.find(Filters.eq("uniqueId", uniqueId.toString())).first();

        if (authPlayer != null) {
            PlayerManager.PUT_PLAYER(authPlayer.getUniqueId(), authPlayer);
        }

        return Optional.ofNullable(authPlayer);
    }

}