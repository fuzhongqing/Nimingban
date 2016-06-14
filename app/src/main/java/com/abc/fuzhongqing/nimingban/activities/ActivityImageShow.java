package com.abc.fuzhongqing.nimingban.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.abc.fuzhongqing.nimingban.R;
import com.abc.fuzhongqing.nimingban.resources.Http;
import com.abc.fuzhongqing.nimingban.resources.Util;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.BitmapRequest;
import com.litesuits.http.response.Response;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ActivityImageShow extends AppCompatActivity {

    ImageView mImageView;
    PhotoViewAttacher mAttacher;
    FloatingActionButton fab;
    String url;
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_image_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        mImageView = (ImageView) findViewById(R.id.image);

        mAttacher = new PhotoViewAttacher(mImageView);

        Http.getHttp(ActivityImageShow.this).executeAsync(new BitmapRequest(url).setHttpListener(
                new HttpListener<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Response<Bitmap> response) {
                        super.onSuccess(bitmap, response);
                        mImageView.setImageBitmap(bitmap);
                        mAttacher.update();
                    }
                }
        ));

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean ok = Util.saveImageToGallery(ActivityImageShow.this,mImageView.getDrawingCache());
                Snackbar.make(view, ok?"保存成功":"保存失败", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
