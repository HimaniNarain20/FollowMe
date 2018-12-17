package com.example.followme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChoosePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_page);
    }
    public void MoveToPairPage(View view) {
        final Context context = this;
        Intent intent = new Intent(context, PairPage.class);

        startActivity(intent);
    }
    public void MoveToScanPage(View view) {
        final Context context = this;
        Intent intent = new Intent(context, ScanPage.class);

        startActivity(intent);
    }
}
