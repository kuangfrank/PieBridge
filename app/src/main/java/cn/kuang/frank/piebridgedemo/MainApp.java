package cn.kuang.frank.piebridgedemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import cn.kuang.frank.piebridge.PieBridge;
import cn.kuang.frank.piebridgedemo.BookApi.BookApiImpl;
import cn.kuang.frank.piebridgedemo.BookApi.IBookApi;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class MainApp extends Application {

    public static boolean isMyProcessNameSameWith(Context ctx, String name) {
        boolean isSameName = false;
        final int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) (ctx.getSystemService(Context.ACTIVITY_SERVICE));
        for (ActivityManager.RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                isSameName = processInfo.processName.equalsIgnoreCase(name);
                break;
            }
        }
        return isSameName;
    }

    public static boolean isMainProcess(Context ctx) {
        return isMyProcessNameSameWith(ctx, ctx.getPackageName());
    }

    public static boolean isPieBridgeProcess(Context ctx) {
        return isMyProcessNameSameWith(ctx, ctx.getPackageName() + ":PieBridge");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (isMainProcess(this)) {
            PieBridge.getInstance().init(this);
        } else if (isPieBridgeProcess(this)) {
            PieBridge.getInstance().register(IBookApi.class, BookApiImpl.getInstance());
        }
    }

}
