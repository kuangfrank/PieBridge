package cn.kuang.frank.piebridgedemo.BookApi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class Book implements Parcelable {
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private int price;
    private String name;

    public Book() {

    }

    protected Book(Parcel in) {
        price = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}