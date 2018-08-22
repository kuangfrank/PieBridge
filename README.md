## PieBridge
An efficient, light, and easy-to-use framework for Android Inter-Process Communication (IPC).
一套高效、小巧、易用的基于 Bundle 的 Android 进程间通信IPC框架。
[https://github.com/kuangfrank/PieBridge](https://github.com/kuangfrank/PieBridge)

这几天学习了爱奇艺的跨进程通信框架——[Andromeda](https://github.com/iqiyi/Andromeda)，
又研究了基于 JSON PRC 协议的 Android 跨进程调用解决方案[Bifrost](https://github.com/LiushuiXiaoxia/Bifrost)，觉得很受启发。

Andromeda 比较适用于 App 多进程架构整体解决方案；而 Bifrost 通讯协议基于 Json，调用效率及对复杂类型的支持上还有进步空间。
如果有一个框架能借用上述两种框架的思路，简化集成过程，并且提升跨进程调用效率，提高对复杂类型的支持度，岂不妙哉。
这就是开发 PieBridge 框架的出发点。恰逢七夕节，PieBridge(鹊桥) 这个名字应景而生，寓意 Android 进程间的通讯畅通无阻。

PieBridge 框架基于Android原生提供的 Bundle 进行通讯。Bundle 实现了 Parcelable 接口，内部维护了 Map <String,Object> 数据结构，
所以既能实现高效的通讯，又能与各种复杂类型之间进行转换。

PieBridge 的 Source & Demo 请点击 [PieBridge GitHub](https://github.com/kuangfrank/PieBridge)
PieBridge 框架，Lib 代码仅包含4个Java文件，不过300行代码，易学易用。

## 调用方式

Android 的跨进程调用使用 AIDL 方式，通常要写很多代码，操作繁杂；不同业务的跨进程调用，不易复用。
若使用 PieBridge 框架库，可降低使用难度。

只需像本地调用一样，先定义一个接口和实现类。
```java
public interface IBookApi {
    Bundle insertBookListMethod(Bundle param);
    Bundle deleteBookListMethod(Bundle param);
    ...
  }
```

```java
public class BookApiImpl implements IBookApi {
    @Override
    public Bundle insertBookListMethod(Bundle param) {
        ArrayList<Book> tmpList = BookApiUtil.fromBundle(param);
        sBookList.addAll(tmpList);

        Bundle result = BookApiUtil.toBundle(sBookList);
        return result;
    }

    @Override
    public Bundle deleteBookListMethod(Bundle param) {
        ...
        return result;
    }
    ...
}
```

再注册接口和实现类，在 Application 初始化过程中调用，请参考 Demo 应用。

```java
public class MainApp extends Application {
    ...
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
```

## 基本原理

定义通用ADIL接口

```java
import android.os.Bundle;
interface IPieBridgeAidl {
    Bundle call(in Bundle args);
}
```

跨进程通讯时，调用方通过ADIL 代理，将调用接口名作为参数传入，服务方以动态代理方式调用实际的接口，返回数据。


## 小结

**优点**

* 易学易用，简化跨进程编码
* 调用高效，支持复杂数据传输
* 源码不依赖任何三方库，文件数、代码量少，易于集成

**缺点**

* Bundle 与原始数据类型之间需要进行转换。即便是简单的数据类型，也需要通过 Bundle 通讯
* 目前代码异步调用尚未实现，只支持同步操作

## 联系方式及相关链接

本人e-mail： kuangrenjin@qq.com

[PieBridge GitHub](https://github.com/kuangfrank/PieBridge)

[Andromeda GitHub](https://github.com/iqiyi/Andromeda)

[Bifrost GitHub](https://github.com/LiushuiXiaoxia/Bifrost)
