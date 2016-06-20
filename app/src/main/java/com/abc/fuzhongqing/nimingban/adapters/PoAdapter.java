package com.abc.fuzhongqing.nimingban.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abc.fuzhongqing.nimingban.Constants;
import com.abc.fuzhongqing.nimingban.R;
import com.abc.fuzhongqing.nimingban.activities.ActivityImageShow;
import com.abc.fuzhongqing.nimingban.resources.Http;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.BitmapRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by fuzhongqing on 16/6/9.
 *
 *
 */
public class PoAdapter extends RecyclerView.Adapter<ViewHolder> {

    public enum ITEM_TYPE {
        ITEM_TYPE_FIRST,
        ITEM_TYPE_NORMAL,
        ITEM_TYPE_PROCESS
    }
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    public  boolean isLoading;

    public void setDatas(JSONObject datas) {
        //System.out.println(datas);
        this.datas = datas;
    }

    private JSONObject datas;

    public PoAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        isLoading = false;
        //datas = new JSONObject();
    }

    public void append(JSONArray a) throws JSONException {
        JSONArray replys = datas.getJSONArray("replys");
        for (int i = 0;i < a.length(); i++)
            replys.put(a.getJSONObject(i));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_FIRST.ordinal())
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_po, parent, false));
        if (viewType == ITEM_TYPE.ITEM_TYPE_NORMAL.ordinal())
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_po_sm, parent, false));
        return new ProgressViewHolder(mLayoutInflater.inflate(R.layout.process_bar, parent, false));
    }
    @Override
    public int getItemViewType(int position) {
        try {
            return position == 0 ? ITEM_TYPE.ITEM_TYPE_FIRST.ordinal() :
                    (position == datas.getJSONArray("replys").length() + 1 ? ITEM_TYPE.ITEM_TYPE_PROCESS.ordinal() : ITEM_TYPE.ITEM_TYPE_NORMAL.ordinal());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  ITEM_TYPE.ITEM_TYPE_NORMAL.ordinal();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setIsRecyclable(true);

        if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
            return;
        }


        try {
            final ImageViewHolder tmp = (ImageViewHolder) holder;

            JSONObject d;
            if (position==0) d = datas;
            else d = datas.getJSONArray("replys").getJSONObject(position - 1);


            String isRedName = d.getString("admin");
            String isSega    = d.getString("sage");
            if (position == 0) {
                tmp.mReplys.setText("回复:" + d.getString("replyCount"));
                if (!isSega.equals("1")) tmp.mSega.setText("");
            }
            tmp.mTextView.setText(Html.fromHtml(d.getString("content")));
            tmp.mId.setText(d.getString("userid"));
            tmp.mNo.setText(d.getString("id"));
            tmp.mTime.setText(d.getString("now").substring(5));
            if (isRedName.equals("1")) tmp.mId.setTextColor(Color.RED);
            else tmp.mId.setTextColor(Color.GRAY);


            if (d.getString("img").length()!=0) {
                //tmp.image
                final String imgUrl = Constants.imgThumb + "/" + d.getString("img")  + d.getString("ext");
                final String imgUrlReal = Constants.imgSource + "/" + d.getString("img")  + d.getString("ext");
                final LiteHttp liteHttp = Http.getHttp(mContext);
                liteHttp.executeAsync(new BitmapRequest(imgUrl).setCacheMode(CacheMode.CacheFirst).setHttpListener(
                        new HttpListener<Bitmap>() {
                            @Override
                            public void onSuccess(Bitmap bitmap, Response<Bitmap> response) {
                                super.onSuccess(bitmap, response);
                                //AlertDialog.Builder b = HttpUtil.dialogBuilder(mContext, "LiteHttp2.0", "");
                                //ImageView iv = new ImageView(mContext);
                                //iv.setImageBitmap(bitmap);
                                tmp.image.setImageBitmap(bitmap);
                                tmp.image.setTag(imgUrlReal);
                                tmp.image.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext, ActivityImageShow.class);
                                        intent.putExtra("url",imgUrlReal);
                                        mContext.startActivity(intent);
                                    }
                                });
                                //b.setView(iv);
                                //b.show();
                            }

                            @Override
                            public void onLoading(AbstractRequest<Bitmap> request, long total, long len) {
                                super.onLoading(request, total, len);
                            }
                        }
                ));
            } else {
                tmp.image.setImageBitmap(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null) return 0;
        try {
            return 1 + datas.getJSONArray("replys").length() + (isLoading ? 1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public static class ImageViewHolder extends ViewHolder {

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

    public static class NormalViewHolder extends ViewHolder {

        TextView mTextView,mNo,mId,mTime,mReplys,mSega;
        ImageView image;
        View v;
        public NormalViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.card_text_view);
            mNo       = (TextView) itemView.findViewById(R.id.card_no);
            mId       = (TextView) itemView.findViewById(R.id.card_uid);
            mTime     = (TextView) itemView.findViewById(R.id.card_time);
            image     = (ImageView)itemView.findViewById(R.id.imageThumb);

        }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }
}
