package com.soyaldo.simplemessaging.message.interfaces;

public interface Response {

    void onResponse(String serverFrom, byte[] bytes);

}