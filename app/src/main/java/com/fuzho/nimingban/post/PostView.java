package com.fuzho.nimingban.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;
import com.fuzho.nimingban.main.Adapter;
import com.fuzho.nimingban.main.MainPresenter;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.tools.ReachedBottomListener;

import java.util.ArrayList;

public class PostView extends MVPBaseActivity implements IPostView {

    private static final String TAG = "PostView";
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ActionBar mActionBar;
    ReachedBottomListener mReachedBottomListener;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.post_recycler_view);
        mAdapter = new Adapter(this, false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReachedBottomListener = new ReachedBottomListener() {
            @Override
            public void onBottom() {
                if (!isLoading) {
                    isLoading = true;
                    ((PostPersenter)mPresenter).LoadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20) {
                    //fab.hide();
                }
                if (dy < -20) {
                    //fab.show();
                }
            }
        };
        mRecyclerView.setOnScrollListener(mReachedBottomListener);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.loadLayout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor0,R.color.mainColor1,R.color.mainColor2,R.color.mainColor3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ((PostPersenter)mPresenter).getArticles(-1);
                    mReachedBottomListener.setLoading(false);
                }
            });
        }
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
            id = intent.getIntExtra(Adapter.aritcle_id_key, 0);
        }
        ((PostPersenter)mPresenter).getArticles(id);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new PostPersenter(this);
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

        mAdapter.setLoading(b);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setData(ArrayList<Article> s) {
        if (mAdapter != null) {
            mAdapter.setLoading(false);
            mReachedBottomListener.setLoading(false);
            mSwipeRefreshLayout.setRefreshing(false);
            setTitle("" + s.get(0).getId());
            mAdapter.setArticles(s);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
