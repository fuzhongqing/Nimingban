package com.abc.fuzhongqing.nimingban.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.abc.fuzhongqing.nimingban.Constants;
import com.abc.fuzhongqing.nimingban.R;
import com.abc.fuzhongqing.nimingban.adapters.PoAdapter;
import com.abc.fuzhongqing.nimingban.resources.Http;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.response.Response;
import com.zzhoujay.richtext.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PoActivity extends AppCompatActivity {
    final String DEBUG_TAG = "Po_Debug";
    private String Po;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    PoAdapter poAdapter;
    Toolbar toolbar;
    FloatingActionButton fab;
    JSONObject dataTmp;

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
            Log.d(DEBUG_TAG,intent.getData().toString());
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
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
                                    dataTmp = poAdapter.append(json.getJSONArray("replys"));
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
                                    dataTmp = json;
                                    poAdapter.setDatas(json);
                                    poAdapter.notifyDataSetChanged();
                                    mRecyclerView.scrollToPosition(0);
                                    toolbar.setTitle(json.getString("title")+ "|" +json.getString("name"));
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


    @Override
    public void startActivity(Intent intent) {

        if (TextUtils.equals(intent.getAction(), Intent.ACTION_VIEW) && intent.getDataString().startsWith(">>")) {
            String idtmp = intent.getDataString().substring(2);
            if (!Character.isDigit(idtmp.charAt(0))) {
                idtmp = idtmp.substring(3);
            }
            String content = "未在这个串中找到相应的主题";
            String userid = "";
            String time = "";
            String id = "";
            if (dataTmp == null) return ;
            try {
                JSONArray tmp = dataTmp.getJSONArray("replys");
                for (int i = 0;i < tmp.length(); i++)
                {
                    JSONObject obj = tmp.getJSONObject(i);
                    if (obj.getString("id").equals(idtmp)) {
                        time = obj.getString("now");
                        id = obj.getString("id");
                        userid = obj.getString("userid");
                        content = obj.getString("content");
                        if (obj.getString("img").length() != 0) {
                            content += "<img src=\""+ Constants.imgThumb +"/"+ obj.getString("img") + obj.getString("ext")  +"\" />";
                        }
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(PoActivity.this);


            CardView v = (CardView) ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_po_sm,null);

            RichText.from(content).into(((TextView)v.findViewById(R.id.card_text_view)));
            Linkify.addLinks(((TextView)v.findViewById(R.id.card_text_view)),poAdapter.REPLYID_TAG_PATTERN,null);
            ((TextView) v.findViewById(R.id.card_no)).setText(id);
            ((TextView) v.findViewById(R.id.card_time)).setText(time);
            ((TextView) v.findViewById(R.id.card_uid)).setText(userid);
            builder.setView(v);
            builder.show();
        } else
        super.startActivity(intent);
    }
}
