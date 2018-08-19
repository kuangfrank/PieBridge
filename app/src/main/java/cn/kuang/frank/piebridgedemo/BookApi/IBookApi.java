package cn.kuang.frank.piebridgedemo.BookApi;

import android.os.Bundle;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public interface IBookApi {
    Bundle insertBookListMethod(Bundle param);

    Bundle deleteBookListMethod(Bundle param);

    Bundle queryBookListMethod(Bundle param);

    Bundle clearBookListMethod(Bundle param);
}
