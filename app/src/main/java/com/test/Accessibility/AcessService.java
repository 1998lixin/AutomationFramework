package com.test.Accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.test.automationframework.utils.Tool;

import static com.test.automationframework.utils.Common.WXFILE;
import static com.test.automationframework.utils.LogUtils.Acesslog;

public class AcessService extends AccessibilityService {

    public static AccessibilityService mss;
    public static Context context;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        try {
            StartAccess();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    protected static void StartAccess() throws InterruptedException {

        while (true) {
            String command = Tool.readSDFile(WXFILE);
            if (command != null) {

                    Wx707.SNSImages(context, true, "嘿嘿", 3);

            } else {

                Thread.sleep(5000);
            }


        }
    }



    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
       Acesslog("系统中断服务 尝试重新运行");
        // stopAccessService();
        //  disableSelf(); // 关闭辅助功能服务 SDK>24
    }


    /**
     * 系统启动
     */
    protected void onServiceConnected() {
        super.onServiceConnected();
        mss = this;
        context=this;
        Acesslog("onServiceConnected: ");

        Toast.makeText(this, "辅助功能已开启", Toast.LENGTH_LONG).show();
        // 服务开启，模拟两次返回键，退出系统设置界面（实际上还应该检查当前UI是否为系统设置界面，但一想到有些厂商可能篡改设置界面，懒得适配了...）
        performGlobalAction(GLOBAL_ACTION_HOME);




    }


    // 某个流程未完成 重新开始任务
    public static void stopAccessService() {
        try {

            Tool.deleteFile2(WXFILE);
            StartAccess();

        } catch (Exception e) {
            Acesslog("重启服务失败 " + e);
        }

    }
}
