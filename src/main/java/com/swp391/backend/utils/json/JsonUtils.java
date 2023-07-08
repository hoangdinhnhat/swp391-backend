package com.swp391.backend.utils.json;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsonUtils<T> {
    private final Gson gsonUtils;

    public String stringify(Object object) {
        return gsonUtils.toJson(object);
    }

    public T parse(String json, Class<T> classOfT) {
        return gsonUtils.fromJson(json, classOfT);
    }
}
