package com.fuzho.nimingban.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fuzho.nimingban.Application;
import com.fuzho.nimingban.MVPBaseActivity;
import com.fuzho.nimingban.R;
import com.fuzho.nimingban.imageviewer.ImageViewerView;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.post.PostView;
import com.fuzho.nimingban.richtext.Block;
import com.fuzho.nimingban.richtext.OnClickListener;
import com.fuzho.nimingban.richtext.RichText;


import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.ViewHolder;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String aritcle_id_key = "aritcle_id";
    private final int TYPE_NORMAL = 0;
    private final int TYPE_LOADING = 1;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<Article> mArticles;
    private ImageLoader mImageLoader;
    private boolean loading = false;
    private boolean hasClick = false;
    private Block mBlock;

    public Adapter(Context context, boolean click) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        mArticles = new ArrayList<>();
        hasClick = click;
        mBlock = new Block(">>No.\\d{1,10}");
        mBlock.setForegroundColor(Color.BLUE);
        mBlock.setClickCallback(new OnClickListener() {

            private Object argv0;

            @Override
            public void onClick(View view) {
                showDialog(argv0.toString());
            }

            @Override
            public void setArgv0(Object argv0) {
                this.argv0 = argv0;
            }
        });
    }

    public void setArticles(ArrayList<Article> as) {
        mArticles = as;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL)
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_po, parent, false));
        else return new LoadViewHolder(mLayoutInflater.inflate(R.layout.progress_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && loading) return TYPE_LOADING;
        else return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof LoadViewHolder) {
            return;
        }
        ImageViewHolder mHolder = (ImageViewHolder) holder;
        mHolder.setIsRecyclable(false);
        final Article article = mArticles.get(position);
        String img = "";
        //检查是否有图片
        if (!"".equals(article.getImgurl())) {
            img = "http://img1.nimingban.com/thumb/" + article.getImgurl();
            /*
            mHolder.image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(500,300));
            img = "http://img1.nimingban.com/thumb/" + article.getImgurl();
            System.out.println(img);
            mHolder.image.setDefaultImageResId(R.drawable.img_loading);
            mHolder.image.setImageUrl(img,mImageLoader);
            mHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageViewerView.class);
                    intent.putExtra("url", article.getImgurl());
                    mContext.startActivity(intent);
                }
            });
            */
            mHolder.image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(300,300));
            Application.getLoader().from(img).to(mHolder.image, 300, 300);
            mHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageViewerView.class);
                    intent.putExtra("url", article.getImgurl());
                    mContext.startActivity(intent);
                }
            });
        } else {
            mHolder.image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0,0));
        }

        RichText.from(article.getContent())
                .addBlock(mBlock)
                .into(mHolder.mTextView);
        final int fId = article.getId();
        if (hasClick) {
            mHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PostView.class);
                    intent.putExtra(aritcle_id_key, fId);
                    mContext.startActivity(intent);
                    ((MVPBaseActivity) mContext).overridePendingTransition(0, 0);
                }
            });
            mHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PostView.class);
                    intent.putExtra(aritcle_id_key, fId);
                    mContext.startActivity(intent);
                    ((MVPBaseActivity) mContext).overridePendingTransition(0, 0);
                }
            });
        }
        mHolder.mId.setText("ID:"+ article.getUid());
        if (article.isadmin()) {
            mHolder.mId.setTextColor(Color.RED);
            mHolder.mId.setText(article.getUid());
        }
        mHolder.mNo.setText("NO." + article.getId());
        mHolder.mSega.setVisibility(article.isSege()?View.VISIBLE:View.GONE);
        if (article.getType() != Article.TYPE.MAIN) {
            mHolder.mLinearLayout.setVisibility(View.GONE);
        }
        mHolder.mReplys.setText(article.getReplys() + "");
        mHolder.mTime.setText(article.getTime());
    }

    @Override
    public int getItemCount() {
        return (mArticles == null ? 0 : mArticles.size()) + (loading ? 1 : 0);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView,mNo,mId,mTime,mReplys,mSega;
        ImageView image;
        View v;
        LinearLayout mLinearLayout;
        CardView mCardView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.card_text_view);
            mNo       = (TextView) itemView.findViewById(R.id.card_no);
            mId       = (TextView) itemView.findViewById(R.id.card_uid);
            mTime     = (TextView) itemView.findViewById(R.id.card_time);
            mReplys   = (TextView) itemView.findViewById(R.id.card_replys);
            mSega     = (TextView) itemView.findViewById(R.id.card_sega);
            image     = (ImageView) itemView.findViewById(R.id.imageThumb);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.replys_block);
            mCardView = (CardView) itemView.findViewById(R.id.cv_item);
        }
    }

    public class LoadViewHolder extends ViewHolder {

        public LoadViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void showDialog(String str) {

        StringBuffer sb = new StringBuffer();
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) sb.append(c);
        }

        new AlertDialog.Builder(mContext)
        .setTitle(Application.contentCache.getString(sb.toString()))
        .show();
        ;//.setAdapter();
    }

}
