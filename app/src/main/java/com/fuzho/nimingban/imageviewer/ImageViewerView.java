package com.fuzho.nimingban.imageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fuzho.nimingban.Application;
import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;

/**
 * Created by fuzho on 2016/9/19.
 */
public class ImageViewerView extends MVPBaseActivity {
    private static final String TAG = "ImageViewerView";

    android.support.v7.app.ActionBar mActionBar;
    ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG , "start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("图片查看");
        mImageView = (ImageView) findViewById(R.id.imageView);
        doIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doIntent(intent);
    }

    void doIntent(Intent intent) {
        String url = "http://img1.nimingban.com/image/" + intent.getStringExtra("url");
        //String url = "http://img1.nimingban.com/image/" + "2016-06-14/57601ee971540.gif";
        Application.getLoader().from(url).to(mImageView);
    }

    @Override
    protected ImageViewerPresenter getPresenter() {
        return new ImageViewerPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
