package com.fuzho.nimingban.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuzho.nimingban.R;
import com.fuzho.nimingban.pojo.Article;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;


import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.ViewHolder;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<Article> mArticles;

    public Adapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        mArticles = new ArrayList<>();
    }

    public void setArticles(ArrayList<Article> as) {
        mArticles = as;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_po, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageViewHolder mHolder = (ImageViewHolder) holder;
        Article article = mArticles.get(position);

        String img = "";
        //检查是否有图片
        if (!"".equals(article.getImgurl())) {
            img = "</br><img src=\"http://img1.nimingban.com/thumb/" + article.getImgurl() +  "\" />";
            System.out.println(img);
        }
        RichText.from(article.getContent() + img)
                .into(mHolder.mTextView);
        mHolder.mId.setText(article.getUid() + "");
        mHolder.mNo.setText(article.getId() + "");
        mHolder.mSega.setVisibility(article.isSege()?View.VISIBLE:View.INVISIBLE);
        mHolder.mReplys.setText(article.getReplys() + "");
        mHolder.mTime.setText(article.getTime());
    }

    @Override
    public int getItemCount() {
        return mArticles == null ? 0 : mArticles.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView,mNo,mId,mTime,mReplys,mSega;
        ImageView image;
        View v;
        public ImageViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.card_text_view);
            mNo       = (TextView) itemView.findViewById(R.id.card_no);
            mId       = (TextView) itemView.findViewById(R.id.card_uid);
            mTime     = (TextView) itemView.findViewById(R.id.card_time);
            mReplys   = (TextView) itemView.findViewById(R.id.card_replys);
            mSega     = (TextView) itemView.findViewById(R.id.card_sega);
            image     = (ImageView)itemView.findViewById(R.id.imageThumb);
        }
    }
}
