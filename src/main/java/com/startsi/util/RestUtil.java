package com.startsi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestUtil {
    public static String sendGetData(String get_url, String content) throws Exception {
        String result = "";
        URL getUrl = null;
        BufferedReader reader = null;
        String lines = "";
        HttpURLConnection connection = null;
        try {
            if (content != null && !content.equals(""))
                get_url = get_url + "?" + content;
            // get_url = get_url + "?" + URLEncoder.encode(content, "utf-8");
            getUrl = new URL(get_url);
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();
            // 取得输入流，并使用Reader读取
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));// 设置编码
            while ((lines = reader.readLine()) != null) {
                result = result + lines;
            }
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
                reader = null;
            }
            connection.disconnect();
        }
    }

    public static String load(String url) throws Exception {

        URL rest =new URL(url);
        /**
         * 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection
         */
        HttpURLConnection conn=(HttpURLConnection)rest.openConnection();
        //设置请求方式
        conn.setRequestMethod("GET");
        //设置是否从httpUrlConnection读入，默认情况为true
        conn.setDoOutput(true);

        //allowUserInteraction 如果为true,则在允许用户交互的上下文中对URL进行检查
        conn.setAllowUserInteraction(false);
        PrintStream ps=new PrintStream(conn.getOutputStream());
//        ps.print(query);
        ps.close();

        BufferedReader bReader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

        String line,resultStr="";
        while (null!=(line=bReader.readLine())){
            resultStr+=line;
        }
        //System.out.println(resultStr);
        bReader.close();
        return resultStr;
    }
}
