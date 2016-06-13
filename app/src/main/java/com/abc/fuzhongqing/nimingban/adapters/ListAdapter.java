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
import android.widget.TextView;

import com.abc.fuzhongqing.nimingban.Constants;
import com.abc.fuzhongqing.nimingban.R;
import com.abc.fuzhongqing.nimingban.activities.ActivityImageShow;
import com.abc.fuzhongqing.nimingban.activities.MainActivity;
import com.abc.fuzhongqing.nimingban.activities.PoActivity;
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
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public void setDatas(JSONArray datas) {
        this.datas = datas;
    }

    private JSONArray datas;

    public ListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        datas = new JSONArray();
    }

    public void append(JSONArray a) throws JSONException {
        for (int i = 0;i < a.length(); i++)
            datas.put(a.getJSONObject(i));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_po, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //if (!holder.isRecyclable())
            holder.setIsRecyclable(true);
        //holder.setIsRecyclable(true);
        try {
            final ImageViewHolder tmp = (ImageViewHolder) holder;
            JSONObject d = datas.getJSONObject(position);

            tmp.v.setTag(Integer.parseInt(d.getString("id")));
            tmp.v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PoActivity.class);
                    intent.putExtra("po",v.getTag().toString());
                    //System.out.println(v.getTag());
                    mContext.startActivity(intent);
                }
            });
            String isRedName = d.getString("admin");
            String isSega    = d.getString("sage");
            tmp.mTextView.setText(Html.fromHtml(d.getString("content")));
            tmp.mId.setText(d.getString("userid"));
            tmp.mNo.setText(d.getString("id"));
            tmp.mTime.setText(d.getString("now").substring(5));
            tmp.mReplys.setText("回复:" + d.getString("replyCount"));
            if (isRedName.equals("1")) tmp.mId.setTextColor(Color.RED);
            else tmp.mId.setTextColor(Color.GRAY);
            if (!isSega.equals("1")) tmp.mSega.setText("");

            if (d.getString("img").length()!=0) {
                //tmp.image
                final String imgUrl = Constants.imgThumb + "/" + d.getString("img")  + d.getString("ext");
                final String imgUrlReal = Constants.imgSource + "/" + d.getString("img")  + d.getString("ext");
                final LiteHttp liteHttp = ((MainActivity)mContext).getLiteHttp();
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
        return datas == null ? 0 : datas.length();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

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
