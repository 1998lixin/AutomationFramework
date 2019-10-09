package com.test.automationframework.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Jurisdiction {

    interface PermissionListener {
        void granted();
        void denied(List<String> deniedList);
    }

    private static PermissionListener mListener;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.CHANGE_WIFI_STATE" ,
            "android.permission.DISABLE_KEYGUARD" ,
            "android.permission.READ_PHONE_STATE" ,
            "android.permission.ACCESS_COARSE_LOCATION" ,
            "android.permission.ACCESS_WIFI_STATE" ,
            "android.permission.READ_WIFI_CREDENTIAL" ,
            "android.permission.BLUETOOTH" ,
            "android.permission.BLUETOOTH_ADMIN" ,
            "android.permission.ACCESS_FINE_LOCATION" ,
            "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" ,
            "android.permission.INTERNET" ,
            "android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permission.GET_ACCOUNTS" ,
            "android.permission.READ_CONTACTS" ,
            "android.permission.RECEIVE_BOOT_COMPLETED" ,
            "android.permission.WAKE_LOCK" ,
            "android.permission.SYSTEM_ALERT_WINDOW" ,
            "android.permission.SYSTEM_OVERLAY_WINDOW" ,
            "android.permission.REQUEST_INSTALL_PACKAGES" ,


    };


    public static void init(Context context){

        //  CopyFile(this);

        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统是不是Android6.0
            requestRuntimePermissions(context,PERMISSIONS_STORAGE, new PermissionListener() {
                @Override
                public void granted() {
                    //权限申请通过
                }

                @Override
                public void denied(List<String> deniedList) {
                    //权限申请未通过
                    for (String denied : deniedList) {
                        if (denied.equals("android.permission.ACCESS_FINE_LOCATION")) {
                            //    CustomToast.INSTANCE.showToast(SDK_WebApp.this, "定位失败，请检查是否打开定位权限！");
                        } else {
                            //   CustomToast.INSTANCE.showToast(SDK_WebApp.this, "没有文件读写权限,请检        查是否打开！");
                        }
                    }
                }
            });
        }
    }





    /**
     * 申请权限
     */
    public static void requestRuntimePermissions(Context context,
            String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        // 遍历每一个申请的权限，把没有通过的权限放在集合中
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            } else {
                mListener.granted();
            }
        }
        // 申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    permissionList.toArray(new String[permissionList.size()]), 1);
        }
    }





    //获取手机类型
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    //跳转至授权页面
    public static void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");

            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                //componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
                //componentName = ComponentName.unflattenFromString("com.samsung.android.sm/.ui.ram.RamActivity");// Permission Denial not exported from uid 1000，不允许被其他程序调用
                componentName = ComponentName.unflattenFromString("com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                //componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");//锁屏清理
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");//跳自启动管理
                //SettingOverlayView.show(context);
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                //componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");//跳转到手机管家
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.SmartBGActivity");//跳转到后台管理页面
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {
                // 将用户引导到系统设置页面
                if (Build.VERSION.SDK_INT >= 9) {
                    Log.e("HLQ_Struggle", "APPLICATION_DETAILS_SETTINGS");
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            context.startActivity(intent);


        } catch (Exception e) {//抛出异常就直接打开设置页面
            Log.e("HLQ_Struggle", e.getLocalizedMessage());
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }


}
