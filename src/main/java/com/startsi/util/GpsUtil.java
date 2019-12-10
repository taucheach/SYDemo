package com.startsi.util;

import net.sf.json.JSONObject;

public class GpsUtil {

    public static JSONObject analyze(String data){
        JSONObject result=new JSONObject();
        String address="";//地址
        String log=data.split(",")[0];//经度0
        String lat=data.split(",")[1];//纬度
        String gpstr = "https://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=gps&output=json&key=bcc621d97e04582acb485b64cd039452";
        gpstr = gpstr + "&locations=" + log + "," + lat;
        String newloc = "";
        try {
            newloc = RestUtil.sendGetData(gpstr, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject oo = JSONObject.fromObject(newloc);
        System.out.println(gpstr);
        System.out.println(oo);
        if (oo.getString("status").equals("1")) {
            System.out.println("转换成功");
            log = oo.getString("locations").split(",")[0];
            lat = oo.getString("locations").split(",")[1];
        }
        String url = "https://restapi.amap.com/v3/geocode/regeo?output=json&key=bcc621d97e04582acb485b64cd039452&radius=100&extensions=all&location=";
        url = url + log + "," + lat;
        String res = "";
        try {
            res = RestUtil.sendGetData(url, "");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        JSONObject json = JSONObject.fromObject(res);
        System.out.println("locations=" + log + "," + lat);
        if (json.getString("status").equals("1")) {
            address = json.getJSONObject("regeocode").getString("formatted_address");
            result.put("address",address);
            result.put("log",log);
            result.put("lat",lat);
        } else {
            System.out.println("未定位到信息");
            log = "";
            lat = "";
            address = "未定位到信息";
            result.put("address",address);
            result.put("log",log);
            result.put("lat",lat);
        }
        return result;
    }

    public static JSONObject wifiAnalyze(String data,String imei){
        JSONObject result=new JSONObject();
        String address="";//地址
        String log="";//经度
        String lat="";//纬度
        String url = "http://apilocate.amap.com/position?accesstype=1&output=json&key=4c86e7c8963e8d93d287a8acf0940f6c";
        url += "&imei=" + imei;
        String finalRes = "";
//						String url2 = url;
        boolean flag = data.contains("|");
        if (data != null && data.length() > 0) {
            if (flag) {
                String wifidata = data.split("\\|")[0];
                String wifi = wifidata.replace("_", "|");
                url += "&macs=" + wifi;
            } else {
                data = data.replaceAll("_", "|");
                url += "&macs=" + data;
            }
        }
        System.out.println(url);

        String res = "";
        try {
            res = RestUtil.load(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject object1 = JSONObject.fromObject(res);
        System.out.println(res);
        if (!object1.getJSONObject("result").getString("type").equals("0")) {
            finalRes = object1.toString();
            JSONObject obj = JSONObject.fromObject(finalRes).getJSONObject("result");
            String flag1="0";
            try {
                address = obj.getString("desc");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                flag1="1";
//				address="未定位到信息";
            }
            log = obj.getString("location").split(",")[0];// 获取经度
            lat = obj.getString("location").split(",")[1];// 获取纬度
            if("1".equals(flag1)) {
                url = "https://restapi.amap.com/v3/geocode/regeo?output=json&key=bcc621d97e04582acb485b64cd039452&radius=100&extensions=all&location=";
                // &location=119.246743706598,26.099906141494
                url = url + log + "," + lat;
                // wifi 03 06
                String res1 = "";
                try {
                    res1 = RestUtil.sendGetData(url, "");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JSONObject json = JSONObject.fromObject(res1);

                if (json.getString("status").equals("1")) {
                    address = json.getJSONObject("regeocode").getString("formatted_address");
                } else {
                    System.out.println("未定位到信息");
                    log = "";
                    lat = "";
                    address = "未定位到信息";
                }
            }
        } else {
            System.out.println("未定位到信息");
            address = "未定位到信息";
        }
        result.put("address",address);
        result.put("log",log);
        result.put("lat",lat);
        return result;
    }

}
