package com.abc.fuzhongqing.nimingban.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.abc.fuzhongqing.nimingban.Constants;
import com.abc.fuzhongqing.nimingban.NmbApplication;
import com.abc.fuzhongqing.nimingban.R;
import com.abc.fuzhongqing.nimingban.adapters.ListAdapter;
import com.abc.fuzhongqing.nimingban.resources.Http;
import com.abc.fuzhongqing.nimingban.resources.Util;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private ListAdapter listAdapter;
    private LiteHttp http;
    private NmbApplication app;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        app = (NmbApplication) getApplication();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "我还没有想好这个按钮干嘛,嘿嘿", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshWidget.setColorSchemeResources(
                R.color.colorLoading1,
                R.color.colorLoading2,
                R.color.colorLoading3,
                R.color.colorLoading4);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applyPage(null,null);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(listAdapter);
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager){

            @Override
            public void onLoadMore(int currentPage) {
                getLiteHttp().executeAsync(new StringRequest(Constants.Showf+"/"+app.currForm+"/page/"+currentPage).setHttpListener(
                        new HttpListener<String>() {
                            @Override
                            public void onSuccess(String s, Response<String> response) {
                                super.onSuccess(s, response);
                                try {
                                    listAdapter.append(new JSONArray(s));
                                    listAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ));
            }
        });
        this.http = Http.getHttp(MainActivity.this);
        updataMenu();
        applyPage(Constants.defaultFid,Constants.defaultFname);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_borad:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("本版信息");
                TextView text = new TextView(this);
                text.setPadding(60,0,60,0);
                boolean founded =  false;
                try {
                    JSONArray jsonArray = app.menu;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray forums = jsonObject.getJSONArray("forums");
                        for (int j = 0; j < forums.length(); j++)
                        {
                            JSONObject tmp = forums.getJSONObject(j);
                            if (tmp.getString("id").equals(app.currForm)) {
                                founded = true;
                                text.setText(Html.fromHtml(tmp.getString("msg")));
                                break;
                            }
                        }
                        if (founded) break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.setView(text);
                dialog.show();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        applyPage(id + "",item.getTitle() + "");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updataMenu() {
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        assert mNavigationView != null;
        final Menu mMenu = mNavigationView.getMenu();



        Http.getHttp(MainActivity.this).executeAsync(new StringRequest(Constants.getForumList)
                .setCacheMode(CacheMode.NetFirst).setHttpListener(
                new HttpListener<String>() {
                    @Override
                    public void onSuccess(String s, Response<String> response) {
                        super.onSuccess(s, response);
                        mMenu.clear();

                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            app.menu = jsonArray;
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SubMenu subMenu = mMenu.addSubMenu(0,
                                        Integer.parseInt(jsonObject.getString("id")),
                                        Integer.parseInt(jsonObject.getString("sort")),
                                        jsonObject.getString("name"));

                                JSONArray forums = jsonObject.getJSONArray("forums");
                                for (int j = 0; j < forums.length(); j++)
                                {
                                    JSONObject tmp = forums.getJSONObject(j);
                                    subMenu.add(j,
                                            Integer.parseInt(tmp.getString("id")),
                                            Integer.parseInt(tmp.getString("sort")),
                                            tmp.getString("name"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ));
    }

    private void applyPage(String fid,String fname) {
        if (mSwipeRefreshWidget!=null) mSwipeRefreshWidget.setRefreshing(true);
        if (fid == null) fid = app.currForm;
        else fid = app.currForm = fid;
        if (fname == null) fname = app.currFormName;
        else fname = app.currFormName = fname;

        toolbar.setTitle(fname);

        Http.getHttp(MainActivity.this).executeAsync(new StringRequest(Constants.Showf+"/"+fid+"/page/0")
                .setCacheMode(CacheMode.NetFirst).setHttpListener(
                new HttpListener<String>() {
                    @Override
                    public void onSuccess(String s, Response<String> response) {
                        mSwipeRefreshWidget.setRefreshing(false);
                        super.onSuccess(s, response);
                        try {
                            listAdapter.setDatas(new JSONArray(s));
                            listAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ));
    }
    public LiteHttp getLiteHttp() {
        return this.http;
    }

    abstract class HttpListener<T> extends com.litesuits.http.listener.HttpListener<T> {
        @Override
        public void onFailure(HttpException e, Response response) {
            super.onFailure(e, response);
            Snackbar.make(fab, "没有网啊~", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
    public abstract class EndlessRecyclerOnScrollListener extends
            RecyclerView.OnScrollListener {
        private int previousTotal = 0;
        private boolean loading = true;
        int firstVisibleItem, visibleItemCount, totalItemCount;

        private int currentPage = 0;

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
        }

        public abstract void onLoadMore(int currentPage);
    }
}
