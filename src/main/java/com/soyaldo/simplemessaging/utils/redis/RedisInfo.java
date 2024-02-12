package com.soyaldo.simplemessaging.utils.redis;

public class RedisInfo {

    private final String host;
    private final int port;
    private final String password;
    private final int maxConnections;
    private final int maxIdleConnections;

    public RedisInfo(String host, int port, String password, int maxConnections, int maxIdleConnections) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.maxConnections = maxConnections;
        this.maxIdleConnections = maxIdleConnections;
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

    public int getMaxConnections() {
        return maxConnections;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }
}