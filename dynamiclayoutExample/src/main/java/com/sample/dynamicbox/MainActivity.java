package com.sample.dynamicbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

        dynamicLayout.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLayout(true);
            }
        });

        initLayout(true);
    }

    private void initLayout(boolean hide) {
        dynamicLayout.showLoadingLayout(hide);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_dynamic, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reload:
                initLayout(true);
                break;

            case R.id.menu_network_error:
                showNetworkError();
                break;

            case R.id.menu_refresh:
                initLayout(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNetworkError() {
        dynamicLayout.showInternetOffLayout(true);
    }
}
