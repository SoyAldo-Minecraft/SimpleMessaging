package com.soyaldo.simplemessaging.utils.redis;


import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;

@RequiredArgsConstructor
public class Redis {

    // Redis Settings
    private final String host;
    private final int port;
    private final String password;
    // Jedis
    private Jedis jedis;

    public void connect() {
        jedis = new Jedis(host, port);
        jedis.auth(password);
        jedis.connect();
    }

    public boolean isConnected() {
        if (jedis != null) return jedis.isConnected();
        return false;
    }

    public void disconnect() {
        jedis.disconnect();
        jedis.close();
    }

}