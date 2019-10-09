package com.test.automationframework.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2019/7/6 0006.
 */

public class Common {
    public static  boolean ROOT_SWITCH = false;

    public static final String APP_package="com.test.automationframework";
    public static final String WX_PACKAGE_NAME = "com.tencent.mm";


    public static  final String WXFILE= Environment.getExternalStoragePublicDirectory("")+"/Acess/command.txt";
    public static  final String WX_FILE_Acess=Environment.getExternalStoragePublicDirectory("")+"/Acess/";
    public static  final String WX_FILE_command="command.txt";
}
