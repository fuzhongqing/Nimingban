package com.fuzho.nimingban.send;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;

/**
 * Created by fuzho on 2016/9/19.
 * 发送窗口
 */
public class SendView extends MVPBaseActivity implements View.OnClickListener,ISendView {

    private static final String TAG = "SendView";
    RelativeLayout mRelativeLayout;
    LinearLayout mBottomLayout;
    LinearLayout mBottomLayoutBig;
    EditText mEditText;
    Button addbutton,picbutton,sendbutton;
    ViewPager mViewPager;
    ActionBar mActionBar;
    private boolean isZD = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_panel);
        mBottomLayoutBig = (LinearLayout) findViewById(R.id.bottom_panel_big);
        mEditText = (EditText) findViewById(R.id.editText);

        //Integer scrollDistance;
        final ViewTreeObserver observer = mBottomLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!isZD)
                    mEditText.setHeight(mRelativeLayout.getHeight() - mBottomLayout.getHeight());
                    else mEditText.setHeight(mRelativeLayout.getHeight() - mBottomLayout.getHeight() - 400);
                mBottomLayoutBig.setMinimumHeight(mBottomLayout.getHeight() + 300);
                //scrollDistance = mBottomLayoutBig.getHeight() - mBottomLayout.getHeight();
                if (observer.isAlive()) observer.removeOnGlobalLayoutListener(this);
            }
        });

        mEditText.setSelection(mEditText.getText().length(), mEditText.getText().length());
        mEditText.setMovementMethod(ScrollingMovementMethod.getInstance());
        //mEditText.setHeight(mRelativeLayout.getHeight() - mBottomLayout.getHeight());

        addbutton = (Button) findViewById(R.id.addbutton);
        picbutton = (Button) findViewById(R.id.addpic);
        sendbutton = (Button) findViewById(R.id.send);

        addbutton.setOnClickListener(this);
        picbutton.setOnClickListener(this);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().send();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new StaticPager(getSupportFragmentManager()));

        ObjectAnimator.ofFloat(mBottomLayoutBig,"translationY", 0, 400).setDuration(1).start();

        doIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doIntent(intent);
    }

    private void doIntent(Intent intent) {
        mActionBar.setTitle("新串");
    }
    @Override
    public void onClick(View view) {
        isZD = !isZD;
        //int move = !isZD ? -400 : 0;
        //ObjectAnimator.ofFloat(mBottomLayoutBig,"translationY", 0, move).setDuration(200).start();

        if (isZD) {
            ObjectAnimator.ofFloat(mBottomLayoutBig,"translationY", 400, 0).setDuration(200).start();
        } else {
            ObjectAnimator.ofFloat(mBottomLayoutBig,"translationY", 0, 400).setDuration(200).start();
        }
        //mBottomLayoutBig.scrollTo(0,-400);
        //ObjectAnimator.ofFloat(mViewPager,"translationY", 0, move).setDuration(200).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.show_more :
                new AlertDialog.Builder(this)
                        .setTitle("串信息")
                        .setView(getView())
                        .setPositiveButton("确定", null)
                        .show();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected SendPresenter getPresenter() {
        return new SendPresenter();
    }

    private View getView() {
        View view = getLayoutInflater().inflate(R.layout.send_detail_view,null,false);
        view.findViewById(R.id.email).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    (getPresenter()).updateEmail(((EditText)view).getText().toString());
                    Log.d(TAG , "copying email to model content :" + ((EditText)view).getText().toString());
                }
            }
        });
        view.findViewById(R.id.name).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    (getPresenter()).updateName(((EditText)view).getText().toString());
                    Log.d(TAG , "copying name to model");
                }
            }
        });
        view.findViewById(R.id.title).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    (getPresenter()).updateTitle(((EditText)view).getText().toString());
                    Log.d(TAG , "copying title to model");
                }
            }
        });
        return view;
    }

    @Override
    public void onCallback(String msg) {

    }

    @Override
    public void onfailed(String errorMsg) {
        Toast.makeText(this, "发生错误" + errorMsg, Toast.LENGTH_LONG).show();
    }
}
