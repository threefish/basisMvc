package com.sgaop.basis.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampTypeAdapter implements JsonDeserializer<Timestamp>, JsonSerializer<Timestamp> {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public JsonElement serialize(Timestamp ts, Type t, JsonSerializationContext jsc) {
        String dfString = format.format(new Date(ts.getTime()));
        return new JsonPrimitive(dfString);
    }

    public Timestamp deserialize(JsonElement json, Type t, JsonDeserializationContext jsc) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }

        try {
            Date date = format.parse(json.getAsString());
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
