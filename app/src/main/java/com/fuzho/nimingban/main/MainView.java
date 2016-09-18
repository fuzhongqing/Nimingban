package com.fuzho.nimingban.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.tools.ReachedBottomListener;

import java.util.ArrayList;

/**
 * Created by fuzhongqing on 16/8/27.
 * 主界面ntegrate the remote changes
 hint: (e.g., 'git pull ...') before pushing again.
 hint: See the 'Note about fast-forwards' in 'git push --help' for details.
 fuzhongngdeiMac:Nimingban2 fuzhongqing$

 */
public class MainView extends MVPBaseActivity implements IMainView,NavigationView.OnNavigationItemSelectedListener {
    static final String TAG = "MAIN_VIEW";
    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView mNavigationView;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ReachedBottomListener mReachedBottomListener;
    Adapter mAdapter;
    FloatingActionButton fab;
    Snackbar mSnackbar;
    android.view.Menu mMenu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        ((IMainPresenter)mPresenter).getMenuList();
        ((IMainPresenter)mPresenter).getArticles();
    }

    void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(MainView.this, mDrawerLayout,mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_list);
        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReachedBottomListener = new ReachedBottomListener() {
            @Override
            public void onBottom() {
                if (!isLoading) {
                    isLoading = true;
                    ((MainPresenter)mPresenter).LoadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20) {
                    fab.hide();
                }
                if (dy < -20) {
                    fab.show();
                }
            }
        };
        mRecyclerView.setOnScrollListener(mReachedBottomListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.loadLayout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor0,R.color.mainColor1,R.color.mainColor2,R.color.mainColor3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainPresenter)mPresenter).getArticles();
                mReachedBottomListener.setLoading(false);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"hello",Snackbar.LENGTH_INDEFINITE).show();
            }
        });

        mSnackbar = Snackbar.make(fab,"",Snackbar.LENGTH_INDEFINITE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mMenu = navigationView.getMenu();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showProcessBar(boolean b) {
        final boolean bs = b;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(bs);
            }
        });
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(MainView.this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingMore(boolean b) {
        mAdapter.setLoading(b);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setData(ArrayList<Article> s) {
        if (mAdapter != null) {
            if (s.size() > 20) {
                //如果有多余20个item说明是经历过Load more的 需要完成取消loading More
                mAdapter.setLoading(false);
                mReachedBottomListener.setLoading(false);
            } else {
                mRecyclerView.smoothScrollToPosition(0);
                mDrawerLayout.closeDrawers();
            }
            mAdapter.setArticles(s);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
