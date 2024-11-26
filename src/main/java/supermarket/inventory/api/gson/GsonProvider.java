package supermarket.inventory.api.gson;

import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    }
}

