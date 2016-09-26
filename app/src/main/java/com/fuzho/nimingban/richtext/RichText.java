package com.fuzho.nimingban.richtext;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by fuzho on 2016/9/23.
 */
public class RichText {
    Pattern replyPattern0 = Pattern.compile(">>No.\\d{1,10}");
    Pattern replyPattern1 = Pattern.compile(">>\\d{1,10}");
    CharSequence paintText;
    ArrayList<Block> mBlocks;

    private RichText(String str){
        this.paintText = str;
        mBlocks = new ArrayList<>();
    }

    public RichText addBlock(Block b) {
        mBlocks.add(b);
        return this;
    }

    public RichText addBlock(ArrayList<Block> arrayList) {
        mBlocks.addAll(arrayList);
        return this;
    }

    public void into(TextView tv) {
        tv.setText(Html.fromHtml(paintText.toString(), imgGetter, null));

        for (Block b : mBlocks) {
            b.toTextView(tv);
        }
    }
    public static RichText from(String str) {
        return new RichText(str);
    }

    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            drawable = Drawable.createFromPath(source);  // Or fetch it from the URL
            // Important
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
            return drawable;
        }
    };
}
