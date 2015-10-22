package com.fei.digitalcity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.fei.digitalcity.R;

public class ReadNewsActivity extends Activity implements View.OnClickListener {

    private ImageView ivBack,ivShare,ivTextSize;
    private WebView wvContent;
    private WebSettings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        initViews();
        initData();
    }



    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivShare.setOnClickListener(this);
        ivTextSize = (ImageView) findViewById(R.id.ivTextSize);
        ivTextSize.setOnClickListener(this);
        wvContent = (WebView) findViewById(R.id.wvContent);
    }

    private void initData() {

        String newsUrl = getIntent().getStringExtra("newsUrl");
        mSettings = wvContent.getSettings();
        mSettings.setJavaScriptEnabled(true);
        wvContent.loadUrl(newsUrl);

    }

    private int textSizeSelection=1;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivShare:
                break;
            case R.id.ivTextSize:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Text Size");
                String[] items = new String[] {"Large","Normal","Small"};
                builder.setSingleChoiceItems(items, textSizeSelection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            mSettings.setTextSize(WebSettings.TextSize.LARGER);
                        } else if (i == 1) {
                            mSettings.setTextSize(WebSettings.TextSize.NORMAL);
                        } else if (i == 2) {
                            mSettings.setTextSize(WebSettings.TextSize.SMALLER);
                        }

                        textSizeSelection = i;
                    }
                });

                builder.setPositiveButton("Ok",null);
//                builder.setNegativeButton("Cancel", null);
                builder.show();

                break;
        }
    }
}
