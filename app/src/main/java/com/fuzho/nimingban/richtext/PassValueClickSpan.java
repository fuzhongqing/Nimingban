package com.fuzho.nimingban.richtext;

import android.text.style.ClickableSpan;

/**
 * Created by fuzho on 2016/9/26.
 */
public abstract class PassValueClickSpan extends ClickableSpan{
    private Object argv0;
    public PassValueClickSpan(Object argv0) {
        this.argv0 = argv0;
    }
    public Object getArgv0() {
        return  this.argv0;
    }
}
