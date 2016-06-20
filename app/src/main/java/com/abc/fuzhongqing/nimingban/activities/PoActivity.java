package com.abc.fuzhongqing.nimingban.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.abc.fuzhongqing.nimingban.Constants;
import com.abc.fuzhongqing.nimingban.R;
import com.abc.fuzhongqing.nimingban.adapters.PoAdapter;
import com.abc.fuzhongqing.nimingban.resources.Http;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PoActivity extends AppCompatActivity {

    private String Po;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    PoAdapter poAdapter;
    Toolbar toolbar;
    FloatingActionButton fab;

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Po = intent.getStringExtra("po");
        if (Po == null) {
            if (intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri url = intent.getData();
                String[] paths = url.toString().split("/");
                Po = paths[paths.length - 1];
            }
        }

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutPo);
        mSwipeRefreshWidget.setColorSchemeResources(
                R.color.colorLoading1,
                R.color.colorLoading2,
                R.color.colorLoading3,
                R.color.colorLoading4);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applyPage();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewPo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        poAdapter = new PoAdapter(this);
        mRecyclerView.setAdapter(poAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager){

            @Override
            public void onScrolledDown() {
                super.onScrolledDown();

            }
            @Override
            public void onScrolledUp() {
                super.onScrolledUp();
                super.loading = false;
            }
            @Override
            public void onLoadMore(int currentPage) {
                poAdapter.isLoading = true;
                poAdapter.notifyDataSetChanged();
                Http.getHttp(PoActivity.this).executeAsync(new StringRequest(Constants.getThread+"/id/"+Po+"/page/"+currentPage).setHttpListener(
                        new HttpListener<String>() {
                            @Override
                            public void onSuccess(String s, Response<String> response) {
                                super.onSuccess(s, response);
                                try {
                                    JSONObject json = new JSONObject(s);
                                    poAdapter.append(json.getJSONArray("replys"));
                                    poAdapter.isLoading = false;
                                    poAdapter.notifyDataSetChanged();
                                    
                                } catch (JSONException e) {
                                   e.printStackTrace();
                                }
                            }
                        }
                ));
            }
        });
        applyPage();
    }

    private void applyPage() {
        if (!mSwipeRefreshWidget.isRefreshing()) mSwipeRefreshWidget.setRefreshing(true);


        Http.getHttp(PoActivity.this).executeAsync(new StringRequest(Constants.getThread+"/id/"+Po+"/page/0")
                .setCacheMode(CacheMode.NetFirst).setHttpListener(
                        new HttpListener<String>() {
                            @Override
                            public void onSuccess(String s, Response<String> response) {
                                mSwipeRefreshWidget.setRefreshing(false);

                                super.onSuccess(s, response);
                                try {
                                    //System.out.println(s);
                                    JSONObject json = new JSONObject(s);
                                    poAdapter.setDatas(json);
                                    poAdapter.notifyDataSetChanged();
                                    mRecyclerView.scrollToPosition(0);
                                    toolbar.setTitle(json.getString("name")+"-"+json.getString("title"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ));
    }
    public abstract class EndlessRecyclerOnScrollListener extends
            RecyclerView.OnScrollListener {
        private int previousTotal = 0;
        private boolean loading = true;
        int firstVisibleItem, visibleItemCount, totalItemCount , LastCount = -1;

        private int currentPage = 1;

        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerOnScrollListener(
                LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading
                    && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
            if (LastCount!= -1 && LastCount != firstVisibleItem) {
                if (LastCount > firstVisibleItem) {
                    onScrolledUp();
                } else {
                    onScrolledDown();
                }
            }
            LastCount = firstVisibleItem;
        }

        public abstract void onLoadMore(int currentPage);
        public void onScrolledUp(){
            System.out.println("up");
        }
        public void onScrolledDown() {
            System.out.println("Down");
        }
    }
}
