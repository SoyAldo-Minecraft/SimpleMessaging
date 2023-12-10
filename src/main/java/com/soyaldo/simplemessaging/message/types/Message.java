package com.soyaldo.simplemessaging.message.types;

import com.soyaldo.simplemessaging.message.enums.MessageType;
import com.soyaldo.simplemessaging.message.interfaces.Fallback;
import com.soyaldo.simplemessaging.message.interfaces.Response;
import lombok.Data;

import java.util.UUID;

@Data
public class Message {

    private final String id = UUID.randomUUID().toString().split("-")[0];
    private final MessageType type = MessageType.MESSAGE;
    private final String serverTo;
    private final byte[] content;
    private Response response = null;
    private Fallback fallback = null;

    public void onResponse(Response response) {
        this.response = response;
    }

    public void onFallback(Fallback fallback) {
        this.fallback = fallback;
    }

    public void response(String serverFrom, byte[] bytes) {
        if (response != null) response.onResponse(serverFrom, bytes);
    }

    public void fallback() {
        if (fallback != null) fallback.onFallback();
    }

}