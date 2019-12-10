package com.startsi.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
//import org.apache.commons.dbutils.QueryRunner;
//import org.apache.commons.dbutils.handlers.MapHandler;
//import org.apache.commons.dbutils.handlers.MapListHandler;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;

public class ConnFactory {
    private static ConnFactory factory=new ConnFactory();
    private ConnFactory(){}
    public static ConnFactory newInstance(){
        return factory;
    }
    //获取C3P0的池数据源 --- 初始化pooledDataSource对象，
    // 所有的Connection信息都包含在内部了，他会去读取我们配置好的xml文件
    private ComboPooledDataSource pooledDataSource=new ComboPooledDataSource();

    public ComboPooledDataSource getDataSources(){
        return pooledDataSource;
    }
    //初始化redis连接池
    public JedisPool getJedisPool(){
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "101.37.246.86", 6379,60000,"startsi");
        return jedisPool;
    }

//    public static void main(String[] args) {
//        long start=System.currentTimeMillis();
        /*ComboPooledDataSource dataSource=ConnFactory.newInstance().getDataSources();
        System.out.println("===========我来了=============");
        System.out.println(dataSource);
        System.out.println(dataSource.getInitialPoolSize());
        String sql="select * from uuserinfotbl where VALID='2'  ";
        long start=System.currentTimeMillis();
        QueryRunner runner=new QueryRunner(dataSource,true);*/
//        Map<String,Object> map=null;
        /*try {
            List<Map<String,Object>> list=runner.query(sql,new MapListHandler());
            System.out.println(list.size());

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("============有异常信息==========");
        }*/
       /* System.out.println();
        long end=System.currentTimeMillis();
        System.out.println("话费时间为："+(end-start)/1000);

        System.out.println("======end=======");*/

//    }


}
