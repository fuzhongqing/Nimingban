package com.fuzho.nimingban.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fuzho.nimingban.Application;
import com.fuzho.nimingban.R;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.post.PostView;
import com.fuzho.nimingban.tools.BitMapCache;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;


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

    public Adapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        mArticles = new ArrayList<>();
        mImageLoader = new ImageLoader(Application.getRequestQueue(),new BitMapCache());
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
        Article article = mArticles.get(position);
        String img = "";
        //检查是否有图片
        if (!"".equals(article.getImgurl())) {
            mHolder.image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(300,300));
            img = "http://img1.nimingban.com/thumb/" + article.getImgurl();

            System.out.println(img);
            mHolder.image.setDefaultImageResId(R.drawable.img_loading);
            mHolder.image.setImageUrl(img,mImageLoader);
        } else {
            mHolder.image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0,0));
        }
        RichText.from(article.getContent())
                .into(mHolder.mTextView);
        final int fId = article.getId();
        mHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostView.class);
                intent.putExtra(aritcle_id_key, fId);
                mContext.startActivity(intent);
                ((MainView)mContext).overridePendingTransition(0, 0);
            }
        });
        mHolder.mId.setText("ID:"+ article.getUid());
        mHolder.mNo.setText("NO." + article.getId());
        mHolder.mSega.setVisibility(article.isSege()?View.VISIBLE:View.INVISIBLE);
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
        NetworkImageView image;
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
            image     = (NetworkImageView) itemView.findViewById(R.id.imageThumb);
        }
    }

    public class LoadViewHolder extends ViewHolder {

        public LoadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
