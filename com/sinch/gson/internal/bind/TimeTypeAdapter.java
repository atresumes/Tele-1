package com.sinch.gson.internal.bind;

import com.sinch.gson.Gson;
import com.sinch.gson.JsonSyntaxException;
import com.sinch.gson.TypeAdapter;
import com.sinch.gson.TypeAdapterFactory;
import com.sinch.gson.reflect.TypeToken;
import com.sinch.gson.stream.JsonReader;
import com.sinch.gson.stream.JsonToken;
import com.sinch.gson.stream.JsonWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class TimeTypeAdapter extends TypeAdapter<Time> {
    public static final TypeAdapterFactory FACTORY = new C09561();
    private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");

    final class C09561 implements TypeAdapterFactory {
        C09561() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Time.class ? new TimeTypeAdapter() : null;
        }
    }

    public synchronized Time read(JsonReader jsonReader) {
        Time time;
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            time = null;
        } else {
            try {
                time = new Time(this.format.parse(jsonReader.nextString()).getTime());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }
        return time;
    }

    public synchronized void write(JsonWriter jsonWriter, Time time) {
        jsonWriter.value(time == null ? null : this.format.format(time));
    }
}
