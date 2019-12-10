package com.startsi.redis;
import com.startsi.util.HandringUtil;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
public class RedisMsgPubSubListener extends JedisPubSub{
	
	@Override
    public void unsubscribe() {
        super.unsubscribe();
    }
 
    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
    }
 
    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }
 
    @Override
    public void psubscribe(String... patterns) {
        super.psubscribe(patterns);
    }
 
    @Override
    public void punsubscribe() {
        super.punsubscribe();
    }
 
    @Override
    public void punsubscribe(String... patterns) {
        super.punsubscribe(patterns);
    }
 
//    监听到订阅模式接受到消息时的回调 (onPMessage)
//    监听到订阅频道接受到消息时的回调 (onMessage )
//    订阅频道时的回调( onSubscribe )
//    取消订阅频道时的回调( onUnsubscribe )
//    订阅频道模式时的回调 ( onPSubscribe )
//    取消订阅模式时的回调( onPUnsubscribe )
    @Override
    public void onMessage(String channel, String message) {
//    	System.out.println(channel);
//    	System.out.println(message);
//    	HandringMapService HM = new HandringMapServiceImpl();
    	HandringUtil handringUtil=new HandringUtil();
    	try {
//			HM.inserrpinfo(channel, message);
    		handringUtil.inserrinfo(channel, message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    @Override
    public void onPMessage(String pattern, String channel, String message) {
    }
 
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    }
 
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }
 
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
    }
 
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

}
