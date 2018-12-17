package com.example.followme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchDevicesPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_devices_page);
    }

    public void MoveToSearchPage(View v)
    {
        final Context context = this;
        Intent intent = new Intent(context, SearchPage.class);
        startActivity(intent);
    }
    public void MoveToStreamPage(View v)
    {
        final Context context = this;
        Intent intent = new Intent(context, StreamPage.class);
        startActivity(intent);
    }
    public void MoveToLogOutPage(View v)
    {
        final Context context = this;
        Intent intent = new Intent(context, HomePage.class);
        startActivity(intent);
    }
}
