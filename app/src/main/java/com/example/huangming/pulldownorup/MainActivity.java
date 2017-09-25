package com.example.huangming.pulldownorup;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    int t;
    int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final List<String> data = new ArrayList<>();
        data.add("A");
        data.add("B");
        data.add("C");
        final PullDownOrUpListView lvPullDownOrUp = (PullDownOrUpListView) findViewById(R.id
            .lvPullDownOrUp);
        lvPullDownOrUp.setPullUpEnabled(false);
        lvPullDownOrUp.setPullDownEnabled(false);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
            .simple_list_item_1, android.R.id.text1, data);
        lvPullDownOrUp.setAdapter(adapter);
        lvPullDownOrUp.setPullUpEnabled(true);
        lvPullDownOrUp.setPullDownEnabled(true);
        // lvPullDownOrUp.setPullDownEnabled(false);
        lvPullDownOrUp.setLoadMoreListener(new PullDownOrUpListView.LoadMoreListener() {
            @Override
            public void onLoadMoreFromTop() {
                System.out.println("顶部。。。");
                // adapter.setNotifyOnChange(false);
                data.add(0, "Top" + t);
                t++;
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // adapter.notifyDataSetChanged();
                        if (t > 3) {
                            lvPullDownOrUp.setPullDownEnabled(false);
                        } else {
                            lvPullDownOrUp.setPullDownEnabled(true);
                        }
                    }
                }, 300);

            }

            @Override
            public void onLoadMoreFromBottom() {
                System.out.println("底部！！！");
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.add("Bottom" + b);
                        b++;
                        if (b > 20) {
                            lvPullDownOrUp.setPullUpEnabled(false);
                        } else {
                            lvPullDownOrUp.setPullUpEnabled(true);
                        }
                    }
                }, 1000);
            }
        });
    }
}
