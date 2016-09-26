package com.fuzho.nimingban.main;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.send.SendView;
import com.fuzho.nimingban.tools.ReachedBottomListener;
import com.zzhoujay.richtext.RichText;

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
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.main_list);
        mAdapter = new Adapter(this ,true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReachedBottomListener = new ReachedBottomListener() {

            private int fabpostion = 0;
            private int lastposition = 0;
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

                fabpostion += dy;
                fabpostion = Math.min(fabpostion, 300);
                fabpostion = Math.max(0, fabpostion);
                ObjectAnimator.ofFloat(fab,"translationY", lastposition, fabpostion).setDuration(200).start();
                lastposition = fabpostion;
                /*if (dy > 20) {
                    //fab.hide();
                    ObjectAnimator.ofFloat(fab,"translationY", 0, 60).setDuration(200).start();
                }
                if (dy < -20) {
                    //fab.show();
                    ObjectAnimator.ofFloat(fab,"translationY", 60, 0).setDuration(200).start();
                }*/
                //Log.d(TAG , dy + "");
            }
        };
        mRecyclerView.setOnScrollListener(mReachedBottomListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.loadLayout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor0,R.color.mainColor1,R.color.mainColor2,R.color.mainColor3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ((MainPresenter)mPresenter).getArticles();
                    mReachedBottomListener.setLoading(false);
                }
            });
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainView.this, SendView.class));
            }
        });
        setTitle("匿名版");
        mSnackbar = Snackbar.make(fab,"",Snackbar.LENGTH_INDEFINITE);
        mMenu = mNavigationView.getMenu();
    }

    @Override
    protected MainPresenter getPresenter() {
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
            }
            mAdapter.setArticles(s);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //mAdapter.notifyDataSetChanged();
        ((MainPresenter)mPresenter).setTid(item.getIntent().getStringExtra("id"));
        ((MainPresenter)mPresenter).getArticles();
        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }
}
