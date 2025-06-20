package com.huaxia.xlib.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;


import com.huaxia.xlib.path.PathUtils;

import java.io.File;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类。
 * <p>
 * 需要初始化。
 *
 * @author xzy
 */
@SuppressWarnings("all")
public class L {
    @SuppressLint("StaticFieldLeak")
    private static L mInstance;
    private static final Object mLock = new Object();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    /**
     * 是否打印日志
     */
    public boolean isDebug = true;
    public String className;
    public String TAG = "";
    public long fileSize = 0;
    public boolean isWriteLog2File = false;
    @SuppressLint("SimpleDateFormat")
    // 日志的输出格式
    private static final SimpleDateFormat LOG_SDF =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    // 日志文件格式
    private static final SimpleDateFormat LOG_FILE =
            new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 实例化
     * @param context 上下文
     * @return L 对象
     */
    public static L getInstance(Context context) {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new L();
                    mContext = context;
                }
            }
        }
        return mInstance;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getTag() {
        return TAG;
    }

    /**
     * 设置调试模式
     * @param debug
     * @return
     */
    public L setDebug(boolean debug) {
        mInstance.isDebug = debug;
        return mInstance;
    }

    public L setTag(String Tag) {
        mInstance.TAG = Tag;
        return mInstance;
    }

    public boolean isWriteLog2File() {
        return isWriteLog2File;
    }

    public L setLog2File(boolean isWriteLog2File) {
        mInstance.isWriteLog2File = isWriteLog2File;
        return mInstance;
    }

    // 下面四个是默认 tag 的函数
    public void i(String msg) {
        if (isDebug()) i(getTag(), msg);
    }

    public void d(String msg) {
        if (isDebug()) d(getTag(), msg);
    }

    public void e(String msg) {
        if (isDebug()) e(getTag(), msg);
    }

    public void w(Throwable ex) {
        if (isDebug()) w(getTag(), ex);
    }

    public void i(String tag, String info) {
        if (isDebug()) {
            try {
                className = (new Throwable().getStackTrace())[1].getFileName();
                if (className == null) {
                    className = "";
                }
            } catch (Exception ignored) {
            }
            Log.i(tag, className.replace(".java", "") + " -> " + info);
        }
        onWriteLog2File(info, className);
    }

    public void d(String tag, String info) {
        if (isDebug()) {
            try {
                className = (new Throwable().getStackTrace())[1].getFileName();
                if (className == null) {
                    className = "";
                }
            } catch (Exception ignored) {
            }
            Log.d(tag, className.replace(".java", "") + " -> " + info);
        }
        onWriteLog2File(info, className);
    }

    public void e(String tag, String info) {
        if (isDebug()) {
            try {
                className = (new Throwable().getStackTrace())[1].getFileName();
                if (className == null) {
                    className = "";
                }
            } catch (Exception ignored) {
            }
            Log.e(tag, className.replace(".java", "") + " -> " + info);
        }
        onWriteLog2File(info, className);
    }

    public void e(String tag, String info, Throwable ex) {
        if (isDebug()) {
            try {
                className = (new Throwable().getStackTrace())[1].getFileName();
                if (className == null) {
                    className = "";
                }
            } catch (Exception ignored) {
            }
            Log.e(tag, className.replace(".java", "") + " -> " + info, ex);
        }
        onWriteLog2File(info, className);
    }

    public void w(String tag, Throwable ex) {
        if (isDebug()) {
            try {
                className = (new Throwable().getStackTrace())[1].getFileName();
                if (className == null) {
                    className = "";
                }
            } catch (Exception ignored) {
            }
            Log.i(tag, className.replace(".java", "") + " -> ");
            Log.w(getTag(), ex);
        }
        onWriteLog2File(ex.getMessage(), className);
    }


    private String getFilePath(Context context) {
        return PathUtils.getExternalAppFilesPath();
    }

    private synchronized void switchFile() {
        if (fileSize > 1024 * 1024) {
            for (int i = 5; i > 0; i--) {
                File file = new File(getFilePath(mContext)
                        + "/log/", "running_log" + (i - 1) + ".txt");
                if (file.exists()) {
                    File temp = new File(getFilePath(mContext)
                            + "/log/", "running_log" + i + ".txt");
                    file.renameTo(temp);
                }
            }
            File file = new File(getFilePath(mContext)
                    + "/log/", "running_log.txt");
            if (file.exists()) {
                File temp = new File(getFilePath(mContext)
                        + "/log/", "running_log1.txt");
                file.renameTo(temp);
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param text 需要写入的日志信息
     * @param type 日志类型
     */
    private void onWriteLog2File(String text, String type) {
        if (!isWriteLog2File()) {
            return;
        }
        Date nowTime = new Date();
        String needWriteFile = LOG_FILE.format(nowTime);
        String needWriteMessage = LOG_SDF.format(nowTime)
                + "    "
                + type
                + " "
                + getTag()
                + "    "
                + text;
        File temp = new File(getFilePath(mContext) + "/log");
        if (!temp.exists()) {
            temp.mkdirs();
        }
        switchFile();
        File file = new File(getFilePath(mContext)
                + "/log/", "running_log.txt");
        try {
            fileSize = file.length();
            // 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
