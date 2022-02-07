package com.ksnk.aaidapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import net.pubnative.AdvertisingIdClient;


public class MainActivity extends AppCompatActivity {
    private String google;
    private TextView textView;
    private Button buttonSetting, buttonCopy;
    private Button shareButton;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListeners();
        setAds();
        getAdId();
    }


    public void getAdId() {
        AdvertisingIdClient.getAdvertisingId(getApplicationContext(), new AdvertisingIdClient.Listener() {
            @Override
            public void onAdvertisingIdClientFinish(AdvertisingIdClient.AdInfo adInfo) {
                google = adInfo.getId();
                textView.setText(google);

            }

            @Override
            public void onAdvertisingIdClientFail(Exception exception) {
                textView.setText(getString(R.string.error));
            }
        });
    }


    private void init() {
        mAdView = findViewById(R.id.adView);
        shareButton = findViewById(R.id.buttonShare);
        textView = findViewById(R.id.textView);
        buttonCopy = findViewById(R.id.button_copy);
        buttonSetting = findViewById(R.id.button_open_settings);
    }

    private final View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "UUID");
            String textSend = "My Google Advertising ID - ";
            intent.putExtra(android.content.Intent.EXTRA_TEXT, textSend + textView.getText().toString());
            startActivity(Intent.createChooser(intent, "Share using"));
        }
    };

    private final View.OnClickListener settingButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String action = "com.google.android.gms.settings.ADS_PRIVACY";
            Intent settings = new Intent(action);
            startActivity(settings);
        }
    };

    private final View.OnClickListener copyButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.text_copy), textView.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(MainActivity.this, getString(R.string.text_copy), Toast.LENGTH_LONG).show();
        }
    };

    private void setListeners() {
        shareButton.setOnClickListener(shareOnClickListener);
        buttonSetting.setOnClickListener(settingButtonOnClickListener);
        buttonCopy.setOnClickListener(copyButtonOnClickListener);
    }

    private void setAds() {
        MobileAds.initialize(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}