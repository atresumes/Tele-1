package com.sinch.gson.internal.bind;

import com.sinch.gson.Gson;
import com.sinch.gson.JsonSyntaxException;
import com.sinch.gson.TypeAdapter;
import com.sinch.gson.TypeAdapterFactory;
import com.sinch.gson.internal.bind.util.ISO8601Utils;
import com.sinch.gson.reflect.TypeToken;
import com.sinch.gson.stream.JsonReader;
import com.sinch.gson.stream.JsonToken;
import com.sinch.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

public final class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new C09521();
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);

    final class C09521 implements TypeAdapterFactory {
        C09521() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
        }
    }

    private synchronized Date deserializeToDate(String str) {
        Date parse;
        try {
            parse = this.localFormat.parse(str);
        } catch (ParseException e) {
            try {
                parse = this.enUsFormat.parse(str);
            } catch (ParseException e2) {
                try {
                    parse = ISO8601Utils.parse(str, new ParsePosition(0));
                } catch (Throwable e3) {
                    throw new JsonSyntaxException(str, e3);
                }
            }
        }
        return parse;
    }

    public Date read(JsonReader jsonReader) {
        if (jsonReader.peek() != JsonToken.NULL) {
            return deserializeToDate(jsonReader.nextString());
        }
        jsonReader.nextNull();
        return null;
    }

    public synchronized void write(JsonWriter jsonWriter, Date date) {
        if (date == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(this.enUsFormat.format(date));
        }
    }
}
