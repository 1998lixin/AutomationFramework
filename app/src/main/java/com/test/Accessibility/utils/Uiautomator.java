package com.test.Accessibility.utils;

import com.test.automationframework.utils.Tool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;

import static com.test.Accessibility.utils.AdbUtils.execShell;
import static com.test.Accessibility.utils.AdbUtils.returnResult;
import static com.test.automationframework.utils.LogUtils.Acesslog;

/**
 * Created by Administrator on 2019/8/2 0002.
 */

public class Uiautomator {
    // adb shell 脚本
    private   static boolean  UIexecShell(String cmd) {
        try {
            //权限设置
            Process p = Runtime.getRuntime().exec("su");  //开始执行shell脚本
            //获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            //将命令写入
            dataOutputStream.writeBytes(cmd);
            //提交命令
            dataOutputStream.flush();
            //关闭流操作
            dataOutputStream.close();
            outputStream.close();

            int value = p.waitFor();
            return returnResult(value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    // 当前界面是否存在指定文字
    public  static boolean  isWebviewTxt(final String txt){

        boolean dump= UIexecShell("uiautomator dump");

        if (dump==true){


            File file1 = new File("/sdcard/window_dump.xml");
            if (file1.isFile() && file1.exists()){
                String uiautomator=  Tool.readSDFile("/sdcard/window_dump.xml");
                //   Acesslog("uiautomator "+uiautomator);
                String[] UI=uiautomator.split("bounds=");
                for(String res : UI) {
                    if (!res.contains("[0,0][0,0]")) {

                        if (res.contains(txt)) {
                            Acesslog(txt+"  "+res);

                            //    onTapwebview.ontapSuccess();
                            return true;
                        }

                    }

                }

            }
        }



        return false;





    }

    // 点击对应的文字
    public  static void     TapWebviewXy(final String txt){


        //   Tool.execShell("uiautomator dump");
      /*      try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        File file1 = new File("/sdcard/window_dump.xml");
        if (file1.isFile() && file1.exists()){
            String uiautomator=  Tool.readSDFile("/sdcard/window_dump.xml");
            // Acesslog("uiautomator "+uiautomator);
            String[] UI=uiautomator.split("<node");
            for(String res : UI) {
                if (!res.contains("[0,0][0,0]")) {

                    if (res.contains(txt)) {
                        String bounds = res.substring(res.indexOf("\"["), res.indexOf("]\"") + 1);
                        String rpl=bounds.replace("\"[","").replace("[",",").replace("]","");
                        Acesslog("rpl " + rpl);
                        String[] splitstr=rpl.split(",");

                        long  xy1= Long.parseLong(splitstr[0].trim());
                        long  xy2= Long.parseLong(splitstr[1].trim());
                        long  xy3= Long.parseLong(splitstr[2].trim());
                        long  xy4= Long.parseLong(splitstr[3].trim());


                        long getX=((xy3-xy1) / 2 )+xy1;
                        long getY=((xy4-xy2) / 2 )+xy2;

                        Acesslog("取到的中心坐标 getX"+getX);
                        Acesslog("取到的中心坐标 getY"+getY);
                        execShell("input tap " + getX + " " + getY);

                    }


                }

            }
        }





    }


    /*
         下滑查找指定的文字
       */
    public static boolean lookisWebviewTxt(final String txt) throws InterruptedException {



        boolean getwindow=  UIexecShell("uiautomator dump");
        if (getwindow==true) {
            Thread.sleep(2000);
            String uiautomator=  Tool.readSDFile("/sdcard/window_dump.xml");
            //   Acesslog("uiautomator "+uiautomator);
            String[] UI=uiautomator.split("<node");
            for(String res : UI) {
                //    longlog("原始数据 ", res);
                if (!res.contains("[0,0][0,0]")) {
                    if (res.indexOf("content-desc=")!=-1) {
                        String desc = res.substring(res.indexOf("content-desc="), res.indexOf("checkable="));
                        Acesslog("测试 "+ desc);
                        if (!desc.trim().equals("content-desc=\"\"")||!desc.isEmpty()){

                            if (desc.contains(txt)) {
                                Acesslog("查找到关键字 "+txt + " || " + desc);
                                return true;


                            }

                        }

                    }

                }

            }




        }

        return false;

    }


    // 检测当前界面是否存在此文字  可以是Webview
    public static void isWebTXT(final String txt) {

        int isTxt = 0;
        while (true) {

            isTxt = isTxt + 1;


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean test = isWebviewTxt(txt);
            if (test == true) {
                break;
            }

            Acesslog(txt + "isWebTXT " + isTxt);
            if (isTxt >= 20) {
                Acesslog("isWebTXT 未识别到 " + txt + " 重新开始");

                break;
            }


        }

    }


}
