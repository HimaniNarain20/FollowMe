package com.example.followme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
    }
    public void exit(View v)
    {
        final Context context = this;
        Intent intent = new Intent(context, HomePage.class);
        startActivity(intent);
    }
}
