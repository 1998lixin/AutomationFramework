package com.test.Accessibility.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.test.automationframework.utils.LogUtils;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.test.Accessibility.AcessService.mss;
import static com.test.Accessibility.utils.AdbUtils.ADBtap_sleep;
import static com.test.Accessibility.utils.AdbUtils.execShell;
import static com.test.automationframework.utils.Common.ROOT_SWITCH;
import static com.test.automationframework.utils.LogUtils.Acesslog;

/**
 * Created by 14194 on 2018/8/28.
 */

public class AccessUtils {




    // 当前窗口是否包含此文字
    public static boolean isScreen(List<AccessibilityNodeInfo> nodes, String test2) {

        if (nodes==null){
            return false;
        }

        try {
            for (AccessibilityNodeInfo node : nodes) {
                Acesslog("文字判断 "+node.toString());
                if (node.toString().trim().contains(test2)) {
                    return true;
                }


            }
            return false;

        } catch (Exception e) {
            Acesslog("文字判断 失败" + e);
            return false;
        }

    }




//    protected static String  X;
//    protected static String Y;
    protected static  String getXY(List<AccessibilityNodeInfo> nodes ) {
        try {
         String listStringXY= CopyOfStringUtil.ListToString(nodes);
            Acesslog("当前屏幕信息 "+listStringXY);
            String test = listStringXY.substring(listStringXY.indexOf("boundsInScreen")+1,listStringXY.indexOf("packageName"));
            String test2 = test.substring(test.indexOf("(")+1,test.indexOf(")"));
            //以a为分割字符 Rect(209, 432 - 860, 494)
                 String[] splitstr=test2.split("-");
               for(String res : splitstr){
                String[] splitsXY=res.split(",");
                String  X=splitsXY[0];
                String  Y=splitsXY[1];
                   Acesslog("测试 "+test2 );

                 Acesslog("取到的坐标 X"+X+" Y "+Y );
                return execShell("input tap "+X+""+Y);

            }





        }catch(Exception e){
            Acesslog("获取坐标 失败"+e);

        }
        return null;
    }





      /*
      *   当前屏幕是否存才要查找的文字  存在返回坐标
      * */
    public static String getScreenXY(List<AccessibilityNodeInfo> nodes ,String txt){
        String listStringXY= CopyOfStringUtil.ListToString(nodes);
        Acesslog(txt+" 当前屏幕信息 "+listStringXY);
        String[] XY=listStringXY.split("boundsInScreen");
        for(String res : XY) {
            if (txt.equals("packageName")){
                if (res.contains(txt)){
                  // String test = res.substring(listStringXY.indexOf("boundsInScreen")+1,listStringXY.indexOf("packageName"));
                        String wxidXY = res.substring(res.indexOf("(")+1,res.indexOf(")"));
                        Acesslog("wxidXY "+wxidXY);
                        return wxidXY.trim();
                }

            }else {

                Acesslog("res  "+res);
                if (res.contains(txt)){
                    String getText=res.substring(res.indexOf("text:")+5,res.indexOf("; error:"));
                    String getcontentDescription=res.substring(res.indexOf("ption:")+6,res.indexOf("; viewIdResName"));
                    Acesslog("getText  "+getText);
                    Acesslog("getcontentDescription  "+getcontentDescription);
                    if (getText.trim().equals(txt)||getcontentDescription.trim().equals(txt)){
                        // String test = res.substring(listStringXY.indexOf("boundsInScreen")+1,listStringXY.indexOf("packageName"));
                        String wxidXY = res.substring(res.indexOf("(")+1,res.indexOf(")"));
                        Acesslog("坐标 "+wxidXY);
                        return wxidXY.trim();
                    }
                }

            }

        }
        return null;
    }



     // 取中心坐标进行点击
    public static  String getCoreXY(List<AccessibilityNodeInfo> nodes ,String txt,AccessibilityService acc) {
        try {

            String   ScreenXY= getScreenXY(nodes,txt);
            if (ScreenXY!=null){
                Acesslog("取到的坐标 ScreenXY"+ScreenXY);
                String[] splitstr=ScreenXY.split("-");
                String   leftsql= splitstr[0];
                String[] leftX=leftsql.split(",");
                String   splitsXY= splitstr[1];
                String[] topXY=splitsXY.split(",");

                long  xy1= Long.parseLong(leftX[0].trim());
                long  xy2= Long.parseLong(leftX[1].trim());
                long  xy3= Long.parseLong(topXY[0].trim());
                long  xy4= Long.parseLong(topXY[1].trim());


                long getX=((xy3-xy1) / 2 )+xy1;
                long getY=((xy4-xy2) / 2 )+xy2;

                Acesslog("取到的中心坐标 getX"+getX);
                Acesslog("取到的中心坐标 getY"+getY);

                if(ROOT_SWITCH) {
                    Acesslog("adb 点击 ");
                    execShell("input tap " + getX + " " + getY);
                }else {
                    Acesslog("无ROOT点击 ");
                    if (Build.VERSION.SDK_INT>=24){
                        useGestureClick(getX,getY,acc);
                    }

                }


            }



        }catch(Exception e){
            Acesslog("获取坐标 失败"+e);

        }
        return null;
    }


