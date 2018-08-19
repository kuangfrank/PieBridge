package cn.kuang.frank.piebridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class PieBridge {
    private final static String TAG = PieBridge.class.getSimpleName();
    private static PieBridge sInstance = new PieBridge();
    Context mContext;
    private Map<Class<?>, Class<?>> classConfig = new HashMap<>();

    private Map<Class<?>, Object> instanceConfig = new HashMap<>();
    private Map<Class<?>, Object> proxyCache = new HashMap<>();
    private IPieBridgeAidl mPieBridgeAidl;
    private boolean mServiceConnected = false;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mPieBridgeAidl == null) {
                return;
            }
            mPieBridgeAidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mPieBridgeAidl = null;
            mServiceConnected = false;
            connectPieBridgeService();
        }
    };
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPieBridgeAidl = IPieBridgeAidl.Stub.asInterface(service);
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed linking to death.");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPieBridgeAidl = null;
            mServiceConnected = false;
        }
    };

    private PieBridge() {

    }

    public static PieBridge getInstance() {
        return sInstance;
    }

    private void startPieBridgeService() {
        Intent toService = new Intent(mContext, PieBridgeService.class);
        try {
            mContext.startService(toService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectPieBridgeService() {
        if (!mServiceConnected) {
            Intent intent = new Intent(mContext, PieBridgeService.class);
            mContext.bindService(intent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);

            mServiceConnected = true;
        }
    }

    public void disconnectPieBridgeService() {
        if (mServiceConnected) {
            try {
                mContext.unbindService(mServiceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mServiceConnected = false;
        }
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();

        startPieBridgeService();
        connectPieBridgeService();
    }

    public void register(Class<?> face, Class<?> impl) {
        classConfig.put(face, impl);
    }

    public <T> void register(Class<T> face, T instance) {
        instanceConfig.put(face, instance);
    }

    public <T> T getInstance(Class<?> face) {
        if (instanceConfig.containsKey(face)) {
            return (T) instanceConfig.get(face);
        } else if (classConfig.containsKey(face)) {
            Class<?> aClass = classConfig.get(face);
            try {
                Object o = aClass.newInstance();
                if (face.isInstance(o)) {
                    instanceConfig.put(face, o);
                    return (T) o;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T getRemoteInstance(Class<T> face) {
        if (mPieBridgeAidl == null) {
            return null;
        }
        if (proxyCache.containsKey(face)) {
            return (T) proxyCache.get(face);
        }
        T proxy = PieBridgeHandler.newProxyInstance(face, mPieBridgeAidl);
        proxyCache.put(face, proxy);
        return proxy;
    }
}