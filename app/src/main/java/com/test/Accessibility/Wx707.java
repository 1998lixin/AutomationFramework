package com.test.Accessibility;

import android.content.Context;

import com.test.automationframework.utils.Tool;

import static com.test.Accessibility.AcessService.mss;
import static com.test.Accessibility.utils.AccessUtils.clickBackKey;
import static com.test.Accessibility.utils.AccessUtils.inputTxt;
import static com.test.Accessibility.utils.AccessUtils.tapimage;
import static com.test.Accessibility.utils.ExclusiveUtils.isTXT;
import static com.test.Accessibility.utils.ExclusiveUtils.isViewIDTXT;
import static com.test.Accessibility.utils.ExclusiveUtils.tapTXT;
import static com.test.Accessibility.utils.ExclusiveUtils.tapViewIdTXT;
import static com.test.automationframework.utils.Common.WXFILE;
import static com.test.automationframework.utils.Common.WX_PACKAGE_NAME;

public class Wx707 {

    // txt 是否发送文字   text  要发送的文字   num 要发送几张图片
    public static void SNSImages(Context context ,boolean txt, String text, int num) throws InterruptedException {
        //startAPP("com.tencent.mm/.ui.LauncherUI");
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(WX_PACKAGE_NAME));
        tapTXT("发现");
        Thread.sleep(2000);

        tapTXT("朋友圈");
        Thread.sleep(2000);

        tapViewIdTXT("拍照分享","com.tencent.mm:id/ln");
        Thread.sleep(2000);

        tapTXT("从相册选择");
        Thread.sleep(2000);

        tapViewIdTXT("图片和视频","com.tencent.mm:id/pe");
        Thread.sleep(2000);

        tapViewIdTXT("WXimage","com.tencent.mm:id/ekj"); //点击指定的相册
        Thread.sleep(2000);

        tapimage(num,mss);
        Thread.sleep(2000);

        tapViewIdTXT("完成("+num+"/9)","com.tencent.mm:id/lm");//  点击完成
        isTXT("发表");
        if (txt==true){

            tapViewIdTXT("这一刻的想法...","com.tencent.mm:id/d3k");
            Thread.sleep(2000);
            inputTxt(text,mss);
            Thread.sleep(2000);
        }
        tapTXT("发表");
        Thread.sleep(2000);

        isViewIDTXT("拍照分享","com.tencent.mm:id/ln"); // 任务完成
        clickBackKey(3);

        Tool.deleteFile2(WXFILE);
    }
}
