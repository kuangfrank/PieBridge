package cn.kuang.frank.piebridgedemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.kuang.frank.piebridge.PieBridge;
import cn.kuang.frank.piebridgedemo.BookApi.Book;
import cn.kuang.frank.piebridgedemo.BookApi.BookApiUtil;
import cn.kuang.frank.piebridgedemo.BookApi.IBookApi;

/**
 * Created by kuangrenjin on 2018/8/19.
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView mBookListView;
    private IBookApi mBookApi;
    private ArrayList<Book> mBookList;
    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initApi();
    }

    private void initView() {
        mBookListView = (RecyclerView) findViewById(R.id.booklist);
        mBookListView.setLayoutManager(new LinearLayoutManager(this));

        mBookAdapter = new BookAdapter(MainActivity.this, mBookList);
        mBookListView.setAdapter(mBookAdapter);
    }

    private void initApi() {
        mBookApi = PieBridge.getInstance().getRemoteInstance(IBookApi.class);
    }

    private void refreshListView() {
        mBookAdapter.setData(mBookList);
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (mBookApi == null) {
            mBookApi = PieBridge.getInstance().getRemoteInstance(IBookApi.class);
        }

        ArrayList<Book> tmpList = new ArrayList<>();
        switch (viewId) {
            case R.id.button1: //add
                tmpList.add(mockNewBook());
                tmpList.add(mockNewBook());
                tmpList.add(mockNewBook());
                mBookList = BookApiUtil.addBookList(mBookApi, tmpList);
                refreshListView();
                break;
            case R.id.button2: //del
                if (mBookList != null) {
                    int listSize = mBookList.size();
                    if (listSize > 1) {
                        tmpList.add(mBookList.get(listSize - 1));
                        tmpList.add(mBookList.get(listSize - 2));
                    } else if (listSize == 1) {
                        tmpList.add(mBookList.get(0));
                    }
                }
                mBookList = BookApiUtil.delBookList(mBookApi, tmpList);
                refreshListView();
                break;
            case R.id.button3: //query
                mBookList = BookApiUtil.queryBookList(mBookApi, null);
                refreshListView();
                break;
            case R.id.button4: //clear
                mBookList = BookApiUtil.clearBookList(mBookApi, null);
                refreshListView();
                break;
        }
    }

    Book mockNewBook() {
        Book fake = new Book();

        long seed = System.nanoTime();
        Random random = new Random(seed);

        int num = 1000 + random.nextInt(1000);
        int price = 10 + random.nextInt(200);

        fake.setName("NewBook#" + num);
        fake.setPrice(price);

        return fake;
    }

    public static class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
        private Context mContext;
        private List<Book> mData;

        public BookAdapter(Context context, List<Book> dataList) {
            mContext = context;
            mData = dataList;
        }


        public void setData(List<Book> dataList) {
            mData = dataList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_book_item, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (mData == null || mData.size() < i + 1) {
                return;
            }

            viewHolder.tvBookName.setText(mData.get(i).getName());
            viewHolder.tvBookPrice.setText("" + mData.get(i).getPrice());
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvBookName;
            public TextView tvBookPrice;

            public ViewHolder(final View itemView) {
                super(itemView);

                tvBookName = (TextView) itemView.findViewById(R.id.tv_bookname);
                tvBookPrice = (TextView) itemView.findViewById(R.id.tv_bookprice);
            }
        }
    }

}
