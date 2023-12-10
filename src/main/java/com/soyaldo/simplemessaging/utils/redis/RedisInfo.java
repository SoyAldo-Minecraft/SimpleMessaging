package com.soyaldo.simplemessaging.utils.redis;

public class RedisInfo {

    private final String host;
    private final int port;
    private final String password;

    public RedisInfo(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public String getHost() {
        return host;
    }


    public int getPort() {
        return port;
    }


    public String getPassword() {
        return password;
    }

}