package com.soyaldo.simplemessaging.utils.redis;


import redis.clients.jedis.Jedis;

public class Redis {

    private final RedisInfo redisInfo;
    private Jedis jedis;

    public Redis(RedisInfo redisInfo) {
        this.redisInfo = redisInfo;
    }

    public RedisInfo getRedisInfo() {
        return redisInfo;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void connect() {
        jedis = new Jedis(redisInfo.getHost(), redisInfo.getPort());
        jedis.auth(redisInfo.getPassword());
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