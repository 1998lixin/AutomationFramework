# AutomationFramework
 基于AccessibilityService（辅助功能）adb命令 一套Android自动化框架 在拥有ROOT情况下可以解决绝大多数情况 包括webview   
## 优点 
我用它写了几十个APP其中包含一些主流APP自动化都完美适用 优点写起来极快 而且不用考虑不同手机坐标的适配问题   
## 缺点
 1 APP更新后viewID会变（后期可以考虑加入微信巫师里的自动遍历查找对应的ID ）    
 2 无ROOT情况下得手动打开辅助功能不能用命令形式启动   
 3 辅助功能内无法直接请求网络和服务端交互得用读文件有点烦 发广播 SharedPreferences 也不行  
 4 无ROOT Android手机SDK必须大于24（使用了手势识别作为点击）而且webview就没办法了  

#### 示范  
这里用微信朋友圈发图片做一个示范  Webview的示范等我过两天在写吧 其实很简单应该一看就懂了和辅助功能写法类似  而且可以在任何地方书写和服务端交换也是极其方便的
```
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

        tapViewIdTXT("WXimage","com.tencent.mm:id/ekj"); //点击指定的相册 可以为默认相册 图片和视频
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
```