    // 长按当前坐标
    public static  String getswipeViewIdXY(List<AccessibilityNodeInfo> nodes ,String txt,AccessibilityService acc) {
        try {
            String   ScreenXY= getScreenXY(nodes,txt);
            if (ScreenXY!=null){
                Acesslog("取到的坐标 getScreenXY "+ScreenXY);
                String[] splitstr=ScreenXY.split("-");
                String   leftsql= splitstr[0];
                String[] leftX=leftsql.split(",");
                String   splitsXY= splitstr[1];
                String[] topXY=splitsXY.split(",");

                long  xy1= Long.parseLong(leftX[0].trim());
                long  xy2= Long.parseLong(leftX[1].trim());
                long  xy3= Long.parseLong(topXY[0].trim());
                long  xy4= Long.parseLong(topXY[1].trim());


                long getX=((xy3-xy1) / 2 )+xy1;
                long getY=((xy4-xy2) / 2 )+xy2;

                Acesslog("取到的中心坐标 getX"+getX);
                Acesslog("取到的中心坐标 getY"+getY);

                if(ROOT_SWITCH) {
                    Acesslog("adb 点击 ");
                    execShell("input swipe "+getX+" "+getY+" "+getX+" "+getY+" 800");
                }else {
                    Acesslog("无ROOT 长按 ");
                    if (Build.VERSION.SDK_INT>=24){
                        swipeClick(getX,getY,acc);
                    }

                }




            }

        }catch(Exception e){
            Acesslog("获取坐标 失败"+e);

        }
        return null;
    }






