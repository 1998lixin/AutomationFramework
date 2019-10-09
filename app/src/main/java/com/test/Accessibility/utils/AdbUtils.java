package com.test.Accessibility.utils;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static com.test.Accessibility.AcessService.mss;
import static com.test.Accessibility.utils.AccessUtils.findTxtClick;
import static com.test.Accessibility.utils.AccessUtils.useGestureClick;
import static com.test.automationframework.utils.Common.APP_package;
import static com.test.automationframework.utils.Common.ROOT_SWITCH;
import static com.test.automationframework.utils.LogUtils.Acesslog;


/**
 * Created by Administrator on 2019/8/2 0002.
 */

public class AdbUtils {
    // adb shell 脚本
    public static final String execShell(String cmd) {
        Acesslog("execShell "+cmd);
        try {
            if(ROOT_SWITCH){
                Acesslog("root权限获取成功");
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


            } else{
                Acesslog("没有 root权限 不执行");

            }



        } catch (Throwable t) {
            t.printStackTrace();
        }
        return cmd;
    }

    /**
     * 判断手机是否有root权限
     */
    public static boolean hasRootPerssion(){
        try {
            PrintWriter PrintWriter = null;
            Process process = null;
            try {
                process = Runtime.getRuntime().exec("su");
                PrintWriter = new PrintWriter(process.getOutputStream());
                PrintWriter.flush();
                PrintWriter.close();
                int value = process.waitFor();
                return returnResult(value);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(process!=null){
                    process.destroy();
                }
            }
            return false;
        }catch (Exception E){

        }
        return false;
    }
    public static boolean returnResult(int value){
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    public  static void stopApp(String Packagename){
        // 结束应用
        execShell("am force-stop "+Packagename);
    }
    public  static void clearApp(String Packagename){
        // 清理应用缓存
        execShell("pm clear "+Packagename);
    }

    public  static void  startAPP(String APP){
        // 跳转到指定APP界面
        execShell("am start -n "+APP);
    }


    public  static void  ADBtap(String XY){
        execShell("input tap "+XY);
    }

    public  static void  ADBtap_sleep(String XY) throws InterruptedException {

        if(ROOT_SWITCH) {
            execShell("input tap "+XY);
        }else {

            if (Build.VERSION.SDK_INT >= 24){
                Acesslog("无ROOT 点击 "+XY);
                String[] ss = XY.split("\\s+");
                long X= Long.parseLong(ss[0]);
                long Y= Long.parseLong(ss[1]);
                useGestureClick(X,Y,mss);
            }

        }

        Thread.sleep(1000);
    }

    /*
       下滑查找指定的文字
     */
    public static   void lookupTxt(String txt)  {
        try {
        int lookup = 0;
        while (true) {
            lookup = lookup + 1;

                Thread.sleep(1000);


            boolean text = Uiautomator.isWebviewTxt( txt);
            if (text==true) {
                Thread.sleep(2000);
                Acesslog("已发现要滑动查找的文字："+txt+" 结束 ");
                break;
            } else {


                execShell("input swipe 383 912 366 422");
                Acesslog("未发现要滑动查找的文字 继续下滑寻找");
                Thread.sleep(2000);
            }

            Acesslog("lookup " + lookup);
            if (lookup >= 20) {
                Acesslog("滑动查找的文字超时 重启");
                break;
            }

        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //  num 滑动次数
    private void Slide(int num, AccessibilityService acc) throws InterruptedException {
        for (int i=0;i<=num;i++){
            Thread.sleep(1000);
            AccessibilityNodeInfo root = acc.getRootInActiveWindow();
            findTxtClick(root, "取消");
            execShell("input swipe 598 1583 689 812");
        }

    }


    // 输入文本   需要配合 ADBKeyBoard.apk  https://github.com/senzhk/ADBKeyBoard
    public static void adbinputTxt(String txt ){
           // adb shell ime set com.android.adbkeyboard/.AdbIME
         execShell("am broadcast -a ADB_INPUT_TEXT --es msg '"+txt+"'");
    }


    // 具备ROOT情况下 可以直接开启指定的辅助功能
    public static void StarAcess(){
        execShell("settings put secure enabled_accessibility_services "+APP_package+"/com.test.Accessibility.AcessService");
        execShell("settings put secure accessibility_enabled 1");


    }

}
