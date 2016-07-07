package se.magmag.oauth.endpoint.parser.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefaultGsonParser implements GsonParser {
    public Gson getGson() {
        return new GsonBuilder().create();
    }
}