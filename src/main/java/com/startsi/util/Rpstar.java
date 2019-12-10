package com.startsi.util;

import com.startsi.thread.SubThread;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Rpstar {

    private static JedisPool jedisPool=ConnFactory.newInstance().getJedisPool();
    public static void init() {
        // TODO Auto-generated method stub
        System.out.println("进入main方法 ==> " );
        System.out.println("Netty4.0 ");
//        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "101.37.246.86", 6379,60000,"startsi");
//        JedisPool jedisPool=ConnFactory.newInstance().getJedisPool();
        System.out.println("Port:5555");
        SubThread subThread = new SubThread(jedisPool);  //订阅者
        System.out.println("Port:66666");
        subThread.start();
    }

}
