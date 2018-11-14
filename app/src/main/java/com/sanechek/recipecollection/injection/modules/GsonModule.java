package com.sanechek.recipecollection.injection.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/* provide gson */
@Module
public class GsonModule {

    @Provides
    @Singleton
    @Inject
    public Gson provideGson(SimpleDateFormat sdf) {

        JsonSerializer<Date> serializer = (date, type, jsonSerializationContext) -> {
            return new JsonPrimitive(sdf.format(date));
        };

        JsonDeserializer<Date> deserializer = (jsonElement, type, jsonDeserializationContext) -> {
            try {
                return sdf.parse(jsonElement.getAsJsonPrimitive().getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, serializer)
                .registerTypeAdapter(Date.class, deserializer)
                .disableHtmlEscaping()
                .create();
        return gson;
    }
}
