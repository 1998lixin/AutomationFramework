package com.test.automationframework.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.test.automationframework.utils.LogUtils.Acesslog;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class Tool {





/*   / *
           此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
     *
             * @param buffer   写入文件的内容
     * @param folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
     * @param fileName 文件名称，默认app_log.txt
     * @param append   是否追加写入，true为追加写入，false为重写文件
     * @param autoLine 针对追加模式，true为增加时换行，false为增加时不换行
     /*/
    public synchronized static void writeFileToSDCard( final String buffer,  final String folder,
                                                       final String fileName, final boolean append, final boolean autoLine) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sdCardExist = Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED);
                String folderPath = "";
                if (sdCardExist) {
                    //TextUtils为android自带的帮助类
                    if (TextUtils.isEmpty(folder)) {
                        //如果folder为空，则直接保存在sd卡的根目录
                        folderPath = Environment.getExternalStorageDirectory()
                                + File.separator;
                    } else {
                        folderPath =  folder + File.separator;
                    }
                } else {
                    return;
                }


                File fileDir = new File(folderPath);
                if (!fileDir.exists()) {
                    if (!fileDir.mkdirs()) {
                        return;
                    }
                }
                File file;
                //判断文件名是否为空
                if (TextUtils.isEmpty(fileName)) {
                    file = new File(folderPath + "app_log.txt");
                } else {
                    file = new File(folderPath + fileName);
                }
                RandomAccessFile raf = null;
                FileOutputStream out = null;
                try {
                    if (append) {
                        //如果为追加则在原来的基础上继续写文件
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(file.length());
                        raf.write(buffer.getBytes());
                        if (autoLine) {
                            raf.write("".getBytes());
                        }
                    } else {
                        //重写文件，覆盖掉原来的数据
                        out = new FileOutputStream(file);
                        out.write(buffer.getBytes());
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static String readSDFile(String fname) {
        File file1 = new File(fname);
        if (file1.isFile() && file1.exists()) {
            String result=null;
            long millisRead = System.currentTimeMillis();
            try {
                File f=new File(fname);   //File(Environment.getExternalStorageDirectory().getPath()+fname);
                int length=(int)f.length();
                byte[] buff=new byte[length];
                FileInputStream fin=new FileInputStream(f);
                fin.read(buff);
                fin.close();
                result=new String(buff,"UTF-8");

//            Acesslog("readSDFile time = "
//                    + (System.currentTimeMillis() - millisRead)+"ms;");

                return result;

        }catch (Exception e){

            Acesslog("读取文件异常:"+e);

        }

        } else {
        //    Acesslog(fname+ "不存在  ");
        }
        return null;
    }





    /**
     * 判断服务是否处于运行状态.
     * @param servicename
     * @param context
     * @return
     */
    public static boolean isServiceRunning(String servicename,Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info: infos){
            if(servicename.equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }






    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile2(String fileName) {
        try {
            File file = new File(fileName);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("删除单个文件" + fileName + "成功！");
                    return true;
                } else {
                    System.out.println("删除单个文件" + fileName + "失败！");
                    return false;
                }
            } else {
                System.out.println("删除单个文件失败：" + fileName + "不存在！");
                return false;
            }
        }catch (Exception e){
            System.out.println("删除单个文件失败：" + e);
        }
        return false;
    }




    /**
     * 获取版本号
     *
     * @param context
     * @return 获取失败则返回0
     */
    public static int getVersionCode(Context context,String packagename) {
        // 包管理者
        PackageManager mg = context.getPackageManager();
        try {
            // getPackageInfo(packageName 包名, flags 标志位（表示要获取什么数据）);
            // 0表示获取基本数据
            PackageInfo info = mg.getPackageInfo(packagename, 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取版本名称
     *
     * @param context
     * @return 获取失败则返回0
     */
    public static String getVersionName(Context context,String packagename) {
        // 包管理者
        PackageManager mg = context.getPackageManager();
        try {
            // getPackageInfo(packageName 包名, flags 标志位（表示要获取什么数据）);
            // 0表示获取基本数据
            PackageInfo info = mg.getPackageInfo(packagename, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }







}