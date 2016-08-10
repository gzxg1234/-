package com.sanron.sunweather.engine;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 异常处理保存
 * Created by 三荣 on 2015/12/1.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;


    private volatile static CrashHandler mInstance;

    public static CrashHandler getmInstance(Context context){
        if(mInstance == null){
            synchronized (CrashHandler.class){
                if(mInstance == null){
                    mInstance = new CrashHandler(context);
                }
            }
        }
        return mInstance;
    }

    public CrashHandler(Context context){
        mContext = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            saveToSdCard(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        if(mDefaultHandler!=null){
            mDefaultHandler.uncaughtException(thread,ex);
        }
    }

    private void saveToSdCard(Throwable ex) throws IOException {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)){
            throw new IOException("no sdcard");
        }
        File dir = new File(mContext.getExternalCacheDir().getPath()+"/log");
        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir,"error.log");
        if(!file.exists()){
            file.createNewFile();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        String time = sdf.format(new Date());
        PrintWriter pw = new PrintWriter(new FileOutputStream(file,true));
        pw.println("----"+time+"----");
        printPhoneInfo(pw);
        ex.printStackTrace(pw);
        pw.println();
        pw.close();
    }

    private void printPhoneInfo(PrintWriter pw){
        pw.println("android version:"+Build.VERSION.RELEASE+"_"+Build.VERSION.SDK_INT);
        pw.println("vendor:"+Build.MANUFACTURER);
        pw.println("model:"+Build.MODEL);
        pw.println("cpu abi:"+Build.CPU_ABI);
    }
}
