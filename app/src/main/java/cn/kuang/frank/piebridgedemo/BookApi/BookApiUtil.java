package cn.kuang.frank.piebridgedemo.BookApi;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class BookApiUtil {
    private static final String KEY_BOOKLIST = "booklist";

    public static Bundle toBundle(ArrayList<Book> in) {
        Bundle out = new Bundle();

        if (in != null) {
            out.setClassLoader(Book.class.getClassLoader());
            out.putParcelableArrayList(KEY_BOOKLIST, in);
        }
        return out;
    }

    public static ArrayList<Book> fromBundle(Bundle in) {
        if (in == null) {
            return null;
        }

        in.setClassLoader(Book.class.getClassLoader());
        ArrayList<Book> out = in.getParcelableArrayList(KEY_BOOKLIST);

        return out;
    }


    public static ArrayList<Book> addBookList(IBookApi bookApi, ArrayList<Book> bookList) {
        if (bookApi == null) {
            return null;
        }

        Bundle in = toBundle(bookList);

        Bundle out = bookApi.insertBookListMethod(in);

        return fromBundle(out);
    }

    public static ArrayList<Book> delBookList(IBookApi bookApi, ArrayList<Book> bookList) {
        if (bookApi == null) {
            return null;
        }

        Bundle in = toBundle(bookList);

        Bundle out = bookApi.deleteBookListMethod(in);

        return fromBundle(out);
    }

    public static ArrayList<Book> queryBookList(IBookApi bookApi, ArrayList<Book> bookList) {
        if (bookApi == null) {
            return null;
        }

        Bundle in = toBundle(bookList);

        Bundle out = bookApi.queryBookListMethod(in);

        return fromBundle(out);
    }

    public static ArrayList<Book> clearBookList(IBookApi bookApi, ArrayList<Book> bookList) {
        if (bookApi == null) {
            return null;
        }

        Bundle in = toBundle(bookList);

        Bundle out = bookApi.clearBookListMethod(in);

        return fromBundle(out);
    }

}
