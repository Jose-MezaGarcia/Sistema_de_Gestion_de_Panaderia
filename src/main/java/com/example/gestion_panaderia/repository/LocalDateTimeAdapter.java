package com.example.gestion_panaderia.repository;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador para convertir LocalDateTime a JSON y viceversa
 * Se le dice a Gson cómo serializar y deserializar fechas
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    /** Se define el formato ISO para las fechas */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Se convierte un LocalDateTime a JsonElement
     * Básicamente se pasa la fecha a texto con el formato y se mete en JSON
     */
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }

    /**
     * Se convierte un JsonElement a LocalDateTime
     * Se agarra el texto del JSON y se transforma en fecha
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
