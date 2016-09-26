package com.fuzho.nimingban.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fuzho.nimingban.R;
import com.fuzho.nimingban.richtext.Block;
import com.fuzho.nimingban.richtext.OnClickListener;
import com.fuzho.nimingban.richtext.RichText;

public class RichTextTestActivity extends AppCompatActivity {

    private static final String TAG = "RichTextTestActivity";
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Block block = new Block(">>No.\\d{1,10}");
        block.setForegroundColor(Color.BLUE);
        block.setClickCallback(new OnClickListener() {

            private Object argv0;

            @Override
            public void onClick(View view) {
                Log.d(TAG, argv0.toString());
            }

            @Override
            public void setArgv0(Object argv0) {
                this.argv0 = argv0;
            }
        });
        mTextView = (TextView) findViewById(R.id.testtext);
        RichText.from(textBlock).addBlock(block).into(mTextView);

    }

    static String textBlock = " <br> >>454354353 <br> >>No.12345678 \n bkb \n<br> >>No.123645 <br> >>No.418326 <br> <h1>hello</h1>";

}
