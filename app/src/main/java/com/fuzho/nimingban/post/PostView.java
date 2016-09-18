package com.fuzho.nimingban.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;
import com.fuzho.nimingban.main.Adapter;
import com.fuzho.nimingban.pojo.Article;

import java.util.ArrayList;

public class PostView extends MVPBaseActivity implements IPostView {

    private static final String TAG = "PostView";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        doIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doIntent(intent);
    }

    private void doIntent(Intent intent) {
        int id = 0;
        if (intent != null) {
            id = intent.getIntExtra(Adapter.aritcle_id_key, 123);
        }
        ((PostPersenter)mPresenter).getArticles();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new PostPersenter();
    }


    @Override
    public void showProcessBar(boolean b) {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(PostView.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingMore(boolean b) {

    }

    @Override
    public void setData(ArrayList<Article> s) {

    }
}
