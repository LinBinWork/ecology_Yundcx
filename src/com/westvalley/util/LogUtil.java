package com.westvalley.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.general.GCONST;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类
 *
 * @author ys ou
 * @Despration
 */
public class LogUtil {
    public static final SimpleDateFormat newDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private PrintWriter logPrint;
    private String logForderName;// log文件夹名
    private String logFile = "";
    private Class<?> clazz;

    protected LogUtil(Class<?> clazz, String logForderName) {
        this.logForderName = logForderName;
        this.clazz = clazz;
    }

    public static LogUtil getLogger() {
        return getLogger(LogUtil.class, "007");
    }

    public static LogUtil getLogger(Class<?> clazz) {
        return getLogger(clazz, "007");
    }

    public static LogUtil getLogger(String forderName) {
        return getLogger(LogUtil.class, forderName);
    }

    public static LogUtil getLogger(Class<?> clazz, String forderName) {
        return new LogUtil(clazz, forderName);
    }

    /**
     * 不格式化
     *
     * @param msg
     */
    public void info(String msg) {
        writeLog(msg);
    }

    public void d(String msg) {
        writeLog(msg);
    }

    public void d(String target, Throwable e) {
        writeLog(target, e);
    }

    public void d(String target, Object... data) {
        writeLog(formatDataStr(target, data));
    }

    public void e(String target, Throwable e) {
        writeLog(target, e);
    }

    private String formatDataStr(String target, Object... data) {
        StringBuffer sb = new StringBuffer(target);
        sb.append("  -->  ");
        sb.append(JSONObject.toJSONString(data));
        return sb.toString();
    }

    private String getLogFile() {
        // 获取当前系统路径
        String sysPath = GCONST.getRootPath();
        if (sysPath == null) {
            sysPath = System.getProperty("user.dir");
        }
        sysPath += "/log/" + logForderName + "/" + df.format(new Date()) + ".log";
        return sysPath;
    }

    private synchronized void newLog(Object msg, Throwable e) {
        Log log = LogFactory.getLog(clazz);
        logFile = getLogFile();
        try {
            logPrint = new PrintWriter(new FileWriter(logFile, true), true);
            logPrint.println(newDf.format(new Date()) + " " + clazz.getName() + ": " + msg);
            if (e instanceof Exception) {
                log.error(msg, (Exception) e);
                e.printStackTrace(logPrint);
            } else {
                log.info((String) msg);
            }
            logPrint.flush();
            logPrint.close();
        } catch (IOException e2) {
            try {
                File file = new File(logFile);
                if (!file.getParentFile().exists()) {
                    // 如果目标文件所在的目录不存在，则创建父目录
                    if (file.getParentFile().mkdirs()) {
                        file.createNewFile();
                    }
                }
                logPrint = new PrintWriter(new FileWriter(logFile, true), true);
                logPrint.println(newDf.format(new Date()) + " " + clazz.getName() + ": " + msg);
                if (e instanceof Exception) {
                    log.error(msg, (Exception) e);
                    e.printStackTrace(logPrint);
                } else {
                    log.info((String) msg);
                }
                logPrint.flush();
                logPrint.close();
            } catch (IOException ex) {
                log.error("Log记录出错了", (Exception) ex);
            }
        } finally {
            if (logPrint != null) {
                logPrint.flush();
                logPrint.close();
            }
        }
    }

    private void writeLog(Object msg) {
        newLog(msg, null);
    }

    private void writeLog(String msg, Throwable e) {
        newLog(msg, e);
    }
}
