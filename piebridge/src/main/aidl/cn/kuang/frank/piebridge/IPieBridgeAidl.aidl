// IPieBridgeAidl.aidl
package cn.kuang.frank.piebridge;

import android.os.Bundle;
// Declare any non-default types here with import statements

interface IPieBridgeAidl {
    Bundle call(in Bundle args);
}