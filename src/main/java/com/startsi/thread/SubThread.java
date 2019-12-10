package com.startsi.thread;

import com.startsi.redis.RedisMsgPubSubListener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubThread  extends  Thread 
{

    private final RedisMsgPubSubListener subscriber = new RedisMsgPubSubListener();
    private final JedisPool jedisPool;
    private final String channel = "rpdata";

    public SubThread(JedisPool jedisPool) {
        super("SubThread");
        this.jedisPool = jedisPool;
    }

    @Override
    public void run()
    {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        Jedis jedis = null;
        try {
        	System.out.println("1");
            jedis = jedisPool.getResource();   //取出一个连接
        	System.out.println("2");
            jedis.subscribe(subscriber, channel);    //通过subscribe 的api去订阅，入参是订阅者和频道名
        	System.out.println("3");
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
        	  if (jedis != null) {
                  jedis.close();
        	  }
        }
    }
}