    protected  static  int time(){
     Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
     int year = calendar.get(Calendar.YEAR);
//月
     int month = calendar.get(Calendar.MONTH)+1;
//日
     int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
     int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
     int minute = calendar.get(Calendar.MINUTE);
//秒
     int second = calendar.get(Calendar.SECOND);
     Acesslog("Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
     return hour;


 }




    /**
     * 检查系统设置：是否开启辅助服务
     * @param service 辅助服务
     */
    private static boolean isSettingOpen(Class service, Context cxt) {
        try {
            int enable = Settings.Secure.getInt(cxt.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
            if (enable != 1)
                return false;
            String services = Settings.Secure.getString(cxt.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!TextUtils.isEmpty(services)) {
                TextUtils.SimpleStringSplitter split = new TextUtils.SimpleStringSplitter(':');
                split.setString(services);
                while (split.hasNext()) { // 遍历所有已开启的辅助服务名
                    if (split.next().equalsIgnoreCase(cxt.getPackageName() + "/" + service.getName()))
                        return true;
                }
            }
        } catch (Throwable e) {//若出现异常，则说明该手机设置被厂商篡改了,需要适配
            Acesslog("isSettingOpen"+ e.getMessage());
        }
        return false;
    }

    /**
     * 跳转到系统设置：开启辅助服务
     */
    public static void jumpToSetting(final Context cxt) {
        try {
            cxt.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } catch (Throwable e) {//若出现异常，则说明该手机设置被厂商篡改了,需要适配
            try {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
            } catch (Throwable e2) {
                Acesslog("jumpToSetting"+ e2.getMessage());
            }
        }
    }







    // 下滑  API大于24
    @RequiresApi(api = Build.VERSION_CODES.N)
    public     static   void lookup(AccessibilityService acc) {
        Acesslog("下滑");
        Path path2 = new Path();
        path2.moveTo(300, 1000);
        path2.lineTo(300, 0);



        final GestureDescription.StrokeDescription sd2 = new GestureDescription.StrokeDescription(path2, 0, 300);
        acc.dispatchGesture(new GestureDescription.Builder().addStroke(sd2).build(), null, null);

    }





    //点击坐标 API >=24
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static void useGestureClick(long X, long Y, AccessibilityService acc) {
        Acesslog("useGestureClick X:"+X+"Y:"+Y);


        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(X, Y);
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 30))
                .build();
        acc.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }



// 长按指定坐标
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static void swipeClick(long X, long Y, AccessibilityService acc) {
        Acesslog("swipeClick ");


        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(X, Y);
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 500))
                .build();
        acc.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void Click(String getXY, AccessibilityService acc) {
        Acesslog("Click ");
        String[] ss = getXY.split("\\s+");
        long X= Long.parseLong(ss[0]);
        long Y= Long.parseLong(ss[1]);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(X, Y);
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 30))
                .build();
        acc.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }


    // 返回键
    public static void clickBackKey(int num) throws InterruptedException {

        for (int i = 0; i < num; i++) {
            if (ROOT_SWITCH){
                execShell("input keyevent 4");
                Thread.sleep(1000);
            }else {
                mss.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Thread.sleep(1000);
            }

        }
    }



    //获取微信图片视频选择界面坐标    num第几张图片或视频
    protected static String getimgXY(int num ,AccessibilityService accessibilityService){


        List<AccessibilityNodeInfo> aler = findByViewId(accessibilityService,"com.tencent.mm:id/bwn");
        String listStringXY= CopyOfStringUtil.ListToString(aler);


        if (!listStringXY.isEmpty()){
            String[] XY=listStringXY.split("boundsInScreen");
            Acesslog("测试 "+listStringXY);

            String wxidXY = XY[num].substring(XY[num].indexOf("(")+1,XY[num].indexOf(")"));
            Acesslog("wxidXY "+wxidXY);
            if (!wxidXY.isEmpty()){
                String[] getimgXY=wxidXY.split("-");
                String   leftsql= getimgXY[0];
                String[] leftX=leftsql.split(",");
                String   splitsXY= getimgXY[1];
                String[] topXY=splitsXY.split(",");

                long  xy1= Long.parseLong(leftX[0].trim());
                long  xy2= Long.parseLong(leftX[1].trim());
                long  xy3= Long.parseLong(topXY[0].trim());
                long  xy4= Long.parseLong(topXY[1].trim());


                long getX=((xy3-xy1) / 2 )+xy1;
                long getY=((xy4-xy2) / 2 )+xy2;

                String getXY=getX+" "+getY;
                Acesslog("图片坐标 "+getXY);
                return getXY;


            }


        }

        return null;

    }

    // 点击微信图片坐标 number 一次点击几张
    public static void tapimage(int number,AccessibilityService acc) throws InterruptedException {



        if (number==0){
            Acesslog("图片不存在 停止发送 ");
        }else if (number==1){

            ADBtap_sleep(  getimgXY(1,acc));

        }else if (number==2){

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));

        }else if (number==3){

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));ADBtap_sleep(getimgXY(3,acc));

        }else if (number==4){

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));ADBtap_sleep(getimgXY(3,acc));
            ADBtap_sleep(getimgXY(4,acc));

        }else if (number==5){
            ADBtap_sleep(getimgXY(1,acc));
            ADBtap_sleep(getimgXY(2,acc));
            ADBtap_sleep(getimgXY(3,acc));
            ADBtap_sleep(getimgXY(4,acc));
            ADBtap_sleep(getimgXY(5,acc));

        }else if (number==6){

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));ADBtap_sleep(getimgXY(3,acc));
            ADBtap_sleep(getimgXY(4,acc)); ADBtap_sleep(getimgXY(5,acc)); ADBtap_sleep(getimgXY(6,acc));

        }else if (number==7){

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));ADBtap_sleep(getimgXY(3,acc));
            ADBtap_sleep(getimgXY(4,acc)); ADBtap_sleep(getimgXY(5,acc)); ADBtap_sleep(getimgXY(6,acc));
            ADBtap_sleep(getimgXY(7,acc));

        }else if (number==8){

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));ADBtap_sleep(getimgXY(3,acc));
            ADBtap_sleep(getimgXY(4,acc)); ADBtap_sleep(getimgXY(5,acc)); ADBtap_sleep(getimgXY(6,acc));
            ADBtap_sleep(getimgXY(7,acc)); ADBtap_sleep(getimgXY(8,acc));

        }else if (number==9){

        /*        ADBtap_sleep("150 163"); ADBtap_sleep("331 167");ADBtap_sleep("507 162");
                ADBtap_sleep("689 165"); ADBtap_sleep("147 340"); ADBtap_sleep("328 343");
                ADBtap_sleep("498 344"); ADBtap_sleep("688 350"); ADBtap_sleep("147 520");*/

            ADBtap_sleep(getimgXY(1,acc)); ADBtap_sleep(getimgXY(2,acc));ADBtap_sleep(getimgXY(3,acc));
            ADBtap_sleep(getimgXY(4,acc)); ADBtap_sleep(getimgXY(5,acc)); ADBtap_sleep(getimgXY(6,acc));
            ADBtap_sleep(getimgXY(7,acc)); ADBtap_sleep(getimgXY(8,acc));ADBtap_sleep(getimgXY(9,acc));

        }






    }



