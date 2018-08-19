package cn.kuang.frank.piebridge;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class PieBridgeHandler implements InvocationHandler {
    private final static String TAG = PieBridgeHandler.class.getSimpleName();
    private Class<?> mClazz;
    private IPieBridgeAidl mPieBridgeAidl;

    private PieBridgeHandler(Class<?> clazz, IPieBridgeAidl aidl) {
        this.mClazz = clazz;
        this.mPieBridgeAidl = aidl;
    }

    public static <T> T newProxyInstance(Class<T> tClass, IPieBridgeAidl aidl) {
        Class<?>[] interfaces = new Class[]{tClass};
        PieBridgeHandler handler = new PieBridgeHandler(tClass, aidl);
        Object proxy = Proxy.newProxyInstance(tClass.getClassLoader(), interfaces, handler);
        return (T) proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String clazzName = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        String longName = clazzName + "." + methodName;
        Log.i(TAG, "method: " + longName);

        if (args != null) {
            Log.i(TAG, "args: " + args.toString());
        }

        if ("Object".equals(clazzName)) {
            return null;
        }
        try {
            if (args == null || args[0] == null) {
                return null;
            }

            Bundle params = (Bundle) args[0];
            params.putString("clazzName", mClazz.getName());
            params.putString("methodName", methodName);

            Bundle result = mPieBridgeAidl.call(params);

            Log.e(TAG, "===result===: " + result == null ? "null" : result.toString());
            return result;
        } catch (Throwable e) {
            Log.e(TAG, longName + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                throw cause;
            } else {
                throw e;
            }
        }
    }
}