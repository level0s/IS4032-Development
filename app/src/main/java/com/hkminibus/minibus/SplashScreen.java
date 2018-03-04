package com.hkminibus.minibus;

/**
 * Created by fucheuk on 4/3/2018.
 */


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        // close splash activity
        finish();
    }
}