// 返回当前list 指定坐标
    public static   String getListXY(int num ,String viewid ,AccessibilityService accessibilityService){


        List<AccessibilityNodeInfo> aler = findByViewId(accessibilityService,viewid);
        String listStringXY= CopyOfStringUtil.ListToString(aler);


        if (!listStringXY.isEmpty()){
            String[] XY=listStringXY.split("boundsInScreen");

            String wxidXY = XY[num].substring(XY[num].indexOf("(")+1,XY[num].indexOf(")"));
            Acesslog("wxidXY "+wxidXY);
            if (!wxidXY.isEmpty()){
                String[] getimgXY=wxidXY.split("-");
                String   leftsql= getimgXY[0];
                String[] leftX=leftsql.split(",");
                String   splitsXY= getimgXY[1];
                String[] topXY=splitsXY.split(",");

                long  xy1= Long.parseLong(leftX[0].trim());
                long  xy2= Long.parseLong(leftX[1].trim());
                long  xy3= Long.parseLong(topXY[0].trim());
                long  xy4= Long.parseLong(topXY[1].trim());


                long getX=((xy3-xy1) / 2 )+xy1;
                long getY=((xy4-xy2) / 2 )+xy2;

                String getXY=getX+" "+getY;
                Acesslog("图片坐标 "+getXY);
                return getXY;


            }


        }

        return null;

    }


    // 输入文本
    public static void inputTxt(String txt ,AccessibilityService acc){
        ClipboardManager clipboard = (ClipboardManager)acc.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(txt, txt);
        clipboard.setPrimaryClip(clip);

        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow();
        AccessibilityNodeInfo accessibilityNodeInfo = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);

//焦点（n是AccessibilityNodeInfo对象）
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
//粘贴进入内容
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);

    }








    /**
     * 模拟点击
     *
     * @param event      事件
     * @param text       按钮文字
     * @param widgetType 按钮类型，如android.widget.Button，android.widget.TextView
     */
    protected static void click(AccessibilityEvent event, String text, String widgetType) {
        // 事件页面节点信息不为空
        if (event.getSource() != null) {
            // 根据Text搜索所有符合条件的节点, 模糊搜索方式; 还可以通过ID来精确搜索findAccessibilityNodeInfosByViewId
            List<AccessibilityNodeInfo> stop_nodes = event.getSource().findAccessibilityNodeInfosByText(text);
            // 遍历节点
            if (stop_nodes != null && !stop_nodes.isEmpty()) {
                AccessibilityNodeInfo node;
                for (int i = 0; i < stop_nodes.size(); i++) {
                    node = stop_nodes.get(i);
                    // 判断按钮类型
                    if (node.getClassName().equals(widgetType)) {
                        // 可用则模拟点击
                        if (node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }
    }


    protected static boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 获得点击View的类型
                Acesslog("View类型：" + node.getClassName());
                Acesslog("getText类型：" + node.getText());
                // 进行模拟点击
                if (node.isEnabled()) {
                    return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
        return false;
    }





    /*
*      判断应用是否后台运行中
* */
    public static boolean isRunning(Context context, String packageName){
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo rapi : infos){
            if(rapi.processName.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        try {
            PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (appPackageName.equals(pn)) {
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e){

        }
        return false;
    }


    // 查找安装,并模拟点击(findAccessibilityNodeInfosByText判断逻辑是contains而非equals)
    public static void findTxtClick(AccessibilityNodeInfo nodeInfo, String txt) {
        try {
            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(txt);
            if (nodes == null || nodes.isEmpty())
                return;
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isEnabled() && node.isClickable() && (node.getClassName().equals("android.widget.Button")
                        || node.getClassName().equals("android.widget.CheckBox") // 兼容华为安装界面的复选框
                )) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }catch (Exception e){
            LogUtils.Acesslog("findTxtClick 异常 "+e);
        }

    }

    public static List<AccessibilityNodeInfo> findByViewId(AccessibilityService accessibilityService, String txt) {
        try {
            AccessibilityNodeInfo root = accessibilityService.getRootInActiveWindow();
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId(txt);
            if (nodes == null || nodes.isEmpty())
                return nodes;

            return nodes;
        }catch (Exception e){
            LogUtils.Acesslog("findByViewId 异常 "+e);
        }

        return null;
    }
    public static List<AccessibilityNodeInfo> findByText(AccessibilityService accessibilityService, String txt) {
        try {
            AccessibilityNodeInfo root = accessibilityService.getRootInActiveWindow();
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByText(txt);
            if (nodes == null || nodes.isEmpty())
                return nodes;

            return nodes;
        }catch (Exception e){
            LogUtils.Acesslog("findByText  异常 "+e);
        }

        return null;
    }



}
