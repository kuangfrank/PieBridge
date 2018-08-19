package cn.kuang.frank.piebridge;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class PieBridgeService extends Service {
    private final static String TAG = PieBridgeService.class.getSimpleName();

    private final IPieBridgeAidl.Stub mPieBridgeBinder = new IPieBridgeAidl.Stub() {
        @Override
        public Bundle call(Bundle args) throws RemoteException {
            Log.i(TAG, "PieBridgeAidl call");
            Log.i(TAG, "args = " + args.toString());

            args.setClassLoader(PieBridge.class.getClassLoader());
            String clazzName = args.getString("clazzName");
            String methodName = args.getString("methodName");

            try {
                Class<?> clazz = Class.forName(clazzName);
                Method method = null;
                for (Method m : clazz.getMethods()) {
                    if (m.getName().equals(methodName)) {
                        method = m;
                        break;
                    }
                }
                if (method != null) {
                    Class<?>[] types = method.getParameterTypes();
                    List<Object> objs = new ArrayList<>();
                    objs.add(args);
                    Object instance = PieBridge.getInstance().getInstance(clazz);
                    Log.i(TAG, "instance = " + instance.toString());
                    Log.i(TAG, "method = " + method.toString());
                    Log.i(TAG, "types = " + Arrays.toString(types));
                    Log.i(TAG, "params = " + args.toString());
                    Object returnObj = method.invoke(instance, objs.toArray());

                    Bundle result = (Bundle) returnObj;
                    if (result == null) {
                        Log.i(TAG, "returnObj = null");
                        result = new Bundle();
                        result.putInt(PieBridgeConsts.KEY_ERROR_CODE, -1);
                        result.putString(PieBridgeConsts.KEY_ERROR_MSG, "FAIL");
                    } else {
                        result.putInt(PieBridgeConsts.KEY_ERROR_CODE, 0);
                        result.putString(PieBridgeConsts.KEY_ERROR_MSG, "OK");
                    }

                    Log.i(TAG, "result = " + result.toString());
                    return result;
                }
                throw new RuntimeException("method " + methodName + " cant not find");
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                return null;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand,pid:" + android.os.Process.myPid()
                + ",action:" + intent.getAction());

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mPieBridgeBinder;
    }
}