package com.fuzho.nimingban.richtext;

import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fuzho on 2016/9/26.
 */
public class Block {

    private static final String TAG = "Block";

    Pattern mPattern;

    OnClickListener mOnClickListener;
    int mColor;

    public Block(String pattern) {
        this.mPattern = Pattern.compile(pattern);
    }

    public void setClickCallback(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setForegroundColor(int colorRes) {
        this.mColor = colorRes;
    }

    public void toTextView(TextView tv) {
        SpannableStringBuilder sp = new SpannableStringBuilder(tv.getText());
        Matcher matcher = mPattern.matcher(tv.getText());
        while (matcher.find()) {
            sp.setSpan(new PassValueClickSpan(tv.getText().subSequence(matcher.start(), matcher.end())) {
                @Override
                public void onClick(View view) {
                    if (mOnClickListener != null) {
                        mOnClickListener.setArgv0(this.getArgv0());
                        mOnClickListener.onClick(view);
                    }
                }
            }, matcher.start(), matcher.end(), 0);
            sp.setSpan(new ForegroundColorSpan(mColor), matcher.start(), matcher.end(), 0);

        }

        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(sp, TextView.BufferType.SPANNABLE);
    }
}