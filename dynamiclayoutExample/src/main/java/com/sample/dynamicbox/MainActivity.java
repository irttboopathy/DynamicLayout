package com.sample.dynamicbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.boopi.dynamiclayout.DynamicLayout;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DynamicLayout dynamicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dynamicLayout = (DynamicLayout) findViewById(R.id.dynamic_layout);
        listView = (ListView) findViewById(R.id.listview);

        initLayout();
    }

    private void initLayout() {
        dynamicLayout.showLoadingLayout(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (int i = 0; i<100; i++) {
            adapter.add("String " + i + 1);
        }
        listView.setAdapter(adapter);
        dynamicLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                dynamicLayout.hideAll();
            }
        }, 2000);
    }
}
