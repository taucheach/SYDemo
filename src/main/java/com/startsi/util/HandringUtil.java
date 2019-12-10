package com.startsi.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.sf.json.JSONObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HandringUtil {

    private ComboPooledDataSource dataSource=ConnFactory.newInstance().getDataSources();
    private QueryRunner runner = new QueryRunner(dataSource, true);
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat logformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ");
    /*JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "101.37.246.86", 6379, 60000, "startsi");
    Jedis jedis = jedisPool.getResource();*/
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    // 生成文件名
    String filepath = "D:\\logfile\\" + format.format(new Date()) + ".txt";
    /**
     * @param channel   数据包时间
     * @param message   数据包
     * @return
     * @throws Exception
     */
    public String inserrinfo(String channel, String message) throws Exception {
        long start=System.currentTimeMillis();
        // 生成文件
        File dirfile = new File(filepath);
        if (!dirfile.exists()) {
            dirfile.getParentFile().mkdirs();
        }
        FileWriter writer = new FileWriter(dirfile, true);
        writer.write("[" + logformat.format(new Date()) + "]:" + message + System.getProperty("line.separator"));
        System.out.println("RP获取信息！");
        System.out.println(channel);
        System.out.println(message);
        //数据包进行处理
        String[] dres = message.split("[+]");
        if (dres.length == 4 && ("deviceDataChanged".equals(dres[1]) || "deviceDatasChanged".equals(dres[1]))) {
			/*System.out.println("0:" + dres[0]);
			System.out.println("1:" + dres[1]);
			System.out.println("2:" + dres[2]);
			System.out.println("3:" + dres[3]);
			String deviceid = dres[2];*/
            String data = dres[3];
            String[] datas = data.split("[,]");
            if ("03".equals(dres[0])) {
                String[] datas03 = datas[0].split(":");
                String datetime = dateFormat.format(new Date());
                if (datas[0].indexOf("T29") > 0) {
                    if ("T29".equals(datas03[5])) {
                        String imei03 = datas03[4];
                        String sql = "select * from braceletuserinfotbl where IMEI='" + imei03 + "'";
                        String letno = "";
                        /*try {
                            ps = conn.prepareStatement(fsql);
                            rs = ps.executeQuery();
                            writer.write("[" + logformat.format(new Date()) + "]:" + fsql
                                    + System.getProperty("line.separator"));
                            if (!rs.next()) {
                                return "";
                            } else {
                                letno = rs.getString("LETNO");
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }*/
                        Map<String,Object> bmap=runner.query(sql,new MapHandler());
                        if(bmap!=null){
                            letno=String.valueOf(bmap.get("LETNO"));
                        }
                        String type = datas03[6];// 3:wifi;4:gps
                        String date = datas[1];
                        String dwtype = "";
                        if ("3".equals(type)) {
                            date = date.replace("&", "");
                            date = date.replace("-", ",-");
                            date = date.replace("|", "_");
                            dwtype = "03";
                        } else if ("4".equals(type)) {
                            String[] jwd = date.split("|");
                            if (!"".equals(jwd[0]) && !"".equals(jwd[1])) {
                                date = jwd[1] + "," + jwd[0] + "," + datetime;
                            }
                            dwtype = "02";
                        }
                        //0_0
                        if (!"".equals(date) && !"".equals(dwtype)&&!"0_0".equals(date)) {

                            sql = "INSERT INTO `wifiinfotbl` (`IMEI`, `FUNC`, `DATA`, `CREATEDATE`,LETNO) VALUES ('"
                                    + imei03 + "', '" + dwtype + "', '" + date + "', '" + datetime + "','" + letno
                                    + "')";
//							System.out.println(sql);
                            /*try {
                                ps = conn.prepareStatement(sql);
//								int i =
                                ps.executeUpdate();
                                writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                        + System.getProperty("line.separator"));
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }*/
                            int i=runner.update(sql);
                            if(i>0){
                                System.out.println("执行成功！");
                            }
                        }
                    }
                }
            }
            if (datas.length > 10) {
//				String sjt = datas[0]; // <数据长度><数据头><协议版本>
//				String bsjxlh = datas[1]; // <本数据序列号>
                String imei = datas[2]; // <终端 IMEI>
//				String zdmc = datas[3]; // <终端名称>
//				String GPRSBS = datas[4]; // <GPRS 实时/历史数据标志>

                String date = datas[5]; // <日期>
                String time = datas[6]; // <时间>
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                Date dateyyy = new Date();
                String datetime = sdf.format(dateyyy) + date.substring(2, 4) + date.substring(0, 2) + time;
                System.out.println("dateyyy:" + datetime);
                String gpszt = datas[7]; // <GPS 定位状态>
                String jd = datas[8]; // <纬度值>
//				String jdfx = datas[9]; // <N/S>
                String wd = datas[10]; // <经度值>
//				String wdfx = datas[11]; // <W/E>

//				String bdwxgs = datas[12]; // <使用北斗卫星个数>
//				String gpsgs = datas[13]; // <使用 GPS 卫星个数>
//				String glonasswxgs = datas[14]; // <使用 GLONASS 卫星个数>
//				String spdwjd = datas[15]; // <水平定位精度>
                String sd = datas[16]; // <速度>
//				String hx = datas[17]; // <航向>
//				String hbgd = datas[18]; // <海拨高度>
//				String lc = datas[19]; // <里程>
//				String mcc = datas[20]; // <MCC>
//				String mnc = datas[21]; // <MNC>
//				String lac = datas[22]; // <LAC1|LAC2|….>
//				String cell = datas[23]; // <Cell ID1| Cell ID2|….>
//				String gms = datas[24]; // <GSM 信号强度 1|GSM 信号强度 2|….>

                String gdxl = datas[25]; // <光电心率>
                String jbs = datas[26]; // <计步数>
                gdxl = StringUtils.isNotBlank(gdxl) ? gdxl : "0";
                jbs = StringUtils.isNotBlank(jbs) ? jbs : "0";
//				String hdsj = datas[27]; // <活动时间>
//				String qsmsj = datas[28]; // <浅睡眠时间>
//				String ssmsj = datas[29]; // <深睡眠时间>
//				String wdcgqo = datas[30]; // <温度传感器 1>
//				String wdcgqt = datas[31]; // <温度传感器2>
//				String rfid = datas[32]; // <RFID>
//				String wjzdzt = datas[33]; // <外接终端状态>
                String dl = datas[34]; // <电量>

                String bjsjlx = datas[35]; // <报警事件类型>
                String sql = "select a.*,b.CODE from braceletuserinfotbl a inner join region b on a.LETNO=b.LETNO and b.FLAG='0' where IMEI='" + imei + "' LIMIT 1 ";
                String letno = "";
                String code = "";
                String province = "";
                String city = "";
                String name = "";
                List<Map<String, String>> mlist = new ArrayList<Map<String, String>>();
               /* ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();*/
                Map<String,Object> bmap=runner.query(sql,new MapHandler());
                writer.write("[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                String upsjlx="";
                if(bmap!=null){
                    System.out.println("使用者信息："+bmap.get("USERNAME"));
                    letno=String.valueOf(bmap.get("LETNO"));
                    code=String.valueOf(bmap.get("CODE"));
                    name=String.valueOf(bmap.get("USERNAME"));
                    //================自动解除超时报警=================
                    long castart=System.currentTimeMillis();
                    System.out.println("===========自动解除超时报警===========");
                    sql="select * from alarminfotbl where LETNO='"+letno+"' and FUNC='66'";
                    List<Map<String,Object>> csList=runner.query(sql,new MapListHandler());
                    writer.write("[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                    System.out.println(csList.size()+"==================");
                    if(csList.size()>0){
                        String kaishi =String.valueOf(csList.get(0).get("CREATEDATE"));

                        String jieshu= String.valueOf(csList.get(csList.size()-1).get("CREATEDATE"));

                        String shul=String.valueOf(csList.size());
                        //构造删除超时sql
                        sql="delete from alarminfotbl where LETNO='"+letno+"' and FUNC='66'";
                        writer.write("[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                        runner.update(sql);
                        //删除超时最新表信息
                        sql="delete from newalarm where LETNO='"+letno+"' and FUNC='66'";
                        runner.update(sql);
                        //构造删除日志
                        sql="INSERT INTO alarmloginfotbl (`IMEI`, `FUNC`, `STARTDATE`, `ENDDATE`, `ALARMCOUNT`, `REASON`, `LETNO`) VALUES ('"+imei+"', '66', '"+kaishi+"', '"+jieshu+"', '"+shul+"', '超时量自动解除', '"+letno+"')";
                        int i=runner.update(sql);
                        if(i>0){
                            System.out.println("解除超时报警成功！");
                        }
                    }
                    long csend=System.currentTimeMillis();
                    System.out.println("超时解除花费时间："+(csend-castart));
                    //============================
                    //    查询上次数据包
                    sql="select * from heart where LETNO='"+letno+"'";
                    //最新数据包时间，数据库中的
                    Map<String,Object> hmap=runner.query(sql,new MapHandler());
                    if(hmap!=null){
                        upsjlx=String.valueOf(hmap.get("SJLX"));
                        sql="update heart set CREATEDATE='"+datetime+"',SJLX='"+bjsjlx+"' where LETNO='"+letno+"'";
                        int i=runner.update(sql);
                        if(i>0)
                            System.out.println("数据包时间更新");
                    }else{
                        upsjlx="";
                        sql="insert into heart values('"+letno+"','"+datetime+"','"+bjsjlx+"')";
                        int i=runner.update(sql);
                        if(i>0)
                            System.out.println("有新手环进来");
                    }
                    // 查询市
                    sql = "select CODE,NOTE FROM pcsareacodetbl where CODE='" + code.substring(0, 1) + "0000'";
                    //使用者所在时区
                    Map<String,Object> cmap=runner.query(sql,new MapHandler());
                    writer.write("[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                    if(cmap!=null){
                        city = String.valueOf(cmap.get("NOTE"));
                    }

                    // 查询省
                    sql = "select CODE,NOTE FROM pcsareacodetbl where CODE='" + code.substring(0, 1) + "0000'";
                    writer.write("[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                    Map<String,Object> pmap=runner.query(sql,new MapHandler());
                    if(pmap!=null){
                        province = String.valueOf(pmap.get("NOTE"));
                    }

                    //查询电话 使用者对应的监护人
//                    sql = "select a.TBLNO,b.CHNNAME,b.USERTELEPHONE from guardian_imei  a left join uuserinfotbl b on a.TBLNO=b.TBLNO where a.LETNO='"
//                            + letno + "' and b.VALID='1'";
//                    runner.query()

                }else{
                    return "";
                }
                System.out.println("=====电量==="+dl);

                if (Integer.parseInt(dl) <= 20) {
                    sql = "select * from alarminfotbl where LETNO='" + letno
                            + "' and FUNC='61' and CREATEDATE='" + datetime + "'";
                    //报警信息查询
                    Map amap=runner.query(sql,new MapHandler());
                    writer.write(
                            "[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                    if(amap==null){
                        sql = "INSERT INTO `alarminfotbl` (`IMEI`, `FUNC`, `MODE`, `CREATEDATE`, `FLAG`,LETNO) VALUES ('"
                                + imei + "', '61', '1', '" + datetime + "', '1','" + letno + "')";
                        int i=runner.update(sql);
                        writer.write(
                                "[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                        //低电量查询
                        String lowersql = "select * from newalarm where LETNO='" + letno + "' and FUNC='61' ";
//                        for (int j = 0; j < mlist.size(); j++) {
//									Map<String, String> dmap = mlist.get(j);
//									String infoa = city + "禁毒办告知" + name;
//									String infob = "电量过低，请及时充电";
//									String infoc = "完全断电";
//									String infod = province + "社区戒毒/社区康复协议";
////									SmsUtil.sendSms(dmap.get("phone"), infoa,infob,infoc,infod);
//								}
////								String content=city+"禁毒办"+name+" 设备电量过低 请及时充电 完全断电将违反"+province+"社区戒毒/社区康复协议一次,";
//								String content = city + "禁毒办告知:" + name + "设备电量过低 请及时充电 完全断电将违反" + province
//										+ "社区戒毒/社区康复协议一次";
                        if(i>0){
                            sql="update heart set SJLX='LowBattery' where LETNO='"+letno+"'";
                            i=runner.update(sql);
                            if(i>0){
                                System.out.println("添加低电量信息,且更新数据包类型为低电量成功");
                            }

                        }
                    }else{

                    }
                }else{
                    System.out.println("===============进入自动解除===============");
                    //查询要解除的低电量信息
                    sql = "select * from alarminfotbl where LETNO='" + letno
                            + "' AND  FUNC='61' order by CREATEDATE asc";
                    writer.write(
                            "[" + logformat.format(new Date()) + "]:" + sql + System.getProperty("line.separator"));
                    List<Map<String,Object>> list=runner.query(sql,new MapListHandler());
                    System.out.println("打印:"+sql);
                    System.out.println("自动解除数量大小"+list.size());
                    if(list.size()>0){
                        String starttime=String.valueOf(list.get(0).get("CREATEDATE"));
                        String endtime=String.valueOf(list.get(list.size()-1).get("CREATEDATE"));
                        String count = String.valueOf(list.size());
                        //删除这个手环的低电量信息
                        sql = "delete from ALARMINFOTBL  WHERE LETNO='" + letno + "' AND FUNC='61' AND FLAG=1";
                        int i=runner.update(sql);
                        if(i>0){
                            //添加删除低电量报警日志
                            String sql1 = "INSERT INTO ALARMLOGINFOTBL(IMEI,FUNC,STARTDATE,ENDDATE,ALARMCOUNT,REASON,LETNO) VALUES(?,?,?,?,?,?,?)";
                            i=runner.update(sql1,imei,"61",starttime,endtime,count,"低电量自动解除",letno);
                            writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                    + System.getProperty("line.separator"));
                            if(i>0) {
                                System.out.println("低电量自动解除成功！");
                                sql = "DELETE FROM newalarm WHERE LETNO='" + letno + "' AND FUNC='61'";
                                //删除最新低电量
                                runner.update(sql);
                                System.out.println("============低电量自动解除===========");
                                writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                        + System.getProperty("line.separator"));
                            }

                        }

                    }

                }

                if (!"".equals(gdxl) && !"0".equals(gdxl) && !"0".equals(jbs) && !"".equals(jbs)) {
                    sql = "select * from healthyinfotbl where LETNO='" + letno + "' and CREATEDATE='"
                            + datetime + "' ";
                    //健康信息查询
                    Map hmap=runner.query(sql,new MapHandler());
                    writer.write("[" + logformat.format(new Date()) + "]:" + sql
                            + System.getProperty("line.separator"));
                    if(hmap==null){
                        int tempFlag=0;
                        // 计算每个上传时间段步数 beign
                        //查询使用者步数
                        sql = "select * from newwalk where LETNO='" + letno+ "' ";
                        Map bsmap=runner.query(sql,new MapHandler());
                        String walknum=jbs;
                        if ("-1".equals(jbs)) {
                            jbs = "0";
                            walknum="0";
                        } else {
                            if (bsmap!=null) {
                                tempFlag=1;
                                String qjbs = String.valueOf(bsmap.get("WALKINGNUM"));
                                if (Integer.parseInt(jbs) > Integer.parseInt(qjbs)) {
                                    jbs = Integer.parseInt(jbs) - Integer.parseInt(qjbs) + "";
                                }
                            }else{
                                jbs=jbs;
                            }
                        }
                        // 计算每个上传时间段步数 end
                        sql = "INSERT INTO `healthyinfotbl` (`IMEI`, `CATEGORY`, `STARTTIME`, `ENDTIME`, `HEARTRATE`, `WALKINGNUM`, `CREATEDATE`,BATTERY,LETNO) VALUES ('"
                                + imei + "', '20', '" + datetime + "', '" + datetime + "', '" + gdxl + "', '" + jbs
                                + "', '" + datetime + "','" + dl + "','" + letno + "')";
                        //添加健康信息
                        int i=runner.update(sql);
                        if(i>0){
                            System.out.println("添加健康信息成功！");
                            writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                    + System.getProperty("line.separator"));
                        }
                        if(tempFlag==1) {
                            sql="update newwalk set WALKINGNUM='"+walknum+"',CREATEDATE='"+datetime+"' where LETNO='"+letno+"'";
                            runner.update(sql);
                        }else {
                            sql="insert into newwalk values('"+letno+"','"+walknum+"','"+datetime+"')";
                            runner.update(sql);
                        }

                    }

                }

                if ("Sos".equals(bjsjlx) || "LowBattery".equals(bjsjlx) || bjsjlx.endsWith("In")
                        || bjsjlx.endsWith("Out") || "BeltOff".equals(bjsjlx) || "Illegal Meeting ".equals(bjsjlx)||"PowerOn".equals(bjsjlx)) {
                    String func = "";
                    if ("Sos".equals(bjsjlx)) {
                        func = "70";
                    } else if ("LowBattery".equals(bjsjlx)) {
                        func = "61";
                    } else if (bjsjlx.endsWith("In")) {
                        func = "71";// 进电子围栏
                    } else if (bjsjlx.endsWith("Out")) {
                        func = "72";// 超出电子围栏
                    } else if ("BeltOff".equals(bjsjlx)) {
                        func = "64";
                    } else if ("Illegal Meeting ".equals(bjsjlx)) {
                        func = "73";// 非法会面
                    }else if("PowerOn".equals(bjsjlx)){
                        if("LowBattery".equals(upsjlx)){
                            func="91";//电量耗尽重新开机
                        }else{
                            func="90";//有电手动开机
                        }
                    }
                    if("90".equals(func)||"91".equals(func)){
                        //开机数据
                        sql = "INSERT INTO power_on (`IMEI`, `FUNC`, `MODE`, `CREATEDATE`, `FLAG`,LETNO) VALUES ('"
                                + imei + "', '" + func + "', '1', '" + datetime + "', '1','" + letno + "');";
                        System.out.println(sql);
                        runner.update(sql);
                    }else if("64".equals(func)){
                        //查询报警事件
                        String alarminfosql = "select * from alarminfotbl where LETNO='" + letno + "' and FUNC='" + func
                                + "' and CREATEDATE='" + datetime + "' ";
                        writer.write("[" + logformat.format(new Date()) + "]:" + alarminfosql
                                + System.getProperty("line.separator"));
                        Map amap=runner.query(alarminfosql,new MapHandler());
                        if(amap==null){
                            //没有此信息，添加信息
                            sql = "INSERT INTO alarminfotbl (`IMEI`, `FUNC`, `MODE`, `CREATEDATE`, `FLAG`,LETNO) VALUES ('"
                                    + imei + "', '" + func + "', '1', '" + datetime + "', '1','" + letno + "');";
                            int i=runner.update(sql);
                            if(i>0){
                                System.out.println("添加剪断报警成功！");
                            }

                        }
                    }else{

                    }

                }else{
                    //修改剪断报警状态
                    sql="select * from newalarm where LETNO='"+letno+"' and FUNC='64' ";
                    Map jmap=runner.query(sql,new MapHandler());
                    writer.write("[" + logformat.format(new Date()) + "]:" + sql
                            + System.getProperty("line.separator"));
                    if(jmap!=null){
                        //修改剪断报警，改为可解除状态
                        sql="update newalarm set MODE='0' where LETNO='"+letno+"' and FUNC='64'";
                        int i=runner.update(sql);
                        if(i>0){
                            System.out.println("修改剪断报警状态为可解除");
                        }
                    }
                }


//				String sim = datas[36]; // <SIM 卡 ICCID>
//				String gjbbh = datas[37]; // <固件版本号>
//				String djzsj = datas[38]; // <多基站数据>
                String wifeo = "";
                String wifet = "";
                if ("02".equals(dres[0])) {
                    wifeo = datas[39].replaceAll("/", ",-"); // <周围 wifi0 的 mac 地址/信号强度>
                    System.out.println("02:+" + wifeo);
                    wifet = datas[40].replaceAll("/", ",-"); // ,<周围 wifi1 的 mac 地址/信号强度/|周围 wifi2 的 mac 地址/信号强度/|….. >
                    System.out.println("02:+" + wifet);
                } else {
                    wifeo = datas[39].replaceAll("/", "-"); // <周围 wifi0 的 mac 地址/信号强度>
                    System.out.println(wifeo);
                    wifet = datas[40].replaceAll("/", "-"); // ,<周围 wifi1 的 mac 地址/信号强度/|周围 wifi2 的 mac 地址/信号强度/|….. >
                    System.out.println(wifet);
                }

                String wife = "";
                String jwd = "";
                // 如果获取到GPS定位数据则往wifeinfotbl表保存数据
                if ("A".equals(gpszt)) {
                    jwd = wd + "," + jd + "," + datetime + "";
                    if (!"".equals(sd)) {
                        jwd = jwd + "," + sd;
                    }
                    //构造添加定位数据
                    sql = "INSERT INTO `wifiinfotbl` (`IMEI`, `FUNC`, `DATA`, `CREATEDATE`,LETNO) VALUES ('"
                            + imei + "', '02', '" + jwd + "', '" + datetime + "','" + letno + "')";
                    System.out.println(sql);
                    //查询是否有相同的定位信息
                    String wifisql = "select * from wifiinfotbl where LETNO='" + letno + "' and CREATEDATE='"
                            + datetime + "'";
                    Map wmap=runner.query(wifisql,new MapHandler());
                    writer.write("[" + logformat.format(new Date()) + "]:" + wifisql
                            + System.getProperty("line.separator"));
                    if(wmap==null){
                        //添加定位信息
                        int i=runner.update(sql);
                        writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                + System.getProperty("line.separator"));
                        if(i>0){
                            //解析gps定位
                            JSONObject gpsobj=GpsUtil.analyze(jwd);
                            if(!"未定位到信息".equals(gpsobj.getString("address"))){
                                sql="INSERT INTO loglatinfotbl (`IMEI`, `LONGITUDE`,  `LATITUDE`, `ADDRESS`, `CREATEDATE`, `LETNO`) VALUES ('"+imei+"', '"+gpsobj.getString("log")+"', '"+gpsobj.getString("lat")+"', '"+gpsobj.getString("address")+"', '"+datetime+"', '"+letno+"');";
                                runner.update(sql);
                                System.out.println("转换gps地址转换成功！");
                                writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                        + System.getProperty("line.separator"));
                            }
                            System.out.println("添加定位信息成功！");

                        }
                    }

                } else {// 如果获取到WIFE定位数据则往wifeinfotbl表保存数据
                    if (!"".equals(wifeo)) {
                        wife = wifeo;
                    }
                    if (!"".equals(wifet)) {
                        if (!"".equals(wife)) {
                            wife = wifeo + "|" + wifet;
                        } else {
                            wife = wifet;
                        }
                    }
                    wife = wife.replaceAll("\\|", "_");
                    if (!"".equals(wife)&&!"0_0".equals(wife)) {
                        sql = "INSERT INTO `wifiinfotbl` (`IMEI`, `FUNC`, `DATA`, `CREATEDATE`,LETNO) VALUES ('"
                                + imei + "', '03', '" + wife + "', '" + datetime + "','" + letno + "')";
                        System.out.println(sql);
                        String wifisql = "select *  from wifiinfotbl where LETNO='" + letno + "' and CREATEDATE='"
                                + datetime + "'";

                        Map wimap=runner.query(wifisql,new MapHandler());
                        writer.write("[" + logformat.format(new Date()) + "]:" + wifisql
                                + System.getProperty("line.separator"));
                        if(wimap==null){
                            int i=runner.update(sql);
                            writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                    + System.getProperty("line.separator"));
                            if(i>0){
                                //解析wifi数据
                                JSONObject wifiobj=GpsUtil.wifiAnalyze(wife,imei);
                                if(!"未定位到信息".equals(wifiobj.getString("address"))){
                                    sql="INSERT INTO loglatinfotbl (`IMEI`, `LONGITUDE`, `LATITUDE`, `ADDRESS`, `CREATEDATE`, `LETNO`) VALUES ('"+imei+"', '"+wifiobj.getString("log")+"', '"+wifiobj.getString("lat")+"', '"+wifiobj.getString("address")+"', '"+datetime+"', '"+letno+"');";
                                    runner.update(sql);
                                    System.out.println("解析wifi地址成功！");
                                    writer.write("[" + logformat.format(new Date()) + "]:" + sql
                                            + System.getProperty("line.separator"));
                                }
                                System.out.println("添加wifi信息成功！");
                            }
                        }

                    }

                }
                String gxy = datas[41]; // <血压高压>
                String dxy = datas[42].substring(0, datas[42].length() - 2); // <血压低压>
                if (!"".equals(gxy) && !"".equals(dxy)) {
                    sql = "INSERT INTO `bloodinfotbl` (`IMEI`, `SYSTOLICPRESSURE`, `DIASTOLICPRESSURE`, `CREATEDATE`,LETNO) VALUES ('"
                            + imei + "', '" + dxy + "', '" + gxy + "', '" + datetime + "','" + letno + "');";
                    System.out.println(sql);
                    String bloodsql = "select * from bloodinfotbl where LETNO='" + letno + "' and CREATEDATE='"
                            + datetime + "'";
                    Map blmap=runner.query(bloodsql,new MapHandler());
                    writer.write("[" + logformat.format(new Date()) + "]:" + bloodsql
                            + System.getProperty("line.separator"));
                    if(blmap==null){
                        int i=runner.update(sql);
                        if(i>0){
                            System.out.println("添加血压信息成功！");
                        }
                    }
                }

                System.out.println("截取尾标识:" + datas[42].substring(0, datas[42].length() - 1));

            }

        }
        writer.flush();
        writer.close();
        long end=System.currentTimeMillis();
        System.out.println("话费时间："+(end-start));

        return null;
    }
}
