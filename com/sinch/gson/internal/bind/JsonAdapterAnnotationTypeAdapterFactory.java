package com.sinch.gson.internal.bind;

import com.sinch.gson.Gson;
import com.sinch.gson.JsonDeserializer;
import com.sinch.gson.JsonSerializer;
import com.sinch.gson.TypeAdapter;
import com.sinch.gson.TypeAdapterFactory;
import com.sinch.gson.annotations.JsonAdapter;
import com.sinch.gson.internal.ConstructorConstructor;
import com.sinch.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        JsonAdapter jsonAdapter = (JsonAdapter) typeToken.getRawType().getAnnotation(JsonAdapter.class);
        return jsonAdapter == null ? null : getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }

    TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> typeToken, JsonAdapter jsonAdapter) {
        TypeAdapter<?> typeAdapter;
        Object construct = constructorConstructor.get(TypeToken.get(jsonAdapter.value())).construct();
        if (construct instanceof TypeAdapter) {
            typeAdapter = (TypeAdapter) construct;
        } else if (construct instanceof TypeAdapterFactory) {
            typeAdapter = ((TypeAdapterFactory) construct).create(gson, typeToken);
        } else if ((construct instanceof JsonSerializer) || (construct instanceof JsonDeserializer)) {
            typeAdapter = new TreeTypeAdapter(construct instanceof JsonSerializer ? (JsonSerializer) construct : null, construct instanceof JsonDeserializer ? (JsonDeserializer) construct : null, gson, typeToken, null);
        } else {
            throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer reference.");
        }
        return (typeAdapter == null || !jsonAdapter.nullSafe()) ? typeAdapter : typeAdapter.nullSafe();
    }
}
