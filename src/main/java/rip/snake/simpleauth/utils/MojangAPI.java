package rip.snake.simpleauth.utils;

import com.google.gson.JsonElement;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

public class MojangAPI {

    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";

    public static Optional<UUID> fetchUsername(String username) {
        try {
            URL url = new URL(MOJANG_API_URL + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                return Optional.empty();
            }

            JsonElement jsonResponse = GsonUtils.parseJson(new InputStreamReader(connection.getInputStream()), JsonElement.class);
            String idStr = jsonResponse.getAsJsonObject().get("id").getAsString();

            return Optional.of(formatIdToUUID(idStr));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private static UUID formatIdToUUID(String idStr) {
        // Insert hyphens into the id string to format it as a UUID
        StringBuilder uuidStr = new StringBuilder(idStr);
        uuidStr.insert(20, '-');
        uuidStr.insert(16, '-');
        uuidStr.insert(12, '-');
        uuidStr.insert(8, '-');

        return UUID.fromString(uuidStr.toString());
    }
}
