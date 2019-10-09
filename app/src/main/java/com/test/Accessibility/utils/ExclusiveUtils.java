package com.test.Accessibility.utils;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import static com.test.Accessibility.AcessService.mss;
import static com.test.Accessibility.AcessService.stopAccessService;
import static com.test.Accessibility.utils.AccessUtils.findByText;
import static com.test.Accessibility.utils.AccessUtils.findByViewId;
import static com.test.Accessibility.utils.AccessUtils.getCoreXY;
import static com.test.Accessibility.utils.AccessUtils.getswipeViewIdXY;
import static com.test.Accessibility.utils.AccessUtils.isScreen;
import static com.test.automationframework.utils.LogUtils.Acesslog;

public class ExclusiveUtils {

   
 

    // 点击文字
    public static void tapTXT(String txt) throws InterruptedException {

        isTXT(txt);

        List<AccessibilityNodeInfo> aler = findByText(mss, txt);
        getCoreXY(aler, txt, mss);
        Acesslog("点击" + txt + "成功");
    }



    // 点击ViewId文字
    public static void tapViewIdTXT(String txt, String viewID) throws InterruptedException {
        isViewIDTXT(txt, viewID);

        List<AccessibilityNodeInfo> aler = findByViewId(mss, viewID);
        getCoreXY(aler, txt, mss);
        Acesslog("点击ViewId文字 " + txt + "成功");


    }



    // 检测当前屏幕是否存在此文字 txt
    public static void isTXT(String txt) throws InterruptedException {

        int isTxt = 0;
        while (true) {
            isTxt = isTxt + 1;
            Thread.sleep(1000);

            List<AccessibilityNodeInfo> searchT = findByText(mss, txt);
            boolean text = isScreen(searchT, txt);
            if (text == true) {
                break;
            }

            Acesslog(txt + "isTxt " + isTxt);
            if (isTxt >= 20) {
                Acesslog("isTxt 未识别到 " + txt + " 重新开始");
                stopAccessService();
            }


        }
    }



    // 当前viewID是否存在指定文字
    public static void isViewIDTXT(String txt, String viewID) throws InterruptedException {

        int isTxt = 0;
        while (true) {
            isTxt = isTxt + 1;
            Thread.sleep(1000);

            List<AccessibilityNodeInfo> aler = findByViewId(mss, viewID);
            boolean text = isScreen(aler, txt);
            if (text == true) {
                break;
            }

            Acesslog(txt + " isViewIDTXT " + isTxt + " " + aler);
            if (isTxt >= 20) {
                Acesslog("isViewIDTXT 未识别到 " + txt + " 重新开始");
                stopAccessService();

            }


        }

    }


    // swipe 长按此坐标
    protected static void swipeViewIdTXT(String txt, String viewID) throws InterruptedException {
        isViewIDTXT(txt, viewID);

        List<AccessibilityNodeInfo> aler = findByViewId(mss, viewID);
        getswipeViewIdXY(aler, txt, mss);
        Acesslog("长按 " + txt + "成功");


    }



 




}
