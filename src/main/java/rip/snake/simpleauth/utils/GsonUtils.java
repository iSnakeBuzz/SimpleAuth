package rip.snake.simpleauth.utils;

import com.google.gson.Gson;

import java.io.InputStreamReader;

public class GsonUtils {

    private static final Gson GSON = new Gson();

    public static <T> T parseJson(InputStreamReader inputStreamReader, Class<T> clazz) {
        return GSON.fromJson(inputStreamReader, clazz);
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }


}
