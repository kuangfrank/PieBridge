package cn.kuang.frank.piebridgedemo.BookApi;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class BookApiImpl implements IBookApi {
    private static ArrayList<Book> sBookList = new ArrayList<>();
    private static BookApiImpl sBookApiImplInstance = null;

    private BookApiImpl() {
        Book book1 = new Book();
        book1.setName("笑傲江湖");
        book1.setPrice(188);

        Book book2 = new Book();
        book2.setName("天龙八部");
        book2.setPrice(168);

        sBookList.add(book1);
        sBookList.add(book2);
    }

    public static BookApiImpl getInstance() {
        if (null == sBookApiImplInstance) {
            synchronized (BookApiImpl.class) {
                if (null == sBookApiImplInstance) {
                    sBookApiImplInstance = new BookApiImpl();
                }
            }
        }
        return sBookApiImplInstance;
    }

    @Override
    public Bundle insertBookListMethod(Bundle param) {
        ArrayList<Book> tmpList = BookApiUtil.fromBundle(param);
        sBookList.addAll(tmpList);

        Bundle result = BookApiUtil.toBundle(sBookList);
        return result;
    }

    @Override
    public Bundle deleteBookListMethod(Bundle param) {
        ArrayList<Book> tmpList = BookApiUtil.fromBundle(param);

        ArrayList<Book> removeList = new ArrayList<>();
        for (Book element : sBookList) {
            for (Book tmp : tmpList) {
                if (tmp.getName().equals(element.getName())
                        && tmp.getPrice() == element.getPrice()) {
                    removeList.add(element);
                }
            }
        }

        sBookList.removeAll(removeList);

        Bundle result = BookApiUtil.toBundle(sBookList);
        return result;
    }

    @Override
    public Bundle queryBookListMethod(Bundle param) {
        Bundle result = BookApiUtil.toBundle(sBookList);
        return result;
    }

    @Override
    public Bundle clearBookListMethod(Bundle param) {
        sBookList.clear();

        Bundle result = BookApiUtil.toBundle(sBookList);
        return result;
    }
}
